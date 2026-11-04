package com.amity.socialcloud.uikit.community.compose.search.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserListItem
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.search.global.AmityGlobalSearchViewModel
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityUserListShimmer
import kotlinx.coroutines.delay

private const val EMPTY_RESULT_GRACE_MS = 1_000L

@Composable
fun AmityUserSearchResultComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityGlobalSearchViewModel,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.userSearchResultComponentBehavior
    }

    // The keyword getter returns a new debounced flow on each call. Keep one, so a recomposition
    // does not restart the debounce.
    val keyword by remember { viewModel.keyword }.collectAsState("")

    val users = remember(keyword) {
        viewModel.searchUsers()
    }.collectAsLazyPagingItems()

    // PDT-6753: same cause as PDT-6353. The combined refresh state reads NotLoading while the local
    // query or the network refresh is still running, so either one still loading counts as loading.
    // The empty state is held back until it has stood for a moment.
    val combinedLoadState = users.loadState
    val isAnyRefreshLoading = combinedLoadState.refresh is LoadState.Loading ||
        combinedLoadState.source.refresh is LoadState.Loading ||
        combinedLoadState.mediator?.refresh is LoadState.Loading
    val listState = AmityGlobalSearchViewModel.UserListState.from(
        loadState = if (isAnyRefreshLoading) LoadState.Loading else combinedLoadState.refresh,
        itemCount = users.itemCount,
        keyword = keyword
    )
    LaunchedEffect(listState) {
        if (listState == AmityGlobalSearchViewModel.UserListState.EMPTY) {
            delay(EMPTY_RESULT_GRACE_MS)
        }
        viewModel.setUserListState(listState)
    }

    val loadState by viewModel.userListState.collectAsState()

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "user_search_result"
    ) {
        if (keyword.isNotEmptyOrBlank()) {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                when (loadState) {
                    AmityGlobalSearchViewModel.UserListState.SHORT_INPUT -> {
                        item {
                            AmityShortSearchInputComponent(modifier)
                        }
                    }

                    AmityGlobalSearchViewModel.UserListState.EMPTY -> {
                        item {
                            AmityEmptySearchResultComponent(modifier)
                        }
                    }

                    AmityGlobalSearchViewModel.UserListState.LOADING -> {
                        item {
                            AmityUserListShimmer(
                                modifier = modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }

                    AmityGlobalSearchViewModel.UserListState.SUCCESS -> {
                        items(
                            count = users.itemCount,
                            key = { index -> index }
                        ) { index ->
                            val user = users[index] ?: return@items

                            AmityUserListItem(
                                modifier = modifier,
                                user = user,
                                showRightMenu = false,
                                onUserClick = {
                                    behavior.goToUserProfilePage(
                                        context = context,
                                        user = user,
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}