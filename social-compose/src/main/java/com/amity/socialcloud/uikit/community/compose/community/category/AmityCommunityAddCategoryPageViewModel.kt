package com.amity.socialcloud.uikit.community.compose.community.category

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.category.query.AmityCommunityCategorySortOption
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow

class AmityCommunityAddCategoryPageViewModel : AmityBaseViewModel() {

    private val _selectedItemStates by lazy {
        MutableStateFlow<MutableList<AmityCommunityCategoryItemState>>(mutableListOf())
    }
    val selectedItemStates get() = _selectedItemStates

    fun updateSelectedItemState(categoryId: String, isSelected: Boolean) {
        viewModelScope.launch {
            if (_selectedItemStates.value.find { it.categoryId == categoryId } == null) {
                _selectedItemStates.value.add(
                    AmityCommunityCategoryItemState(categoryId, isSelected)
                )
            } else {
                _selectedItemStates.value = _selectedItemStates.value.map {
                    if (it.categoryId == categoryId) {
                        it.copy(isSelected = isSelected)
                    } else {
                        it
                    }
                }.toMutableList()
            }
        }
    }

    fun getCommunityCategories(): Flow<PagingData<AmityCommunityCategory>> {
        return AmitySocialClient.newCommunityRepository()
            .getCategories()
            .sortBy(AmityCommunityCategorySortOption.NAME)
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }
}

data class AmityCommunityCategoryItemState(
    val categoryId: String,
    var isSelected: Boolean = false,
)
