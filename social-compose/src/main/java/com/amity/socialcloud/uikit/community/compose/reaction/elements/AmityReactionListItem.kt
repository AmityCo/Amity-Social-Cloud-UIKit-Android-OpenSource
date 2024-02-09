package com.amity.socialcloud.uikit.community.compose.reaction.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.comment.elements.AmityCommentAvatarView
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme

@Composable
fun AmityReactionListItem(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    displayName: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        AmityCommentAvatarView(
            modifier = modifier,
            size = 40.dp,
            avatarUrl = avatarUrl
        )

        Text(
            text = displayName,
            style = AmityTheme.typography.body.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AmityReactionListItemPreview() {
    AmityReactionListItem(
        avatarUrl = "",
        displayName = "Jackie Jones",
    )
}