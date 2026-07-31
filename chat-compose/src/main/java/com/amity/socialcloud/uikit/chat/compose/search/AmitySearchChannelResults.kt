package com.amity.socialcloud.uikit.chat.compose.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
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
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.uikit.chat.compose.home.element.AmityChatListItem
import com.amity.socialcloud.uikit.chat.compose.home.element.AmityChatListSkeleton
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonColor
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoader
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoaderSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoaderVariant
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken

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
                    AmityLoader(
                        variant = AmityLoaderVariant.Spinner,
                        size = AmityLoaderSize.Sm,
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
            val iconResId = if (isArchived) CommonR.drawable.amity_ic_unarchive_r
                else CommonR.drawable.amity_ic_archive_r
            val label = if (isArchived) amityChatString("chat.unarchive")
                else amityChatString("chat.archive")

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
                    onClick = currentOnSwipe,
                )
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
