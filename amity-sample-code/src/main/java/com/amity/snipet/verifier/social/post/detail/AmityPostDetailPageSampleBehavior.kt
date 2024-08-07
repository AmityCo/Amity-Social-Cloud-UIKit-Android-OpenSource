package com.amity.snipet.verifier.social.post.detail

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageBehavior

class AmityPostDetailPageSampleBehavior {
    /* begin_sample_code
     gist_id: fd252cb9f594a8b584bef6c187dfc0ef
     filename: AmityPostDetailPageSampleBehavior.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/customization/
     description: Post detail page behavior customization
     */
    class CustomPostDetailPageBehaviour : AmityPostDetailPageBehavior() {

    }

    // Call this function in AmityUIKit4Manager class to setup custom behaviour class in UIKit
    fun setCustomBehavior() {
        val customBehaviour = CustomPostDetailPageBehaviour()
        AmityUIKit4Manager.behavior.postDetailPageBehavior = customBehaviour
    }
    /* end_sample_code */
}