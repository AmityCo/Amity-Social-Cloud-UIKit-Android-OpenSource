package com.amity.socialcloud.uikit.chat.compose.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.uikit.chat.compose.AmityChatBehaviorHelper
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.chat.compose.home.component.AmityChatListComponent
import com.amity.socialcloud.uikit.chat.compose.home.component.SwipeAction
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.amity.socialcloud.uikit.chat.compose.message.element.AmityChatWaitingForNetworkRow
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityTab
import com.amity.socialcloud.uikit.common.ui.atoms.AmityTabVariant
import com.amity.socialcloud.uikit.common.ui.elements.AmityPopover
import com.amity.socialcloud.uikit.common.ui.elements.AmityPopoverRow
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmityChatHomePage(
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    val behavior = remember { AmityChatBehaviorHelper.chatHomePageBehavior }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel = viewModel<AmityChatHomePageViewModel>(
        viewModelStoreOwner = viewModelStoreOwner,
    )

    var selectedTab by remember { mutableStateOf(AmityChatHomePageTab.ALL) }

    val enabledTypes = remember { AmityUIKitConfigController.getEnabledChannelTypes() }
    val showConversation = "conversation" in enabledTypes
    val showCommunity = "community" in enabledTypes
    val showBothTypes = showConversation && showCommunity

    val tabs: List<AmityChatHomePageTab> = remember(enabledTypes) {
        if (showBothTypes) {
            val ordered = mutableListOf(AmityChatHomePageTab.ALL)
            enabledTypes.forEach { type ->
                when (type) {
                    "conversation" -> ordered.add(AmityChatHomePageTab.DIRECT)
                    "community" -> ordered.add(AmityChatHomePageTab.GROUPS)
                }
            }
            ordered
        } else if (showConversation) {
            listOf(AmityChatHomePageTab.DIRECT)
        } else {
            listOf(AmityChatHomePageTab.GROUPS)
        }
    }

    if (selectedTab !in tabs) selectedTab = tabs.first()

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size },
    )
    val scrollScope = rememberCoroutineScope()
    val archivedMessage = amityChatString("chat.archived.toast")
    val archiveErrorMessage = amityChatString("chat.archive.error.toast")
    val archiveLimitTitle = amityChatString("chat.archive.limit.title")
    val archiveLimitMessage = amityChatString("chat.archive.limit.message")
    var showArchiveLimitDialog by remember { mutableStateOf(false) }
    val otherMembersMap by viewModel.otherMembers.collectAsState()
    val connection by viewModel.getNetworkConnectionStateFlow()
        .collectAsState(initial = NetworkConnectionEvent.Connected)
    val isConnected = connection is NetworkConnectionEvent.Connected

    if (showArchiveLimitDialog) {
        AlertDialog(
            onDismissRequest = { showArchiveLimitDialog = false },
            containerColor = AmityTheme.token(AmityColorToken.SurfaceAlertDialogBackgroundDefault),
            titleContentColor = AmityTheme.token(AmityColorToken.TextAlertDialogHeaderTitleDefault),
            textContentColor = AmityTheme.token(AmityColorToken.TextAlertDialogBodyDefault),
            title = { Text(text = archiveLimitTitle) },
            text = { Text(text = archiveLimitMessage) },
            confirmButton = {
                TextButton(onClick = { showArchiveLimitDialog = false }) {
                    Text(text = amityChatString("chat.button.ok"))
                }
            },
        )
    }

    val onSwipeArchive: (AmityChannel) -> Unit = { channel ->
        viewModel.archiveChannel(
            channelId = channel.getChannelId(),
            onSuccess = {
                AmityUIKitSnackbar.publishSnackbarMessage(message = archivedMessage)
            },
            onError = { error ->
                if (error.message?.contains("Archive limit exceeded") == true) {
                    showArchiveLimitDialog = true
                } else {
                    AmityUIKitSnackbar.publishSnackbarMessage(message = archiveErrorMessage)
                }
            },
        )
    }

    AmityBasePage(pageId = "chat_home_page", useAmityToast = true) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.token(AmityColorToken.SurfacePageBackgroundDefault)),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                // Optional leading back affordance for integrators hosting this page as a sub-page, so they
                // don't have to overlay their own back control on the leading-pinned title.
                if (onBackClick != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_chevron_left),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickableWithoutRipple { onBackClick() },
                        tint = AmityTheme.token(AmityColorToken.IconIconButtonGhostSecondaryDefault),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                AmityChatHomeTopNavigationBar(
                    isConnected = isConnected,
                    onSearchClick = { behavior.goToSearchPage(context) },
                    onCreateDirectChatClick = { behavior.goToCreateConversationPage(context) },
                    onCreateGroupChatClick = { behavior.goToCreateGroupPage(context) },
                    onArchivedClick = { behavior.goToArchivedChatPage(context) },
                    showConversationType = showConversation,
                    showCommunityType = showCommunity,
                )

                LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                modifier = Modifier.wrapContentHeight(),
            ) {
                tabs.forEachIndexed { index, tab ->
                    item {
                        val (title, testTag) = when (tab) {
                            AmityChatHomePageTab.ALL -> amityChatString("chat.tab.all") to "chat_home_page/tab_all"
                            AmityChatHomePageTab.DIRECT -> amityChatString("chat.tab.direct") to "chat_home_page/tab_direct"
                            AmityChatHomePageTab.GROUPS -> amityChatString("chat.tab.groups") to "chat_home_page/tab_groups"
                        }
                        AmityChatHomeTabButton(
                            title = title,
                            isSelected = selectedTab == tab,
                            modifier = Modifier.testTag(testTag),
                        ) {
                            selectedTab = tab
                            scrollScope.launch { pagerState.scrollToPage(index) }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                HorizontalPager(
                    state = pagerState,
                    beyondViewportPageCount = 2,
                    userScrollEnabled = false,
                ) { page ->
                    val onChannelClick: (AmityChannel) -> Unit = { channel ->
                        if (channel.getChannelType() == AmityChannel.Type.COMMUNITY) {
                            behavior.goToGroupChatPage(context, channel.getChannelId())
                        } else {
                            behavior.goToConversationChatPage(context, channel.getChannelId())
                        }
                    }

                    when (tabs.getOrNull(page)) {
                        AmityChatHomePageTab.ALL -> AmityChatListComponent(
                            modifier = Modifier.fillMaxSize(),
                            pageScope = getPageScope(),
                            componentId = "all_chat_list",
                            channelsFlow = viewModel.allChannels,
                            otherMembersMap = otherMembersMap,
                            onFetchOtherMember = { viewModel.fetchOtherMember(it) },
                            onChannelClick = onChannelClick,
                            swipeAction = SwipeAction.ARCHIVE,
                            onSwipeAction = onSwipeArchive,
                            onCreateChatClick = { behavior.goToCreateConversationPage(context) },
                            chatNotificationsEnabled = viewModel.isChatNotificationEnabled,
                        )
                        AmityChatHomePageTab.DIRECT -> AmityChatListComponent(
                            modifier = Modifier.fillMaxSize(),
                            pageScope = getPageScope(),
                            componentId = "conversation_chat_list",
                            channelsFlow = viewModel.conversationChannels,
                            otherMembersMap = otherMembersMap,
                            onFetchOtherMember = { viewModel.fetchOtherMember(it) },
                            onChannelClick = onChannelClick,
                            swipeAction = SwipeAction.ARCHIVE,
                            onSwipeAction = onSwipeArchive,
                            onCreateChatClick = { behavior.goToCreateConversationPage(context) },
                            chatNotificationsEnabled = viewModel.isChatNotificationEnabled,
                        )
                        AmityChatHomePageTab.GROUPS -> AmityChatListComponent(
                            modifier = Modifier.fillMaxSize(),
                            pageScope = getPageScope(),
                            componentId = "group_chat_list",
                            channelsFlow = viewModel.groupChannels,
                            otherMembersMap = otherMembersMap,
                            onFetchOtherMember = { viewModel.fetchOtherMember(it) },
                            onChannelClick = onChannelClick,
                            swipeAction = SwipeAction.ARCHIVE,
                            onSwipeAction = onSwipeArchive,
                            onCreateChatClick = { behavior.goToCreateGroupPage(context) },
                            chatNotificationsEnabled = viewModel.isChatNotificationEnabled,
                        )
                        null -> Unit
                    }
                }
            }
            }
        }
    }
}

@Composable
private fun AmityChatHomeTopNavigationBar(
    modifier: Modifier = Modifier,
    isConnected: Boolean = true,
    onSearchClick: () -> Unit = {},
    onCreateDirectChatClick: () -> Unit = {},
    onCreateGroupChatClick: () -> Unit = {},
    onArchivedClick: () -> Unit = {},
    showConversationType: Boolean = true,
    showCommunityType: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isConnected) {
            Text(
                text = amityChatString("chat.home.title"),
                style = AmityTheme.typography.headLine.copy(
                    color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
        } else {
            Text(
                text = amityChatString("chat.home.title"),
                style = AmityTheme.typography.headLine.copy(
                    color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            AmityChatWaitingForNetworkRow(
                modifier = Modifier.weight(1f)
            )
        }

        AmityButton(
            variant = AmityButtonVariant.ICON,
            style = AmityButtonStyle.FILLED,
            hierarchy = AmityButtonHierarchy.SECONDARY,
            iconSize = AmityIconButtonSize.SIZE32,
            icon = CommonR.drawable.amity_ic_search_r,
            onClick = onSearchClick,
        )

        Spacer(modifier = Modifier.width(12.dp))

        AmityCreateChatButton(
            onCreateDirectChatClick = onCreateDirectChatClick,
            onCreateGroupChatClick = onCreateGroupChatClick,
            showConversationType = showConversationType,
            showCommunityType = showCommunityType,
        )

        Spacer(modifier = Modifier.width(12.dp))

        AmityChatMoreMenuButton(
            onArchivedClick = onArchivedClick,
        )
    }
}

/**
 * Transparent gutter placed around the popover inside its `Popup` window so the AmityPopover
 * two-layer drop shadow has room to render. A `Popup` window is sized tightly to its content, so a
 * shadow drawn at the surface's own edge bleeds outside the window and gets clipped away (the
 * popover then looks flat/elevation-less). The gutter enlarges the window by this much on every
 * side; the position provider compensates so the visible surface stays anchored where it was.
 */
private val AmityPopoverShadowGutter = 16.dp

/**
 * Anchors a popover directly below its trigger button, right-aligned to the button's end edge,
 * since these top-bar icon buttons sit close to the screen's trailing edge. The offset is not
 * part of the shared AmityPopover component — it is this page's own placement, the same pattern
 * as `AmityMessageActionMenuPopup`'s bubble-relative provider.
 */
@Composable
private fun rememberBelowAnchorEndAlignedPositionProvider(
    gap: Dp = 4.dp,
    shadowGutter: Dp = AmityPopoverShadowGutter,
): PopupPositionProvider {
    val density = LocalDensity.current
    val gapPx = with(density) { gap.roundToPx() }
    val gutterPx = with(density) { shadowGutter.roundToPx() }
    return remember(gapPx, gutterPx) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize,
            ): IntOffset {
                // popupContentSize includes the transparent shadow gutter on every side, so offset
                // by // +gutter (x) / −gutter (y) to keep the visible surface end-aligned to the
                // anchor and `gap` below it.
                val x = (anchorBounds.right - popupContentSize.width + gutterPx).coerceAtLeast(0)
                val maxY = (windowSize.height - popupContentSize.height).coerceAtLeast(0)
                val y = (anchorBounds.bottom + gapPx - gutterPx).coerceAtLeast(0).coerceAtMost(maxY)
                return IntOffset(x, y)
            }
        }
    }
}

@Composable
private fun AmityCreateChatButton(
    onCreateDirectChatClick: () -> Unit,
    onCreateGroupChatClick: () -> Unit,
    showConversationType: Boolean = true,
    showCommunityType: Boolean = true,
) {
    // When only one type is enabled, skip the picker and navigate directly.
    val onlyConversation = showConversationType && !showCommunityType
    val onlyCommunity = showCommunityType && !showConversationType

    var expanded by remember { mutableStateOf(false) }
    val positionProvider = rememberBelowAnchorEndAlignedPositionProvider()

    Box {
        AmityButton(
            variant = AmityButtonVariant.ICON,
            style = AmityButtonStyle.FILLED,
            hierarchy = AmityButtonHierarchy.SECONDARY,
            iconSize = AmityIconButtonSize.SIZE32,
            icon = CommonR.drawable.amity_ic_plus_r,
            onClick = {
                when {
                    onlyConversation -> onCreateDirectChatClick()
                    onlyCommunity -> onCreateGroupChatClick()
                    else -> expanded = true
                }
            },
        )

        if (expanded) {
            Popup(
                popupPositionProvider = positionProvider,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true),
            ) {
                // The gutter Box gives the popover's drop shadow room inside the Popup window; the
                // position // provider compensates so the surface stays anchored (see the provider
                // above).
                Box(modifier = Modifier.padding(AmityPopoverShadowGutter)) {
                    AmityPopover {
                        if (showConversationType) {
                            AmityPopoverRow(
                                icon = CommonR.drawable.amity_ic_user_plus_r,
                                label = amityChatString("chat.create.direct"),
                                onSelect = {
                                    expanded = false
                                    onCreateDirectChatClick()
                                },
                            )
                        }
                        if (showCommunityType) {
                            AmityPopoverRow(
                                icon = CommonR.drawable.amity_ic_user_group_r,
                                label = amityChatString("chat.create.group"),
                                onSelect = {
                                    expanded = false
                                    onCreateGroupChatClick()
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AmityChatMoreMenuButton(
    onArchivedClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val positionProvider = rememberBelowAnchorEndAlignedPositionProvider()

    Box {
        AmityButton(
            variant = AmityButtonVariant.ICON,
            style = AmityButtonStyle.FILLED,
            hierarchy = AmityButtonHierarchy.SECONDARY,
            iconSize = AmityIconButtonSize.SIZE32,
            icon = CommonR.drawable.amity_ic_ellipsis_v_r,
            onClick = { expanded = true },
        )

        if (expanded) {
            Popup(
                popupPositionProvider = positionProvider,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true),
            ) {
                Box(modifier = Modifier.padding(AmityPopoverShadowGutter)) {
                    AmityPopover {
                        AmityPopoverRow(
                            icon = CommonR.drawable.amity_ic_archive_r,
                            label = amityChatString("chat.archived"),
                            onSelect = {
                                expanded = false
                                onArchivedClick()
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AmityChatHomeTabButton(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    AmityTab(
        variant = AmityTabVariant.Pill,
        modifier = modifier,
        label = title,
        selected = isSelected,
        onPress = onClick,
    )
}
