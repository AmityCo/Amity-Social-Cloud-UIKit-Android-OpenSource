package com.amity.snipet.verifier.social.community.profile

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityCommunityHeaderComponent


class AmityCommunityHeader {

    /* begin_sample_code
      gist_id: ab4f92c3162cd10335e486e71542a2e2
      filename: AmityCommunityHeader.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/
      description: Community header component
      */
    @Composable
    fun compose(
        community: AmityCommunity,
    ) {
        //  It's available as Composable element
        AmityCommunityHeaderComponent(
            community = community
        )
    }
    /* end_sample_code */
}