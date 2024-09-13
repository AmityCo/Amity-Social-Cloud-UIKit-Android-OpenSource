package com.amity.snipet.verifier.social.community.setting.notification

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.AmityCommunityNotificationSettingPage

class AmityCommunityNotificationSettingPageSample {
    /* begin_sample_code
    gist_id: cf8480190c9a0f3e3c4fc4048f4aae49
    filename: AmityCommunitySettingPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Community Setting Page sample
    */
    @Composable
    fun composeCommunityNotificationSettingPage(community: AmityCommunity) {
        AmityCommunityNotificationSettingPage(
            community = community,
        )
    }
    /* end_sample_code */
}