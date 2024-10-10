package com.amity.snipet.verifier.social.community.profile

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityCommunityVideoFeedComponent

class AmityCommunityVideoFeed {
    /* begin_sample_code
      gist_id: db3407e1ac2d64401ea6d0a35be14f8f
      filename: AmityCommunityVideoFeed.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/
      description: Community video feed component
      */
    @Composable
    fun compose(
        communityId: String,
    ) {
        //  It's available as Composable element
        AmityCommunityVideoFeedComponent(
            communityId = communityId
        )
    }
    /* end_sample_code */
}