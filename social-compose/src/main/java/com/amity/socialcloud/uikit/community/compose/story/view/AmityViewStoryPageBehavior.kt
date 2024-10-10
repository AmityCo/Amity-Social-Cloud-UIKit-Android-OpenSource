package com.amity.socialcloud.uikit.community.compose.story.view

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageActivity

open class AmityViewStoryPageBehavior {

    open fun goToCreateStoryPage(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        targetId: String,
        targetType: AmityStory.TargetType,
    ) {
        val intent = AmityCreateStoryPageActivity.newIntent(
            context = context,
            targetId = targetId,
            targetType = targetType,
        )
        launcher.launch(intent)
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

}