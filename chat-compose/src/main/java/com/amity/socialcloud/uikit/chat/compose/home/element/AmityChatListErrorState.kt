package com.amity.socialcloud.uikit.chat.compose.home.element

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyState
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyStateVariant

/**
 * PDT-5753, AmityChatHomePage v3 REQ-022. Shown in place of the channel list when the query fails,
 * which an integrator meets by setting `channel_list_sort_by` to `"lastMessage"` on an environment
 * where the backend flag is off: the query then returns 422 with code 500000.
 *
 * Same container, illustration and text styles as [AmityChatListEmptyState], with **no** call to
 * action. Offering "create a chat" here would be misleading, because nothing the user creates
 * fixes a list that failed to load.
 */
@Composable
fun AmityChatListErrorState(
    modifier: Modifier = Modifier,
) {
    AmityEmptyState(
        modifier = modifier.fillMaxSize(),
        variant = AmityEmptyStateVariant.ILLUSTRATION,
        illustrationLight = R.drawable.amity_ic_empty_community_2,
        illustrationDark = R.drawable.amity_ic_empty_community_2_dark,
        title = amityChatString("chat.home.error.title"),
        description = amityChatString("chat.home.error.description"),
    )
}
