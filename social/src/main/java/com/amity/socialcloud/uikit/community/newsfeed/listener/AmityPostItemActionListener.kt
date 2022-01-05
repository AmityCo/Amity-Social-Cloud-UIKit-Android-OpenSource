package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.feed.AmityPost

interface AmityPostItemActionListener {
    fun onFeedAction(feed: AmityPost, position: Int)
    fun onClickItem(postId: String, position: Int)
    fun onCommentAction(feed: AmityPost, comment: AmityComment, position: Int)
    fun showAllReply(feed: AmityPost, comment: AmityComment, position: Int)
    fun onLikeAction(liked: Boolean, ekoPost: AmityPost, position: Int)
    fun onShareAction(ekoPost: AmityPost, position: Int)
    fun onClickUserAvatar(feed: AmityPost, user: AmityUser, position: Int)
    fun onClickCommunity(community: AmityCommunity)
}