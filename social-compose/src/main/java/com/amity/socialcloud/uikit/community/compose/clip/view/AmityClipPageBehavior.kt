package com.amity.socialcloud.uikit.community.compose.clip.view

import android.content.Context
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.clip.create.AmityCreateClipPageActivity
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageMode
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.target.AmityPostTargetSelectionPageType
import com.amity.socialcloud.uikit.community.compose.target.post.AmityPostTargetSelectionPageActivity
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity

open class AmityClipPageBehavior {

    open fun goToClipPostComposerPage(
        context: Context,
        targetId: String? = null,
        targetType: AmityPostTargetType,
    ) {
        val intent = AmityCreateClipPageActivity.newIntent(
            context = context,
            targetId = targetId ?: "",
            targetType = targetType,
        )
        context.startActivity(intent)
    }

    open fun goToUserProfilePage(context: Context, userId: String) {
        val intent = AmityUserProfilePageActivity.newIntent(
            context = context,
            userId = userId,
        )
        context.startActivity(intent)
    }

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


    open fun goToPostDetailPage(
        context: Context,
        postId: String,
    ) {
        val intent = AmityPostDetailPageActivity.newIntent(
            context = context,
            id = postId,
            hideTarget = true,
        )
        context.startActivity(intent)
    }

    open fun goToSelectPostTargetPage(
        context: Context,
        type: AmityPostTargetSelectionPageType
    ) {
        val intent = AmityPostTargetSelectionPageActivity.newIntent(
            context = context,
            type = type,
        )
        context.startActivity(intent)
    }

    open fun goToCreateCommunityPage(
        context: Context,
    ) {
        val intent = AmityCommunitySetupPageActivity.newIntent(
            context = context,
            mode = AmityCommunitySetupPageMode.Create
        )
        context.startActivity(intent)
    }

    open fun handleVisitorUserAction() {
        AmitySocialBehaviorHelper.globalBehavior.handleVisitorUserAction()
    }

    open fun handleNonMemberAction() {
        AmitySocialBehaviorHelper.globalBehavior.handleNonMemberAction()
    }

    open fun handleNonFollowerAction() {
        AmitySocialBehaviorHelper.globalBehavior.handleNonFollowerAction()
    }
}