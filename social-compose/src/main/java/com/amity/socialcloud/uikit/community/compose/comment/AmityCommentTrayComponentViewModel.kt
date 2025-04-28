package com.amity.socialcloud.uikit.community.compose.comment

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.comment.query.AmityCommentQuery
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ad.AmityAdInjector
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AmityCommentTrayComponentViewModel : AmityBaseViewModel() {

    private val _commentListState by lazy {
        MutableStateFlow<CommentListState>(CommentListState.LOADING)
    }
    val commentListState get() = _commentListState

    private val _comment = MutableStateFlow<AmityComment?>(null)
    val comment get() = _comment.asStateFlow()

    var community: AmityCommunity? = null
        private set

    fun setCommunity(community: AmityCommunity?) {
        this.community = community
    }

    fun setCommentListState(state: CommentListState) {
        _commentListState.value = state
    }

    fun getCurrentUser(): Flowable<AmityUser> {
        return AmityCoreClient.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getCommentById(commentId: String) {
        viewModelScope.launch {
            AmitySocialClient.newCommentRepository()
                .getComment(commentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .asFlow()
                .catch {  }
                .collectLatest { data ->
                    _comment.update { data }
                }
        }
    }

    fun getComments(
        referenceId: String,
        referenceType: AmityCommentReferenceType,
        communityId: String?,
    ): Flow<PagingData<AmityListItem>> {
        val injector = AmityAdInjector<AmityComment>(
            AmityAdPlacement.COMMENT,
            communityId,
        )
        return getCommentQuery(
            referenceId = referenceId,
            referenceType = referenceType,
        )
            .build()
            .query()
            .onBackpressureBuffer()
            .throttleLatest(800, TimeUnit.MILLISECONDS)
            .map {
                injector.inject(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun addComment(
        referenceId: String,
        referenceType: AmityCommentReferenceType,
        replyCommentId: String?,
        commentText: String,
        mentionedUsers: List<AmityMentionMetadata.USER> = emptyList(),
        onSuccess: (AmityComment) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        val commentCreator = AmitySocialClient.newCommentRepository()
            .createComment()
            .run {
                when (referenceType) {
                    AmityCommentReferenceType.POST -> post(referenceId)
                    AmityCommentReferenceType.STORY -> story(referenceId)
                    AmityCommentReferenceType.CONTENT -> content(referenceId)
                }
            }

        if (!replyCommentId.isNullOrEmpty()) {
            commentCreator.parentId(replyCommentId)
        }

        val textCommentCreator = commentCreator.with().text(commentText)
        if (mentionedUsers.isNotEmpty()) {
            textCommentCreator
                .metadata(AmityMentionMetadataCreator(mentionedUsers).create())
                .mentionUsers(mentionedUsers.map { it.getUserId() })
        }

        textCommentCreator.build().send()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(onSuccess)
            .doOnError(onError)
            .ignoreElement()
            .subscribe()
    }

    fun addReaction(
        commentId: String,
    ) {
        AmityCoreClient.newReactionRepository()
            .addReaction(
                reactionReference = AmityReactionReference.COMMENT(commentId),
                reactionName = AmityConstants.POST_REACTION
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun removeReaction(
        commentId: String,
    ) {
        AmityCoreClient.newReactionRepository()
            .removeReaction(
                reactionReference = AmityReactionReference.COMMENT(commentId),
                reactionName = AmityConstants.POST_REACTION
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun editComment(
        commentId: String,
        commentText: String,
        userMentions: List<AmityMentionMetadata.USER> = emptyList(),
        onSuccess: (AmityComment) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        val textCommentEditor = AmitySocialClient.newCommentRepository()
            .editComment(commentId)
            .text(commentText)

        if (userMentions.isNotEmpty()) {
            textCommentEditor
                .metadata(AmityMentionMetadataCreator(userMentions).create())
                .mentionUsers(userMentions.map { it.getUserId() })
        }

        textCommentEditor
            .build()
            .apply()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(onSuccess)
            .doOnError(onError)
            .ignoreElement()
            .subscribe()
    }

    fun deleteComment(
        commentId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmitySocialClient.newCommentRepository()
            .softDeleteComment(commentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError(onError)
            .subscribe()
    }

    fun flagComment(
        commentId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmitySocialClient.newCommentRepository()
            .flagComment(commentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError(onError)
            .subscribe()
    }

    fun unflagComment(
        commentId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmitySocialClient.newCommentRepository()
            .unflagComment(commentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError(onError)
            .subscribe()
    }

    private fun getCommentQuery(
        referenceId: String,
        referenceType: AmityCommentReferenceType,
    ): AmityCommentQuery.Builder {
        return AmitySocialClient.newCommentRepository()
            .getComments()
            .run {
                when (referenceType) {
                    AmityCommentReferenceType.POST -> post(referenceId)
                    AmityCommentReferenceType.STORY -> story(referenceId)
                    AmityCommentReferenceType.CONTENT -> content(referenceId)
                }
            }
    }

    sealed class CommentListState {
        object LOADING : CommentListState()
        object SUCCESS : CommentListState()
        object EMPTY : CommentListState()
        object ERROR : CommentListState()

        companion object {
            fun from(
                loadState: LoadState,
                itemCount: Int,
            ): CommentListState {
                return if (loadState is LoadState.Loading && itemCount == 0) {
                    LOADING
                } else if (loadState is LoadState.NotLoading && loadState.endOfPaginationReached && itemCount == 0) {
                    EMPTY
                } else if (loadState is LoadState.Error && itemCount == 0) {
                    ERROR
                } else {
                    SUCCESS
                }
            }
        }
    }
}