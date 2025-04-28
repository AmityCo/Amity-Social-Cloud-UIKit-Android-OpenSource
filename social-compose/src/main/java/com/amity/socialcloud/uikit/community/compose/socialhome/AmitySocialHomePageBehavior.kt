package com.amity.socialcloud.uikit.community.compose.socialhome

import android.content.Context
import com.amity.socialcloud.uikit.community.compose.notificationtray.AmityNotificationTrayPageActivity
import com.amity.socialcloud.uikit.community.compose.search.community.AmityMyCommunitiesSearchPageActivity
import com.amity.socialcloud.uikit.community.compose.search.global.AmitySocialGlobalSearchPageActivity

open class AmitySocialHomePageBehavior {

    open fun goToGlobalSearchPage(
        context: Context,
    ) {
        val intent = AmitySocialGlobalSearchPageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }

    open fun goToMyCommunitiesSearchPage(
        context: Context,
    ) {
        val intent = AmityMyCommunitiesSearchPageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }

    open fun goToNotificationTrayPage(
        context: Context,
    ) {
        val intent = AmityNotificationTrayPageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }
}