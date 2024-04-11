package com.amity.socialcloud.uikit.community.compose.comment.query.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.AmityCommentViewReplyBar
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope

@Composable
fun AmityReplyCommentContainer(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    allowInteraction: Boolean,
    reference: AmityComment.Reference,
    currentUserId: String,
    commentId: String,
    editingCommentId: String?,
    replyCount: Int,
    replies: List<AmityComment>,
    onEdit: (String?) -> Unit,
) {
    var shouldShowReplies by rememberSaveable { mutableStateOf(false) }

    if (shouldShowReplies) {
        AmityReplyCommentListView(
            modifier = modifier,
            componentScope = componentScope,
            allowInteraction = allowInteraction,
            reference = reference,
            currentUserId = currentUserId,
            commentId = commentId,
            editingCommentId = editingCommentId,
            replyCount = replyCount,
            replies = replies,
            onEdit = onEdit,
        )
    } else if (replyCount > 0) {
        AmityCommentViewReplyBar(
            modifier = modifier,
            isViewAllReplies = false,
            replyCount = replyCount,
        ) {
            shouldShowReplies = true
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AmityReplyCommentContainerPreview() {
    AmityReplyCommentContainer(
        allowInteraction = true,
        reference = AmityComment.Reference.STORY(""),
        currentUserId = "",
        commentId = "",
        editingCommentId = null,
        replyCount = 3,
        replies = emptyList(),
        onEdit = {},
    )
}