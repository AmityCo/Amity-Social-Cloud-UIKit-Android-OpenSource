package com.amity.socialcloud.uikit.community.compose.comment.query

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.AmityCommentViewReplyBar

@Composable
fun AmityReplyCommentView(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    allowInteraction: Boolean,
    referenceId: String,
    referenceType: AmityCommentReferenceType,
    currentUserId: String,
    editingCommentId: String?,
    comment: AmityComment,
    onEdit: (String?) -> Unit,
    replyCount: Int? = null,
    shouldShowReplies: (Boolean) -> Unit = {},
) {
    if (comment.isDeleted()) {
        AmityDeletedCommentView(modifier = modifier, isReplyComment = true, replyCount = replyCount)

        replyCount?.let {
            if (replyCount - 1 > 0) {
                AmityCommentViewReplyBar(
                    modifier = modifier,
                    isViewAllReplies = true,
                    replyCount = replyCount - 1,
                ) {
                    shouldShowReplies(true)
                }
            }
        }
    } else {
        AmitySingleCommentView(
            modifier = modifier,
            componentScope = componentScope,
            allowInteraction = allowInteraction,
            comment = comment,
            referenceId = referenceId,
            referenceType = referenceType,
            isReplyComment = true,
            currentUserId = currentUserId,
            editingCommentId = editingCommentId,
            onReply = {},
            onEdit = onEdit,
            replyCount = replyCount,
            shouldShowReplies = shouldShowReplies,
        )
    }
}
