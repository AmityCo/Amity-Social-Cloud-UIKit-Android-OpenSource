package com.amity.socialcloud.uikit.common.reaction

import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType

sealed interface AmityReactionListPageAction {

    data class LoadData(val referenceType: AmityReactionReferenceType,
        val referenceId: String) : AmityReactionListPageAction

    data class GoToTab(val tabIndex: Int) : AmityReactionListPageAction
    data class RemoveReaction(val reactionName: String) : AmityReactionListPageAction
}