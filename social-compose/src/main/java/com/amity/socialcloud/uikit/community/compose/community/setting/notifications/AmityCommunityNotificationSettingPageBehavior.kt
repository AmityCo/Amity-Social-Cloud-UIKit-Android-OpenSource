package com.amity.socialcloud.uikit.community.compose.community.setting.notifications

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehaviorContext
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.comments.AmityCommunityCommentsNotificationSettingPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.posts.AmityCommunityPostsNotificationSettingPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.stories.AmityCommunityStoriesNotificationSettingPageActivity

open class AmityCommunityNotificationSettingPageBehavior {

    class Context(
        val pageContext: android.content.Context,
        val activityLauncher: ActivityResultLauncher<Intent>? = null,
        val community: AmityCommunity? = null,
    ) : AmityBaseBehaviorContext(pageContext, activityLauncher)

    open fun goToPostsNotificationSettingPage(context: Context) {
        val intent = AmityCommunityPostsNotificationSettingPageActivity.newIntent(
            context = context.pageContext,
            community = context.community!!,
        )
        context.launchActivity(intent)
    }

    open fun goToCommentsNotificationSettingPage(context: Context) {
        val intent = AmityCommunityCommentsNotificationSettingPageActivity.newIntent(
            context = context.pageContext,
            community = context.community!!,
        )
        context.launchActivity(intent)
    }

    open fun goToStoriesNotificationSettingPage(context: Context) {
        val intent = AmityCommunityStoriesNotificationSettingPageActivity.newIntent(
            context = context.pageContext,
            community = context.community!!,
        )
        context.launchActivity(intent)
    }
}