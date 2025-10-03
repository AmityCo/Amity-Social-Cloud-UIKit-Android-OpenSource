package com.amity.socialcloud.uikit.community.compose.comment

import android.content.Context
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehavior
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity

open class AmityCommentTrayComponentBehavior : AmityBaseBehavior() {

    open fun goToUserProfilePage(context: Context, userId: String) {
        val intent = AmityUserProfilePageActivity.newIntent(
            context = context,
            userId = userId,
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