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


private const val COMMENT_PREVIEW_SIZE = 0
private const val TYPICAL_REPLY_PAGE_SIZE = 15

class AmityStoryCommentReplyLoader(
    referenceId: String,
    referenceType: AmityCommentReferenceType,
    parentCommentId: String,
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
            .sortBy(AmityCommentSortOption.LAST_CREATED)
            .includeDeleted(true)
            .build()
            .loader()
    }

    private val commentsSubject = PublishSubject.create<List<AmityComment>>()
    private val showLoadMoreButtonSubject = PublishSubject.create<Boolean>()
    private var loadedComments: List<AmityComment> = emptyList()
    private var publishingComments: List<AmityComment> = emptyList()
    private var isLoading: Boolean = false
    private var publishingSize = COMMENT_PREVIEW_SIZE
    private var selfLoad: Boolean = false

    fun showLoadMoreButton(): Flowable<Boolean> {
        return showLoadMoreButtonSubject.toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showLoadMoreButtonSubject.onNext(shouldShowLoadMoreButton())
            }
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
                publishingComments = loadedComments.take(publishingSize)
            }
            showLoadMoreButtonSubject.onNext(shouldShowLoadMoreButton())
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
        showLoadMoreButtonSubject.onNext(shouldShowLoadMoreButton())

        val targetSize =
            if (isReload) COMMENT_PREVIEW_SIZE else publishingSize + TYPICAL_REPLY_PAGE_SIZE
        if (loadedComments.size >= targetSize) {
            publishingSize = targetSize
            isLoading = false
            commentsSubject.onNext(loadedComments.take(publishingSize))
        } else {
            loader.load().concatWith(
                Completable.defer {
                    val keepLoading = loader.hasMore() && loadedComments.size < targetSize
                    if (keepLoading) {
                        selfLoad = true
                        load()
                        Completable.complete()
                    } else {
                        publishingSize = targetSize
                        isLoading = false
                        commentsSubject.onNext(loadedComments.take(publishingSize))
                        Completable.complete()
                    }
                }
            ).doFinally {
                isLoading = false
                showLoadMoreButtonSubject.onNext(shouldShowLoadMoreButton())
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }
    }

    private fun shouldShowLoadMoreButton(): Boolean {
        return !isLoading && (loadedComments.size > publishingComments.size || loader.hasMore())
    }

}