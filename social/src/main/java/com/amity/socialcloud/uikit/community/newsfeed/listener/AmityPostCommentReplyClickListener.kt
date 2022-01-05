package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.social.comment.AmityComment

interface AmityPostCommentReplyClickListener {
    fun onClickCommentReply(comment: AmityComment, position: Int)
}