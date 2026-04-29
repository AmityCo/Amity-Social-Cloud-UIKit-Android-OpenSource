package com.amity.socialcloud.uikit.community.compose.comment.query

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.uikit.common.ui.elements.EXPANDABLE_TEXT_MAX_LINES
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.comment.query.components.AmityReplyCommentContainer
import kotlin.math.max

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
    includeDeleted: Boolean = true,
    showEngagementRow: Boolean,
    isEventHost: Boolean = false,
    onReply: (String) -> Unit,
    onEdit: (String?) -> Unit,
    replyTargetId: String? = null,
    l2TargetId: String? = null,
    showBounceEffect: Boolean = false,
    previewLines: Int = EXPANDABLE_TEXT_MAX_LINES,
    allowAction: Boolean = true,
    expandReplies: Boolean = false,
    fromNonMemberCommunity: Boolean = false,
) {
    if (comment.isDeleted()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.wrapContentSize(),
        ) {
            AmityDeletedCommentView(
                modifier = modifier,
                isReplyComment = false,
                replyCount = comment.getChildCount()
            )

            // Still show replies under deleted L0 comments.
            Box(
                Modifier.padding(start = 44.dp)
            ) {
                AmityReplyCommentContainer(
                    modifier = modifier,
                    componentScope = componentScope,
                    allowInteraction = allowInteraction,
                    referenceId = referenceId,
                    referenceType = referenceType,
                    currentUserId = currentUserId,
                    commentId = comment.getCommentId(),
                    editingCommentId = editingCommentId,
                    includeDeleted = false,
                    replyCount = max(
                        comment.getChildCount(),
                        comment.getLatestReplies().filter { !it.isDeleted() && it.getState() != AmityComment.State.FAILED }.size
                    ),
                    showEngagementRow = showEngagementRow,
                    replies = comment.getLatestReplies(),
                    onEdit = onEdit,
                    replyTargetId = replyTargetId,
                    l2TargetId = l2TargetId,
                    showBounceEffect = showBounceEffect,
                    previewLines = previewLines,
                    isExpanded = expandReplies,
                    isL2Thread = false,
                    fromNonMemberCommunity = fromNonMemberCommunity,
                    onReply = onReply,
                )
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
            isReplyComment = false,
            currentUserId = currentUserId,
            editingCommentId = editingCommentId,
            includeDeleted = includeDeleted,
            showEngagementRow = showEngagementRow,
            isEventHost = isEventHost,
            onReply = onReply,
            onEdit = onEdit,
            replyTargetId = replyTargetId,
            l2TargetId = l2TargetId,
            showBounceEffect = showBounceEffect,
            previewLines = previewLines,
            allowAction = allowAction,
            expandReplies = expandReplies,
            fromNonMemberCommunity = fromNonMemberCommunity,
        )
    }
}