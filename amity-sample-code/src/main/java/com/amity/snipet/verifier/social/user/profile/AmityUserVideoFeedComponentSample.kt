package com.amity.snipet.verifier.social.user.profile

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.user.profile.components.AmityUserVideoFeedComponent

class AmityUserVideoFeedComponentSample {
    /* begin_sample_code
    gist_id: 82eb45b2690c30d87c3f91fbec2bd111
    filename: AmityUserVideoFeedComponentSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: User Video Feed Component sample
    */
    @Composable
    fun composeComponent(userId: String) {
        AmityUserVideoFeedComponent(
            userId = userId
        )
    }
    /* end_sample_code */
}