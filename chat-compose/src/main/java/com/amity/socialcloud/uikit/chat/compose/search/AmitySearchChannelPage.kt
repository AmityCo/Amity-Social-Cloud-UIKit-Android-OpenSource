package com.amity.socialcloud.uikit.chat.compose.search

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.home.AmityChatHomePageBehavior
import com.amity.socialcloud.uikit.chat.compose.home.element.AmityChatListSkeleton
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.R as CommonR
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityTabRow
import com.amity.socialcloud.uikit.common.ui.elements.AmityTabRowItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite

@Composable
fun AmitySearchChannelPage(
    modifier: Modifier = Modifier,
) {
    val viewModel: AmitySearchChannelPageViewModel = viewModel()
    val searchKeyword by viewModel.searchKeyword.collectAsState()
    val searchState by viewModel.searchState.collectAsState()
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val behavior = remember { AmityChatHomePageBehavior() }
    val unarchivedMessage = amityChatString("chat.unarchived.toast")
    val unarchiveErrorMessage = amityChatString("chat.unarchive.error.toast")
    val archivedMessage = amityChatString("chat.archived.toast")
    val archiveErrorMessage = amityChatString("chat.archive.error.toast")

    // Auto-focus the search field on open
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val tabChats = amityChatString("chat.search.tab.chats")
    val tabMessages = amityChatString("chat.search.tab.messages")
    val tabs = remember(tabChats, tabMessages) {
        listOf(
            AmityTabRowItem(title = tabChats),
            AmityTabRowItem(title = tabMessages),
        )
    }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Keep selectedTabIndex in sync with ViewModel tab state
    LaunchedEffect(searchState.activeTab) {
        selectedTabIndex = if (searchState.activeTab == SearchTab.CHATS) 0 else 1
    }

    AmityBasePage(pageId = "search_channel_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background),
        ) {
            // Search header — matches AmityTopSearchBarComponent visual style
            Row(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(8.dp),
                        )
                        .padding(horizontal = 12.dp),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_home_search),
                        tint = AmityTheme.colors.baseShade2,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )

                    BasicTextField(
                        value = searchKeyword,
                        onValueChange = { viewModel.onSearchKeywordChanged(it) },
                        singleLine = true,
                        textStyle = AmityTheme.typography.bodyLegacy.copy(
                            color = AmityTheme.colors.base,
                        ),
                        cursorBrush = SolidColor(AmityTheme.colors.highlight),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = { keyboardController?.hide() }
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 14.dp)
                            .focusRequester(focusRequester),
                        decorationBox = { innerTextField ->
                            if (searchKeyword.isEmpty()) {
                                Text(
                                    text = amityChatString("chat.search.placeholder"),
                                    style = AmityTheme.typography.bodyLegacy.copy(
                                        color = AmityTheme.colors.baseShade2,
                                    ),
                                )
                            }
                            innerTextField()
                        },
                    )

                    if (searchKeyword.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(AmityTheme.colors.baseShade3)
                                .clickableWithoutRipple {
                                    viewModel.onSearchKeywordChanged("")
                                },
                        ) {
                            Icon(
                                painter = painterResource(id = CommonR.drawable.amity_ic_close),
                                tint = amityColorWhite,
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(5.dp),
                            )
                        }
                    }
                }

                Text(
                    text = amityChatString("chat.cancel"),
                    style = AmityTheme.typography.bodyBold,
                    color = AmityTheme.colors.primary,
                    modifier = Modifier.clickableWithoutRipple {
                        (context as? Activity)?.finish()
                    },
                )
            }

            // Tabs — AmityTabRow includes its own bottom divider
            AmityTabRow(
                tabs = tabs,
                selectedIndex = selectedTabIndex,
            ) { index ->
                viewModel.changeTab(if (index == 0) SearchTab.CHATS else SearchTab.MESSAGES)
            }

            // Content
            when {
                // Not enough characters yet
                searchKeyword.trim().length < 3 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.amity_ic_empty_search),
                            contentDescription = "Search",
                            modifier = Modifier.size(60.dp),
                            alpha = 0.5f,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = amityChatString("chat.search.min.chars"),
                            style = AmityTheme.typography.titleBold.copy(
                                textAlign = TextAlign.Center,
                                color = AmityTheme.colors.baseShade3,
                            ),
                        )
                    }
                }

                // Initial loading — show skeleton
                searchState.isLoading && searchState.channels.isEmpty() -> {
                    AmityChatListSkeleton()
                }

                // No results
                !searchState.isLoading && searchState.channels.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = amityChatString("chat.search.no.results"),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = AmityTheme.colors.baseShade2,
                            ),
                        )
                    }
                }

                // Results list
                else -> {
                    val isMessagesTab = searchState.activeTab == SearchTab.MESSAGES
                    if (isMessagesTab) {
                        // Build paired list: channel + message for each index
                        val messageResults = searchState.channels.mapIndexedNotNull { index, channel ->
                            val message = searchState.getSearchMessageForIndex(index) ?: return@mapIndexedNotNull null
                            channel to message
                        }
                        AmitySearchMessageResults(
                            results = messageResults,
                            query = searchState.lastValidSearchText,
                            otherMembers = searchState.channelMembers,
                            archivedChannelIds = searchState.archivedChannelIds.toSet(),
                            isLoadingMore = searchState.isLoadingMore,
                            onSelectMessage = { channel, message ->
                                val jumpToMessageId = message.getMessageId()
                                when (channel.getChannelType()) {
                                    AmityChannel.Type.CONVERSATION -> {
                                        behavior.goToConversationChatPage(context, channel.getChannelId(), jumpToMessageId)
                                    }
                                    AmityChannel.Type.COMMUNITY -> {
                                        behavior.goToGroupChatPage(context, channel.getChannelId(), jumpToMessageId)
                                    }
                                    else -> {}
                                }
                            },
                            onLoadMore = { viewModel.loadMore() },
                        )
                    } else {
                        AmitySearchChannelResults(
                            results = searchState.channels,
                            query = searchState.lastValidSearchText,
                            otherMembers = searchState.channelMembers,
                            archivedChannelIds = searchState.archivedChannelIds.toSet(),
                            isLoadingMore = searchState.isLoadingMore,
                            onSelectChannel = { channel ->
                                when (channel.getChannelType()) {
                                    AmityChannel.Type.CONVERSATION -> {
                                        behavior.goToConversationChatPage(
                                            context,
                                            channel.getChannelId(),
                                            null
                                        )
                                    }

                                    AmityChannel.Type.COMMUNITY -> {
                                        behavior.goToGroupChatPage(
                                            context,
                                            channel.getChannelId(),
                                            null
                                        )
                                    }

                                    else -> {}
                                }
                            },
                            onLoadMore = { viewModel.loadMore() },
                            onArchive = { channel ->
                                viewModel.archiveChannel(
                                    channel.getChannelId(), onSuccess = {
                                        AmityUIKitSnackbar.publishSnackbarMessage(
                                            message = archivedMessage,
                                        )
                                    },
                                    onError = {
                                        AmityUIKitSnackbar.publishSnackbarMessage(
                                            message = archiveErrorMessage,
                                        )
                                    })
                            },
                            onUnarchive = { channel ->
                                viewModel.unarchiveChannel(
                                    channel.getChannelId(),
                                    onSuccess = {
                                        AmityUIKitSnackbar.publishSnackbarMessage(
                                            message = unarchivedMessage,
                                        )
                                    },
                                    onError = {
                                        AmityUIKitSnackbar.publishSnackbarMessage(
                                            message = unarchiveErrorMessage,
                                        )
                                    })
                            },
                        )
                    }
                }
            }
        }
    }
}
