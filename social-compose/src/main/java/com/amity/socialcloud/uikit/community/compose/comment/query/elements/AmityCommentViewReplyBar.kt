package com.amity.socialcloud.uikit.community.compose.comment.query.elements

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
    isReplyComment: Boolean = false,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClick() }
            .padding(start = 8.dp, end = 12.dp, bottom = 8.dp)
    ) {

        if (isViewAllReplies) {
            Spacer(Modifier.width(16.dp))
            Icon(
                painter = painterResource(id = R.drawable.amity_arrow_down),
                contentDescription = null,
                tint = AmityTheme.colors.baseShade1,
                modifier = Modifier.size(12.dp)
            )
        } else if (!isReplyComment) {
            Icon(
                painter = painterResource(id = R.drawable.amity_view_replies),
                contentDescription = null,
                tint = AmityTheme.colors.baseShade1,
                modifier = Modifier.size(16.dp)
            )
        }

        val text = if (isViewAllReplies) {
            context.getString(R.string.amity_view_more_replies)
        } else {
            "View $replyCount ${if (replyCount > 1) "replies" else "reply"}"
        }

        Text(
            text = text,
            style = AmityTheme.typography.captionBold.copy(
                color = AmityTheme.colors.baseShade1,
            ),
            modifier = modifier
                .padding(start = 8.dp)
                .testTag("comment_list/comment_bubble_view_reply_button")
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AmityCommentViewReplyBarPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AmityCommentViewReplyBar(
            isViewAllReplies = false,
            replyCount = 1
        ) {}
        AmityCommentViewReplyBar(
            isViewAllReplies = true,
            replyCount = 1
        ) {}
        AmityCommentViewReplyBar(
            isViewAllReplies = false,
            isReplyComment = true,
            replyCount = 6
        ) {}
    }
}