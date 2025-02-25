package com.amity.snipet.verifier.social.community.setting

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.setting.AmityCommunitySettingPage

class AmityCommunityStorySettingPageSample {
    /* begin_sample_code
    gist_id: f006d34bf371c74af9b59b37981bce43
    filename: AmityCommunityStorySettingPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Community Story Setting Page sample
    */
    @Composable
    fun composeStorySettingPage(community: AmityCommunity) {
        AmityCommunitySettingPage(
            community = community,
        )
    }
    /* end_sample_code */
}