package com.amity.socialcloud.uikit.chat.compose.group

import android.content.Context
import com.amity.socialcloud.uikit.chat.compose.setting.AmityGroupMemberListPageActivity
import com.amity.socialcloud.uikit.chat.compose.setting.AmityGroupSettingPageActivity

open class AmityGroupChatPageBehavior {

    open fun goToGroupSetting(
        context: Context,
        channelId: String,
    ) {
        context.startActivity(AmityGroupSettingPageActivity.newIntent(context, channelId))
    }

    open fun goToMemberList(
        context: Context,
        channelId: String,
    ) {
        context.startActivity(AmityGroupMemberListPageActivity.newIntent(context, channelId))
    }

    open fun goToUserProfile(
        context: Context,
        userId: String,
    ) {
        // Override to navigate to user profile page
    }

    open fun goToMessageReport(
        context: Context,
        messageId: String,
    ) {
        context.startActivity(
            com.amity.socialcloud.uikit.chat.compose.report.AmityMessageReportPageActivity.newIntent(context, messageId)
        )
    }
}
