package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType

private const val SAVED_REFERENCE_TYPE = "SAVED_REFERENCE_TYPE"
private const val SAVED_REFERENCE_ID = "SAVED_REFERENCE_ID"

class AmityReactionListViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    init {
        savedState.get<AmityReactionReferenceType>(SAVED_REFERENCE_TYPE)?.let { referenceType = it }
        savedState.get<String>(SAVED_REFERENCE_ID)?.let { referenceId = it }
    }

    var referenceType: AmityReactionReferenceType = AmityReactionReferenceType.POST
        set(value) {
            savedState.set(SAVED_REFERENCE_TYPE, value)
            field = value
        }

    var referenceId: String = ""
        set(value) {
            savedState.set(SAVED_REFERENCE_ID, value)
            field = value
        }

    fun getReactionCount(reactionName: String? = null): Int {
        return when (referenceType) {
            AmityReactionReferenceType.POST -> {
                AmitySocialClient.newPostRepository()
                    .getPost(referenceId)
                    .map {
                        if (reactionName == null) {
                            it.getReactionCount()
                        } else {
                            it.getReactionMap().getCount(reactionName)
                        }
                    }
                    .blockingFirst(0)

            }
            AmityReactionReferenceType.COMMENT -> {
                AmitySocialClient.newCommentRepository()
                    .getComment(referenceId)
                    .map {
                        if (reactionName == null) {
                            it.getReactionCount()
                        } else {
                            it.getReactionMap().getCount(reactionName)
                        }
                    }
                    .blockingFirst(0)
            }
            else -> 0
        }
    }
}