package com.amity.socialcloud.uikit.community.compose.event.setup

import android.content.Context
import com.amity.socialcloud.uikit.community.compose.event.detail.AmityEventDetailPageActivity

open class AmityEventSetupPageBehavior {

    open fun goToEventDetailPage(
        context: Context,
        eventId: String,
    ) {
        val intent = AmityEventDetailPageActivity.newIntent(
            context = context,
            eventId = eventId
        )
        context.startActivity(intent)
    }
}
