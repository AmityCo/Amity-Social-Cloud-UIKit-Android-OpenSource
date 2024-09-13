package com.amity.snipet.verifier.social.community.setting.notification

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.comments.AmityCommunityCommentsNotificationSettingPage

class AmityCommunityCommentsNotificationSettingPageSample {
    /* begin_sample_code
    gist_id: 4bfa6096256bfe5c00ead84e4c573140
    filename: AmityCommunityCommentsNotificationSettingPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Community Comments Setting Page sample
    */
    @Composable
    fun composeCommunityCommentsNotificationSettingPage(community: AmityCommunity) {
        AmityCommunityCommentsNotificationSettingPage(
            community = community,
        )
    }
    /* end_sample_code */
}