package com.amity.snipet.verifier.notificationtray

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amity.socialcloud.uikit.community.compose.notificationtray.AmityNotificationTrayPage
import com.amity.socialcloud.uikit.community.compose.notificationtray.AmityNotificationTrayPageActivity


class AmityNotificationTrayPageSample {
    /* begin_sample_code
          gist_id: e9c53c9a6ab165dea8c20645f82f5ae8
          filename: AmityNotificationTrayPageSample.kt
          asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta
          description: AmityNotification Tray Page Sample
          */
    @Composable
    fun composeNotificationTrayPage(modifier: Modifier = Modifier) {
        AmityNotificationTrayPage(modifier = modifier)
    }

    fun startAnActivity(
        context: Context,
    ) {
        val intent = AmityNotificationTrayPageActivity.newIntent(
            context = context,
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}