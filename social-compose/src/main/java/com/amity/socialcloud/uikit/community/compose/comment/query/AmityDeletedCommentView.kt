package com.amity.socialcloud.uikit.community.compose.comment.query

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityDeletedCommentView(
    modifier: Modifier = Modifier,
    isReplyComment: Boolean,
    replyCount: Int? = null,
) {
    val context = LocalContext.current
    if (isReplyComment) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(top = 8.dp, bottom = if (replyCount != null) 0.dp else 8.dp)
                .testTag("comment_list/comment_bubble_deleted_view")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = modifier
                    .background(
                        color = Color.Transparent,//AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(size = 4.dp)

                    )
                    .border(
                        width = 1.dp,
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
                    text = context.getString(R.string.amity_reply_deleted_message),
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.baseShade2,
                    )
                )
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(vertical = 8.dp)
                .testTag("comment_list/comment_bubble_deleted_view")
        ) {
            HorizontalDivider(
                color = AmityTheme.colors.baseShade4,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.amity_ic_remove),
                    contentDescription = null,
                    tint = AmityTheme.colors.baseShade2,
                    modifier = modifier.size(16.dp)
                )
                Text(
                    text = context.getString(R.string.amity_comment_deleted_message),
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.baseShade2,
                    )
                )
            }
            HorizontalDivider(
                color = AmityTheme.colors.baseShade4,
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