package com.amity.socialcloud.uikit.community.compose.community.setup

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.community.compose.community.category.AmityCommunityAddCategoryPageActivity
import com.amity.socialcloud.uikit.community.compose.community.membership.add.AmityCommunityAddMemberPageActivity
import com.amity.socialcloud.uikit.community.compose.community.membership.invite.AmityCommunityInviteMemberPageActivity
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.compose.community.setting.AmityCommunitySettingPageBehavior.Context

open class AmityCommunitySetupPageBehavior {

    class Context(
        val pageContext: android.content.Context,
        val launcher: ActivityResultLauncher<Intent>? = null,
        val communityId: String? = null,
        val categories: List<AmityCommunityCategory> = emptyList(),
        val users: List<AmityUser> = emptyList(),
        val isCommunityJustCreated: Boolean = false,
    )

    open fun goToCommunityProfilePage(context: Context) {
        val intent = AmityCommunityProfilePageActivity.newIntent(
            context = context.pageContext,
            communityId = context.communityId!!,
            isCommunityJustCreated = context.isCommunityJustCreated,
        )
        context.pageContext.startActivity(intent)
    }

    open fun goToAddCategoryPage(context: Context) {
        val intent = AmityCommunityAddCategoryPageActivity.newIntent(
            context = context.pageContext,
            categories = context.categories,
        )
        context.launcher?.launch(intent)
    }

    open fun goToAddMemberPage(context: Context) {
        val intent = AmityCommunityAddMemberPageActivity.newIntent(
            context = context.pageContext,
            users = context.users,
        )
        context.launcher?.launch(intent)
    }

    open fun goToPendingInvitationsPage(context: Context) {
        val intent = AmityCommunityInviteMemberPageActivity.newIntent(
            context = context.pageContext,
            users = context.users,
        )
        context.launcher?.launch(intent)
    }
}