package com.amity.socialcloud.uikit.community.compose.search.components

import android.content.Context
import com.amity.socialcloud.sdk.model.core.user.AmityUser

open class AmityUserSearchResultComponentBehavior {

    open fun goToUserProfilePage(
        context: Context,
        user: AmityUser,
    ) {
        //  do nothing, need to override in social module to access user profile page
    }
}