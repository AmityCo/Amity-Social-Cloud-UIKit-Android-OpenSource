package com.amity.socialcloud.uikit.common.ad

import com.amity.socialcloud.sdk.model.core.ad.AmityAd
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.social.story.AmityStory

sealed class AmityListItem {

    data class CommentItem(val comment: AmityComment) : AmityListItem()
    data class PostItem(val post: AmityPost) : AmityListItem()

    data class StoryItem(val story: AmityStory) : AmityListItem()
    data class AdItem(val ad: AmityAd) : AmityListItem()

    object Separator : AmityListItem()
}