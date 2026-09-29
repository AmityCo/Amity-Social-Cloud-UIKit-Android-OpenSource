package com.amity.socialcloud.uikit.common.config

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.events.AmityAutoSubscription
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Keeps the SDK's managed realtime subscriptions in step with the module flags.
 *
 * The SDK opens four MQTT subscriptions of its own the moment a session is
 * established — chat's three smart-feed topics, the livestream topic, and the two
 * follow membership wildcards — and it does so whether or not the UIKit shows a
 * surface for any of them. A view gate cannot reach that: the subscription is
 * asked for by the session, not by a screen. So with Chat switched off a network
 * trace still showed the app subscribing to chat topics, which is exactly the
 * thing rule 6 says must not happen.
 *
 * The registry records an opt-out and its `applyAll` skips it on every
 * (re)connect, so calling this before the session establishes means the subscribe
 * is never sent at all rather than sent and then withdrawn. That is why this runs
 * from config load and from every override change, not from a session callback.
 *
 * NETWORK is deliberately absent. It carries account-level events that belong to
 * the base layer no module owns, so no flag may switch it off.
 */
internal object AmityUIKitRealtimeSubscriptions {

    private val OWNERS = mapOf(
        AmityAutoSubscription.CHAT to AmityUIKitFeature.CHAT,
        AmityAutoSubscription.LIVESTREAM to AmityUIKitFeature.LIVE,
        AmityAutoSubscription.BLOCK to AmityUIKitFeature.USER_RELATIONSHIP,
    )

    private var sessionWatch: Disposable? = null

    /**
     * The registry is reset to its defaults when the MQTT client is disposed, so
     * an opt-out recorded at config load does not survive a reconnect: the next
     * `applyAll` re-subscribes everything a system handle defaults to. Watching
     * the session and reconciling again is what keeps a switched-off module off
     * for the life of the app rather than only until the first reconnect.
     */
    fun watchSession() {
        if (sessionWatch != null) return
        sessionWatch = AmityCoreClient.observeSessionState()
            .subscribeOn(Schedulers.io())
            .filter { it is SessionState.Established }
            .subscribe({ sync() }, {})
    }

    fun sync() {
        val active = runCatching {
            AmityCoreClient.autoSubscriptions().associate { it.feature to it.active }
        }.getOrElse { return }

        OWNERS.forEach { (subscription, feature) ->
            val wanted = AmityUIKitConfigController.isFeatureEnabled(feature)
            // A handle already in the state we want must be left alone: the
            // registry is ref-counted, and a redundant subscribe would take a
            // second ref that the matching unsubscribe would then not release.
            if (active[subscription] == wanted) return@forEach

            val call = if (wanted) {
                AmityCoreClient.subscribeAuto(subscription)
            } else {
                AmityCoreClient.unsubscribeAuto(subscription)
            }
            call.subscribeOn(Schedulers.io())
                .onErrorComplete()
                .subscribe()
        }
    }
}
