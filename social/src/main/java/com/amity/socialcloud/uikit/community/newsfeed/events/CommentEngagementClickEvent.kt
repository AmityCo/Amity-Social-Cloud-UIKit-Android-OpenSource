package com.amity.socialcloud.uikit.community.newsfeed.events

import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.sdk.social.feed.AmityPost

sealed class CommentEngagementClickEvent {

    class Reaction(val comment: AmityComment, val isAdding: Boolean) : CommentEngagementClickEvent()

    class Reply(val comment: AmityComment, val post: AmityPost?) : CommentEngagementClickEvent()

}