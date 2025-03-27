package com.amity.socialcloud.uikit.community.compose.user.relationship.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserListItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityUserListShimmer
import com.amity.socialcloud.uikit.community.compose.user.relationship.AmityUserRelationshipPageViewModel

@Composable
fun AmityUserFollowingListComponent(
    modifier: Modifier = Modifier,
    viewModel: AmityUserRelationshipPageViewModel,
    onUserAction: (AmityUser) -> Unit,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.userRelationshipPageBehavior
    }

    val loadState by viewModel.followingListState.collectAsState()
    val lazyPagingItems = remember {
        viewModel.getUserFollowings()
    }.collectAsLazyPagingItems()

    LazyColumn(modifier.fillMaxSize()) {
        AmityUserRelationshipPageViewModel.FollowListState.from(
            loadState = lazyPagingItems.loadState.refresh,
            itemCount = lazyPagingItems.itemCount,
        ).let(viewModel::setFollowingListState)

        when (loadState) {
            AmityUserRelationshipPageViewModel.FollowListState.EMPTY -> {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(600.dp)
                            .padding(32.dp),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.amity_ic_empty_list),
                            contentDescription = null,
                            modifier = modifier.size(60.dp)
                        )
                        Spacer(modifier = modifier.size(8.dp))
                        Text(
                            "Nothing here to see yet",
                            style = AmityTheme.typography.titleLegacy.copy(
                                color = AmityTheme.colors.baseShade3
                            )
                        )
                    }
                }
            }

            AmityUserRelationshipPageViewModel.FollowListState.LOADING -> {
                item {
                    AmityUserListShimmer(
                        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            AmityUserRelationshipPageViewModel.FollowListState.SUCCESS -> {
                items(
                    count = lazyPagingItems.itemCount,
                    key = { index -> lazyPagingItems[index]?.getTargetUser()?.getUserId() ?: index }
                ) { index ->
                    val following = lazyPagingItems[index]?.getTargetUser() ?: return@items

                    AmityUserListItem(
                        user = following,
                        showRightMenu = following.getUserId() != AmityCoreClient.getUserId(),
                        onUserClick = {
                            behavior.goToUserProfilePage(
                                context = context,
                                userId = it.getUserId(),
                            )
                        },
                        onRightMenuClick = onUserAction,
                    )
                }
            }
        }
    }
}