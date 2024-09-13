package com.amity.snipet.verifier.social.community.setting.notification

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.stories.AmityCommunityStoriesNotificationSettingPage

class AmityCommunityStoriesNotificationSettingPageSample {
    /* begin_sample_code
    gist_id: 12cab5f2266b37e8b5f031d28bd72ad5
    filename: AmityCommunityStoriesNotificationSettingPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Community Stories Setting Page sample
    */
    @Composable
    fun composeCommunityStoriesNotificationSettingPage(community: AmityCommunity) {
        AmityCommunityStoriesNotificationSettingPage(
            community = community,
        )
    }
    /* end_sample_code */
}