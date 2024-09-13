package com.amity.snipet.verifier.social.community.setup

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPage
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageMode

class AmityCommunitySetupPageSample {
    /* begin_sample_code
    gist_id: b1cd8006517217ea8919188c8267e28b
    filename: AmityCommunitySetupPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Community Setup Page sample
    */
    @Composable
    fun composeCommunitySetupPage(community: AmityCommunity) {
        AmityCommunitySetupPage(
            mode = AmityCommunitySetupPageMode.Create,
        )

        AmityCommunitySetupPage(
            mode = AmityCommunitySetupPageMode.Edit(community),
        )
    }
    /* end_sample_code */
}