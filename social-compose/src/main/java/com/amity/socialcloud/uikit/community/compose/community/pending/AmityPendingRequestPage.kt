package com.amity.socialcloud.uikit.community.compose.community.pending

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityPostSettings
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequest
import com.amity.socialcloud.sdk.model.social.community.AmityJoinRequestStatus
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityNumberUtil.getNumberAbbreveation
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.community.compose.community.pending.elements.AmityEmptyPendingPostsElement
import com.amity.socialcloud.uikit.community.compose.community.pending.elements.AmityJoinRequestShimmer
import com.amity.socialcloud.uikit.community.compose.community.pending.elements.AmityPendingJoinRequestComponent
import com.amity.socialcloud.uikit.community.compose.community.pending.elements.AmityPendingPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostShimmer
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider

@Composable
fun AmityPendingRequestPage(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
) {
    val context = LocalContext.current

    val viewModel = remember(community.getCommunityId()) {
        AmityPendingRequestPageViewModel(community.getCommunityId())
    }

    val isModerator by AmityCoreClient.hasPermission(AmityPermission.EDIT_COMMUNITY)
        .atCommunity(community.getCommunityId())
        .check()
        .asFlow()
        .collectAsState(initial = false)

    val pendingPosts = remember(community.getCommunityId()) {
        viewModel.getPendingPosts()
    }.collectAsLazyPagingItems()

    val postListState by viewModel.postListState.collectAsState()

    val joinRequest = remember {
        viewModel.getJoinRequests(AmityJoinRequestStatus.PENDING, community)
    }.collectAsLazyPagingItems()

    val isRequireJoinApproval = community.requiresJoinApproval()
    val isRequirePostApproval =
        community.getPostSettings() == AmityCommunityPostSettings.ADMIN_REVIEW_POST_REQUIRED

    // Track if initial selection has been made
    val hasInitialSelectionBeenMade = remember { mutableStateOf(false) }

    // Default initial tab selection
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Set up tabs based on community settings and item counts
    // Use a typed pair (type, label) so tab routing is locale-independent
    val tabs = remember(
        isRequireJoinApproval,
        isRequirePostApproval,
        pendingPosts.itemCount,
        joinRequest.itemCount
    ) {
        val tabList = mutableListOf<Pair<PendingTabType, String>>()
        if (isRequirePostApproval) {
            tabList.add(PendingTabType.POSTS to DefaultAmitySocialStringProvider.getInstance().getString("amity_social_tab_tab_posts_count").format(if (pendingPosts.itemCount > 10) "10+" else pendingPosts.itemCount.toString()))
        }
        if (isRequireJoinApproval) {
            tabList.add(PendingTabType.JOIN_REQUESTS to DefaultAmitySocialStringProvider.getInstance().getString("amity_social_tab_tab_join_requests_count").format(if (joinRequest.itemCount > 10) "10+" else joinRequest.itemCount.toString()))
        }
        tabList
    }

    // Check when data has loaded
    LaunchedEffect(
        pendingPosts.loadState.refresh,
        joinRequest.loadState.refresh,
        isRequirePostApproval,
        isRequireJoinApproval,
        pendingPosts.itemCount,
        joinRequest.itemCount
    ) {
        // Only update tab selection once after data has loaded
        if (!hasInitialSelectionBeenMade.value &&
            (pendingPosts.loadState.refresh is LoadState.NotLoading || !isRequirePostApproval) &&
            (joinRequest.loadState.refresh is LoadState.NotLoading || !isRequireJoinApproval)
        ) {
            // Now we have the actual counts, set the initial tab
            val newIndex = when {
                // If only join requests tab is available
                !isRequirePostApproval && isRequireJoinApproval -> 0

                // If only pending posts tab is available
                isRequirePostApproval && !isRequireJoinApproval -> 0

                // If both tabs are available, prioritize the one with items
                isRequirePostApproval && isRequireJoinApproval -> {
                    if (pendingPosts.itemCount > 0 && joinRequest.itemCount == 0) 0
                    else if (pendingPosts.itemCount == 0 && joinRequest.itemCount > 0) 1
                    else 0 // Default to first tab if both have items or both are empty
                }

                // Fallback
                else -> 0
            }

            selectedTabIndex = newIndex
            hasInitialSelectionBeenMade.value = true
        }
    }

    AmityBasePage(pageId = "pending_request_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            AmityToolBar(
                title = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_pending_requests"),
                onBackClick = {
                    context.closePage()
                }
            )

            AmityCustomTabRow(
                selectedTabIndex = selectedTabIndex,
                tabs = tabs.map { it.second },
                onTabSelected = { index ->
                    selectedTabIndex = index
                },
            )

            if (tabs.isNotEmpty()) {
                when (tabs.getOrNull(selectedTabIndex)?.first) {
                    PendingTabType.POSTS -> {
                        PendingPostsListContent(
                            isModerator = isModerator,
                            pendingPosts = pendingPosts,
                            requestListState = postListState,
                            viewModel = viewModel,
                            pageScope = getPageScope()
                        )
                    }
                    PendingTabType.JOIN_REQUESTS -> {
                        PendingJoinRequestsContent(
                            isModerator = isModerator,
                            joinRequests = joinRequest,
                            requestListState = postListState,
                            viewModel = viewModel,
                            pageScope = getPageScope()
                        )
                    }
                    null -> {}
                }
            }
        }
    }
}

@Composable
fun PendingPostsListContent(
    isModerator: Boolean,
    pendingPosts: LazyPagingItems<AmityPost>,
    requestListState: AmityPendingRequestPageViewModel.RequestListState,
    viewModel: AmityPendingRequestPageViewModel,
    pageScope: AmityComposePageScope,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (isModerator && (requestListState == AmityPendingRequestPageViewModel.RequestListState.SUCCESS || requestListState == AmityPendingRequestPageViewModel.RequestListState.EMPTY)) {
            Text(
                text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_decline_pending_post_will_permanently_delete_the_select"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    color = AmityTheme.colors.baseShade1
                ),
                modifier = Modifier
                    .background(color = AmityTheme.colors.baseShade4)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }

        LazyColumn {
            AmityPendingRequestPageViewModel.RequestListState.from(
                loadState = pendingPosts.loadState.refresh,
                itemCount = pendingPosts.itemCount,
            ).let(viewModel::setRequestListState)

            when (requestListState) {
                AmityPendingRequestPageViewModel.RequestListState.ERROR,
                AmityPendingRequestPageViewModel.RequestListState.EMPTY,
                    -> {
                    item {
                        AmityEmptyPendingPostsElement(
                            modifier = Modifier.fillParentMaxSize(),
                        )
                    }
                }

                AmityPendingRequestPageViewModel.RequestListState.LOADING -> {
                    items(2) {
                        AmityPostShimmer()
                        AmityNewsFeedDivider()
                    }
                }

                AmityPendingRequestPageViewModel.RequestListState.SUCCESS -> {
                    items(
                        count = pendingPosts.itemCount,
                        key = { pendingPosts[it]?.getPostId() ?: it }
                    ) {
                        val post = pendingPosts[it] ?: return@items

                        AmityPendingPostContentComponent(
                            pageScope = pageScope,
                            post = post,
                            componentId = "pending_post_list",
                            onAcceptAction = {
                                viewModel.approvePost(
                                    postId = post.getPostId(),
                                    onApproved = {
                                        pageScope.showSnackbar(
                                            message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_post_accepted"),
                                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                                        )
                                    },
                                    onAlreadyApproved = {
                                        pageScope.showSnackbar(
                                            message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_post_already_reviewed"),
                                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                                        )
                                    },
                                    onError = {
                                        pageScope.showSnackbar(
                                            message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_review_post_failed"),
                                            drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                        )
                                    }
                                )
                            },
                            onDeclineAction = {
                                viewModel.declinePost(
                                    postId = post.getPostId(),
                                    onApproved = {
                                        pageScope.showSnackbar(
                                            message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_post_declined"),
                                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                                        )
                                    },
                                    onAlreadyDeclined = {
                                        pageScope.showSnackbar(
                                            message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_post_already_reviewed"),
                                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                                        )
                                    },
                                    onError = {
                                        pageScope.showSnackbar(
                                            message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_review_post_failed"),
                                            drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                        )
                                    }
                                )
                            }
                        )
                        if (pendingPosts.itemCount > 1 && it < pendingPosts.itemCount - 1) {
                            AmityNewsFeedDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PendingJoinRequestsContent(
    isModerator: Boolean,
    joinRequests: LazyPagingItems<AmityJoinRequest>,
    requestListState: AmityPendingRequestPageViewModel.RequestListState,
    viewModel: AmityPendingRequestPageViewModel,
    pageScope: AmityComposePageScope,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AmityTheme.colors.background)
    ) {

        if (isModerator && (requestListState == AmityPendingRequestPageViewModel.RequestListState.SUCCESS || requestListState == AmityPendingRequestPageViewModel.RequestListState.EMPTY)) {
            Text(
                text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_decline_follow_request_message"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    color = AmityTheme.colors.baseShade1
                ),
                modifier = Modifier
                    .background(color = AmityTheme.colors.baseShade4)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }

        LazyColumn {
            AmityPendingRequestPageViewModel.RequestListState.from(
                loadState = joinRequests.loadState.refresh,
                itemCount = joinRequests.itemCount,
            ).let(viewModel::setRequestListState)

            when (requestListState) {
                AmityPendingRequestPageViewModel.RequestListState.ERROR,
                AmityPendingRequestPageViewModel.RequestListState.EMPTY,
                    -> {
                    item {
                        AmityEmptyPendingPostsElement(
                            modifier = Modifier.fillParentMaxSize(),
                            title = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_no_pending_requests")
                        )
                    }
                }

                AmityPendingRequestPageViewModel.RequestListState.LOADING -> {
                    items(3) {
                        AmityJoinRequestShimmer()
                    }
                }

                AmityPendingRequestPageViewModel.RequestListState.SUCCESS -> {
                    items(
                        count = joinRequests.itemCount,
                        key = { joinRequests[it]?.getId() ?: it }
                    ) {
                        val joinRequest = joinRequests[it] ?: return@items
                        AmityPendingJoinRequestComponent(
                            joinRequest = joinRequest,
                            pageScope = pageScope,
                            onAcceptAction = {
                                viewModel.approveJoinRequest(
                                    joinRequest = joinRequest,
                                    onApproved = {
                                        pageScope.showSnackbar(
                                            message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_join_request_accepted"),
                                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                                        )
                                    },
                                    onAlreadyApproved = {
                                        pageScope.showSnackbar(
                                            message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_join_request_unavailable"),
                                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                                        )
                                    },
                                    onError = {
                                        pageScope.showSnackbar(
                                            message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_accept_join_request_failed"),
                                            drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                        )
                                    }
                                )
                            },
                            onDeclineAction = {
                                viewModel.declineJoinRequest(
                                    joinRequest = joinRequest,
                                    onDeclined = {
                                        pageScope.showSnackbar(
                                            message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_join_request_declined"),
                                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                                        )
                                    },
                                    onAlreadyDeclined = {
                                        pageScope.showSnackbar(
                                            message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_join_request_unavailable"),
                                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                                        )
                                    },
                                    onError = {
                                        pageScope.showSnackbar(
                                            message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_decline_join_request_failed"),
                                            drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                        )
                                    }
                                )
                            }
                        )
                        if (joinRequests.itemCount > 1 && it < joinRequests.itemCount - 1) {
                            AmityNewsFeedDivider()
                        }
                    }
                }
            }
        }

    }

}

@Composable
private fun AmityCustomTabRow(
    selectedTabIndex: Int,
    tabs: List<String>,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val tabTextWidths =
        remember { mutableStateListOf<Dp>().apply { repeat(tabs.size) { add(0.dp) } } }
    val tabContainerWidths = // Width of the actual Column element for each tab
        remember { mutableStateListOf<Dp>().apply { repeat(tabs.size) { add(0.dp) } } }

    // Define padding values
    val overallRowHorizontalPadding = 16.dp
    val textInternalHorizontalPadding = 0.dp // Padding inside each tab, around the text
    val spaceBetweenTabItems = 20.dp      // Space between individual tab items

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = overallRowHorizontalPadding), // Overall padding for the tab row
            horizontalArrangement = Arrangement.Start // Align tabs to the start
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .then(if (index > 0) Modifier.padding(start = spaceBetweenTabItems) else Modifier)
                        .onGloballyPositioned { coordinates ->
                            tabContainerWidths[index] =
                                with(density) { coordinates.size.width.toDp() }
                        }
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onTabSelected(index)
                        }
                        .padding(vertical = 12.dp) // Vertical padding for the clickable area
                ) {
                    Text(
                        text = title,
                        style = AmityTheme.typography.titleBold,
                        color = if (isSelected) AmityTheme.colors.primary else AmityTheme.colors.baseShade2,
                        modifier = Modifier
                            .padding(horizontal = textInternalHorizontalPadding) // Horizontal padding for the text itself
                            .onGloballyPositioned { coordinates ->
                                tabTextWidths[index] =
                                    with(density) { coordinates.size.width.toDp() }
                            }
                    )
                }
            }
        }

        // Indicator
        if (selectedTabIndex < tabs.size &&
            tabTextWidths.getOrNull(selectedTabIndex)?.let { it > 0.dp } == true &&
            tabContainerWidths.getOrNull(selectedTabIndex)?.let { it > 0.dp } == true
        ) {
            val indicatorWidth = tabTextWidths[selectedTabIndex]

            var indicatorOffset =
                overallRowHorizontalPadding // Start with the Row's leading padding

            for (i in 0 until selectedTabIndex) {
                indicatorOffset += tabContainerWidths[i] // Add the width of the tab item
                indicatorOffset += spaceBetweenTabItems  // Add the space after this tab item
            }
            // Adjust offset by the internal padding of the text to align with the text itself
            indicatorOffset += textInternalHorizontalPadding

            Box(
                modifier = Modifier
                    .padding(start = indicatorOffset)
                    .width(indicatorWidth)
                    .height(2.dp)
                    .background(AmityTheme.colors.primary)
            )
        }
        HorizontalDivider(color = AmityTheme.colors.baseShade4)
    }
}

internal enum class PendingTabType {
    POSTS,
    JOIN_REQUESTS,
}

@Preview
@Composable
fun AmityPendingPostsPagePreview() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Pending Posts (5)", "Join Requests (0)")

    AmityCustomTabRow(
        selectedTabIndex = selectedTabIndex,
        tabs = tabs,
        onTabSelected = {
            selectedTabIndex = it
        },
        modifier = Modifier
            .background(AmityTheme.colors.background)
    )
}