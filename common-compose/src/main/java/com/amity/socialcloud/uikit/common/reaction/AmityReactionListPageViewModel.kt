package com.amity.socialcloud.uikit.common.reaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.reaction.AmityReaction
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionMap
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AmityReactionListPageViewModel(
    //private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(AmityReactionListPageState())
        private set


    fun onAction(action: AmityReactionListPageAction) {
        when (action) {
            is AmityReactionListPageAction.LoadData -> {
                loadData(action.referenceType, action.referenceId)
            }

            is AmityReactionListPageAction.RemoveReaction -> {
                removeReaction(action.reactionName)
            }

            is AmityReactionListPageAction.GoToTab -> {
                goToTab(action.tabIndex)
            }

            else -> Unit
        }
    }

    private fun loadData(
        referenceType: AmityReactionReferenceType,
        referenceId: String
    ) {
        if(state.referenceId == referenceId && state.referenceType == referenceType) return
        viewModelScope.launch {
            getReactionTabs(referenceType, referenceId)
                .catch {

                }
                .collect { tabs ->
                    state = state.copy(
                        referenceType = referenceType,
                        referenceId = referenceId,
                        tabItems = tabs,
                    )
                }
        }
    }

    private fun removeReaction(reactionName: String) {
        AmityCoreClient.newReactionRepository()
            .removeReaction(getReference(), reactionName)
            .subscribeOn(Schedulers.io())
            .doOnComplete {

            }
            .doOnError {

            }
            .subscribe()
    }

    private fun goToTab(tabIndex: Int) {
        if (state.currentIndex == tabIndex) return
        state = state.copy(currentIndex = tabIndex)
    }


    fun getReactions(
        reactionName: String?
    ): Flow<PagingData<AmityReaction>> {
        return AmityCoreClient.newReactionRepository()
            .getReactions(getReference())
            .reactionName(reactionName)
            .build()
            .query()
            .asFlow()
    }

    private fun getReference(): AmityReactionReference {
        val referenceId = state.referenceId
        return when (state.referenceType) {
            AmityReactionReferenceType.POST -> AmityReactionReference.POST(referenceId)
            AmityReactionReferenceType.COMMENT -> AmityReactionReference.COMMENT(referenceId)
            AmityReactionReferenceType.MESSAGE -> AmityReactionReference.MESSAGE(referenceId)
            AmityReactionReferenceType.STORY -> AmityReactionReference.STORY(referenceId)
        }
    }

    private fun getMessage(messageId: String): Flowable<AmityMessage> {
        return AmityChatClient.newMessageRepository()
            .getMessage(messageId)
    }

    private fun getStory(storyId: String): Flowable<AmityStory> {
        return AmitySocialClient.newStoryRepository()
            .getStory(storyId)
    }

    private fun getPost(postId: String): Flowable<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .getPost(postId)
    }

    private fun getComment(commentId: String): Flowable<AmityComment> {
        return AmitySocialClient.newCommentRepository()
            .getComment(commentId)
    }

    private fun generateReactionTabs(reactionCount: Int, reactionMap: AmityReactionMap): List<ReactionTab> {
        val tabs: MutableList<ReactionTab> = reactionMap
            .map {
                ReactionTab(
                    title = it.key,
                    count = it.value
                )
            }
            .filter { it.count > 0 }
            .sortedByDescending {
                it.count
            }
            .toMutableList()

        if (tabs.size > 1) {
            tabs.add(0, ReactionTab("All", reactionCount, isAllTab = true))
        }
        return if (tabs.isEmpty()) listOf(ReactionTab("All", reactionCount, isAllTab = true)) else tabs
    }

    private fun getReactionTabs(
        referenceType: AmityReactionReferenceType,
        referenceId: String
    ): Flow<List<ReactionTab>> {
        return when (referenceType) {
            AmityReactionReferenceType.COMMENT -> {
                getComment(referenceId)
                    .map {
                        generateReactionTabs(it.getReactionCount(), it.getReactionMap())
                    }
                    .asFlow()
            }

            AmityReactionReferenceType.POST -> {
                getPost(referenceId)
                    .map {
                        generateReactionTabs(it.getReactionCount(), it.getReactionMap())
                    }
                    .asFlow()
            }

            AmityReactionReferenceType.STORY -> {
                getStory(referenceId)
                    .map {
                        generateReactionTabs(it.getReactionCount(), it.getReactions())
                    }
                    .asFlow()
            }

            AmityReactionReferenceType.MESSAGE -> {
                getMessage(referenceId)
                    .map {
                        generateReactionTabs(it.getReactionCount(), it.getReactionMap())
                    }
                    .asFlow()
            }
        }
    }

}