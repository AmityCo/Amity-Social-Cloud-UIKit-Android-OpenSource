@file:OptIn(com.amity.socialcloud.uikit.common.config.AmityUIKitInternalApi::class)

package com.amity.socialcloud.uikit.common.config

import io.reactivex.rxjava3.plugins.RxJavaPlugins
import com.amity.socialcloud.sdk.core.session.model.SessionState
import io.reactivex.rxjava3.processors.BehaviorProcessor
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.module.AmityModuleDefinition
import com.amity.socialcloud.sdk.model.core.module.AmityModuleEnforcementMode
import com.amity.socialcloud.sdk.model.core.module.AmityModuleSettings
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Spec: UIKit module availability REQ-003a · TC-uikit-mavail-012a.
 *
 * The setup() read answers from the row a previous launch wrote. On a first
 * install there is none — the fetch that writes one starts on session
 * establishment, after setup() has returned — so a gate that only seeded
 * itself is fully open for that whole launch and tells the truth on the next.
 *
 * This is that launch: no row at setup, the row arriving later, and the gate
 * expected to change without a relaunch.
 */
class AmityModuleEntitlementArrivalTest {

    private val rows = PublishProcessor.create<AmityModuleSettings>()
    private val sessions = BehaviorProcessor.createDefault<SessionState>(SessionState.Established)

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
        // The row subscription subscribes on io; a processor that emits before
        // it lands would drop the row and read as a gate that never listened.
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        mockkObject(AmityUIKitRealtimeSubscriptions)
        every { AmityUIKitRealtimeSubscriptions.sync() } returns Unit
        mockkObject(AmityCoreClient)
        every { AmityCoreClient.getModuleSettings() } returns rows
        every { AmityCoreClient.observeSessionState() } returns sessions
        AmityUIKitConfigController.setConfigForTesting(
            Gson().fromJson("""{"excludes": []}""", AmityUIKitConfig::class.java),
        )
        AmityUIKitConfigController.setModuleEntitlementForTesting(null)
    }

    @After
    fun tearDown() {
        AmityUIKitConfigController.setModuleEntitlementForTesting(null)
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
        unmockkAll()
    }

    private fun enforcing(vararg granted: String): AmityModuleSettings {
        val chains = mapOf(
            "community" to emptyList<String>(),
            "chat" to emptyList(),
            "post" to listOf("community"),
        )
        return mockk<AmityModuleSettings>().also {
            every { it.enforcement } returns AmityModuleEnforcementMode.ENFORCE
            every { it.modules } returns granted.associateWith { true }
            every { it.catalog } returns chains.mapValues { (_, requires) ->
                mockk<AmityModuleDefinition>().also { d -> every { d.requires } returns requires }
            }
        }
    }

    @Test
    fun `a row written after setup reaches the gate without a relaunch`() {
        AmityUIKitConfigController.readModuleEntitlements()

        // The first install: nothing to read, so nothing is withheld.
        assertFalse(AmityUIKitConfigController.isExcluded("chat_page/*/*"))

        rows.onNext(enforcing("community", "post"))

        assertTrue(
            "the gate is still answering from the empty snapshot it was built with",
            AmityUIKitConfigController.isExcluded("chat_page/*/*"),
        )
        assertFalse(
            AmityUIKitConfigController.isExcluded("post_detail_page/*/*"),
        )
    }

    @Test
    fun `a later row replaces the one before it`() {
        AmityUIKitConfigController.readModuleEntitlements()
        rows.onNext(enforcing("community", "post"))
        assertTrue(AmityUIKitConfigController.isExcluded("chat_page/*/*"))

        rows.onNext(enforcing("community", "post", "chat"))
        assertFalse(
            "the exclusion memo outlived the grants it was built on",
            AmityUIKitConfigController.isExcluded("chat_page/*/*"),
        )
    }

    @Test
    fun `resubscribing leaves one subscription, not two`() {
        // setup() runs again on a network switch, and a leaked subscription
        // would rebuild the snapshot twice per row for the rest of the process.
        AmityUIKitConfigController.readModuleEntitlements()
        AmityUIKitConfigController.readModuleEntitlements()
        // After both: each read first forgets what it held, and says so.
        var fired = 0
        AmityUIKitConfigController.registerChangeCallback("arrival-test") { fired++ }

        rows.onNext(enforcing("community", "post"))

        assertEquals(1, fired)
        AmityUIKitConfigController.unregisterChangeCallback("arrival-test")
    }

    // A log-out drops the row with the store, and a single-row query emits
    // nothing for an absent row — so the gate has to forget on its own, or the
    // next user is gated by the last network's grants.
    @Test
    fun `a log-out forgets the held grants`() {
        AmityUIKitConfigController.readModuleEntitlements()
        rows.onNext(enforcing("community", "post"))
        assertTrue(AmityUIKitConfigController.isExcluded("chat_page/*/*"))

        sessions.onNext(SessionState.NotLoggedIn)

        assertFalse(
            "the last network's grants outlived the log-out",
            AmityUIKitConfigController.isExcluded("chat_page/*/*"),
        )
    }

    @Test
    fun `the same row written again after a log-out is applied`() {
        // distinctUntilChanged on the old subscription would swallow it: a
        // login back into the same network writes an identical row.
        AmityUIKitConfigController.readModuleEntitlements()
        val row = enforcing("community", "post")
        rows.onNext(row)
        sessions.onNext(SessionState.NotLoggedIn)
        sessions.onNext(SessionState.Established)

        rows.onNext(row)

        assertTrue(AmityUIKitConfigController.isExcluded("chat_page/*/*"))
    }

    @Test
    fun `the session state at subscription is not a log-out`() {
        sessions.onNext(SessionState.NotLoggedIn)
        AmityUIKitConfigController.readModuleEntitlements()
        rows.onNext(enforcing("community", "post"))
        assertTrue(AmityUIKitConfigController.isExcluded("chat_page/*/*"))
    }
}
