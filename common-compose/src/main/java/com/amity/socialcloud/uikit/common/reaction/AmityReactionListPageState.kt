package com.amity.socialcloud.uikit.common.reaction

import androidx.paging.PagingData
import com.amity.socialcloud.sdk.model.core.reaction.AmityReaction
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class AmityReactionListPageState (
    val referenceType : AmityReactionReferenceType = AmityReactionReferenceType.MESSAGE,
    val referenceId : String = "",
    val tabItems : List<ReactionTab> = listOf(ReactionTab("All", 0)),
    val currentIndex : Int = 0,
)