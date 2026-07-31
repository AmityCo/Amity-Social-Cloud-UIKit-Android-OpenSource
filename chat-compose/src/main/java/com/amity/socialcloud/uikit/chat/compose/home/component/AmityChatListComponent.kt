package com.amity.socialcloud.uikit.chat.compose.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.uikit.chat.compose.home.AmityChatHomePageViewModel
import com.amity.socialcloud.uikit.chat.compose.home.element.AmityChatListEmptyState
import com.amity.socialcloud.uikit.chat.compose.home.element.AmityChatListItem
import com.amity.socialcloud.uikit.chat.compose.home.element.AmityChatListSkeleton
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBanner
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBannerHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonColor
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken

enum class SwipeAction {
    ARCHIVE,
    UNARCHIVE,
}

@Composable
fun AmityChatListComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentId: String,
    channelsFlow: Flow<androidx.paging.PagingData<AmityChannel>>,
    otherMembersMap: Map<String, AmityChannelMember?> = emptyMap(),
    onFetchOtherMember: (String) -> Unit = {},
    onChannelClick: (AmityChannel) -> Unit = {},
    swipeAction: SwipeAction? = null,
    onSwipeAction: (AmityChannel) -> Unit = {},
    onCreateChatClick: () -> Unit = {},
    isLoadingOverride: StateFlow<Boolean>? = null,
    chatNotificationsEnabled: StateFlow<Boolean>? = null,
    emptyContent: (@Composable () -> Unit)? = null,
) {
    AmityBaseComponent(
        modifier = modifier,
        pageScope = pageScope,
        componentId = componentId,
    ) {
        val channels = channelsFlow.collectAsLazyPagingItems()
        val lazyListState = rememberLazyListState()

        // True while the mediator's HTTP fetch is in-flight, or null before it has reported at all
        // (e.g. fresh install). // The paging source can briefly read itemCount==0 mid-refresh
        // (mediator clears the join table before inserting), so treat both as Loading, not Empty.
        val mediatorRefreshing = channels.loadState.mediator?.refresh.let {
            it == null || it is LoadState.Loading
        }

        // isLoadingOverride: used by pages (e.g. archive) whose flow has no RemoteMediator
        // but still needs to show the skeleton while an initial server fetch is in-flight.
        // Use a stable remembered default so we don't recreate a new StateFlow each recomposition.
        val stableDefaultLoading = remember { MutableStateFlow(false) }
        val externalLoading by (isLoadingOverride ?: stableDefaultLoading).collectAsState()

        val rawListState = when {
            externalLoading -> AmityChatHomePageViewModel.ChannelListState.LOADING
            // isLoadingOverride was provided (non-null) and has completed (externalLoading=false):
            // trust itemCount directly — don't rely on loadState.refresh which stays Loading
            // for PagingData.empty() replayed through cachedIn.
            isLoadingOverride != null && channels.itemCount == 0 ->
                AmityChatHomePageViewModel.ChannelListState.EMPTY
            isLoadingOverride != null && channels.itemCount > 0 ->
                AmityChatHomePageViewModel.ChannelListState.SUCCESS
            mediatorRefreshing && channels.itemCount == 0 ->
                AmityChatHomePageViewModel.ChannelListState.LOADING
            else -> AmityChatHomePageViewModel.ChannelListState.from(
                loadState = channels.loadState.refresh,
                itemCount = channels.itemCount,
            )
        }

        // Debounce EMPTY/LOADING to avoid flashing states during background refreshes.
        var hasEverHadData by remember { mutableStateOf(false) }
        var debouncedState by remember {
            mutableStateOf<AmityChatHomePageViewModel.ChannelListState>(
                AmityChatHomePageViewModel.ChannelListState.LOADING
            )
        }

        if (rawListState == AmityChatHomePageViewModel.ChannelListState.SUCCESS) {
            hasEverHadData = true
        }

        LaunchedEffect(rawListState) {
            when {
                rawListState == AmityChatHomePageViewModel.ChannelListState.EMPTY -> {
                    // After we've had data: give a 5s window for the SDK to repopulate
                    // from the server before committing the empty state.
                    if (hasEverHadData) delay(5_000L)
                    debouncedState = rawListState
                }
                hasEverHadData && rawListState == AmityChatHomePageViewModel.ChannelListState.LOADING -> {
                    // Already have cached data — keep showing the list while the network refreshes.
                    // debouncedState stays SUCCESS; the LazyColumn updates automatically via Room.
                }
                else -> {
                    debouncedState = rawListState
                }
            }
        }

        val listState = debouncedState

        LaunchedEffect(listState) {
            if (listState == AmityChatHomePageViewModel.ChannelListState.SUCCESS) {
                lazyListState.scrollToItem(0)
            }
        }

        val firstChannelId = if (channels.itemCount > 0) channels.peek(0)?.getChannelId() else null
        LaunchedEffect(firstChannelId) {
            if (firstChannelId != null && lazyListState.firstVisibleItemIndex <= 1) {
                lazyListState.animateScrollToItem(0)
            }
        }

        when (listState) {
            AmityChatHomePageViewModel.ChannelListState.LOADING -> {
                AmityChatListSkeleton(modifier = modifier, itemCount = 9)
                // Pre-render hidden LazyColumn so it's in the composition tree
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                ) { }
            }

            AmityChatHomePageViewModel.ChannelListState.EMPTY -> {
                if (emptyContent != null) {
                    emptyContent()
                } else {
                    AmityChatListEmptyState(
                        modifier = modifier,
                        onCreateChatClick = onCreateChatClick,
                    )
                }
            }

            AmityChatHomePageViewModel.ChannelListState.ERROR -> {
                // Renders nothing by design: the empty state's "create a chat" call to action
                // would be misleading when the list merely failed to load.
            }

            AmityChatHomePageViewModel.ChannelListState.SUCCESS -> {
                val notificationsEnabled by (chatNotificationsEnabled
                    ?: MutableStateFlow(true)).collectAsState()

                LazyColumn(
                    modifier = modifier,
                    state = lazyListState,
                ) {
                    if (!notificationsEnabled) {
                        item(key = "notifications_disabled_banner") {
                            NotificationsDisabledBanner()
                        }
                    }
                    items(
                        count = channels.itemCount,
                        key = { index -> channels.peek(index)?.getChannelId() ?: index },
                    ) { index ->
                        val channel = channels[index]
                        if (channel != null) {
                            if (channel.getChannelType() == AmityChannel.Type.CONVERSATION) {
                                LaunchedEffect(channel.getChannelId()) {
                                    onFetchOtherMember(channel.getChannelId())
                                }
                            }
                            val otherMember = otherMembersMap[channel.getChannelId()]

                            if (swipeAction != null) {
                                SwipeToDismissListItem(
                                    channel = channel,
                                    otherMember = otherMember,
                                    swipeAction = swipeAction,
                                    onClick = { onChannelClick(channel) },
                                    onSwipe = { onSwipeAction(channel) },
                                )
                            } else {
                                AmityChatListItem(
                                    channel = channel,
                                    otherMember = otherMember,
                                    onClick = { onChannelClick(channel) },
                                )
                            }
                        }
                    }

                    if (channels.loadState.append is LoadState.Loading) {
                        item {
                            AmityChatListSkeleton(itemCount = 2)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDismissListItem(
    channel: AmityChannel,
    otherMember: AmityChannelMember? = null,
    swipeAction: SwipeAction,
    onClick: () -> Unit,
    onSwipe: () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
    )

    SwipeToDismissBox(
        state = dismissState,
        onDismiss = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onSwipe()
            }
        },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val iconResId = when (swipeAction) {
                SwipeAction.ARCHIVE -> CommonR.drawable.amity_ic_archive_r
                SwipeAction.UNARCHIVE -> CommonR.drawable.amity_ic_unarchive_r
            }
            val label = when (swipeAction) {
                SwipeAction.ARCHIVE -> DefaultAmityChatStringProvider.getInstance().getString("chat.archive")
                SwipeAction.UNARCHIVE -> DefaultAmityChatStringProvider.getInstance().getString("chat.unarchive")
            }

            // Full-width reveal keeps the SquareButton surface behind the whole row
            // while it slides; the atom sits flush at the trailing edge.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AmityTheme.token(AmityColorToken.SurfaceSquareButtonDefaultSecondaryDefault)),
                contentAlignment = Alignment.CenterEnd,
            ) {
                AmityButton(
                    variant = AmityButtonVariant.SQUARE,
                    color = AmityButtonColor.DEFAULT,
                    hierarchy = AmityButtonHierarchy.SECONDARY,
                    label = label,
                    icon = iconResId,
                    onClick = onSwipe,
                )
            }
        },
        content = {
            AmityChatListItem(
                channel = channel,
                otherMember = otherMember,
                onClick = onClick,
            )
        },
    )
}

@Composable
private fun NotificationsDisabledBanner() {
    AmityBanner(
        hierarchy = AmityBannerHierarchy.SUBDUE,
        centered = true,   // full-width system notice: icon + text are centred
        description = amityChatString("chat.notifications.disabled"),
        descriptionIcon = CommonR.drawable.amity_ic_bell_slash_r,
    )
}
