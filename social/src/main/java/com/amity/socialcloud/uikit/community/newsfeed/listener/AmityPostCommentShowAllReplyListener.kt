package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.social.comment.AmityComment

interface AmityPostCommentShowAllReplyListener {
    fun onClickShowAllReplies(comment: AmityComment, position: Int)
}