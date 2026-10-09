package com.amity.socialcloud.uikit.common.infra.networklog

import com.amity.socialcloud.sdk.model.core.network.AmityNetworkActivity

/**
 * Seam between UIKit-originated network traffic and whatever is collecting it.
 *
 * Some signals the network log needs cannot be tapped from the SDK: image loads go through
 * the UIKit's own Coil OkHttpClient, and VOD/room HLS goes through UIKit-constructed
 * ExoPlayer data sources. Those capture points therefore live in published library code —
 * but the viewer does not.
 *
 * This bridge keeps that honest: with no sink attached, [isActive] is false and every seam
 * returns before allocating anything, so a customer application pays nothing for code it
 * never uses (NFR-1). The sample app attaches a sink; nothing else does.
 *
 * Spec: front-end-tech-specs/UIKIT/components/AmityNetworkLogOverlay/v1.md
 */
object AmityNetworkCaptureBridge {

    @Volatile
    private var sink: ((AmityNetworkActivity) -> Unit)? = null

    /** Read by every UIKit capture seam before doing any work. */
    val isActive: Boolean
        get() = sink != null

    fun attach(sink: (AmityNetworkActivity) -> Unit) {
        this.sink = sink
    }

    fun detach() {
        this.sink = null
    }

    fun record(activity: AmityNetworkActivity) {
        sink?.invoke(activity)
    }
}
