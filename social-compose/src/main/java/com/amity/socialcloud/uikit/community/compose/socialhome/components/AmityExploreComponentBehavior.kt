package com.amity.socialcloud.uikit.community.compose.socialhome.components

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity

open class AmityExploreComponentBehavior {

    open fun goToCommunityProfilePage(
        context: Context,
        communityId: String
    ) {
        val intent = AmityCommunityProfilePageActivity.newIntent(
            context = context,
            communityId = communityId
        )
        context.startActivity(intent)
    }
}