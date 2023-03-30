package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.comment.AmityComment


interface AmityPostCommentItemClickListener {
    fun onClickItem(comment: AmityComment, position: Int)
    fun onClickAvatar(user: AmityUser)
}