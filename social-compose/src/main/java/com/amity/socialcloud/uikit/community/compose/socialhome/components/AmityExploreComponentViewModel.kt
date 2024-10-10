package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.community.compose.community.category.component.AmityCommunityCategoriesViewModel
import com.amity.socialcloud.uikit.community.compose.community.recommending.AmityRecommendedCommunitiesViewModel
import com.amity.socialcloud.uikit.community.compose.community.trending.AmityTrendingCommunitiesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AmityExploreComponentViewModel : AmityBaseViewModel() {

    private val _isRefreshing by lazy {
        MutableStateFlow(false)
    }
    val isRefreshing get() = _isRefreshing

    private val _isEmpty by lazy {
        MutableStateFlow(false)
    }
    val isEmpty get() = _isEmpty

    private val _isError by lazy {
        MutableStateFlow(false)
    }
    val isError get() = _isError


    private var categoryState: AmityCommunityCategoriesViewModel.CategoryListState =
        AmityCommunityCategoriesViewModel.CategoryListState.LOADING
    private var recommendedState: AmityRecommendedCommunitiesViewModel.CommunityListState =
        AmityRecommendedCommunitiesViewModel.CommunityListState.LOADING
    private var trendingState: AmityTrendingCommunitiesViewModel.CommunityListState =
        AmityTrendingCommunitiesViewModel.CommunityListState.LOADING


    fun setRefreshing() {
        viewModelScope.launch {
            _isError.value = false
            _isEmpty.value = false
            isRefreshing.value = true
            delay(1500)
            isRefreshing.value = false
        }
    }

    fun setCategoryState(state: AmityCommunityCategoriesViewModel.CategoryListState) {
        if (categoryState == state) {
            return
        }
        categoryState = state
        recheckState()
    }

    fun getCategoryState(): AmityCommunityCategoriesViewModel.CategoryListState {
        return categoryState
    }

    fun setRecommendedState(state: AmityRecommendedCommunitiesViewModel.CommunityListState) {
        if (recommendedState == state) {
            return
        }
        recommendedState = state
        recheckState()
    }

    fun setTrendingState(state: AmityTrendingCommunitiesViewModel.CommunityListState) {
        if (trendingState == state) {
            return
        }
        trendingState = state
        recheckState()
    }

    private fun recheckState() {
        if (recommendedState == AmityRecommendedCommunitiesViewModel.CommunityListState.EMPTY &&
            trendingState == AmityTrendingCommunitiesViewModel.CommunityListState.EMPTY
        ) {
            if (isEmpty.value && !isError.value) {
                return
            }
            _isEmpty.value = true
            _isError.value = false
        } else if (recommendedState == AmityRecommendedCommunitiesViewModel.CommunityListState.ERROR &&
            trendingState == AmityTrendingCommunitiesViewModel.CommunityListState.ERROR
        ) {
            if (!isEmpty.value && isError.value) {
                return
            }
            _isError.value = true
            _isEmpty.value = false
        } else if (recommendedState != AmityRecommendedCommunitiesViewModel.CommunityListState.LOADING &&
            trendingState != AmityTrendingCommunitiesViewModel.CommunityListState.LOADING
        ) {
            if (!isEmpty.value && !isError.value) {
                return
            }
            _isEmpty.value = false
            _isError.value = false
        }
    }

}