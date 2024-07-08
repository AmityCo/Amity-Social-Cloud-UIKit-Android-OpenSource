package com.amity.socialcloud.uikit.community.compose.comment.query.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityCommentModeratorBadge(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        modifier = modifier
            .background(
                color = AmityTheme.colors.primaryShade3,
                shape = RoundedCornerShape(size = 20.dp)
            )
            .padding(start = 4.dp, end = 6.dp)
            .testTag("comment_list/comment_bubble_moderator_badge")
    ) {
        Icon(
            painter = painterResource(id = R.drawable.amity_ic_moderator_social),
            contentDescription = null,
            tint = AmityTheme.colors.primary,
        )
        Text(
            text = "Moderator",
            style = TextStyle(
                fontSize = 10.sp,
                lineHeight = 18.sp,
                color = AmityTheme.colors.primary,
            )
        )
    }
}

@Preview
@Composable
fun AmityCommentModeratorBadgePreview() {
    AmityCommentModeratorBadge()
}