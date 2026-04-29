package com.amity.socialcloud.uikit.community.compose.comment.query

import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.comment.query.AmityCommentLoader
import com.amity.socialcloud.sdk.api.social.comment.query.AmityCommentSortOption
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CompletableDeferred


private const val COMMENT_PREVIEW_SIZE = 0
private const val DEFAULT_REPLY_PAGE_SIZE = 5

class AmityStoryCommentReplyLoader(
    referenceId: String,
    referenceType: AmityCommentReferenceType,
    private val parentCommentId: String,
    includeDeleted: Boolean = false,
    isL2Thread: Boolean = false,
    private val replyPageSize: Int = DEFAULT_REPLY_PAGE_SIZE,
) {
    private var loader: AmityCommentLoader

    init {
        loader = AmitySocialClient.newCommentRepository()
            .getComments()
            .run {

                when (referenceType) {
                    AmityCommentReferenceType.POST -> post(referenceId)
                    AmityCommentReferenceType.STORY -> story(referenceId)
                    AmityCommentReferenceType.CONTENT -> content(referenceId)
                }
            }
            .parentId(parentCommentId)
            .sortBy(
                if (isL2Thread) AmityCommentSortOption.FIRST_CREATED
                else AmityCommentSortOption.LAST_CREATED
            )
            .includeDeleted(includeDeleted)
            .pageSize(replyPageSize)
            .build()
            .loader()
    }

    private val commentsSubject = PublishSubject.create<List<AmityComment>>()
    private val showLoadMoreButtonSubject = PublishSubject.create<Boolean>()
    private val loadingSubject = PublishSubject.create<Boolean>()
    private val loadErrorSubject = PublishSubject.create<Throwable>()
    private var loadedComments: List<AmityComment> = emptyList()
    private var publishingComments: List<AmityComment> = emptyList()
    private var isLoading: Boolean = false
    private var publishingSize = COMMENT_PREVIEW_SIZE
    private var selfLoad: Boolean = false
    private var loadGeneration = 0
    @Volatile
    private var hasCompletedInitialLoad = false

    /** Completes when the first emission from the SDK's live query has been processed. */
    val readyDeferred = CompletableDeferred<Unit>()

    fun showLoadMoreButton(): Flowable<Boolean> {
        return showLoadMoreButtonSubject.toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showLoadMoreButtonSubject.onNext(shouldShowLoadMoreButton())
            }
    }

    fun isLoadingReplies(): Flowable<Boolean> {
        return loadingSubject.toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadingSubject.onNext(isLoading)
            }
    }

    fun getLoadErrors(): Flowable<Throwable> {
        return loadErrorSubject.toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getComments(): Flowable<List<AmityComment>> {
        return Flowable.combineLatest(
            loader.getResult(),
            commentsSubject.toFlowable(BackpressureStrategy.BUFFER)
                .startWith(Single.just(mutableListOf()))
        ) { loadedResult, publishingResult ->
            if (publishingResult.size > publishingComments.size) {
                publishingComments = publishingResult
            } else {
                loadedComments = loadedResult
                publishingComments = if (hasCompletedInitialLoad) {
                    loadedComments.take(publishingSize)
                } else {
                    emptyList()
                }
            }
            showLoadMoreButtonSubject.onNext(shouldShowLoadMoreButton())
            readyDeferred.complete(Unit)
            publishingComments
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun load(isReload: Boolean = false) {
        if (!selfLoad && !shouldShowLoadMoreButton()) {
            showLoadMoreButtonSubject.onNext(shouldShowLoadMoreButton())
            return
        }
        selfLoad = false
        isLoading = true
        loadingSubject.onNext(true)
        showLoadMoreButtonSubject.onNext(shouldShowLoadMoreButton())

        val targetSize =
            if (isReload) COMMENT_PREVIEW_SIZE else publishingSize + replyPageSize
        if (loadedComments.size >= targetSize) {
            publishingSize = targetSize
            hasCompletedInitialLoad = true
            commentsSubject.onNext(loadedComments.take(publishingSize))
            isLoading = false
            loadingSubject.onNext(false)
        } else {
            val currentGeneration = ++loadGeneration
            loader.load().concatWith(
                Completable.defer {
                    val keepLoading = loader.hasMore() && loadedComments.size < targetSize
                    if (keepLoading) {
                        selfLoad = true
                        load()
                        Completable.complete()
                    } else {
                        publishingSize = targetSize
                        hasCompletedInitialLoad = true
                        commentsSubject.onNext(loadedComments.take(publishingSize))
                        isLoading = false
                        loadingSubject.onNext(false)
                        Completable.complete()
                    }
                }
            ).doFinally {
                // Only emit loading=false if no newer load() was started (e.g. recursive load)
                if (currentGeneration == loadGeneration) {
                    isLoading = false
                    loadingSubject.onNext(false)
                    showLoadMoreButtonSubject.onNext(shouldShowLoadMoreButton())
                } else {
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { error ->
                    loadErrorSubject.onNext(error)
                })
        }
    }

    fun forceRefresh() {
        if (!hasCompletedInitialLoad) {
            hasCompletedInitialLoad = true
            publishingSize = maxOf(publishingSize, 15)
        }
        commentsSubject.onNext(loadedComments.take(publishingSize))
    }

    private fun shouldShowLoadMoreButton(): Boolean {
        return !isLoading && (loadedComments.size > publishingComments.size || loader.hasMore())
    }

}