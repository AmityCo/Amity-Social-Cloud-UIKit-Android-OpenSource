package com.amity.socialcloud.uikit.chat.compose.search

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.uikit.chat.compose.home.AmityChatHomePageBehavior
import com.amity.socialcloud.uikit.chat.compose.home.element.AmityChatListSkeleton
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBoxedInputStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyState
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyStateVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityInput
import com.amity.socialcloud.uikit.common.ui.atoms.AmityInputSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityInputVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityTab
import com.amity.socialcloud.uikit.common.ui.atoms.AmityTabVariant
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

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
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Keep selectedTabIndex in sync with ViewModel tab state
    LaunchedEffect(searchState.activeTab) {
        selectedTabIndex = if (searchState.activeTab == SearchTab.CHATS) 0 else 1
    }

    AmityBasePage(pageId = "search_channel_page", useAmityToast = true) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.token(AmityColorToken.SurfacePageBackgroundDefault)),
        ) {
            // Search header — matches AmityTopSearchBarComponent visual style
            Row(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                ) {
                    AmityInput(
                        variant = AmityInputVariant.BOXED,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        value = searchKeyword,
                        placeholder = amityChatString("chat.search.placeholder"),
                        leadingIcon = CommonR.drawable.amity_ic_search_r,
                        trailingIcon = if (searchKeyword.isNotEmpty()) CommonR.drawable.amity_ic_clear_r else null,
                        size = AmityInputSize.M,
                        boxedStyle = AmityBoxedInputStyle.SQUARE,
                        onChangeText = { viewModel.onSearchKeywordChanged(it) },
                        onSubmit = { keyboardController?.hide() },
                    )

                    if (searchKeyword.isNotEmpty()) {
                        // Transparent tap target over the atom's own trailing clear glyph — AmityInput
                        // renders the icon but exposes no click callback for it. Height matches the
                        // AmityInputSize.M Boxed Input row height (48.dp) selected above.
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .height(48.dp)
                                .width(32.dp)
                                .clickableWithoutRipple {
                                    viewModel.onSearchKeywordChanged("")
                                },
                        )
                    }
                }

                AmityButton(
                    variant = AmityButtonVariant.MAIN,
                    style = AmityButtonStyle.GHOST,
                    hierarchy = AmityButtonHierarchy.PRIMARY,
                    label = amityChatString("chat.cancel"),
                    onClick = {
                        (context as? Activity)?.finish()
                    },
                )
            }

            // Tabs — Underlined (Tab atom) over a Post hairline (Divider atom)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AmityTheme.token(AmityColorToken.SurfaceListDefaultDefault))
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AmityTab(
                    variant = AmityTabVariant.Underlined,
                    label = tabChats,
                    selected = selectedTabIndex == 0,
                    onPress = {
                        if (selectedTabIndex != 0) viewModel.changeTab(SearchTab.CHATS)
                    },
                )
                AmityTab(
                    variant = AmityTabVariant.Underlined,
                    label = tabMessages,
                    selected = selectedTabIndex == 1,
                    onPress = {
                        if (selectedTabIndex != 1) viewModel.changeTab(SearchTab.MESSAGES)
                    },
                )
            }
            AmityDivider(variant = AmityDividerVariant.Post)

            // Content
            when {
                // Not enough characters yet
                searchKeyword.trim().length < 3 -> {
                    AmityEmptyState(
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding()
                            .padding(32.dp),
                        variant = AmityEmptyStateVariant.ICON,
                        icon = CommonR.drawable.amity_ic_search_l,
                        title = amityChatString("chat.search.min.chars"),
                    )
                }

                // Initial loading — show skeleton
                searchState.isLoading && searchState.channels.isEmpty() -> {
                    AmityChatListSkeleton()
                }

                // No results
                !searchState.isLoading && searchState.channels.isEmpty() -> {
                    AmityEmptyState(
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding()
                            .padding(32.dp),
                        variant = AmityEmptyStateVariant.ICON,
                        icon = CommonR.drawable.amity_ic_search_cross_l,
                        title = amityChatString("chat.search.no.results"),
                    )
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
