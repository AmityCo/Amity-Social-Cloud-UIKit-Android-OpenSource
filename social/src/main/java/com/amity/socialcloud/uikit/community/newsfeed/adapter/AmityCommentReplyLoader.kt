package com.amity.socialcloud.uikit.community.newsfeed.adapter

import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.sdk.social.comment.AmityCommentLoader
import com.amity.socialcloud.sdk.social.comment.AmityCommentSortOption
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject


private const val COMMENT_PREVIEW_SIZE = 3
private const val TYPICAL_REPLY_PAGE_SIZE = 5

class AmityCommentReplyLoader(comment: AmityComment) {

    private var loader: AmityCommentLoader

    init {
        val postId = (comment.getReference() as? AmityComment.Reference.POST)?.getPostId() ?: ""
        loader = AmitySocialClient.newCommentRepository()
            .getComments()
            .post(postId)
            .parentId(comment.getCommentId())
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
            .doOnSubscribe {
                showLoadMoreButtonSubject.onNext(shouldShowLoadMoreButton())
            }
    }

    fun getComments(): Flowable<List<AmityComment>> {
        return Flowable.combineLatest(
            loader.getResult(),
            commentsSubject.toFlowable(BackpressureStrategy.BUFFER).startWith(mutableListOf<AmityComment>()),
            BiFunction { loadedResult, publishingResult ->
                if(publishingResult.size > publishingComments.size) {
                    publishingComments = publishingResult
                } else {
                    loadedComments = loadedResult
                    publishingComments = loadedComments.take(publishingSize)
                }
                showLoadMoreButtonSubject.onNext(shouldShowLoadMoreButton())
                publishingComments
            }
        )
    }

    fun load(isReload : Boolean = false): Completable {
        if(!selfLoad && !shouldShowLoadMoreButton()) {
            showLoadMoreButtonSubject.onNext(shouldShowLoadMoreButton())
            return Completable.complete()
        }
        selfLoad = false
        isLoading = true
        showLoadMoreButtonSubject.onNext(shouldShowLoadMoreButton())

        val targetSize = if(isReload) COMMENT_PREVIEW_SIZE else publishingSize + TYPICAL_REPLY_PAGE_SIZE
        if (loadedComments.size >= targetSize) {
            publishingSize = targetSize
            isLoading = false
            commentsSubject.onNext(loadedComments.take(publishingSize))
            return Completable.complete()
        } else {
            return loader.load().concatWith(Completable.defer {
                val keepLoading = loader.hasMore() && loadedComments.size < targetSize
                if(keepLoading) {
                    selfLoad = true
                    load()
                } else {
                    publishingSize = targetSize
                    isLoading = false
                    commentsSubject.onNext(loadedComments.take(publishingSize))
                    Completable.complete()
                }
            }).doFinally {
                isLoading = false
                showLoadMoreButtonSubject.onNext(shouldShowLoadMoreButton())
            }
        }
    }

    private fun shouldShowLoadMoreButton() : Boolean {
        return !isLoading && (loadedComments.size > publishingComments.size || loader.hasMore())
    }

}