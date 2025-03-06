package com.amity.socialcloud.uikit.community.compose.community.trending

import androidx.paging.LoadState
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch

class AmityTrendingCommunitiesViewModel : AmityBaseViewModel() {
    private val _communityListState = MutableStateFlow<CommunityListState>(CommunityListState.EMPTY)
    val communityListState: StateFlow<CommunityListState> = _communityListState.asStateFlow()

    private val trendingCommunitiesFlow = AmitySocialClient.newCommunityRepository()
        .getTrendingCommunities()
        .map {
            if (it.size > 5) {
                it.subList(0, 5)
            } else {
                it
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

    fun getTrendingCommunities(): Flow<List<AmityCommunity>> {
        _communityListState.value = CommunityListState.LOADING
        return trendingCommunitiesFlow
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