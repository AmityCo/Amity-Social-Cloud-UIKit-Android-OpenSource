package com.amity.socialcloud.uikit

import android.content.Context
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.community.compose.search.components.AmityUserSearchResultComponentBehavior
import com.amity.socialcloud.uikit.community.profile.activity.AmityUserProfileActivity

class AmityCustomUserSearchResultComponentBehavior : AmityUserSearchResultComponentBehavior() {
    override fun goToUserProfilePage(context: Context, user: AmityUser) {
        val intent = AmityUserProfileActivity.newIntent(
            context = context,
            id = user.getUserId(),
        )
        context.startActivity(intent)
    }
}