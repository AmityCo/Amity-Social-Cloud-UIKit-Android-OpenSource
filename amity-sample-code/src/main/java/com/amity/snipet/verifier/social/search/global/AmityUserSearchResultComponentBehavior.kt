package com.amity.snipet.verifier.social.search.global

import android.content.Context
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.search.components.AmityUserSearchResultComponentBehavior

class AmityUserSearchResultComponentBehavior {
    /* begin_sample_code
      gist_id: 5ec0de381a6cd0a642b6041293cbac35
      filename: AmityUserSearchResultComponentBehavior.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
      description: User search result component behavior customization
      */
    class CustomUserSearchResultComponentBehavior : AmityUserSearchResultComponentBehavior() {
        override fun goToUserProfilePage(context: Context, user: AmityUser) {
            // custom implementation for navigating to User Profile Page
        }
    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomUserSearchResultComponentBehavior()
        AmityUIKit4Manager.behavior.userSearchResultComponentBehavior = customBehaviour
    }
    /* end_sample_code */
}