package com.amity.socialcloud.uikit.community.compose.community.trending

import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.community.query.AmityCommunitySortOption
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityFilter
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.compose.community.recommending.AmityRecommendedCommunitiesViewModel
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch

class AmityTrendingCommunitiesViewModel: AmityBaseViewModel() {

    private val _communityListState by lazy {
        MutableStateFlow<CommunityListState>(CommunityListState.EMPTY)
    }

    val communityListState get() = _communityListState

    private fun setCommunityListState(state: CommunityListState) {
        _communityListState.value = state
    }

    fun getTrendingCommunities(): Flow<List<AmityCommunity>> {
        setCommunityListState(CommunityListState.LOADING)
        return AmitySocialClient.newCommunityRepository()
            .getTrendingCommunities()
            .map {
                if(it.size > 5) {
                    it.subList(0, 5)
                } else {
                    it
                }
            }
//            .map {
//                emptyList<AmityCommunity>()
//            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                if(it.isEmpty() && communityListState.value != CommunityListState.EMPTY) {
                    setCommunityListState(CommunityListState.EMPTY)
                }
                else if(it.isNotEmpty() && communityListState.value != CommunityListState.SUCCESS) {
                    setCommunityListState(CommunityListState.SUCCESS)
                }
            }
            .asFlow()
            .catch {
                setCommunityListState(CommunityListState.ERROR)
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