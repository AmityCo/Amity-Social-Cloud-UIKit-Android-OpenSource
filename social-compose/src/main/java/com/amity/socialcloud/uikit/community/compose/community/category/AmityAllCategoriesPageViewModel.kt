package com.amity.socialcloud.uikit.community.compose.community.category

import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.category.query.AmityCommunityCategorySortOption
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch

class AmityAllCategoriesPageViewModel : AmityBaseViewModel() {

    private val _categoryListState by lazy {
        MutableStateFlow<CategoryListState>(
            CategoryListState.EMPTY
        )
    }
    val categoryListState get() = _categoryListState

    fun setCategoryListState(state: AmityAllCategoriesPageViewModel.CategoryListState) {
        _categoryListState.value = state
    }

    fun getCategories(): Flow<PagingData<AmityCommunityCategory>> {
        return AmitySocialClient.newCommunityRepository()
            .getCategories()
            .sortBy(AmityCommunityCategorySortOption.NAME)
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }

    sealed class CategoryListState {
        object LOADING : CategoryListState()
        object SUCCESS : CategoryListState()
        object EMPTY : CategoryListState()
        object ERROR : CategoryListState()

        companion object {
            fun from(
                loadState: LoadState,
                itemCount: Int,
            ): CategoryListState {
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

