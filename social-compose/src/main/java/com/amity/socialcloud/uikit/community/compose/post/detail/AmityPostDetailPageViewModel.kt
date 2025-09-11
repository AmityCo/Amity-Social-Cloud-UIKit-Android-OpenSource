package com.amity.socialcloud.uikit.community.compose.post.detail

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.sdk.model.core.events.AmityPostEvents
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit

class AmityPostDetailPageViewModel : AmityBaseViewModel() {

    private val changeReactionSubject: PublishSubject<ReactionAction> =
        PublishSubject.create()
    private val _internetState =
        MutableStateFlow<NetworkConnectionEvent>(NetworkConnectionEvent.Connected)
    val internetState = _internetState.asStateFlow()

    private val _postErrorState =
        MutableStateFlow<Boolean>(false)
    val postErrorState = _postErrorState.asStateFlow()

    init {
        observeReactionChange()
    }

    private val fetchPostError = MutableStateFlow<Throwable?>(null)

    fun getFetchErrorState() = fetchPostError.asStateFlow()

    fun getCurrentUser(): Flowable<AmityUser> {
        return AmityCoreClient.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getPost(postId: String): Flow<AmityPost> {
        fetchPostError.value = null
        return AmitySocialClient.newPostRepository()
            .getPost(postId)
            .asFlow()
            .catch {
                fetchPostError.emit(it)
                if (it is AmityException) {
                    if (it.code == AmityError.CONNECTION_ERROR.code) {
                        _internetState.update {
                            NetworkConnectionEvent.Disconnected
                        }
                    } else {
                        _postErrorState.update { true }
                    }
                }
            }
    }

    fun subscribePostRT(post: AmityPost) {
        post.subscription(events = AmityPostEvents.POST)
            .subscribeTopic()
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun unSubscribePostRT(post: AmityPost) {
        post.subscription(events = AmityPostEvents.POST)
            .unsubscribeTopic()
            .subscribeOn(Schedulers.io())
            .subscribe()
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

    fun isCommunityModerator(post: AmityPost?): Boolean {
        val roles =
            (post?.getTarget() as? AmityPost.Target.COMMUNITY)?.getCreatorMember()?.getRoles()
        return roles?.any {
            it == AmityConstants.MODERATOR_ROLE || it == AmityConstants.COMMUNITY_MODERATOR_ROLE
        } ?: false
    }

    data class ReactionAction(
        val postId: String,
        val reactionName: String,
        val isReacted: Boolean,
        val onSuccess: () -> Unit,
        val onError: (Throwable) -> Unit,
    )
}