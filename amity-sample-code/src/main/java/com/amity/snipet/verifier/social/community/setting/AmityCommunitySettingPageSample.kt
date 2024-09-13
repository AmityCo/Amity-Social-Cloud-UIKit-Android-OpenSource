package com.amity.snipet.verifier.social.community.setting

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.setting.AmityCommunitySettingPage

class AmityCommunitySettingPageSample {
    /* begin_sample_code
    gist_id: 4a0f361450ada243fad81a07815b77ce
    filename: AmityCommunitySettingPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Community Setting Page sample
    */
    @Composable
    fun composeCommunitySettingPage(community: AmityCommunity) {
        AmityCommunitySettingPage(
            community = community,
        )
    }
    /* end_sample_code */
}