package com.amity.snipet.verifier.social.community.profile

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityCommunityImageFeedComponent

class AmityCommunityImageFeed {
    /* begin_sample_code
      gist_id: fe93b29f1a5af2f3163aaa306237cc8e
      filename: AmityCommunityImageFeed.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/
      description: Community image feed component
      */
    @Composable
    fun compose(
        communityId: String,
    ) {
        //  It's available as Composable element
        AmityCommunityImageFeedComponent(
            communityId = communityId
        )
    }
    /* end_sample_code */
}