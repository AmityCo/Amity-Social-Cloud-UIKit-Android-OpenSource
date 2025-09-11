package com.amity.socialcloud.uikit.community.compose.user.profile

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.amity.socialcloud.sdk.api.social.post.query.AmityFeedSource
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.follow.AmityFollowStatus
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageType
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityVideoAndClipChipSelector
import com.amity.socialcloud.uikit.community.compose.paging.feed.user.amityUserClipFeedLLS
import com.amity.socialcloud.uikit.community.compose.paging.feed.user.amityUserFeedLLS
import com.amity.socialcloud.uikit.community.compose.paging.feed.user.amityUserImageFeedLLS
import com.amity.socialcloud.uikit.community.compose.paging.feed.user.amityUserVideoFeedLLS
import com.amity.socialcloud.uikit.community.compose.post.composer.poll.AmityPollPostTypeSelectionBottomSheet
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileImageFeedItemPreviewDialog
import com.amity.socialcloud.uikit.community.compose.user.profile.components.AmityUserProfileHeaderComponent
import com.amity.socialcloud.uikit.community.compose.user.profile.elements.AmityFeedFilterBottomSheet
import com.amity.socialcloud.uikit.community.compose.user.profile.elements.AmityUserActionsBottomSheet
import com.amity.socialcloud.uikit.community.compose.user.profile.elements.AmityUserMenuBottomSheet
import com.amity.socialcloud.uikit.community.compose.user.profile.elements.AmityUserProfileHeaderShimmer
import com.amity.socialcloud.uikit.community.compose.user.profile.elements.AmityUserProfileTabRow
import com.amity.socialcloud.uikit.community.compose.user.profile.elements.AmityUserUnfollowBottomSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun AmityUserProfilePage(
    modifier: Modifier = Modifier,
    userId: String,
    content: @Composable () -> Unit = {},
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
    val coroutineScope = rememberCoroutineScope()

    val behavior by lazy {
        AmitySocialBehaviorHelper.userProfilePageBehavior
    }

    var showPollSelectionBottomSheet by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        context.closePageWithResult(Activity.RESULT_OK)
    }

    LaunchedEffect(userId) {
        viewModel.refresh()
    }

    val state by viewModel.userProfileState.collectAsState()
    val user by remember(state) { derivedStateOf { state.user } }
    val isBlockedByMe by remember(state) { derivedStateOf { state.userFollowInfo?.getStatus() == AmityFollowStatus.BLOCKED } }
    val isFollowedByMe by remember(state) { derivedStateOf { state.userFollowInfo?.getStatus() == AmityFollowStatus.ACCEPTED } }
    var targetUser by remember(state) { mutableStateOf<AmityUser?>(null) }

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
    var showFeedFilterSheet by remember { mutableStateOf(false) }
    var selectedFilterIndex by remember { mutableIntStateOf(0) }
    var showUnfollowPopupDialog by remember { mutableStateOf(false) }
    var showUnfollowErrorDialog by remember { mutableStateOf(false) }
    var showUnfollowSheet by remember { mutableStateOf(false) }

    val userPosts = remember(userId, user?.getUpdatedAt(), selectedFilterIndex) {
        viewModel.getUserPosts()
    }.collectAsLazyPagingItems()
    val imagePosts = remember(
        userId,
        selectedFilterIndex
    ) { viewModel.getUserImagePosts() }.collectAsLazyPagingItems()
    val videoPosts = remember(
        userId,
        selectedFilterIndex
    ) { viewModel.getUserVideoPosts() }.collectAsLazyPagingItems()
    val clipPosts = remember(
        userId,
        selectedFilterIndex
    ) { viewModel.getUserClipPosts() }.collectAsLazyPagingItems()

    val postListState by viewModel.postListState.collectAsState()
    val imagePostListState by viewModel.imagePostListState.collectAsState()
    val videoPostListState by viewModel.videoPostListState.collectAsState()
    val clipPostListState by viewModel.clipPostListState.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val feedFilter = listOf(
        context.getString(R.string.amity_v4_user_profile_feed_option_all),
        context.getString(R.string.amity_v4_user_profile_feed_option_community),
        context.getString(R.string.amity_v4_user_profile_feed_option_user),
    )

    val error by remember(userId) {
        viewModel.getFetchErrorState()
    }.collectAsState()

    LaunchedEffect(userId) {
        viewModel.refresh()
    }

    LaunchedEffect(state.isRefreshing) {
        if (state.isRefreshing) {
            userPosts.refresh()
            imagePosts.refresh()
            videoPosts.refresh()
        }
    }

    var selectedVideoClipsTabIndex by remember { mutableIntStateOf(0) }
    val videoClipTabsTitles = listOf("Videos", "Clips")

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
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 0.dp
                )
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
                            onSelect = {
                                selectedTabIndex = it
                            },
                            currentFilter = feedFilter[selectedFilterIndex],
                            onFilterLaunch = {
                                showFeedFilterSheet = true
                            }
                        )
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
                            onAvatarClick = {
                                if (user?.getAvatar() != null) {
                                    showAvatarPopupDialog = true
                                }
                            },
                            onMenuClick = {
                                showMenuSheet = true
                            }
                        )
                    }
                }
                item {
                    AmityUserProfileTabRow(
                        selectedIndex = selectedTabIndex,
                        onSelect = { it ->
                            selectedTabIndex = it
                        },
                        currentFilter = feedFilter[selectedFilterIndex],
                        onFilterLaunch = {
                            showFeedFilterSheet = true
                        }
                    )
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
                            onClipClick = {
                                behavior.goToClipFeedPage(
                                    context = context,
                                    postId = it.getPostId(),
                                    type = AmityClipFeedPageType.UserNewsFeed(it.getPostId())
                                )
                            },
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
                            onPostClick = { postId ->
                                behavior.goToPostDetailPage(
                                    context = context,
                                    postId = postId,
                                )
                            }
                        )
                    }

                    2 -> {
                        item {
                            AmityVideoAndClipChipSelector(
                                tabTitles = videoClipTabsTitles,
                                selectedTabIndex = selectedVideoClipsTabIndex,
                                onTabSelected = { index ->
                                    selectedVideoClipsTabIndex = index
                                },
                            )
                        }

                        when (selectedVideoClipsTabIndex) {
                            0 -> {
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

                            1 -> {
                                AmityUserProfilePageViewModel.PostListState.from(
                                    loadState = clipPosts.loadState.refresh,
                                    itemCount = clipPosts.itemCount
                                ).let(viewModel::setClipPostListState)

                                amityUserClipFeedLLS(
                                    modifier = modifier,
                                    pageScope = getPageScope(),
                                    clipPosts = clipPosts,
                                    postListState = clipPostListState,
                                    isBlockedByMe = isBlockedByMe,
                                    // In AmityUserProfilePage onClipClick
                                    onClipClick = { postId ->
                                        coroutineScope.launch {
                                            val selectedIndex =
                                                clipPosts.itemSnapshotList.items.indexOfFirst {
                                                    it.getPostId() == postId
                                                }

                                            if (selectedIndex >= clipPosts.itemCount && selectedIndex >= 0) {
                                                // Wait for more data to load
                                                val targetPage = selectedIndex / 20
                                                val currentPage = clipPosts.itemCount / 20

                                                if (targetPage > currentPage) {
                                                    // Show loading indicator
                                                    // Access items to trigger loading
                                                    repeat(targetPage - currentPage) {
                                                        val triggerIndex = clipPosts.itemCount
                                                        if (triggerIndex < selectedIndex) {
                                                            clipPosts[triggerIndex] // Trigger next page load
                                                            delay(500) // Wait for load
                                                        }
                                                    }
                                                }
                                            }

                                            behavior.goToClipFeedPage(
                                                context = context,
                                                postId = postId,
                                                userId = userId,
                                                clipPagingData = viewModel.getUserClipPosts(),
                                                selectedIndex = maxOf(
                                                    0,
                                                    minOf(selectedIndex, clipPosts.itemCount - 1)
                                                ),
                                                type = AmityClipFeedPageType.UserClipFeed(userId)
                                            )
                                        }
                                    }
                                )
                            }
                        }

                    }
                }
            }

            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = androidx.compose.ui.graphics.Color(0xFFF26B1C),
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
                    isFollowedByMe = isFollowedByMe,
                    onCloseSheet = {
                        showMenuSheet = false
                    },
                    onBlockUser = {
                        showBlockUserDialog = true
                    },
                    onUnblockUser = {
                        showUnblockUserDialog = true
                    },
                    onUnfollow = {
                        targetUser = it
                        showUnfollowPopupDialog = true
                    }
                )
            }

            if (showUserActionSheet && user != null) {
                AmityUserActionsBottomSheet(
                    user = user!!,
                    showPollTypeSelectionSheet = {
                        showPollSelectionBottomSheet = true
                    }
                ) {
                    showUserActionSheet = false
                }
            }

            if (showBlockUserDialog && user != null) {
                AmityAlertDialog(
                    dialogTitle = "Block member?",
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
                                    message = "User blocked",
                                    drawableRes = R.drawable.amity_ic_snack_bar_success,
                                )
                            },
                            onError = {
                                getPageScope().showErrorSnackbar(
                                    message = "Block not successful. Please try again.",
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
                    dialogTitle = "Unblock member?",
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
                                getPageScope().showErrorSnackbar(
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

            if (showPollSelectionBottomSheet) {
                user?.let {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showPollSelectionBottomSheet = false
                        },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                        containerColor = AmityTheme.colors.background,
                        contentWindowInsets = { WindowInsets.waterfall },
                        modifier = Modifier
                            .navigationBarsPadding()
                            .statusBarsPadding()
                    ) {
                        AmityPollPostTypeSelectionBottomSheet(
                            onCloseSheet = {
                                showPollSelectionBottomSheet = false
                            },
                            onNextClicked = { selectedType ->
                                behavior.goToPollComposerPage(
                                    context = context,
                                    userId = it.getUserId(),
                                    pollType = selectedType
                                )
                            }
                        )
                    }
                }
            }

            if (showFeedFilterSheet) {
                AmityFeedFilterBottomSheet(
                    feedFilters = feedFilter,
                    selectedFilterIndex = selectedFilterIndex,
                    onFilterSelected = { newIndex ->
                        if (newIndex == selectedFilterIndex) return@AmityFeedFilterBottomSheet
                        val filter = when (newIndex) {
                            0 -> listOf(AmityFeedSource.USER, AmityFeedSource.COMMUNITY)
                            1 -> listOf(AmityFeedSource.COMMUNITY)
                            2 -> listOf(AmityFeedSource.USER)
                            else -> listOf(AmityFeedSource.USER, AmityFeedSource.COMMUNITY)
                        }
                        viewModel.filter = filter
                        selectedFilterIndex = newIndex
                    },
                    onCloseSheet = {
                        showFeedFilterSheet = false
                    }
                )
            }


            if (showUnfollowSheet) {
                AmityUserUnfollowBottomSheet(
                    user = targetUser,
                    onDismiss = {
                        showUnfollowSheet = false
                    },
                    onUnfollow = {
                        showUnfollowPopupDialog = true
                    }
                )
            }

            if (showUnfollowPopupDialog) {
                AmityAlertDialog(
                    dialogTitle = "Unfollow this user?",
                    dialogText = "If you change your mind, you'll have to request to follow them again.",
                    confirmText = "Unfollow",
                    dismissText = "Cancel",
                    confirmTextColor = AmityTheme.colors.alert,
                    onConfirmation = {
                        showUnfollowPopupDialog = false
                        user?.getUserId()?.let {
                            viewModel.unfollowUser(
                                targetUserId = it,
                                onError = {
                                    showUnfollowErrorDialog = true
                                }
                            )
                        }
                    },
                    onDismissRequest = {
                        showUnfollowPopupDialog = false
                    }
                )
            }
        }

        if (AmityError.from(error) == AmityError.ITEM_NOT_FOUND || user?.isDeleted() == true) {
            val title = if (user?.isDeleted() == true) {
                context.getString(R.string.amity_invalid_user_dialog_title)
            } else {
                context.getString(R.string.amity_deleted_user_dialog_title)
            }
            AmityAlertDialog(
                dialogTitle = title,
                dialogText = "",
                dismissText = context.getString(R.string.amity_ok),
                onDismissRequest = {
                    context.closePage()
                }
            )
        }

        if (showUnfollowErrorDialog) {
            AmityAlertDialog(
                dialogTitle = "Unable to unfollow this user",
                dialogText = "Oops! something went wrong. Please try again later.",
                dismissText = "OK",
                onDismissRequest = {
                    showUnfollowErrorDialog = false
                }
            )
        }
    }
}