package com.amity.socialcloud.uikit.community.compose.comment.query.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.uikit.common.ui.elements.EXPANDABLE_TEXT_MAX_LINES
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.comment.query.AmityReplyCommentView
import com.amity.socialcloud.uikit.community.compose.comment.query.AmityStoryCommentReplyLoader
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.AmityCommentViewReplyBar

@Composable
fun AmityReplyCommentListView(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    allowInteraction: Boolean,
    referenceId: String,
    referenceType: AmityCommentReferenceType,
    currentUserId: String,
    commentId: String,
    editingCommentId: String?,
    includeDeleted: Boolean = true,
    replyCount: Int,
    replyTargetId: String? = null,
    showEngagementRow: Boolean,
    replies: List<AmityComment>,
    previewLines: Int = EXPANDABLE_TEXT_MAX_LINES,
    onEdit: (String?) -> Unit,
) {
    val loader = remember {
        AmityStoryCommentReplyLoader(
            referenceId = referenceId,
            referenceType = referenceType,
            parentCommentId = commentId,
            includeDeleted = includeDeleted
        ).apply { load() }
    }

    val shouldShowLoadMoreButton by remember {
        loader.showLoadMoreButton()
    }.subscribeAsState(initial = true)

    val commentsRaw by remember {
        loader.getComments()
    }.subscribeAsState(initial = replies.let { if(includeDeleted) it else it.filter { it.isDeleted() == false } })

    val comments = remember(commentsRaw, replyTargetId) {
        if (replyTargetId != null) {
            commentsRaw.sortedByDescending { it.getCommentId() == replyTargetId }
        } else {
            commentsRaw
        }
    }

    Column {
        comments.forEach { comment ->
            AmityReplyCommentView(
                modifier = modifier,
                componentScope = componentScope,
                allowInteraction = allowInteraction,
                referenceId = referenceId,
                referenceType = referenceType,
                currentUserId = currentUserId,
                editingCommentId = editingCommentId,
                showEngagementRow = showEngagementRow,
                comment = comment,
                previewLines = previewLines,
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
