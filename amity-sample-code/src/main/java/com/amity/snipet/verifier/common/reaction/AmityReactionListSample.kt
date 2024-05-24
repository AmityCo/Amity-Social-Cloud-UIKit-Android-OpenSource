package com.amity.snipet.verifier.common.reaction

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.uikit.common.reaction.AmityReactionList


class AmityReactionListSample {
    /* begin_sample_code
       gist_id: 95bb12b0e79a0fb35593c8ed629c9193
       filename: AmityReactionListSample.kt
       asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/chat/
       description: initialize reaction component
       */
    @Composable
    fun ReactionListSample(
        referenceType: AmityReactionReferenceType,
        referenceId: String
    ) {
        AmityReactionList(
            referenceType = referenceType,
            referenceId = referenceId
        )
    }
    /* end_sample_code */
}