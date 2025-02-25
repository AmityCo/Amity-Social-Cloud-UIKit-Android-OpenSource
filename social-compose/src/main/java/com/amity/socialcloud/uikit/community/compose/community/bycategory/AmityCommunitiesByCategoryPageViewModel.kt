package com.amity.socialcloud.uikit.community.compose.community.bycategory

import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.community.query.AmityCommunitySortOption
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import java.util.concurrent.TimeUnit


class AmityCommunitiesByCategoryPageViewModel : AmityBaseViewModel() {

    private val _communityListState by lazy {
        MutableStateFlow<CommunityListState>(CommunityListState.EMPTY)
    }
    val communityListState get() = _communityListState

    fun setCommunityListState(state: CommunityListState) {
        _communityListState.value = state
    }

    fun getCategory(categoryId: String): Flow<String> {
        return AmitySocialClient.newCommunityRepository()
            .getCategory(categoryId)
            .map {
                it.getName()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {

            }
    }

    fun getCommunities(categoryId: String): Flow<PagingData<AmityCommunity>> {
        return AmitySocialClient.newCommunityRepository()
            .getCommunities()
            .categoryId(categoryId)
            .sortBy(AmityCommunitySortOption.DISPLAY_NAME)
            .includeDeleted(false)
            .build()
            .query()
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
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
                } else if (loadState is LoadState.Error && itemCount == 0) {
                    ERROR
                } else if (itemCount == 0) {
                    EMPTY
                } else {
                    SUCCESS
                }
            }
        }
    }

}

