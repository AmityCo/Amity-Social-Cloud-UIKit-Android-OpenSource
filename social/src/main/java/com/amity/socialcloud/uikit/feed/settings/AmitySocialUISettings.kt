package com.amity.socialcloud.uikit.social

import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityGlobalCommunityClickListener
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityGlobalUserClickListener
import com.amity.socialcloud.uikit.feed.settings.AmityDefaultPostViewHolders
import com.amity.socialcloud.uikit.feed.settings.AmityPostRenderer
import com.amity.socialcloud.uikit.feed.settings.AmityPostShareClickListener
import com.amity.socialcloud.uikit.feed.settings.AmityPostSharingSettings
import com.amity.socialcloud.uikit.markup.PostMarkupProcessor

object AmitySocialUISettings {

    var postShareClickListener: AmityPostShareClickListener = object : AmityPostShareClickListener {}

    var postSharingSettings = AmityPostSharingSettings()

    var globalUserClickListener: AmityGlobalUserClickListener = object : AmityGlobalUserClickListener {}

    internal var globalCommunityClickListener: AmityGlobalCommunityClickListener = object : AmityGlobalCommunityClickListener {}

    private var postViewHolders: MutableMap<String, AmityPostRenderer> =
        AmityDefaultPostViewHolders.getDefaultMap()

    lateinit var postMarkupProcessor: PostMarkupProcessor

    fun init(markupProcessor: PostMarkupProcessor) {
        postMarkupProcessor = markupProcessor
    }

    fun registerPostRenderers(renderers: List<AmityPostRenderer>) {
        renderers.forEach { renderer ->
            postViewHolders[renderer.getDataType()] = renderer
        }
    }

    internal fun getViewHolder(dataType: String): AmityPostRenderer {
        return postViewHolders[dataType] ?: AmityDefaultPostViewHolders.unknownViewHolder
    }

    internal fun getViewHolder(viewType: Int): AmityPostRenderer {
        for (viewHolder in postViewHolders.values.toList()) {
            if (viewType == viewHolder.getDataType().hashCode()) {
                return viewHolder
            }
        }
        return AmityDefaultPostViewHolders.unknownViewHolder
    }

}
