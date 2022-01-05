package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.social.comment.AmityComment

interface AmityPostCommentShowMoreActionListener {
    fun onClickNewsFeedCommentShowMoreAction(comment: AmityComment, position: Int)
}