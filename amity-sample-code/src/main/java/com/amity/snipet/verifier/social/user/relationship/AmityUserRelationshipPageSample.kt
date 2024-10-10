package com.amity.snipet.verifier.social.user.relationship

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.user.relationship.AmityUserRelationshipPage
import com.amity.socialcloud.uikit.community.compose.user.relationship.AmityUserRelationshipPageTab

class AmityUserRelationshipPageSample {
    /* begin_sample_code
    gist_id: be809636b47ac9002bcdb836549e3223
    filename: AmityUserRelationshipPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: User Relationship Page sample
    */
    @Composable
    fun composePage(userId: String) {
        AmityUserRelationshipPage(
            userId = userId,
            selectedTab = AmityUserRelationshipPageTab.FOLLOWING
        )
    }
    /* end_sample_code */
}