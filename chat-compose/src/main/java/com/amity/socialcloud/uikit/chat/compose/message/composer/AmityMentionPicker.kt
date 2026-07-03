package com.amity.socialcloud.uikit.chat.compose.message.composer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amity.socialcloud.uikit.chat.compose.group.AmityGroupChatPageViewModel
import com.amity.socialcloud.uikit.chat.compose.group.composer.GroupMentionSuggestionView
import com.amity.socialcloud.uikit.chat.compose.live.mention.AmityMentionSuggestion

/**
 * Public thin wrapper for the mention picker overlay.
 * Component ID: mention_picker
 *
 * Shows a list of channel member suggestions when the composer enters @mention mode.
 * Delegates to the group chat mention suggestion implementation.
 *
 * @param modifier         Optional modifier.
 * @param keyword          Current mention query fragment (text after @).
 * @param viewModel        ViewModel providing paged member suggestions.
 * @param onSelectMember   Called when the user taps a suggestion; insert the mention token.
 */
@Composable
fun AmityMentionPicker(
    modifier: Modifier = Modifier,
    keyword: String,
    viewModel: AmityGroupChatPageViewModel,
    onSelectMember: (AmityMentionSuggestion) -> Unit,
    onClose: (() -> Unit)? = null,
) {
    GroupMentionSuggestionView(
        modifier = modifier,
        keyword = keyword,
        viewModel = viewModel,
        onClick = onSelectMember,
        onClose = onClose,
    )
}
