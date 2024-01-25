package com.amity.snipet.verifier.social.story

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTargetTabComponent

class AmityStoryTabComponent {
    /* begin_sample_code
       gist_id: 3f23dc1b5018132d1b891ac1f9f067fb
       filename: AmityStoryTabComponent.kt
       asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/community-profile-page/story-target-tab-component
       description: Navigate to story tab component
       */
    @androidx.media3.common.util.UnstableApi
    @Composable
    fun compose(
        community: AmityCommunity
    ) {
        //  It's available as Composable element
        AmityStoryTargetTabComponent(community = community)

        //  binding.composeViewStoryTarget.setContent {
        //      AmityStoryTargetTabComponent(community = community)
        //  }
    }
    /* end_sample_code */
}