package com.amity.snipet.verifier.social.user.profile

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePage


class AmityUserProfilePageSample {
    /* begin_sample_code
    gist_id: 3afd3a8dbf6c4123aad19e452e1db448
    filename: AmityUserProfilePageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: User Profile Page sample
    */
    @Composable
    fun composePage(userId: String) {
        AmityUserProfilePage(
            userId = userId
        )
    }
    /* end_sample_code */
}