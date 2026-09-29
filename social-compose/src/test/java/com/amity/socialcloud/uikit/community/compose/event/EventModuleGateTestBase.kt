@file:OptIn(AmityUIKitInternalApi::class)
package com.amity.socialcloud.uikit.community.compose.event

import com.amity.socialcloud.uikit.community.compose.ModuleEntitlementForTests

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.config.AmityUIKitInternalApi
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * The event screens' doors into Post and Live.
 *
 * Each surface under test renders on a page Events owns, so the page gate never
 * asks about Post or Live: the surface has to ask itself. The tests render the
 * real composables with a module withheld and look for what a user would see.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
abstract class EventModuleGateTestBase {

    @get:Rule
    val composeTestRule = createComposeRule()

    protected fun withhold(vararg keys: String) =
        ModuleEntitlementForTests.withhold(*keys)

    @Before
    fun setUpGate() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        AmityUIKitConfigController.setup(context)
        // Without it every label renders as its key and no text assertion can match.
        DefaultAmitySocialStringProvider.initialize(context)
    }

    @After
    fun tearDownGate() {
        ModuleEntitlementForTests.clear()
    }
}
