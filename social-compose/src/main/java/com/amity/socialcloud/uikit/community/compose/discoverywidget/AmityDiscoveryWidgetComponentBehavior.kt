package com.amity.socialcloud.uikit.community.compose.discoverywidget

import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehaviorContext

open class AmityDiscoveryWidgetComponentBehavior {

    class Context(
        val pageContext: android.content.Context,
    ) : AmityBaseBehaviorContext(pageContext)

    /**
     * Where a tapped card goes. The topic travels with the post so an override can route per topic
     * without the host threading that through its own state.
     *
     * There is no default: the destination belongs to the page embedding the widget, which the SDK
     * cannot know. Doing nothing is the honest default -- navigating somewhere arbitrary would be
     * worse than not moving.
     */
    open fun goToDestination(
        context: Context,
        topicId: String,
        post: AmityPost,
    ) {
    }
}
