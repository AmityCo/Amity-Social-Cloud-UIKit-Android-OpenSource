package com.amity.socialcloud.uikit.community.compose.community.profile.component

import android.content.Context
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerOptions
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostPriority

open class AmityCommunityProfilePageBehavior {

    open fun goToPostDetailPage(
        context: Context,
        postId: String,
        priority: AmityPostPriority = AmityPostPriority.GENERAL,
    ) {
        val intent = AmityPostDetailPageActivity.newIntent(
            context = context,
            id = postId,
            priority = priority,
            hideTarget = true,
        )
        context.startActivity(intent)
    }
    
    fun goToCreatePostPage(
        context: Context,
        communityId: String,
    ) {
        val intent = AmityPostComposerPageActivity.newIntent(
            context = context,
            options = AmityPostComposerOptions.AmityPostComposerCreateOptions(
                targetId = communityId,
                targetType = AmityPostTargetType.COMMUNITY,
            )
        )
        context.startActivity(intent)
    }
    
    open fun goToCommunitySettingPage(
        context: Context,
        communityId: String,
    ) {
    
    }
    
    open fun goToPostReviewPage(
        context: Context,
        communityId: String,
    ) {
    
    }
}