package com.amity.snipet.verifier.social.user.profile

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.user.profile.components.AmityUserFeedComponent

class AmityUserFeedComponentSample {
    /* begin_sample_code
    gist_id: 77ffa2cc77de60ce311391b60e01ea79
    filename: AmityUserFeedComponentSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: User Feed Component sample
    */
    @Composable
    fun composeComponent(userId: String) {
        AmityUserFeedComponent(
            userId = userId
        )
    }
    /* end_sample_code */
}