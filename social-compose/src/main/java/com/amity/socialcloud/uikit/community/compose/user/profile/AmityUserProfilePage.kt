package com.amity.socialcloud.uikit.community.compose.user.profile

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.follow.AmityFollowStatus
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.paging.feed.user.amityUserFeedLLS
import com.amity.socialcloud.uikit.community.compose.paging.feed.user.amityUserImageFeedLLS
import com.amity.socialcloud.uikit.community.compose.paging.feed.user.amityUserVideoFeedLLS
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileImageFeedItemPreviewDialog
import com.amity.socialcloud.uikit.community.compose.user.profile.components.AmityUserProfileHeaderComponent
import com.amity.socialcloud.uikit.community.compose.user.profile.elements.AmityUserActionsBottomSheet
import com.amity.socialcloud.uikit.community.compose.user.profile.elements.AmityUserMenuBottomSheet
import com.amity.socialcloud.uikit.community.compose.user.profile.elements.AmityUserProfileHeaderShimmer
import com.amity.socialcloud.uikit.community.compose.user.profile.elements.AmityUserProfileTabRow

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun AmityUserProfilePage(
    modifier: Modifier = Modifier,
    userId: String,
) {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    val viewModelKey = remember(userId) { "profile_$userId" }
    val viewModel = viewModel<AmityUserProfilePageViewModel>(
        key = viewModelKey,
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AmityUserProfilePageViewModel(userId) as T
            }
        }
    )

    LaunchedEffect(userId) {
        viewModel.refresh()
    }

    val state by viewModel.userProfileState.collectAsState()
    val user by remember(state) { derivedStateOf { state.user } }
    val isBlockedByMe by remember(state) { derivedStateOf { state.userFollowInfo?.getStatus() == AmityFollowStatus.BLOCKED } }

    var isHeaderSticky by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = {
            viewModel.refresh()
        }
    )
    var showAvatarPopupDialog by remember { mutableStateOf(false) }

    var showMenuSheet by remember { mutableStateOf(false) }
    var showUserActionSheet by remember { mutableStateOf(false) }
    var showBlockUserDialog by remember { mutableStateOf(false) }
    var showUnblockUserDialog by remember { mutableStateOf(false) }

    val userPosts = remember(userId, user?.getUpdatedAt()) {
        viewModel.getUserPosts()
    }.collectAsLazyPagingItems()
    val imagePosts = remember(userId) { viewModel.getUserImagePosts() }.collectAsLazyPagingItems()
    val videoPosts = remember(userId) { viewModel.getUserVideoPosts() }.collectAsLazyPagingItems()

    val postListState by viewModel.postListState.collectAsState()
    val imagePostListState by viewModel.imagePostListState.collectAsState()
    val videoPostListState by viewModel.videoPostListState.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(state.isRefreshing) {
        if (state.isRefreshing) {
            userPosts.refresh()
            imagePosts.refresh()
            videoPosts.refresh()
        }
    }

    AmityBasePage("user_profile_page") {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .pullRefresh(pullRefreshState),
        ) {
            LaunchedEffect(lazyListState) {
                snapshotFlow { lazyListState.firstVisibleItemIndex }
                    .collect { index ->
                        if (index > 2 && !isHeaderSticky) {
                            isHeaderSticky = true
                        } else if (index <= 2 && isHeaderSticky) {
                            isHeaderSticky = false
                        }
                    }
            }

            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize()
            ) {
                stickyHeader {
                    if (isHeaderSticky) {
                        AmityToolBar(
                            title = user?.getDisplayName() ?: "",
                            onBackClick = {
                                context.closePageWithResult(Activity.RESULT_CANCELED)
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.amity_ic_more_horiz),
                                contentDescription = "Close",
                                tint = AmityTheme.colors.base,
                                modifier = modifier
                                    .size(24.dp)
                                    .clickableWithoutRipple {
                                        showMenuSheet = true
                                    }
                            )
                        }

                        AmityUserProfileTabRow(
                            selectedIndex = selectedTabIndex,
                        ) {
                            selectedTabIndex = it
                        }
                    }
                }

                item {
                    AmityToolBar(
                        onBackClick = {
                            context.closePageWithResult(Activity.RESULT_CANCELED)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.amity_ic_more_horiz),
                            contentDescription = "Close",
                            tint = AmityTheme.colors.base,
                            modifier = modifier
                                .size(24.dp)
                                .clickableWithoutRipple {
                                    showMenuSheet = true
                                }
                        )
                    }
                }
                item {
                    if (user == null) {
                        AmityUserProfileHeaderShimmer()
                    } else {
                        AmityUserProfileHeaderComponent(
                            pageScope = getPageScope(),
                            user = user!!,
                        ) {
                            if (user?.getAvatar() != null) {
                                showAvatarPopupDialog = true
                            }
                        }
                    }
                }
                item {
                    AmityUserProfileTabRow(
                        selectedIndex = selectedTabIndex,
                    ) {
                        selectedTabIndex = it
                    }
                }

                when (selectedTabIndex) {
                    0 -> {
                        AmityUserProfilePageViewModel.PostListState.from(
                            loadState = userPosts.loadState.refresh,
                            itemCount = userPosts.itemCount
                        ).let(viewModel::setPostListState)

                        amityUserFeedLLS(
                            modifier = modifier,
                            context = context,
                            pageScope = getPageScope(),
                            userPosts = userPosts,
                            postListState = postListState,
                            isBlockedByMe = isBlockedByMe,
                        )
                    }

                    1 -> {
                        AmityUserProfilePageViewModel.PostListState.from(
                            loadState = imagePosts.loadState.refresh,
                            itemCount = imagePosts.itemCount
                        ).let(viewModel::setImagePostListState)

                        amityUserImageFeedLLS(
                            modifier = modifier,
                            pageScope = getPageScope(),
                            imagePosts = imagePosts,
                            postListState = imagePostListState,
                            isBlockedByMe = isBlockedByMe,
                        )
                    }

                    2 -> {
                        AmityUserProfilePageViewModel.PostListState.from(
                            loadState = videoPosts.loadState.refresh,
                            itemCount = videoPosts.itemCount
                        ).let(viewModel::setVideoPostListState)

                        amityUserVideoFeedLLS(
                            modifier = modifier,
                            pageScope = getPageScope(),
                            videoPosts = videoPosts,
                            postListState = videoPostListState,
                            isBlockedByMe = isBlockedByMe,
                        )
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )

            if (state.isMyUserProfile()) {
                FloatingActionButton(
                    onClick = {
                        showUserActionSheet = true
                    },
                    shape = RoundedCornerShape(size = 32.dp),
                    containerColor = AmityTheme.colors.primary,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(64.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_plus),
                        contentDescription = "create post",
                        tint = Color.White,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(4.dp)
                    )
                }
            }

            if (showAvatarPopupDialog) {
                AmityProfileImageFeedItemPreviewDialog(data = user?.getAvatar()) {
                    showAvatarPopupDialog = false
                }
            }

            if (showMenuSheet && user != null) {
                AmityUserMenuBottomSheet(
                    pageScope = getPageScope(),
                    user = user!!,
                    isBlockedByMe = isBlockedByMe,
                    onCloseSheet = {
                        showMenuSheet = false
                    },
                    onBlockUser = {
                        showBlockUserDialog = true
                    },
                    onUnblockUser = {
                        showUnblockUserDialog = true
                    }
                )
            }

            if (showUserActionSheet && user != null) {
                AmityUserActionsBottomSheet(
                    user = user!!,
                ) {
                    showUserActionSheet = false
                }
            }

            if (showBlockUserDialog && user != null) {
                AmityAlertDialog(
                    dialogTitle = "Block user?",
                    dialogText = buildAnnotatedString {
                        val displayName = user?.getDisplayName() ?: ""
                        append(displayName)
                        addStyle(
                            style = SpanStyle(AmityTheme.colors.base),
                            start = 0,
                            end = displayName.length,
                        )
                        append(" won't be able to see posts and comments that you've created. They won't be notified that you've blocked them.")
                    },
                    confirmText = "Block",
                    dismissText = "Cancel",
                    confirmTextColor = AmityTheme.colors.alert,
                    onConfirmation = {
                        showBlockUserDialog = false

                        viewModel.blockUser(
                            targetUserId = user!!.getUserId(),
                            onSuccess = {
                                getPageScope().showSnackbar(
                                    message = "User blocked.",
                                    drawableRes = R.drawable.amity_ic_snack_bar_success,
                                )
                            },
                            onError = {
                                getPageScope().showSnackbar(
                                    message = "Failed to block user. Please try again.",
                                    drawableRes = R.drawable.amity_ic_snack_bar_warning
                                )
                            }
                        )
                    },
                    onDismissRequest = {
                        showBlockUserDialog = false
                    }
                )
            }

            if (showUnblockUserDialog && user != null) {
                AmityAlertDialog(
                    dialogTitle = "Unblock user?",
                    dialogText = buildAnnotatedString {
                        val displayName = user?.getDisplayName() ?: ""
                        append(displayName)
                        addStyle(
                            style = SpanStyle(AmityTheme.colors.base),
                            start = 0,
                            end = displayName.length,
                        )
                        append(" will now be able to see posts and comments that you've created. They won't be notified that you've unblocked them.")
                    },
                    confirmText = "Unblock",
                    dismissText = "Cancel",
                    confirmTextColor = AmityTheme.colors.alert,
                    onConfirmation = {
                        showUnblockUserDialog = false

                        viewModel.unblockUser(
                            targetUserId = user!!.getUserId(),
                            onSuccess = {
                                getPageScope().showSnackbar(
                                    message = "User unblocked.",
                                    drawableRes = R.drawable.amity_ic_snack_bar_success,
                                )
                            },
                            onError = {
                                getPageScope().showSnackbar(
                                    message = "Failed to unblock user. Please try again.",
                                    drawableRes = R.drawable.amity_ic_snack_bar_warning
                                )
                            }
                        )
                    },
                    onDismissRequest = {
                        showUnblockUserDialog = false
                    }
                )
            }
        }
    }
}