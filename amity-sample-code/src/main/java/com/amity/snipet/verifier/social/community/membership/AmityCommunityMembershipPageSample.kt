package com.amity.snipet.verifier.social.community.membership

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipPage

class AmityCommunityMembershipPageSample {
    /* begin_sample_code
    gist_id: 8328c322ed56661f1d11195e3f1b6bce
    filename: AmityCommunityMembershipPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Community Membership Page sample
    */
    @Composable
    fun composeCommunityMembershipPage(community: AmityCommunity) {
        AmityCommunityMembershipPage(
            community = community,
        )
    }
    /* end_sample_code */
}