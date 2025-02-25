package com.amity.socialcloud.uikit.community.compose.community.membership.list

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.uikit.community.compose.community.membership.add.AmityCommunityAddMemberPageActivity
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity

open class AmityCommunityMembershipPageBehavior {

    class Context(
        val pageContext: android.content.Context,
        val launcher: ActivityResultLauncher<Intent>? = null,
        val userId: String? = null,
    )

    open fun goToAddMemberPage(context: Context) {
        val intent = AmityCommunityAddMemberPageActivity.newIntent(
            context = context.pageContext,
            users = emptyList(),
        )
        context.launcher?.launch(intent)
    }

    open fun goToUserProfilePage(context: Context) {
        val intent = AmityUserProfilePageActivity.newIntent(
            context = context.pageContext,
            userId = context.userId!!,
        )
        context.pageContext.startActivity(intent)
    }
}