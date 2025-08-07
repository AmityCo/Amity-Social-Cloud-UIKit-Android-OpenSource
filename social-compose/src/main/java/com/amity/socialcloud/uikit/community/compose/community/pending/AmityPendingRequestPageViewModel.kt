package com.amity.socialcloud.uikit.community.compose.community.pending

import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.post.query.AmityCommunityFeedSortOption
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequest
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequestStatus
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AmityPendingRequestPageViewModel(val communityId: String) : AmityBaseViewModel() {

    private val _requestListState by lazy {
        MutableStateFlow<RequestListState>(RequestListState.EMPTY)
    }
    val postListState get() = _requestListState

    fun setRequestListState(state: RequestListState) {
        _requestListState.value = state
    }

    fun getPendingPosts(): Flow<PagingData<AmityPost>> {
        return AmitySocialClient.newPostRepository().getPosts()
            .targetCommunity(communityId)
            .reviewStatus(AmityReviewStatus.UNDER_REVIEW)
            .sortBy(AmityCommunityFeedSortOption.LAST_CREATED)
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun approvePost(
        postId: String,
        onApproved: () -> Unit,
        onAlreadyApproved: () -> Unit,
        onError: () -> Unit,
    ) {
        AmitySocialClient.newPostRepository()
            .approvePost(postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onApproved)
            .doOnError {
                if (AmityError.from(it) == AmityError.FORBIDDEN_ERROR) {
                    onAlreadyApproved.invoke()
                } else {
                    onError.invoke()
                }
            }
            .subscribe()
    }

    fun declinePost(
        postId: String,
        onApproved: () -> Unit,
        onAlreadyDeclined: () -> Unit,
        onError: () -> Unit,
    ) {
        AmitySocialClient.newPostRepository()
            .declinePost(postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onApproved)
            .doOnError {
                if (AmityError.from(it) == AmityError.FORBIDDEN_ERROR) {
                    onAlreadyDeclined.invoke()
                } else {
                    onError.invoke()
                }
            }
            .subscribe()
    }

    fun getJoinRequests(
        status: AmityJoinRequestStatus,
        community: AmityCommunity,
    ): Flow<PagingData<AmityJoinRequest>> {
        return community.getJoinRequests(status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun approveJoinRequest(
        joinRequest: AmityJoinRequest,
        onApproved: () -> Unit = {},
        onAlreadyApproved: () -> Unit = {},
        onError: () -> Unit = {},
    ) {
        joinRequest.approve().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onApproved)
            .doOnError {
                if (AmityError.from(it) == AmityError.BUSINESS_ERROR || AmityError.from(it) == AmityError.ITEM_NOT_FOUND) {
                    onAlreadyApproved.invoke()
                } else {
                    onError.invoke()
                }
            }
            .subscribe()
    }

    fun declineJoinRequest(
        joinRequest: AmityJoinRequest,
        onDeclined: () -> Unit = {},
        onAlreadyDeclined: () -> Unit = {},
        onError: () -> Unit = {},
    ) {
        joinRequest.reject().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onDeclined)
            .doOnError {
                if (AmityError.from(it) == AmityError.BUSINESS_ERROR || AmityError.from(it) == AmityError.ITEM_NOT_FOUND) {
                    onAlreadyDeclined.invoke()
                } else {
                    onError.invoke()
                }
            }
            .subscribe()
    }

    sealed class RequestListState {
        object LOADING : RequestListState()
        object SUCCESS : RequestListState()
        object EMPTY : RequestListState()
        object ERROR : RequestListState()

        companion object {
            fun from(
                loadState: LoadState,
                itemCount: Int,
            ): RequestListState {
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
}