package com.amity.snipet.verifier.social.community.profile

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityCommunityFeedComponent

class AmityCommunityFeed {
    /* begin_sample_code
      gist_id: fe9f0a91b5c83730b8e462329ccc7d9f
      filename: AmityCommunityFeed.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/
      description: Community feed component
      */
    @Composable
    fun compose(
        communityId: String,
    ) {
        //  It's available as Composable element
        AmityCommunityFeedComponent(
            communityId = communityId
        )
    }
    /* end_sample_code */
}