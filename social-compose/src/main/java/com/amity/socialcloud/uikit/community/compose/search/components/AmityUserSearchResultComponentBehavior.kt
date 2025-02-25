package com.amity.socialcloud.uikit.community.compose.search.components

import android.content.Context
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity

open class AmityUserSearchResultComponentBehavior {

    open fun goToUserProfilePage(
        context: Context,
        user: AmityUser,
    ) {
        val intent = AmityUserProfilePageActivity.newIntent(
            context = context,
            userId = user.getUserId(),
        )
        context.startActivity(intent)
    }
}