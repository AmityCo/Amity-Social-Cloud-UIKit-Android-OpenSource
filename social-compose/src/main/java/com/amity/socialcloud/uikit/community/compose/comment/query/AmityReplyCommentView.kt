package com.amity.socialcloud.uikit.community.compose.comment.query

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.uikit.common.ui.elements.EXPANDABLE_TEXT_MAX_LINES
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
    showEngagementRow: Boolean,
    comment: AmityComment,
    isL2Comment: Boolean = false,
    onReply: (String) -> Unit,
    onEdit: (String?) -> Unit,
    replyCount: Int? = null,
    previewLines: Int = EXPANDABLE_TEXT_MAX_LINES,
    allowAction: Boolean = true,
    shouldShowReplies: (Boolean) -> Unit = {},
    replyTargetId: String? = null,
    showBounceEffect: Boolean = false,
    expandReplies: Boolean = false,
    shouldHighlight: Boolean = false,
    fromNonMemberCommunity: Boolean = false,
) {
    if (comment.isDeleted()) {
        AmityDeletedCommentView(modifier = modifier, isReplyComment = true, replyCount = replyCount)

    } else {
        AmitySingleCommentView(
            modifier = modifier,
            componentScope = componentScope,
            allowInteraction = allowInteraction,
            comment = comment,
            referenceId = referenceId,
            referenceType = referenceType,
            isReplyComment = true,
            isL2Comment = isL2Comment,
            currentUserId = currentUserId,
            editingCommentId = editingCommentId,
            showEngagementRow = showEngagementRow,
            onReply = onReply,
            onEdit = onEdit,
            replyCount = replyCount,
            shouldShowReplies = shouldShowReplies,
            previewLines = previewLines,
            allowAction = allowAction,
            replyTargetId = replyTargetId,
            showBounceEffect = showBounceEffect,
            expandReplies = expandReplies,
            shouldHighlight = shouldHighlight,
            fromNonMemberCommunity = fromNonMemberCommunity,
        )
    }
}
