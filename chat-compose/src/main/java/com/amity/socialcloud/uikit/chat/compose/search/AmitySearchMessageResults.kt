package com.amity.socialcloud.uikit.chat.compose.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.compose.home.element.AmityChatListItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * Public thin wrapper for message search results list.
 * Component ID: search_message_results
 *
 * Renders a scrollable list of message search matches with keyword context.
 * Each item shows the containing channel plus the matching message snippet.
 * Tapping a row invokes [onSelectMessage].
 *
 * @param modifier              Optional modifier.
 * @param results               Message search results paired with their containing channel.
 * @param query                 Active search query, used for highlight rendering in rows.
 * @param otherMembers          Map of channelId → other member, for conversation channel avatars.
 * @param archivedChannelIds    Set of channel IDs that are archived, for badge rendering.
 * @param isLoadingMore         Whether a pagination load is in flight (shows a spinner).
 * @param onSelectMessage       Called when the user taps a result row.
 * @param onLoadMore            Optional callback to load the next page when near the bottom.
 */
@Composable
fun AmitySearchMessageResults(
    modifier: Modifier = Modifier,
    results: List<Pair<AmityChannel, AmityMessage>>,
    query: String = "",
    otherMembers: Map<String, AmityChannelMember?> = emptyMap(),
    archivedChannelIds: Set<String> = emptySet(),
    isLoadingMore: Boolean = false,
    onSelectMessage: (channel: AmityChannel, message: AmityMessage) -> Unit = { _, _ -> },
    onLoadMore: (() -> Unit)? = null,
) {
    val listState = rememberLazyListState()

    if (onLoadMore != null) {
        val shouldLoadMore by remember {
            derivedStateOf {
                val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItems = listState.layoutInfo.totalItemsCount
                totalItems > 0 && lastVisible >= totalItems - 3
            }
        }
        LaunchedEffect(shouldLoadMore) {
            if (shouldLoadMore) onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            count = results.size,
            key = { index -> results[index].second.getMessageId() + index },
        ) { index ->
            val (channel, message) = results[index]
            val otherMember = otherMembers[channel.getChannelId()]
            AmityChatListItem(
                channel = channel,
                otherMember = otherMember,
                searchMessage = message,
                searchQuery = query,
                isArchived = archivedChannelIds.contains(channel.getChannelId()),
                onClick = { onSelectMessage(channel, message) },
            )
        }

        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = AmityTheme.colors.primary,
                        strokeWidth = 2.dp,
                    )
                }
            }
        }
    }
}
