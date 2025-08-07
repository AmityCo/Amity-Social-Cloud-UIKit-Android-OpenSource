package com.amity.socialcloud.uikit.community.compose.community.membership.invite

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmitySearchBarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.shade
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.membership.element.AmityCommunityInviteMemberItem
import com.amity.socialcloud.uikit.community.compose.community.membership.element.AmityCommunityInviteMemberRowList
import com.amity.socialcloud.uikit.community.compose.search.components.AmityEmptySearchResultComponent
import com.amity.socialcloud.uikit.community.compose.search.components.AmityEmptyUserListComponent
import com.amity.socialcloud.uikit.community.compose.search.components.AmitySearchPlaceholderComponent
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityUserListShimmer

@Composable
fun AmityCommunityInviteMemberPage(
    modifier: Modifier = Modifier,
    users: List<AmityUser> = emptyList(),
    onInvitedAction: (List<AmityUser>) -> Unit
) {
    val context = LocalContext.current

    val selectedUsers = remember(users.size) {
        mutableStateListOf(*users.toTypedArray())
    }

    var keyword by remember {
        mutableStateOf<String>("")
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommunityInviteMemberPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val loadState by viewModel.userListState.collectAsState()

    val lazyPagingItems = remember(keyword) {
        viewModel.searchUsers(keyword)
    }.collectAsLazyPagingItems()

    AmityBasePage(pageId = "community_invite_member_page") {
        Box(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Column(modifier.fillMaxSize()) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_close),
                        contentDescription = "Close",
                        modifier = modifier
                            .size(16.dp)
                            .align(Alignment.CenterStart)
                            .clickableWithoutRipple {
                                context.closePageWithResult(Activity.RESULT_CANCELED)
                            }
                    )

                    Text(
                        text = "Invite member",
                        style = AmityTheme.typography.titleLegacy,
                        modifier = modifier
                            .padding(vertical = 17.dp)
                            .align(Alignment.Center)
                    )
                }

                HorizontalDivider(
                    color = AmityTheme.colors.baseShade4,
                )
                Spacer(modifier.height(4.dp))

                AmitySearchBarView(hint = "Search user") {
                    keyword = it
                }

                if (selectedUsers.isNotEmpty()) {
                    Spacer(modifier.height(24.dp))

                    AmityCommunityInviteMemberRowList(
                        users = selectedUsers,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        selectedUsers.find {
                            it.getUserId() == it.getUserId()
                        }.let(selectedUsers::remove)
                    }

                    Spacer(modifier.height(24.dp))
                    HorizontalDivider(
                        color = AmityTheme.colors.baseShade4,
                    )
                    Spacer(modifier.height(16.dp))
                }

                LazyColumn {
                    AmityCommunityInviteMemberPageViewModel.UserListState.from(
                        loadState = lazyPagingItems.loadState.refresh,
                        itemCount = lazyPagingItems.itemCount,
                    ).let(viewModel::setUserListState)

                    when (loadState) {
                        AmityCommunityInviteMemberPageViewModel.UserListState.EMPTY -> {
                            item {
                                if (keyword.isNotBlank() && keyword.length < viewModel.minKeywordLength) {
                                    AmitySearchPlaceholderComponent(modifier)
                                } else {
                                    AmityEmptySearchResultComponent(modifier)
                                }
                            }
                        }

                        AmityCommunityInviteMemberPageViewModel.UserListState.LOADING -> {
                            item {
                                if (keyword.isNotBlank() && keyword.length < viewModel.minKeywordLength) {
                                    AmitySearchPlaceholderComponent(modifier)
                                } else {
                                    AmityUserListShimmer(
                                        modifier = modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 8.dp
                                        )
                                    )
                                }
                            }
                        }

                        AmityCommunityInviteMemberPageViewModel.UserListState.SUCCESS -> {
                            items(
                                count = lazyPagingItems.itemCount,
                                key = { index -> index }
                            ) { index ->

                                val user = lazyPagingItems[index] ?: return@items
                                viewModel.updateSelectedItemState(
                                    userId = user.getUserId(),
                                    isSelected = selectedUsers.find { it.getUserId() == user.getUserId() } != null,
                                )

                                AmityCommunityInviteMemberItem(
                                    viewModel = viewModel,
                                    user = user,
                                    onSelect = { userToAdd ->
                                        val isAdded = selectedUsers.find {
                                            it.getUserId() == user.getUserId()
                                        } != null

                                        if (!isAdded) {
                                            selectedUsers.add(userToAdd)
                                        }
                                    },
                                    onRemove = { userToRemove ->
                                        selectedUsers.find {
                                            it.getUserId() == userToRemove.getUserId()
                                        }.let(selectedUsers::remove)
                                    }
                                )
                            }
                            item {
                                Spacer(modifier.height(80.dp))
                            }
                        }

                        else -> {}
                    }
                }
            }

            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .background(AmityTheme.colors.background)
            ) {
                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                )

                Spacer(modifier = modifier.height(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmityTheme.colors.highlight,
                        disabledContainerColor = AmityTheme.colors.highlight.shade(AmityColorShade.SHADE2),
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    enabled = selectedUsers.isNotEmpty(),
                    modifier = modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = {
                        onInvitedAction(selectedUsers)
                    }
                ) {
                    Text(
                        text = "Invite",
                        style = AmityTheme.typography.captionLegacy.copy(
                            Color.White
                        ),
                    )
                }
                Spacer(modifier = modifier.height(16.dp))
            }
        }
    }
}