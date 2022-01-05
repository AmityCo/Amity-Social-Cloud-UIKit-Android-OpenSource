package com.amity.socialcloud.uikit.community.newsfeed.events

import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.sdk.social.feed.AmityPost

sealed class CommentContentClickEvent {

    class Text(val comment: AmityComment, val post: AmityPost?): CommentContentClickEvent()

}