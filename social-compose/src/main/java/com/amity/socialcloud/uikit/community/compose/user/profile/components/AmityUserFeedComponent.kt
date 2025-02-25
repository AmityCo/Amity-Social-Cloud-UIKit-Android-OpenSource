package com.amity.socialcloud.uikit.community.compose.user.profile.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.paging.feed.user.amityUserFeedLLS
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageViewModel

@Composable
fun AmityUserFeedComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    userId: String,
    shouldRefresh: Boolean = false,
) {
    val context = LocalContext.current
    val viewModel = viewModel<AmityUserProfilePageViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AmityUserProfilePageViewModel(userId) as T
            }
        }
    )
    val userPosts = remember(userId) {
        viewModel.getUserPosts()
    }.collectAsLazyPagingItems()
    val postListState by viewModel.postListState.collectAsState()

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            userPosts.refresh()
        }
    }

    LazyColumn {
        amityUserFeedLLS(
            modifier = modifier,
            context = context,
            pageScope = pageScope,
            userPosts = userPosts,
            postListState = postListState,
            isBlockedByMe = false,
        )
    }
}