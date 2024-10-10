package com.amity.socialcloud.uikit.community.compose.user.relationship

import android.content.Context
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehavior
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity

open class AmityUserRelationshipPageBehavior : AmityBaseBehavior() {

    open fun goToUserProfilePage(
        context: Context,
        userId: String,
    ) {
        val intent = AmityUserProfilePageActivity.newIntent(
            context = context,
            userId = userId
        )
        context.startActivity(intent)
    }
}