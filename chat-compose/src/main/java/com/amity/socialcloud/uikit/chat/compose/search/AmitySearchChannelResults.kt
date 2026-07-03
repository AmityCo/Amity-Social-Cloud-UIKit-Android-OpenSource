package com.amity.socialcloud.uikit.chat.compose.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.home.element.AmityChatListItem
import com.amity.socialcloud.uikit.chat.compose.home.element.AmityChatListSkeleton
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite

/**
 * Public thin wrapper for channel search results list.
 * Component ID: search_channel_results
 *
 * Renders a scrollable list of channel search matches with keyword context.
 * Tapping a row invokes [onSelectChannel].
 *
 * @param modifier              Optional modifier.
 * @param results               Channel search results to display.
 * @param query                 Active search query, used for highlight rendering in rows.
 * @param otherMembers          Map of channelId → other member, for conversation channel avatars.
 * @param archivedChannelIds    Set of channel IDs that are archived, for badge rendering.
 * @param isLoadingMore         Whether a pagination load is in flight (shows a spinner).
 * @param onSelectChannel       Called when the user taps a result row.
 * @param onLoadMore            Optional callback to load the next page when near the bottom.
 * @param onArchive             Called when the user swipes to archive a channel.
 * @param onUnarchive           Called when the user swipes to unarchive a channel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmitySearchChannelResults(
    modifier: Modifier = Modifier,
    results: List<AmityChannel>,
    query: String = "",
    otherMembers: Map<String, AmityChannelMember?> = emptyMap(),
    archivedChannelIds: Set<String> = emptySet(),
    isLoadingMore: Boolean = false,
    onSelectChannel: (AmityChannel) -> Unit = {},
    onLoadMore: (() -> Unit)? = null,
    onArchive: (AmityChannel) -> Unit = {},
    onUnarchive: (AmityChannel) -> Unit = {},
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
            key = { index -> results[index].getChannelId() + index },
        ) { index ->
            val channel = results[index]
            val otherMember = otherMembers[channel.getChannelId()]
            val isArchived = archivedChannelIds.contains(channel.getChannelId())

            SwipeToDismissListItem(
                channel = channel,
                otherMember = otherMember,
                searchQuery = query,
                isArchived = isArchived,
                onClick = { onSelectChannel(channel) },
                onSwipe = {
                    if (isArchived) onUnarchive(channel) else onArchive(channel)
                },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDismissListItem(
    channel: AmityChannel,
    otherMember: AmityChannelMember? = null,
    searchQuery: String = "",
    isArchived: Boolean = false,
    onClick: () -> Unit,
    onSwipe: () -> Unit,
) {
    val currentOnSwipe by rememberUpdatedState(onSwipe)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                currentOnSwipe()
                false
            } else {
                false
            }
        },
        positionalThreshold = { totalDistance -> totalDistance * 0.4f },
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val iconResId = if (isArchived) R.drawable.amity_ic_chat_unarchive
                else R.drawable.amity_ic_chat_home_archive
            val label = if (isArchived) amityChatString("chat.unarchive")
                else amityChatString("chat.archive")

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AmityTheme.colors.baseShade2)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = amityColorWhite,
                    )
                    Text(
                        text = label,
                        color = amityColorWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        },
        content = {
            AmityChatListItem(
                channel = channel,
                otherMember = otherMember,
                searchQuery = searchQuery,
                isArchived = isArchived,
                onClick = onClick,
            )
        },
    )
}
