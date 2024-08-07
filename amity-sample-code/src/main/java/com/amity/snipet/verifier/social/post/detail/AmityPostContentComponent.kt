package com.amity.snipet.verifier.social.post.detail

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle

class AmityPostContentComponent {
    /* begin_sample_code
     gist_id: 9d3a0a0eba4bb6d39171ee6b98e7ddbe
     filename: AmityPostContentComponent.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
     description: Post content component
     */
    @Composable
    fun composePostContentComponent(
        post: AmityPost,
        style: AmityPostContentComponentStyle,
        hideMenuButton: Boolean,
    ) {
        AmityPostContentComponent(
            post = post,
            style = style,
            hideMenuButton = hideMenuButton,
            onTapAction = {}
        )
    }
    /* end_sample_code */
}