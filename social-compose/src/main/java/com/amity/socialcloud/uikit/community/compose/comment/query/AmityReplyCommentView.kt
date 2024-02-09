package com.amity.socialcloud.uikit.community.compose.comment.query

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.utils.isCommunityModerator

@Composable
fun AmityReplyCommentView(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    allowInteraction: Boolean,
    storyId: String,
    currentUserId: String,
    editingCommentId: String?,
    comment: AmityComment,
    onEdit: (String?) -> Unit,
) {
    if (comment.isDeleted()) {
        AmityDeletedCommentView(isReplyComment = true)
    } else {
        val commentText = if (comment.getDataTypes().contains(AmityComment.DataType.TEXT)) {
            (comment.getData() as? AmityComment.Data.TEXT)?.getText() ?: ""
        } else {
            ""
        }

        AmitySingleCommentView(
            modifier = modifier,
            componentScope = componentScope,
            allowInteraction = allowInteraction,
            storyId = storyId,
            isReplyComment = true,
            currentUserId = currentUserId,
            commentId = comment.getCommentId(),
            commentText = commentText,
            creatorId = comment.getCreatorId(),
            editingCommentId = editingCommentId,
            creatorAvatarUrl = comment.getCreator()?.getAvatar()?.getUrl() ?: "",
            creatorDisplayName = comment.getCreator()?.getDisplayName() ?: "",
            createdAt = comment.getCreatedAt(),
            isEdited = comment.isEdited(),
            isCommunityModerator = comment.isCommunityModerator(),
            isFlaggedByMe = comment.isFlaggedByMe(),
            isReactedByMe = comment.getMyReactions().isNotEmpty(),
            reactionCount = comment.getReactionCount(),
            childCount = null,
            replyComments = emptyList(),
            onReply = {},
            onEdit = onEdit,
        )
    }
}
