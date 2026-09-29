package com.amity.socialcloud.uikit

import android.util.Log
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.encryption.AmityDBEncryption
import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint
import com.amity.socialcloud.sdk.video.AmityStreamBroadcasterClient
import com.amity.socialcloud.sdk.video.AmityStreamPlayerClient
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.amity.socialcloud.uikit.common.ad.AmityAdEngine
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.config.AmityUIKitFeature
import com.amity.socialcloud.uikit.common.config.AmityUIKitModuleAvailability
import com.amity.socialcloud.uikit.common.eventbus.NetworkConnectionEventPublisher
import com.amity.socialcloud.uikit.common.infra.db.AmityUIKitDB
import com.amity.socialcloud.uikit.common.infra.initializer.AmityAppContext
import com.amity.socialcloud.uikit.common.networkconfig.AmityNetworkConfigService
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageBehavior
import com.amity.socialcloud.uikit.community.compose.visitor.AmityVisitorUsageLimitObserver
import io.reactivex.rxjava3.core.Completable

object AmityUIKit4Manager {

    private const val BUILD_INFO_TAG = "AmityUIKit"

    val behavior = AmityUIKit4Behavior()

    fun setup(
        apiKey: String,
        endpoint: AmityEndpoint,
        dbEncryption: AmityDBEncryption = AmityDBEncryption.NONE
    ) {
        AmityUIKitDB.init()
        // Before AmityCoreClient.setup, because the SDK starts talking during it.
        // The push contract re-registers a stored device token as soon as it is
        // constructed, so the module flags have to be readable — and the push
        // suppression already set — before that happens. Only reads the config
        // asset and the UIKit database, both available by now.
        AmityUIKitConfigController.setup(AmityAppContext.getContext())
        AmityCoreClient.setup(
            apiKey = apiKey,
            endpoint = endpoint,
            dbEncryption = dbEncryption
        )
        // After AmityCoreClient.setup, because the read needs the SDK's database
        // open. The UIKit reads; it never fetches — the SDK refreshes its own
        // row on session establishment and the read is pushed the new one.
        AmityUIKitConfigController.readModuleEntitlements()
        AmityStreamBroadcasterClient.setup(AmityCoreClient.getConfiguration())
        AmityStreamPlayerClient.setup(AmityCoreClient.getConfiguration())
        AmityAdEngine.init()
        AmityVisitorUsageLimitObserver.init()
        AmityNetworkConfigService.init(apiKey)
        AmityUIKitConfigController.initializeShareableLinkPattern()
        DefaultAmitySocialStringProvider.initialize(AmityAppContext.getContext())
        DefaultAmityChatStringProvider.initialize(AmityAppContext.getContext())
        NetworkConnectionEventPublisher.initPublisher(context = AmityAppContext.getContext())
        logBuildInfo()
    }

    /**
     * One line at startup naming exactly what is running: the UIKit version, the SDK it resolved
     * against, and the commit the UIKit was built from. A bug report that quotes this identifies
     * the build without anyone having to guess which branch or artifact produced it.
     *
     * The SDK version is read at runtime rather than baked in, so it reports the artifact actually
     * on the classpath -- which is the point when a composite build or a version override has
     * replaced the declared one. The commit hash is "unknown" when the artifact was built from a
     * tree without git.
     */
    private fun logBuildInfo() {
        runCatching {
            Log.i(
                BUILD_INFO_TAG,
                "UIKit ${BuildConfig.AMITY_UIKIT_VERSION} " +
                        "(${BuildConfig.AMITY_UIKIT_COMMIT_HASH}) | " +
                        "SDK ${AmityCoreClient.getAmityCoreSdkVersion()}"
            )
        }
    }

    /**
     * Whether a module is available in this app: granted by the network's plan,
     * with the catalog's prerequisites resolved over the grants.
     *
     * The plan is the one source that withholds a module, so this is the whole
     * answer rather than half of one a host has to combine. It is the same question
     * every gated page, component and element already asks, so a host reading it
     * sees exactly what the UIKit will do.
     *
     * Available is the answer before anything has been read — no config, no
     * session, no row — because the gate is asked while views are being built
     * and failing closed would blank paid surfaces on every cold start.
     */
    fun isModuleAvailable(module: AmityUIKitFeature): Boolean {
        return AmityUIKitConfigController.isFeatureEnabled(module)
    }

    /**
     * The same answer with its reason. `NotGranted` is about this module and
     * `PrerequisiteUnavailable` about another, so the gate reports which it was
     * rather than letting a customer ask for something they already have.
     */
    fun moduleAvailability(module: AmityUIKitFeature): AmityUIKitModuleAvailability {
        return AmityUIKitConfigController.moduleAvailability(module)
    }

    /** Every module this UIKit gates, for a host rendering a settings screen. */
    fun moduleAvailability(): List<AmityUIKitModuleAvailability> {
        return AmityUIKitConfigController.moduleAvailabilities()
    }

    fun syncNetworkConfig(): Completable {
        return AmityNetworkConfigService.syncNetworkConfig()
    }
}