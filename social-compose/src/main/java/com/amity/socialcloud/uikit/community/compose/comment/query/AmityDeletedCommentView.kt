package com.amity.socialcloud.uikit.community.compose.comment.query

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityDeletedCommentView(
    modifier: Modifier = Modifier,
    isReplyComment: Boolean
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(start = 52.dp)
            .padding(vertical = 8.dp)
            .testTag("comment_list/comment_bubble_deleted_view")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier
                .background(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .padding(start = 8.dp, end = 12.dp)
                .padding(vertical = 5.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_remove),
                contentDescription = null,
                tint = AmityTheme.colors.baseShade2,
                modifier = modifier.size(16.dp)
            )
            Text(
                text = context.getString(
                    if (isReplyComment) R.string.amity_reply_deleted_message
                    else R.string.amity_comment_deleted_message
                ),
                style = AmityTheme.typography.caption.copy(
                    fontWeight = FontWeight.Normal,
                    color = AmityTheme.colors.baseShade2,
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityDeletedCommentViewPreview() {
    AmityDeletedCommentView(
        isReplyComment = true
    )
}