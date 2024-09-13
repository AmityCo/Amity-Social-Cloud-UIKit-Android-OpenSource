package com.amity.snipet.verifier.social.community.profile

import AmityCommunityProfilePage
import androidx.compose.runtime.Composable


class AmityCommunityProfilePage {

    /* begin_sample_code
      gist_id: f0fc02e9aa6d3800faca45dd412e7667
      filename: AmityCommunityProfilePage.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/
      description: Community profile page
      */
    @Composable
    fun compose(
        communityId: String,
    ) {
        //  It's available as Composable element
        AmityCommunityProfilePage(
            communityId = communityId
        )
    }
    /* end_sample_code */
}
