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
import com.amity.socialcloud.uikit.community.compose.paging.feed.user.amityUserImageFeedLLS
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageViewModel

@Composable
fun AmityUserImageFeedComponent(
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
    val imagePosts = remember(userId) {
        viewModel.getUserImagePosts()
    }.collectAsLazyPagingItems()
    val postListState by viewModel.imagePostListState.collectAsState()

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            imagePosts.refresh()
        }
    }

    LazyColumn {
        AmityUserProfilePageViewModel.PostListState.from(
            loadState = imagePosts.loadState.refresh,
            itemCount = imagePosts.itemCount
        ).let(viewModel::setImagePostListState)

        amityUserImageFeedLLS(
            modifier = modifier,
            imagePosts = imagePosts,
            postListState = postListState,
            isBlockedByMe = false,
        )
    }
}