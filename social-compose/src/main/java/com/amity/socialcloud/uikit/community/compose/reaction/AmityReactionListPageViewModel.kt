package com.amity.socialcloud.uikit.community.compose.reaction

import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.reaction.AmityReaction
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import kotlinx.coroutines.flow.Flow

class AmityReactionListPageViewModel : AmityBaseViewModel() {

    fun getReactions(
        referenceType: AmityReactionReferenceType,
        referenceId: String
    ): Flow<PagingData<AmityReaction>> {
        return AmityCoreClient.newReactionRepository()
            .getReactions(getReference(referenceType, referenceId))
            .reactionName(AmityConstants.POST_REACTION)
            .build()
            .query()
            .asFlow()
    }

    private fun getReference(
        referenceType: AmityReactionReferenceType,
        referenceId: String
    ): AmityReactionReference {
        return when (referenceType) {
            AmityReactionReferenceType.POST -> AmityReactionReference.POST(referenceId)
            AmityReactionReferenceType.COMMENT -> AmityReactionReference.COMMENT(referenceId)
            AmityReactionReferenceType.MESSAGE -> AmityReactionReference.MESSAGE(referenceId)
            AmityReactionReferenceType.STORY -> AmityReactionReference.STORY(referenceId)
        }
    }
}