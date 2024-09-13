package com.amity.snipet.verifier.social.community.setting.notification

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.posts.AmityCommunityPostsNotificationSettingPage

class AmityCommunityPostsNotificationSettingPageSample {
    /* begin_sample_code
    gist_id: 3f2bafffd2f7dc9200675a5574ea918c
    filename: AmityCommunityPostsNotificationSettingPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Community Posts Setting Page sample
    */
    @Composable
    fun composeCommunityPostsNotificationSettingPage(community: AmityCommunity) {
        AmityCommunityPostsNotificationSettingPage(
            community = community,
        )
    }
    /* end_sample_code */
}