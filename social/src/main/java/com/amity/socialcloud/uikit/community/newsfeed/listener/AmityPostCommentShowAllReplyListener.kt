package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.model.social.comment.AmityComment

interface AmityPostCommentShowAllReplyListener {
    fun onClickShowAllReplies(comment: AmityComment, position: Int)
}