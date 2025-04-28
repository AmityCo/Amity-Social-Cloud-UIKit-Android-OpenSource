package com.amity.socialcloud.uikit.community.compose.comment.query

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope

@Composable
fun AmityCommentView(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope,
    referenceId: String,
    referenceType: AmityCommentReferenceType,
    currentUserId: String,
    editingCommentId: String?,
    comment: AmityComment,
    allowInteraction: Boolean,
    onReply: (String) -> Unit,
    onEdit: (String?) -> Unit,
    replyTargetId: String? = null,
    showBounceEffect: Boolean = false,
) {
    if (comment.isDeleted()) {
        AmityDeletedCommentView(modifier = modifier, isReplyComment = false)
    } else {
        AmitySingleCommentView(
            modifier = modifier,
            componentScope = componentScope,
            allowInteraction = allowInteraction,
            comment = comment,
            referenceId = referenceId,
            referenceType = referenceType,
            isReplyComment = false,
            currentUserId = currentUserId,
            editingCommentId = editingCommentId,
            onReply = onReply,
            onEdit = onEdit,
            replyTargetId = replyTargetId,
            showBounceEffect = showBounceEffect
        )
    }
}