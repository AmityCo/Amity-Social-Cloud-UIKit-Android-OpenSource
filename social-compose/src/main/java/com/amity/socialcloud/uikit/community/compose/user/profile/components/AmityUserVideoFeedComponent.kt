package com.amity.socialcloud.uikit.community.compose.user.profile.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.community.compose.paging.feed.user.amityUserVideoFeedLLS
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageViewModel

@Composable
fun AmityUserVideoFeedComponent(
    modifier: Modifier = Modifier,
    userId: String,
    shouldRefresh: Boolean = false,
) {
    val viewModel = viewModel<AmityUserProfilePageViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AmityUserProfilePageViewModel(userId) as T
            }
        }
    )
    val videoPosts = remember(userId) {
        viewModel.getUserVideoPosts()
    }.collectAsLazyPagingItems()
    val postListState by viewModel.videoPostListState.collectAsState()

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            videoPosts.refresh()
        }
    }

    LazyColumn {
        AmityUserProfilePageViewModel.PostListState.from(
            loadState = videoPosts.loadState.refresh,
            itemCount = videoPosts.itemCount
        ).let(viewModel::setVideoPostListState)

        amityUserVideoFeedLLS(
            modifier = modifier,
            videoPosts = videoPosts,
            postListState = postListState,
            isBlockedByMe = false,
        )
    }
}