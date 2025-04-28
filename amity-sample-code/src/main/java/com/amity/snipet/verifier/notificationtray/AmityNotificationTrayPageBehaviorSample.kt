package com.amity.snipet.verifier.notificationtray

import android.content.Context
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.notificationtray.AmityNotificationTrayPageBehavior
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory

class AmityNotificationTrayPageBehaviorSample {
    /* begin_sample_code
   gist_id: 1cca4c2ac199d8aa91a1b0303b76fb54
   filename: AmityNotificationTrayPageBehaviorSample.kt
   asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/customization/
   description: AmityNotificationTray page behavior customization
   */
    class CustomNotificationTrayPageBehavior : AmityNotificationTrayPageBehavior() {

        override fun goToPostDetailPage(
            context: Context,
            postId: String,
            category: AmityPostCategory,
            commentId: String?,
            parentId: String?,
        ) {
            super.goToPostDetailPage(context, postId, category, commentId, parentId)
        }

        override fun goToCommunityProfilePage(
            context: Context,
            communityId: String,
        ) {
            super.goToCommunityProfilePage(context, communityId)
        }

    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomNotificationTrayPageBehavior()
        AmityUIKit4Manager.behavior.notificationTrayPageBehavior = customBehaviour
    }
    /* end_sample_code */
}