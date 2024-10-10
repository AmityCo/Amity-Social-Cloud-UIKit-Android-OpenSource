package com.amity.snipet.verifier.social.community.profile

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityCommunityPinnedPostComponent

class AmityCommunityPinFeed {
    /* begin_sample_code
      gist_id: 0598eadc06ef2ce5977cc38259fde990
      filename: AmityCommunityPinFeed.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/
      description: Community pin feed component
      */
    @Composable
    fun compose(
        communityId: String,
    ) {
        //  It's available as Composable element
        AmityCommunityPinnedPostComponent(
            communityId = communityId
        )
    }
    /* end_sample_code */
}