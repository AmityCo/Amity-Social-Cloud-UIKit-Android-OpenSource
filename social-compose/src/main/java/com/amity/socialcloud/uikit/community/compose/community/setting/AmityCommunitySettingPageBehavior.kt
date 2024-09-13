package com.amity.socialcloud.uikit.community.compose.community.setting

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehavior
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehaviorContext
import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.AmityCommunityNotificationSettingPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setting.post.AmityCommunityPostPermissionPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setting.story.AmityCommunityStorySettingPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageMode

open class AmityCommunitySettingPageBehavior : AmityBaseBehavior() {

    class Context(
        val pageContext: android.content.Context,
        val activityLauncher: ActivityResultLauncher<Intent>? = null,
        val community: AmityCommunity? = null,
    ) : AmityBaseBehaviorContext(pageContext, activityLauncher)

    open fun goToEditCommunityPage(context: Context) {
        val intent = AmityCommunitySetupPageActivity.newIntent(
            context = context.pageContext,
            mode = AmityCommunitySetupPageMode.Edit(community = context.community!!)
        )
        context.launchActivity(intent)
    }

    open fun goToMembershipPage(context: Context) {
        val intent = AmityCommunityMembershipPageActivity.newIntent(
            context = context.pageContext,
            community = context.community!!,
        )
        context.launchActivity(intent)
    }

    open fun goToNotificationPage(context: Context) {
        val intent = AmityCommunityNotificationSettingPageActivity.newIntent(
            context = context.pageContext,
            community = context.community!!,
        )
        context.launchActivity(intent)
    }

    open fun goToPostPermissionPage(context: Context) {
        val intent = AmityCommunityPostPermissionPageActivity.newIntent(
            context = context.pageContext,
            community = context.community!!,
        )
        context.launchActivity(intent)
    }

    open fun goToStorySettingPage(context: Context) {
        val intent = AmityCommunityStorySettingPageActivity.newIntent(
            context = context.pageContext,
            community = context.community!!,
        )
        context.launchActivity(intent)
    }
}