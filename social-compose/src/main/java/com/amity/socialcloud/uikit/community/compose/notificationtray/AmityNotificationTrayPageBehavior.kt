package com.amity.socialcloud.uikit.community.compose.notificationtray

import android.content.Context
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity

open class AmityNotificationTrayPageBehavior {

    open fun goToCommunityProfilePage(
        context: Context,
        communityId: String,
    ) {
        val intent = AmityCommunityProfilePageActivity.newIntent(
            context = context,
            communityId = communityId
        )
        context.startActivity(intent)
    }

    open fun goToPostDetailPage(
        context: Context,
        postId: String,
        category: AmityPostCategory = AmityPostCategory.GENERAL,
        commentId: String? = null,
        parentId: String? = null,
    ) {
        val intent = AmityPostDetailPageActivity.newIntent(
            context = context,
            id = postId,
            category = category,
            commentId = commentId,
            parentId = parentId
        )
        context.startActivity(intent)
    }
}