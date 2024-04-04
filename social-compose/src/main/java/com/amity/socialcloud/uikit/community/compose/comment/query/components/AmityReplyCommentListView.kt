package com.amity.socialcloud.uikit.community.compose.comment.query.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.community.compose.comment.query.AmityReplyCommentView
import com.amity.socialcloud.uikit.community.compose.comment.query.AmityStoryCommentReplyLoader
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.AmityCommentViewReplyBar
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope

@Composable
fun AmityReplyCommentListView(
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
    val loader = remember {
        AmityStoryCommentReplyLoader(reference, commentId).apply { load() }
    }

    val shouldShowLoadMoreButton by remember {
        loader.showLoadMoreButton()
    }.subscribeAsState(initial = true)

    val comments by remember {
        loader.getComments()
    }.subscribeAsState(initial = replies)

    Column {
        comments.forEach { comment ->
            AmityReplyCommentView(
                modifier = modifier,
                componentScope = componentScope,
                allowInteraction = allowInteraction,
                reference = reference,
                currentUserId = currentUserId,
                editingCommentId = editingCommentId,
                comment = comment,
                onEdit = onEdit,
            )
        }
    }

    if (replyCount > 5 && shouldShowLoadMoreButton) {
        AmityCommentViewReplyBar(
            modifier = modifier,
            isViewAllReplies = true,
            replyCount = 0,
        ) {
            loader.load()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AmityReplyCommentListViewPreview() {
    AmityReplyCommentListView(
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