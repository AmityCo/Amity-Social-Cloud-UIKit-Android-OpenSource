package com.amity.socialcloud.uikit.community.compose.comment

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.comment.query.AmityCommentQuery
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.sdk.model.core.flag.AmityContentFlagReason
import com.amity.socialcloud.sdk.model.core.link.AmityLink
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ad.AmityAdInjector
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.extionsions.extractUrls
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.compose.comment.query.AmityStoryCommentReplyLoader
import com.ekoapp.ekosdk.internal.api.socket.request.FlagContentRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.TimeUnit
import kotlin.collections.firstOrNull

open class AmityCommentTrayComponentViewModel : AmityBaseViewModel() {

    private val _commentListState by lazy {
        MutableStateFlow<CommentListState>(CommentListState.LOADING)
    }
    open val commentListState get() = _commentListState

    private val _comment = MutableStateFlow<AmityComment?>(null)
    val comment get() = _comment.asStateFlow()

    private val _sheetUiState =
        MutableStateFlow<CommentBottomSheetState>(CommentBottomSheetState.CloseSheet)
    open val sheetUiState get() = _sheetUiState.asStateFlow()

    var community: AmityCommunity? = null
        private set
    private val _replyContext = MutableStateFlow<Pair<AmityComment, String>?>(null)
    open val replyContext get() = _replyContext.asStateFlow()

    companion object {
        private const val REPLY_PAGE_SIZE = 5
    }

    private val replyLoaders = mutableMapOf<String, AmityStoryCommentReplyLoader>()
    private val replyLoaderDisposables = mutableMapOf<String, CompositeDisposable>()

    // include optimistic comment ID
    private val _replyComments = MutableStateFlow<Map<String, List<AmityComment>>>(emptyMap())
    open val replyComments: StateFlow<Map<String, List<AmityComment>>> = _replyComments.asStateFlow()

    private val _replyShowLoadMore = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val replyShowLoadMore: StateFlow<Map<String, Boolean>> = _replyShowLoadMore.asStateFlow()

    private val _replyLoading = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    open val replyLoading: StateFlow<Map<String, Boolean>> = _replyLoading.asStateFlow()

    private val _optimisticCommentIds = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val optimisticCommentIds: StateFlow<Map<String, List<String>>> = _optimisticCommentIds.asStateFlow()

    private val _creatingReplyForParent = MutableStateFlow<Set<String>>(emptySet())
    val creatingReplyForParent: StateFlow<Set<String>> = _creatingReplyForParent.asStateFlow()

    private val _replyUnavailable = MutableStateFlow<Set<String>>(emptySet())
    val replyUnavailable: StateFlow<Set<String>> = _replyUnavailable.asStateFlow()

    private val _replyLoadErrors = MutableStateFlow<Set<String>>(emptySet())
    val replyLoadErrors: StateFlow<Set<String>> = _replyLoadErrors.asStateFlow()

    private val _commentTarget = MutableStateFlow<AmityComment?>(null)
    val commentTarget: StateFlow<AmityComment?> = _commentTarget.asStateFlow()

    private val _commentTargetUnavailable = MutableStateFlow<Set<String>>(emptySet())
    val commentTargetUnavailable: StateFlow<Set<String>> = _commentTargetUnavailable.asStateFlow()

    fun getOrCreateReplyLoader(
        referenceId: String,
        referenceType: AmityCommentReferenceType,
        parentCommentId: String,
        includeDeleted: Boolean = false,
        isL2Thread: Boolean = false,
        replyCount: Int = 0,
    ): AmityStoryCommentReplyLoader {
        return replyLoaders.getOrPut(parentCommentId) {
            val loader = AmityStoryCommentReplyLoader(
                referenceId = referenceId,
                referenceType = referenceType,
                parentCommentId = parentCommentId,
                includeDeleted = includeDeleted,
                isL2Thread = isL2Thread,
                replyPageSize = REPLY_PAGE_SIZE,
            )
            subscribeToLoader(parentCommentId, loader, isL2Thread, replyCount)
            _replyLoading.update { it + (parentCommentId to true) }
            loader.apply { load() }
        }
    }

    private fun subscribeToLoader(
        parentCommentId: String,
        loader: AmityStoryCommentReplyLoader,
        isL2Thread: Boolean = false,
        replyCount: Int = 0,
    ) {
        val disposable = CompositeDisposable()

        // Delayed check job: when loading finishes, schedule a check after a brief delay.
        // If non-empty comments arrive before the delay, the job is cancelled.
        var unavailableCheckJob: kotlinx.coroutines.Job? = null

        disposable.add(
            loader.getComments()
                .subscribe { rawComments ->
                    val optimisticIds = _optimisticCommentIds.value[parentCommentId].orEmpty()
                    val reordered = if (optimisticIds.isNotEmpty()) {
                        val optimistic = rawComments.filter { it.getCommentId() in optimisticIds }.asReversed()
                        val rest = rawComments.filter { it.getCommentId() !in optimisticIds }
                        optimistic + rest
                    } else {
                        rawComments
                    }
                    _replyComments.update { it + (parentCommentId to reordered) }
                    // If real data arrived, cancel any pending unavailable check
                    if (reordered.isNotEmpty()) {
                        unavailableCheckJob?.cancel()
                        unavailableCheckJob = null
                    }
                }
        )

        disposable.add(
            loader.showLoadMoreButton()
                .subscribe { show ->
                    if (_replyComments.value[parentCommentId]?.firstOrNull()?.getState() != AmityComment.State.FAILED) {
                        _replyShowLoadMore.update { it + (parentCommentId to show) }
                    }
                }
        )
        var wasLoading = false
        disposable.add(
            loader.isLoadingReplies()
                .subscribe { loading ->
                    _replyLoading.update { it + (parentCommentId to loading) }
                    if (wasLoading && !loading) {
                        // Reactively wait for _replyComments to be populated for this parent.
                        // Using withTimeoutOrNull avoids a fixed-delay race: the SDK may deliver
                        // comments well after the loading=false notification.
                        unavailableCheckJob?.cancel()
                        unavailableCheckJob = viewModelScope.launch {
                            val commentsArrived = withTimeoutOrNull(2000L) {
                                _replyComments
                                    .map { it[parentCommentId].orEmpty() }
                                    .first { it.isNotEmpty() }
                            }
                            if (commentsArrived == null) {
                                val finalComments = _replyComments.value[parentCommentId].orEmpty()
                                if (replyCount > 0 && finalComments.isEmpty()) {
                                    _replyUnavailable.update { it + parentCommentId }
                                }
                            }
                        }
                    }
                    wasLoading = loading
                }
        )

        disposable.add(
            loader.getLoadErrors()
                .subscribe { error ->
                    if (AmityError.from(error) == AmityError.CONNECTION_ERROR) {
                        _replyLoadErrors.update { it + parentCommentId }
                    }
                }
        )

        replyLoaderDisposables[parentCommentId] = disposable
    }

    fun clearReplyLoadError(parentCommentId: String) {
        _replyLoadErrors.update { it - parentCommentId }
    }

    fun clearReplyUnavailable(parentCommentId: String) {
        _replyUnavailable.update { it - parentCommentId }
    }

    fun setReplyContext(comment: AmityComment, parentId: String) {
        _replyContext.value = Pair(comment, parentId)
    }

    fun clearReplyContext() {
        _replyContext.value = null
    }

    fun setCommunity(community: AmityCommunity?) {
        this.community = community
    }

    fun setCommentListState(state: CommentListState) {
        _commentListState.value = state
    }

    open fun getCurrentUser(): Flowable<AmityUser> {
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
                .catch { }
                .collectLatest { data ->
                    _comment.update { data }
                }
        }
    }

    fun getCommentTargetById(commentId: String) {
        viewModelScope.launch {
            AmitySocialClient.newCommentRepository()
                .getComment(commentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .asFlow()
                .catch {
                    _commentTargetUnavailable.update { it + commentId }
                }
                .collectLatest { data ->
                    if (data.isDeleted()) {
                        if (data.getParentId() == null) {
                            // still display L0 comment
                            _commentTarget.value = data
                        } else {
                            _commentTargetUnavailable.update { it + commentId }
                        }
                    } else {
                        _commentTarget.value = data
                    }
                }
        }
    }

    fun verifyCommentExists(commentId: String) {
        viewModelScope.launch {
            AmitySocialClient.newCommentRepository()
                .getCommentByIds(setOf(commentId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .asFlow()
                .catch {
                    _commentTargetUnavailable.update { it + commentId }
                }
                .collectLatest { data ->
                    if (data.isEmpty()) {
                        _commentTargetUnavailable.update { it + commentId }
                        return@collectLatest
                    }

                    if (data.first().isDeleted()) {
                        _commentTargetUnavailable.update { it + commentId }
                    }
                }
        }
    }

    fun setCommentTarget(comment: AmityComment?) {
        _commentTarget.value = comment
    }

    open fun getComments(
        referenceId: String,
        referenceType: AmityCommentReferenceType,
        communityId: String?,
        includeDeleted: Boolean = true
    ): Flow<PagingData<AmityListItem>> {
        val injector = AmityAdInjector<AmityComment>(
            AmityAdPlacement.COMMENT,
            communityId,
        )
        return AmitySocialClient.newCommentRepository()
            .getComments()
            .run {
                when (referenceType) {
                    AmityCommentReferenceType.POST -> post(referenceId)
                    AmityCommentReferenceType.STORY -> story(referenceId)
                    AmityCommentReferenceType.CONTENT -> content(referenceId)
                }
            }
            .parentId(null)
            .includeDeleted(includeDeleted)
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

        var replyLoader: AmityStoryCommentReplyLoader? = null
        if (!replyCommentId.isNullOrEmpty()) {
            commentCreator.parentId(replyCommentId)
            _creatingReplyForParent.update { it + replyCommentId }
            _replyLoading.update { it + (replyCommentId to true) }
            // Ensure reply loader exists BEFORE send() so its live query
            // captures the optimistic comment and any state transitions (e.g. SYNCED->FAILED).
            replyLoader = getOrCreateReplyLoader(
                referenceId = referenceId,
                referenceType = referenceType,
                parentCommentId = replyCommentId,
                includeDeleted = false,
                replyCount = 0,
            )
        }

        val textCommentCreator = commentCreator.with().text(commentText)
        if (mentionedUsers.isNotEmpty()) {
            textCommentCreator
                .metadata(AmityMentionMetadataCreator(mentionedUsers).create())
                .mentionUsers(mentionedUsers.map { it.getUserId() })
        }

        val detectedLinks = commentText.extractUrls().mapNotNull { urlPosition ->
            try {
                AmityLink(
                    index = urlPosition.start,
                    length = urlPosition.end - urlPosition.start,
                    url = urlPosition.url,
                    renderPreview = false,
                    domain = null,
                    title = null,
                    imageUrl = null
                )
            } catch (e: Exception) {
                null
            }
        }
        if (detectedLinks.isNotEmpty()) {
            textCommentCreator.links(detectedLinks)
        }

        val doSend = {
            textCommentCreator.build().send()
                .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { createdComment ->
                if (replyCommentId != null) {
                    addOptimisticCommentId(replyCommentId, createdComment.getCommentId())
                    val current = _replyComments.value[replyCommentId].orEmpty()
                    if (current.none { it.getCommentId() == createdComment.getCommentId() }) {
                        _replyComments.update { it + (replyCommentId to (listOf(createdComment) + current)) }
                    }
                    _creatingReplyForParent.update { it - replyCommentId }
                    _replyLoading.update { it + (replyCommentId to false) }
                }
                onSuccess(createdComment)
            }
            .doOnError { error ->
                if (replyCommentId != null) {
                    _creatingReplyForParent.update { it - replyCommentId }
                    _replyLoading.update { it + (replyCommentId to false) }
                    // Loader was created before send(), force it to re-emit so the
                    // FAILED comment (if kept by SDK) flows into _replyComments.
                    val loader = replyLoaders[replyCommentId]
                    loader?.forceRefresh()

                    // Mark FAILED replies as optimistic so the collapsed container renders them.
                    // _replyComments is updated asynchronously after forceRefresh(), so we
                    // observe it until we see FAILED comments arrive.
                    val parentId = replyCommentId
                    viewModelScope.launch {
                        val replies = withTimeoutOrNull(2000L) {
                            _replyComments
                                .map { it[parentId].orEmpty() }
                                .first { list -> list.any { it.getState() == AmityComment.State.FAILED } }
                        }
                        val failedReplies = (replies ?: _replyComments.value[parentId].orEmpty())
                            .filter { it.getState() == AmityComment.State.FAILED }
                        for (failed in failedReplies) {
                            addOptimisticCommentId(parentId, failed.getCommentId())
                        }
                    }
                }
                onError(error)
            }
            .ignoreElement()
            .subscribe()
        }

        if (replyLoader != null) {
            // Wait for the loader's live query to be hot before sending,
            // so it captures the optimistic comment and state transitions.
            viewModelScope.launch {
                withTimeoutOrNull(1000L) { replyLoader.readyDeferred.await() }
                doSend()
            }
        } else {
            doSend()
        }
    }

    fun addReaction(
        commentId: String,
        reaction: String,
        onError: (Throwable) -> Unit = { },
    ) {
        AmityCoreClient.newReactionRepository()
            .addReaction(
                referenceType = AmityReactionReferenceType.COMMENT,
                referenceId = commentId,
                reactionName = reaction,
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                onError(it)
            }
            .subscribe()
    }

    fun removeReaction(
        commentId: String,
        reaction: String,
        onError: (Throwable) -> Unit = { },
    ) {
        AmityCoreClient.newReactionRepository()
            .removeReaction(
                referenceType = AmityReactionReferenceType.COMMENT,
                referenceId = commentId,
                reactionName = reaction,
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                onError(it)
            }
            .subscribe()
    }

    fun switchReaction(
        commentId: String,
        reaction: String,
        previousReaction: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        AmityCoreClient.newReactionRepository().let { repository ->
            repository
                .removeReaction(AmityReactionReferenceType.COMMENT, commentId, previousReaction)
                .andThen(
                    Completable.defer { repository.addReaction(AmityReactionReferenceType.COMMENT, commentId, reaction) }
                        .delay(1000, TimeUnit.MILLISECONDS) // Adding a delay to prevent rapid reaction changes
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete {
                            onSuccess()
                        }
                        .doOnError { error ->
                            onError(error)
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
                .let(compositeDisposable::add)
        }
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
            .metadata(AmityMentionMetadataCreator(userMentions).create())
            .mentionUsers(userMentions.map { it.getUserId() })

        val detectedLinks = commentText.extractUrls().mapNotNull { urlPosition ->
            try {
                AmityLink(
                    index = urlPosition.start,
                    length = urlPosition.end - urlPosition.start,
                    url = urlPosition.url,
                    renderPreview = false,
                    domain = null,
                    title = null,
                    imageUrl = null
                )
            } catch (e: Exception) {
                null
            }
        }
        textCommentEditor.links(detectedLinks)

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
        reason: AmityContentFlagReason,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmitySocialClient.newCommentRepository()
            .flagComment(commentId, reason)
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

    fun updateSheetUIState(page: CommentBottomSheetState) {
        _sheetUiState.update { page }
    }

    fun addOptimisticCommentId(parentCommentId: String, commentId: String) {
        _optimisticCommentIds.update { current ->
            val existing = current[parentCommentId].orEmpty()
            if (commentId in existing) current
            else current + (parentCommentId to (listOf(commentId) + existing))
        }
        reorderRepliesForOptimistic(parentCommentId)
    }

    fun clearOptimisticCommentIds() {
        _optimisticCommentIds.update {
            mutableMapOf()
        }
    }

    private fun reorderRepliesForOptimistic(parentCommentId: String) {
        val ids = _optimisticCommentIds.value[parentCommentId].orEmpty()
        if (ids.isEmpty()) return
        val current = _replyComments.value[parentCommentId] ?: return
        val optimistic = current.filter { it.getCommentId() in ids }
        val rest = current.filter { it.getCommentId() !in ids }
        _replyComments.update { it + (parentCommentId to (optimistic + rest)) }
    }

    fun loadMoreReplies(parentCommentId: String) {
        _replyLoading.update { it + (parentCommentId to true) }
        _replyShowLoadMore.update { it + (parentCommentId to false) }
        replyLoaders[parentCommentId]?.load()
    }

    override fun onCleared() {
        super.onCleared()
        replyLoaderDisposables.values.forEach { it.dispose() }
        replyLoaderDisposables.clear()
        replyLoaders.clear()
        clearOptimisticCommentIds()
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
                } else if (loadState is LoadState.NotLoading && itemCount == 0) {
                    EMPTY
                } else if (loadState is LoadState.Error && itemCount == 0) {
                    ERROR
                } else {
                    SUCCESS
                }
            }
        }
    }

    sealed class CommentBottomSheetState(val commentId: String) {
        data class OpenSheet(val id: String) : CommentBottomSheetState(id)

        data class OpenReportSheet(val id: String) : CommentBottomSheetState(id)

        data class OpenReportCustomReasonSheet(val id: String) : CommentBottomSheetState(id)

        data class OpenErrorSheet(val id: String) : CommentBottomSheetState(id)

        object CloseSheet : CommentBottomSheetState("")
    }
}