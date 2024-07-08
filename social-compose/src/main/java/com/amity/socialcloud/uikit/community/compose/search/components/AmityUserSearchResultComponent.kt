package com.amity.socialcloud.uikit.community.compose.search.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.search.global.AmitySocialGlobalSearchPageViewModel
import com.amity.socialcloud.uikit.community.compose.socialhome.elements.AmityUserView
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityUserListShimmer

@Composable
fun AmityUserSearchResultComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmitySocialGlobalSearchPageViewModel,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.userSearchResultComponentBehavior
    }

    val keyword by viewModel.keyword.collectAsState()

    val users = remember(keyword) {
        viewModel.searchUsers()
    }.collectAsLazyPagingItems()

    val loadState by viewModel.userListState.collectAsState()

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "user_search_result"
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            AmitySocialGlobalSearchPageViewModel.UserListState.from(
                loadState = users.loadState,
                itemCount = users.itemCount,
            ).let(viewModel::setUserListState)

            when (loadState) {
                AmitySocialGlobalSearchPageViewModel.UserListState.EMPTY -> {
                    item {
                        AmityEmptySearchResultComponent(modifier)
                    }
                }

                AmitySocialGlobalSearchPageViewModel.UserListState.LOADING -> {
                    item {
                        AmityUserListShimmer()
                    }
                }

                AmitySocialGlobalSearchPageViewModel.UserListState.SUCCESS -> {
                    items(
                        count = users.itemCount,
                        key = { index -> index }
                    ) { index ->
                        val user = users[index] ?: return@items

                        AmityUserView(
                            modifier = modifier,
                            user = user,
                        ) {
                            behavior.goToUserProfilePage(
                                context = context,
                                user = user,
                            )
                        }
                    }
                }
            }
        }
    }
}