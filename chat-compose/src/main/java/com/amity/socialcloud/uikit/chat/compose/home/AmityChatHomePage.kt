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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.uikit.chat.compose.AmityChatBehaviorHelper
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.home.component.AmityChatListComponent
import com.amity.socialcloud.uikit.chat.compose.home.component.SwipeAction
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.amity.socialcloud.uikit.chat.compose.message.element.AmityChatWaitingForNetworkRow
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import kotlinx.coroutines.launch
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmityChatHomePage(
    modifier: Modifier = Modifier,
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

    // Resolve enabled channel types from config, then derive which tabs to show.
    val enabledTypes = remember { AmityUIKitConfigController.getEnabledChannelTypes() }
    val showConversation = "conversation" in enabledTypes
    val showCommunity = "community" in enabledTypes
    val showBothTypes = showConversation && showCommunity

    // Build ordered tab list. When both are enabled: All first, then Direct/Groups in config order.
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

    // Keep selectedTab valid when tabs list changes.
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

    // Archive limit error dialog
    if (showArchiveLimitDialog) {
        AlertDialog(
            onDismissRequest = { showArchiveLimitDialog = false },
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

    AmityBasePage(pageId = "chat_home_page") {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                // Top navigation bar
                AmityChatHomeTopNavigationBar(
                    isConnected = isConnected,
                    onSearchClick = { behavior.goToSearchPage(context) },
                    onCreateDirectChatClick = { behavior.goToCreateConversationPage(context) },
                    onCreateGroupChatClick = { behavior.goToCreateGroupPage(context) },
                    onArchivedClick = { behavior.goToArchivedChatPage(context) },
                    showConversationType = showConversation,
                    showCommunityType = showCommunity,
                )

                // Tab row — only show tabs for enabled channel types
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

            // Pager content
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
                style = AmityTheme.typography.headLine,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
        } else {
            Text(
                text = amityChatString("chat.home.title"),
                style = AmityTheme.typography.headLine,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            AmityChatWaitingForNetworkRow(
                modifier = Modifier.weight(1f)
            )
        }

        // Search button
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color = AmityTheme.colors.baseShade4)
                .clickableWithoutRipple { onSearchClick() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_home_search),
                contentDescription = "Search",
                modifier = Modifier.size(20.dp),
                tint = AmityTheme.colors.base,
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Create chat button with dropdown
        AmityCreateChatButton(
            onCreateDirectChatClick = onCreateDirectChatClick,
            onCreateGroupChatClick = onCreateGroupChatClick,
            showConversationType = showConversationType,
            showCommunityType = showCommunityType,
        )

        Spacer(modifier = Modifier.width(10.dp))

        // More menu (archived)
        AmityChatMoreMenuButton(
            onArchivedClick = onArchivedClick,
        )
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

    Box {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color = AmityTheme.colors.baseShade4)
                .clickableWithoutRipple {
                    when {
                        onlyConversation -> onCreateDirectChatClick()
                        onlyCommunity -> onCreateGroupChatClick()
                        else -> expanded = true
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_home_create),
                contentDescription = "Create Chat",
                modifier = Modifier.size(20.dp),
                tint = AmityTheme.colors.base,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(200.dp),
            containerColor = AmityTheme.colors.background
        ) {
            if (showConversationType) {
                DropdownMenuItem(
                    contentPadding = PaddingValues(0.dp),
                    text = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_home_create_direct),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = AmityTheme.colors.base,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = amityChatString("chat.create.direct"),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = AmityTheme.colors.base,
                                ),
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        onCreateDirectChatClick()
                    },
                )
            }
            if (showCommunityType) {
                DropdownMenuItem(
                contentPadding = PaddingValues(0.dp),
                text = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_home_create_group),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = AmityTheme.colors.base,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = amityChatString("chat.create.group"),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = AmityTheme.colors.base,
                            ),
                        )
                    }
                },
                onClick = {
                    expanded = false
                    onCreateGroupChatClick()
                },
            )
            } // end if (showCommunityType)
        }
    }
}

@Composable
private fun AmityChatMoreMenuButton(
    onArchivedClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color = AmityTheme.colors.baseShade4)
                .clickableWithoutRipple { expanded = true },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_home_more),
                contentDescription = "More",
                modifier = Modifier.size(20.dp),
                tint = AmityTheme.colors.base,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(200.dp),
            containerColor = AmityTheme.colors.background
        ) {
            DropdownMenuItem(
                contentPadding = PaddingValues(0.dp),
                text = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_home_archive),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = AmityTheme.colors.base,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = amityChatString("chat.archived"),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = AmityTheme.colors.base,
                            ),
                        )
                    }
                },
                onClick = {
                    expanded = false
                    onArchivedClick()
                },
            )
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
    Box(
        modifier = modifier
            .border(
                border = BorderStroke(
                    width = if (isSelected) 0.dp else 1.dp,
                    color = if (isSelected) Color.Transparent else AmityTheme.colors.baseShade4,
                ),
                shape = RoundedCornerShape(size = 24.dp)
            )
            .background(
                color = if (isSelected) AmityTheme.colors.primary else Color.Transparent,
                shape = RoundedCornerShape(size = 24.dp)
            )
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickableWithoutRipple { onClick() }
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 2.dp, vertical = 6.dp),
            text = title,
            style = AmityTheme.typography.titleLegacy.copy(
                color = if (isSelected) amityColorWhite else AmityTheme.colors.secondaryShade1,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            )
        )
    }
}
