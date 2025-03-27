package com.amity.socialcloud.uikit.community.compose.user.profile

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehavior
import com.amity.socialcloud.uikit.community.compose.livestream.create.AmityCreateLivestreamPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerOptions
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.post.composer.poll.AmityPollPostComposerPageActivity
import com.amity.socialcloud.uikit.community.compose.user.blocked.AmityBlockedUsersPageActivity
import com.amity.socialcloud.uikit.community.compose.user.edit.AmityEditUserProfilePageActivity

open class AmityUserProfilePageBehavior : AmityBaseBehavior() {

    open fun goToEditUserPage(
        context: Context,
    ) {
        val intent = AmityEditUserProfilePageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }

    open fun goToBlockedUsersPage(
        context: Context,
    ) {
        val intent = AmityBlockedUsersPageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }

    open fun goToPostComposerPage(
        context: Context,
        userId: String,
    ) {
        val intent = AmityPostComposerPageActivity.newIntent(
            context = context,
            options = AmityPostComposerOptions.AmityPostComposerCreateOptions(
                targetId = userId,
                targetType = AmityPostTargetType.USER,
            )
        )
        context.startActivity(intent)
    }

    open fun goToPollComposerPage(
        context: Context,
        userId: String,
    ) {
        val intent = AmityPollPostComposerPageActivity.newIntent(
            context = context,
            targetId = userId,
            targetType = AmityPost.TargetType.USER,
        )
        context.startActivity(intent)
    }

    open fun goToLivestreamPostComposerPage(
        context: Context,
        targetId: String,
        targetType: AmityPost.TargetType,
        community: AmityCommunity? = null,
    ) {
        val intent = AmityCreateLivestreamPageActivity.newIntent(
            context = context,
            targetId = targetId,
            targetType = targetType,
            community = community,
        )
        context.startActivity(intent)
    }
}