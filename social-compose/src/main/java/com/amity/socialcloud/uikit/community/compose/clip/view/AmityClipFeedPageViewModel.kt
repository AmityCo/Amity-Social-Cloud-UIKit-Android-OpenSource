package com.amity.socialcloud.uikit.community.compose.clip.view

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageViewModel.ReactionAction
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AmityClipFeedPageViewModel : AmityBaseViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow<String?>(null)
    val isError: StateFlow<String?> = _isError.asStateFlow()

    private val _amityClip = MutableStateFlow<List<AmityPost>>(emptyList())
    val amityClip: StateFlow<List<AmityPost>> = _amityClip.asStateFlow()

    private val _amityClips = MutableStateFlow<PagingData<AmityPost>>(PagingData.empty())
    val amityClips: StateFlow<PagingData<AmityPost>> = _amityClips.asStateFlow()

    private val _clipUrl = MutableStateFlow<String>("")
    val clipUrl = _clipUrl.asStateFlow()

    private val _parentPost = MutableStateFlow<AmityPost?>(null)
    val parentPost: StateFlow<AmityPost?> = _parentPost.asStateFlow()

    private val _isCommunityModerator = MutableStateFlow<Boolean>(false)
    val isCommunityModerator: StateFlow<Boolean> = _isCommunityModerator.asStateFlow()

    private val _sheetUIState by lazy {
        MutableStateFlow<AmityClipModalSheetUIState>(AmityClipModalSheetUIState.CloseSheet)
    }
    val sheetUIState get() = _sheetUIState

    private val changeReactionSubject: PublishSubject<ReactionAction> =
        PublishSubject.create()

    init {
        observeReactionChange()
    }

    fun queryClipOnGlobalFeed() {
        addDisposable(
            AmitySocialClient.newFeedRepository()
                .getGlobalFeed()
                .dataTypes(
                    listOf(
                        AmityPost.DataType.sealedOf(AmityPost.DataType.CLIP.getApiKey()),
                        AmityPost.DataType.sealedOf(AmityPost.DataType.VIDEO.getApiKey())
                    )
                )
                .build()
                .query()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { pagingData ->
                    _amityClips.update {
                        pagingData
                    }
                }
                .doOnError { error ->
                }
                .subscribe()
        )
    }

    fun queryClipOnNewFeed(postId: String) {
        _isLoading.update { true }
        viewModelScope.launch {
            AmitySocialClient.newPostRepository()
                .getPost(postId = postId)
                .asFlow()
                .catch { error ->
                    println("Popp Error fetching post: $error")
                    _isLoading.update { false }
                    _isError.update { error.message }
                }
                .collect { post ->
                    _isLoading.update { false }
                    _amityClip.update {
                        listOf(post)
                    }
                }
        }
    }

    fun queryClipOnUserFeed(postId: String) {
        viewModelScope.launch {
            AmitySocialClient.newPostRepository()
                .getPost(postId = postId)
                .asFlow()
                .catch { }
                .collectLatest { post ->
                    _amityClip.update {
                        listOf(post)
                    }
                    queryPagingClipOnUserFeed(singlePost = post)
                }
        }
    }

    fun queryPagingClipOnUserFeed(singlePost: AmityPost) {
        viewModelScope.launch {
            AmitySocialClient.newPostRepository()
                .getPosts()
                .targetUser(singlePost.getCreator()?.getUserId() ?: "")
                .dataTypes(listOf(AmityPost.DataType.sealedOf(AmityPost.DataType.CLIP.getApiKey())))
                .includeDeleted(false)
                .build()
                .query()
                .asFlow()
                .catch { }
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _amityClips.update { pagingData }
                }
        }
    }

    fun queryClipOnCommunityFeed(postId: String, communityId: String) {
        viewModelScope.launch {
            AmitySocialClient.newPostRepository()
                .getPost(postId = postId)
                .asFlow()
                .catch { }
                .collectLatest { post ->
                    _amityClip.update {
                        listOf(post)
                    }
                    queryPagingClipOnCommunityFeed(communityId = communityId)
                }
        }
    }

    private fun queryPagingClipOnCommunityFeed(communityId: String) {
        viewModelScope.launch {
            AmitySocialClient.newPostRepository()
                .getPosts()
                .targetCommunity(communityId)
                .dataTypes(listOf(AmityPost.DataType.sealedOf(AmityPost.DataType.CLIP.getApiKey())))
                .includeDeleted(false)
                .build()
                .query()
                .asFlow()
                .catch {}
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _amityClips.update { pagingData }
                }
        }
    }

    fun getClipUrl(post: AmityPost.Data.CLIP) {
        addDisposable(
            post.getClip()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { clip ->
                    clip.getClipUrl()?.let { url ->
                        _clipUrl.update {
                            url
                        }
                    }
                }
                .doOnError { error ->
                    // Handle error
                    println("Error fetching clip URL: $error")
                }
                .subscribe()
        )
    }

    fun getVideoUrl(post: AmityPost.Data.VIDEO) {
        addDisposable(
            post.getVideo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { clip ->
                    clip.getVideoUrl()?.let { url ->
                        _clipUrl.update {
                            url
                        }
                    }
                }
                .doOnError { error ->
                    // Handle error
                    println("Error fetching clip URL: $error")
                }
                .subscribe()
        )
    }


    fun getParentPost(parentPostId: String) {
        viewModelScope.launch {
            AmitySocialClient.newPostRepository()
                .getPost(parentPostId)
                .take(1)
                .asFlow()
                .flowOn(Dispatchers.IO)
                .distinctUntilChanged() // Only emit when data actually changes
                .collectLatest { post -> // collectLatest cancels previous collection when new emission arrives
                    if (post.getPostId() == parentPostId) {
                        _parentPost.value = post
                    }
                }
                .runCatching { }
        }
    }

    fun isCommunityModerator(post: AmityPost?) {
        viewModelScope.launch {
            val roles =
                (post?.getTarget() as? AmityPost.Target.COMMUNITY)?.getCreatorMember()?.getRoles()
            _isCommunityModerator.update {
                roles?.any {
                    it == AmityConstants.MODERATOR_ROLE || it == AmityConstants.COMMUNITY_MODERATOR_ROLE
                } ?: false
            }
        }
    }

    fun changeReaction(
        postId: String,
        reactionName: String,
        isReacted: Boolean,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        ReactionAction(
            postId = postId,
            reactionName = reactionName,
            isReacted = isReacted,
            onSuccess = onSuccess,
            onError = onError
        ).let(changeReactionSubject::onNext)
    }

    private fun observeReactionChange() {
        changeReactionSubject
            .throttleLatest(1000, TimeUnit.MILLISECONDS)
            .flatMapCompletable { action ->
                if (action.isReacted) {
                    addReaction(
                        postId = action.postId,
                        reaction = action.reactionName
                    )
                } else {
                    removeReaction(
                        postId = action.postId,
                        reaction = action.reactionName
                    )
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        action.onSuccess()
                    }
                    .doOnError { error ->
                        action.onError(error)
                    }
                    .onErrorComplete()
            }
            .subscribe()
            .let(compositeDisposable::add)
    }

    private fun addReaction(postId: String, reaction: String): Completable {
        return AmityCoreClient.newReactionRepository()
            .addReaction(AmityReactionReferenceType.POST, postId, reaction)
    }

    fun switchReaction(
        postId: String,
        reaction: String,
        previousReaction: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        AmityCoreClient.newReactionRepository().let { repository ->
            repository
                .removeReaction(AmityReactionReferenceType.POST, postId, previousReaction)
                .andThen(
                    Completable.defer { repository.addReaction(AmityReactionReferenceType.POST, postId, reaction) }
                        .delay(2000, TimeUnit.MILLISECONDS) // Adding a delay to prevent rapid reaction changes
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

    private fun removeReaction(postId: String, reaction: String): Completable {
        return AmityCoreClient.newReactionRepository()
            .removeReaction(AmityReactionReferenceType.POST, postId, reaction)
    }

    fun updateSheetUIState(sheetUiState: AmityClipModalSheetUIState) {
        viewModelScope.launch {
            _sheetUIState.value = sheetUiState
        }
    }

}

sealed class AmityClipModalSheetUIState {

    data class OpenClipMenuSheet(
        val post: AmityPost,
    ) : AmityClipModalSheetUIState()

    data class OpenCommentTraySheet(
        val postId: String,
        val community: AmityCommunity? = null,
        val shouldAllowInteraction: Boolean,
        val shouldAllowComment: Boolean,
    ) : AmityClipModalSheetUIState()

    object CloseSheet : AmityClipModalSheetUIState()
}