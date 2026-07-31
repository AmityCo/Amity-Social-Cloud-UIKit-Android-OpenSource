package com.amity.socialcloud.uikit.chat.compose.home.element

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyState
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyStateAction
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyStateVariant
import com.amity.socialcloud.uikit.common.compose.R as CommonR

@Composable
fun AmityChatListEmptyState(
    modifier: Modifier = Modifier,
    onCreateChatClick: () -> Unit = {},
) {
    AmityEmptyState(
        modifier = modifier.fillMaxSize(),
        variant = AmityEmptyStateVariant.ILLUSTRATION,
        illustrationLight = R.drawable.amity_ic_empty_community_2,
        illustrationDark = R.drawable.amity_ic_empty_community_2_dark,
        title = amityChatString("chat.home.empty.title"),
        description = amityChatString("chat.home.empty.description"),
        primaryAction = AmityEmptyStateAction(
            label = amityChatString("chat.create.new.chat"),
            icon = CommonR.drawable.amity_ic_plus_r,
            onPress = onCreateChatClick,
        ),
    )
}
