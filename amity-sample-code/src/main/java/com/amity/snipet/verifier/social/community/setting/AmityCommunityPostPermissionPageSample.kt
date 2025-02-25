package com.amity.snipet.verifier.social.community.setting

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.setting.post.AmityCommunityPostPermissionPage

class AmityCommunityPostPermissionPageSample {
    /* begin_sample_code
    gist_id: 046810b0c45f3a176432c1f020f3c31d
    filename: AmityCommunityPostPermissionPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Community Post Permission Page sample
    */
    @Composable
    fun composePostPermissionPage(community: AmityCommunity) {
        AmityCommunityPostPermissionPage(
            community = community,
        )
    }
    /* end_sample_code */
}