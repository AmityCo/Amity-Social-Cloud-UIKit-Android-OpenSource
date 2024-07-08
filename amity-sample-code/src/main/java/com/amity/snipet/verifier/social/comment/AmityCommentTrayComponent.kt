package com.amity.snipet.verifier.social.comment

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponent

class AmityCommentTrayComponent {

    /* begin_sample_code
      gist_id: 426780aff8b9a7c9e2534df24100b321
      filename: AmityCommentTrayComponent.kt
      asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/viewing-story-page/comment-tray-component
      description: Opening comment tray for story
      */
    @Composable
    fun compose(
        storyId: String,
        community: AmityCommunity?,
    ) {
        //  allow user to interact like adding reaction with the story or not
        val shouldAllowInteraction = true

        //  allow user to create comment on the story or not
        val shouldAllowComment = true

        //  It's available as Composable element
        AmityCommentTrayComponent(
            referenceId = storyId,
            referenceType = AmityCommentReferenceType.STORY,
            community = community, //  optional
            shouldAllowInteraction = shouldAllowInteraction,
            shouldAllowCreation = shouldAllowComment,
        )
    }
    /* end_sample_code */
}