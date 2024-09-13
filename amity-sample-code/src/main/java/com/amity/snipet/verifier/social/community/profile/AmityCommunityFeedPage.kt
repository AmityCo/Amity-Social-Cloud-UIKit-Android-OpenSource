package com.amity.snipet.verifier.social.community.profile

import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityCommunityFeedFragment

class AmityCommunityFeedPage {
    /* begin_sample_code
       gist_id: f3361150710b51c08a8af6a066521a58
       filename: AmityCommunityFeedPage.kt
       asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/community-profile-page/community-feed
       description: Navigate to community feed page
       */
    fun createFragment(communityId: String, activity: AppCompatActivity) {
        AmityCommunityFeedFragment
            .newInstance(communityId)
            .build(activity)
    }
    /* end_sample_code */
}