package com.amity.snipet.verifier.social.user.profile

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.community.compose.user.profile.components.AmityUserProfileHeaderComponent

class AmityUserProfileHeaderComponentSample {
    /* begin_sample_code
    gist_id: be860c643f2c3e5f5cf6649218c0f47c
    filename: AmityUserProfileHeaderComponentSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: User Profile Header Component sample
    */
    @Composable
    fun composeComponent(user: AmityUser) {
        AmityUserProfileHeaderComponent(
            user = user,
        )
    }
    /* end_sample_code */
}