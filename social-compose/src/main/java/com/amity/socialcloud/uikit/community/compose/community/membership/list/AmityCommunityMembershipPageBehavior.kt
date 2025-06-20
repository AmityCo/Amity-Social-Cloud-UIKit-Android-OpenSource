package com.amity.socialcloud.uikit.community.compose.community.membership.list

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.membership.add.AmityCommunityAddMemberPageActivity
import com.amity.socialcloud.uikit.community.compose.community.membership.invite.AmityCommunityInviteMemberPageActivity
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity

open class AmityCommunityMembershipPageBehavior {

    class Context(
        val pageContext: android.content.Context,
        val launcher: ActivityResultLauncher<Intent>? = null,
        val userId: String? = null,
        val community: AmityCommunity? = null,
    )

    open fun goToAddMemberPage(context: Context) {
        val intent = AmityCommunityAddMemberPageActivity.newIntent(
            context = context.pageContext,
            users = emptyList(),
        )
        context.launcher?.launch(intent)
    }

    open fun goToUserProfilePage(context: Context) {
        context.userId?.let { userId ->
            val intent = AmityUserProfilePageActivity.newIntent(
                context = context.pageContext,
                userId = userId,
            )
            context.pageContext.startActivity(intent)
        }
    }

    open fun goToInviteMemberPage(context: Context) {
        context.community?.let { community ->
            val intent = AmityCommunityInviteMemberPageActivity.newIntentWithCommunity(
                context = context.pageContext,
                community = community,
            )
            context.launcher?.launch(intent)
        }
    }
}