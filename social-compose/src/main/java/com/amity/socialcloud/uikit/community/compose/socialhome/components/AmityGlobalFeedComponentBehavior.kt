package com.amity.socialcloud.uikit.community.compose.socialhome.components

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehaviorContext
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageActivity
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageType
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageMode
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity

open class AmityGlobalFeedComponentBehavior {

    class Context(
        val pageContext: android.content.Context,
        val target: AmityPost.Target? = null,
    ) : AmityBaseBehaviorContext(pageContext)

    open fun goToPostDetailPage(
        context: Context,
        id: String,
        category: AmityPostCategory = AmityPostCategory.GENERAL
    ) {
        val intent = AmityPostDetailPageActivity.newIntent(
            context = context.pageContext,
            id = id,
            category = category
        )
        context.pageContext.startActivity(intent)
    }

    open fun goToPostDetailPage(
        context: android.content.Context,
        id: String,
        category: AmityPostCategory = AmityPostCategory.GENERAL
    ) {
        val intent = AmityPostDetailPageActivity.newIntent(
            context = context,
            id = id,
            category = category
        )
        context.startActivity(intent)
    }

    open fun goToCreateCommunityPage(
        context: android.content.Context,
    ) {
        val intent = AmityCommunitySetupPageActivity.newIntent(
            context = context,
            mode = AmityCommunitySetupPageMode.Create
        )
        context.startActivity(intent)
    }

    open fun goToUserProfilePage(
        context: android.content.Context,
        userId: String
    ) {
        val intent = AmityUserProfilePageActivity.newIntent(
            context = context,
            userId = userId
        )
        context.startActivity(intent)
    }

    open fun goToClipFeedPage(
        context: android.content.Context,
        postId: String,
    ) {
        val intent = AmityClipFeedPageActivity.newIntent(
            context = context,
            type = AmityClipFeedPageType.NewsFeed(postId)
        )
        context.startActivity(intent)
    }
}