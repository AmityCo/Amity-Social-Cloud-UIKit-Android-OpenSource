package com.amity.socialcloud.uikit.community.compose.comment.query.elements

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityCommentViewReplyBar(
    modifier: Modifier = Modifier,
    isViewAllReplies: Boolean,
    replyCount: Int,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .border(
                width = 1.dp,
                color = AmityTheme.colors.baseShade4,
                shape = RoundedCornerShape(size = 4.dp)
            )
            .clickable { onClick() }
            .padding(start = 8.dp, end = 12.dp, top = 5.dp, bottom = 5.dp)
    ) {

        Icon(
            painter = painterResource(id = R.drawable.amity_view_replies),
            contentDescription = null,
            tint = AmityTheme.colors.baseShade1,
            modifier = modifier.size(16.dp)
        )

        val text = if (isViewAllReplies) {
            context.getString(R.string.amity_view_more_replies)
        } else {
            "View $replyCount ${if (replyCount > 1) "replies" else "reply"}"
        }

        Text(
            text = text,
            style = AmityTheme.typography.captionLegacy.copy(
                color = AmityTheme.colors.baseShade1,
            ),
            modifier = modifier.testTag("comment_list/comment_bubble_view_reply_button")
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AmityCommentViewReplyBarPreview() {
    AmityCommentViewReplyBar(
        isViewAllReplies = false,
        replyCount = 1
    ) {}
}