package com.amity.socialcloud.uikit.community.compose.community.recommending

import androidx.paging.LoadState
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequest
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequestStatus
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.utils.isSignedIn
import io.reactivex.Flowable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlin.compareTo
import kotlin.printStackTrace


class AmityRecommendedCommunitiesViewModel : AmityBaseViewModel() {

    private val _communityListState by lazy {
        MutableStateFlow<CommunityListState>(
            CommunityListState.EMPTY
        )
    }

    val communityListState get() = _communityListState

    private fun setCommunityListState(state: CommunityListState) {
        _communityListState.value = state
    }

    private val _joinRequestList: MutableStateFlow<List<AmityJoinRequest>> =
        MutableStateFlow(emptyList())
    val joinRequestList: StateFlow<List<AmityJoinRequest>> = _joinRequestList.asStateFlow()


    private val recommendedCommunitiesFlow = AmitySocialClient.newCommunityRepository()
        .getRecommendedCommunities(includeDiscoverablePrivateCommunity = true)
        .map { communities ->
            // Get all community IDs that are not already joined
            val notJoinedCommunities = communities.filter { !it.isJoined() }

            val communityIds = notJoinedCommunities.map { it.getCommunityId() }

            getJoinRequestList(communityIds)

            // Filter communities that don't have pending join requests
            // Since getLocalJoinRequest() returns a list, check if any request has PENDING status
            val filteredCommunities = notJoinedCommunities.filter { community ->
                val localJoinRequests = community.getLocalJoinRequest()
                localJoinRequests == null || localJoinRequests.none { it.getStatus() == AmityJoinRequestStatus.PENDING }
            }

            // Take up to 4 communities
            if (filteredCommunities.size > 4) {
                filteredCommunities.subList(0, 4)
            } else {
                filteredCommunities
            }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext {
            if (it.isEmpty() && _communityListState.value != CommunityListState.EMPTY) {
                _communityListState.value = CommunityListState.EMPTY
            } else if (it.isNotEmpty() && _communityListState.value != CommunityListState.SUCCESS) {
                _communityListState.value = CommunityListState.SUCCESS
            }
        }
        .asFlow()
        .catch {
            _communityListState.value = CommunityListState.ERROR
        }

    private fun getJoinRequestList(communityIds: List<String>) {
        if (AmityCoreClient.isSignedIn()) {
            addDisposable(
                AmitySocialClient.newCommunityRepository().getJoinRequestList(communityIds)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { it.printStackTrace() }
                    .doOnNext { requests ->
                        _joinRequestList.update {
                            requests
                        }
                    }
                    .subscribe()
            )
        }
    }

    fun getRecommendedCommunities(): Flow<List<AmityCommunity>> {
        _communityListState.value = CommunityListState.LOADING
        return recommendedCommunitiesFlow
    }

    fun updateMembership(community: AmityCommunity) {
        val isJoined = community.isJoined()
        if (isJoined) {
            AmitySocialClient.newCommunityRepository().leaveCommunity(community.getCommunityId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { }
                .subscribe()
        } else {
            AmitySocialClient.newCommunityRepository().joinCommunity(community.getCommunityId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { }
                .subscribe()
        }
    }

    sealed class CommunityListState {
        object LOADING : CommunityListState()
        object SUCCESS : CommunityListState()
        object EMPTY : CommunityListState()
        object ERROR : CommunityListState()

        companion object {
            fun from(
                loadState: LoadState,
                itemCount: Int,
            ): CommunityListState {
                return if (loadState is LoadState.Loading) {
                    LOADING
                } else if (loadState is LoadState.NotLoading && itemCount == 0 && loadState.endOfPaginationReached) {
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