package com.amity.snipet.verifier.social.user.relationship

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.user.relationship.AmityUserRelationshipPageBehavior

class AmityUserRelationshipPageBehaviorSample {
    /* begin_sample_code
      gist_id: 30141b15bc2b752ae883270391759f4f
      filename: AmityUserRelationshipPageBehaviorSample.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
      description: User relationship page behavior customization
      */
    class CustomAmityUserRelationshipPageBehavior : AmityUserRelationshipPageBehavior() {

    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomAmityUserRelationshipPageBehavior()
        AmityUIKit4Manager.behavior.userRelationshipPageBehavior = customBehaviour
    }
    /* end_sample_code */
}