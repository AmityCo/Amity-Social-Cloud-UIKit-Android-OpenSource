package com.amity.snipet.verifier.social.post.composer

import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageBehavior

class AmityPostComposerPageBehavior {
    /* begin_sample_code
      gist_id: be70698b41419f55e06688d95242af4e
      filename: AmityPostComposerPageBehavior.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
      description: Post composer page behavior customization
      */
    class CustomPostComposerPageBehavior : AmityPostComposerPageBehavior() {

    }

    fun setCustomBehavior() {
        val customBehaviour = CustomPostComposerPageBehavior()
        AmityUIKit4Manager.behavior.postComposerPageBehavior = customBehaviour
    }
    /* end_sample_code */
}