package com.amity.snipet.verifier.social.user.profile

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.user.profile.components.AmityUserImageFeedComponent

class AmityUserImageFeedComponentSample {
    /* begin_sample_code
    gist_id: a2f274853f94399a7678a0c15326f320
    filename: AmityUserFeedComponentSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: User Feed Component sample
    */
    @Composable
    fun composeComponent(userId: String) {
        AmityUserImageFeedComponent(
            userId = userId
        )
    }
    /* end_sample_code */
}