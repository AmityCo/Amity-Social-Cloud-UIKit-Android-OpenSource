package com.amity.socialcloud.uikit.community.compose.community.recommending

import androidx.paging.LoadState
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch


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

    private val recommendedCommunitiesFlow = AmitySocialClient.newCommunityRepository()
        .getRecommendedCommunities()
        .map {
            val recommendedCommunities = it.filter { community -> !community.isJoined() }
            if (recommendedCommunities.size > 4) {
                recommendedCommunities.subList(0, 4)
            } else {
                recommendedCommunities
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