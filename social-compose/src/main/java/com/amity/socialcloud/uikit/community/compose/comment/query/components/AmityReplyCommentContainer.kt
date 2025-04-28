package com.amity.socialcloud.uikit.community.compose.comment.query.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.comment.query.AmityReplyCommentView
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.AmityCommentViewReplyBar
import com.amity.socialcloud.uikit.community.compose.post.detail.bounceEffect

@Composable
fun AmityReplyCommentContainer(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    allowInteraction: Boolean,
    referenceId: String,
    referenceType: AmityCommentReferenceType,
    currentUserId: String,
    commentId: String,
    editingCommentId: String?,
    replyCount: Int,
    replyTargetId: String? = null,
    replies: List<AmityComment>,
    onEdit: (String?) -> Unit,
    showBounceEffect: Boolean = false,
) {
    var shouldShowReplies by rememberSaveable { mutableStateOf(false) }

    if (shouldShowReplies) {
        AmityReplyCommentListView(
            modifier = modifier,
            componentScope = componentScope,
            allowInteraction = allowInteraction,
            referenceId = referenceId,
            referenceType = referenceType,
            currentUserId = currentUserId,
            commentId = commentId,
            editingCommentId = editingCommentId,
            replyCount = replyCount,
            replyTargetId = replyTargetId,
            replies = replies,
            onEdit = onEdit,
        )
    } else if (replyCount > 0) {
        if (replyTargetId != null) {
            val reply = replies.firstOrNull { it.getCommentId() == replyTargetId }
            reply?.let {
                AmityReplyCommentView(
                    modifier = modifier.let { if (showBounceEffect) it.bounceEffect() else it },
                    componentScope = componentScope,
                    allowInteraction = allowInteraction,
                    referenceId = referenceId,
                    referenceType = referenceType,
                    currentUserId = currentUserId,
                    editingCommentId = editingCommentId,
                    comment = it,
                    onEdit = onEdit,
                    replyCount = replyCount,
                    shouldShowReplies = {
                        shouldShowReplies = it
                    }
                )
            }
        } else {
            AmityCommentViewReplyBar(
                modifier = modifier,
                isViewAllReplies = false,
                replyCount = replyCount,
            ) {
                shouldShowReplies = true
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AmityReplyCommentContainerPreview() {
    AmityReplyCommentContainer(
        allowInteraction = true,
        referenceId = "",
        referenceType = AmityCommentReferenceType.POST,
        currentUserId = "",
        commentId = "",
        editingCommentId = null,
        replyCount = 3,
        replies = emptyList(),
        onEdit = {},
    )
}