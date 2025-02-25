package com.amity.socialcloud.uikit.community.compose.post.detail.components

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerOptions
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageActivity
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity

open class AmityPostContentComponentBehavior {

    open fun goToCommunityProfilePage(
        context: Context,
        community: AmityCommunity,
    ) {
        val intent = AmityCommunityProfilePageActivity.newIntent(
            context = context,
            communityId = community.getCommunityId(),
        )
        context.startActivity(intent)
    }

    open fun goToUserProfilePage(
        context: Context,
        userId: String,
    ) {
        val intent = AmityUserProfilePageActivity.newIntent(
            context = context,
            userId = userId,
        )
        context.startActivity(intent)
    }

    open fun goToPostComposerPage(
        context: Context,
        post: AmityPost,
    ) {
        val intent = AmityPostComposerPageActivity.newIntent(
            context = context,
            options = AmityPostComposerOptions.AmityPostComposerEditOptions(post = post)
        )
        context.startActivity(intent)
    }
}