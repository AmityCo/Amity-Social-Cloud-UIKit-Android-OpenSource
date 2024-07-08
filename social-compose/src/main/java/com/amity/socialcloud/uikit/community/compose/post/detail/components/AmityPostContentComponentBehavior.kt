package com.amity.socialcloud.uikit.community.compose.post.detail.components

import android.content.Context
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerOptions
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageActivity

open class AmityPostContentComponentBehavior {

    open fun goToCommunityProfilePage(
        context: Context,
        community: AmityCommunity,
    ) {
        //  do nothing, need to override in social module to access community profile page
    }

    open fun goToUserProfilePage(
        context: Context,
        user: AmityUser,
    ) {
        //  do nothing, need to override in social module to access user profile page
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