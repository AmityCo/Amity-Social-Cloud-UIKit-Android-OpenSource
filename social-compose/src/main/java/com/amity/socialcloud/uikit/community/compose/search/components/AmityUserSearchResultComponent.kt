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
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserListItem
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.search.global.AmityGlobalSearchViewModel
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityUserListShimmer

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

    val keyword by viewModel.keyword.collectAsState("")

    val users = remember(keyword) {
        viewModel.searchUsers()
    }.collectAsLazyPagingItems()

    val loadState by viewModel.userListState.collectAsState()

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "user_search_result"
    ) {
        if (keyword.isNotEmptyOrBlank()) {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                AmityGlobalSearchViewModel.UserListState.from(
                    loadState = users.loadState.refresh,
                    itemCount = users.itemCount,
                    keyword = keyword
                ).let(viewModel::setUserListState)

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