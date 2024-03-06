package com.amity.socialcloud.uikit.community.compose.comment.query

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.utils.isCommunityModerator
import com.google.gson.JsonObject

@Composable
fun AmityReplyCommentView(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    allowInteraction: Boolean,
    reference: AmityComment.Reference,
    currentUserId: String,
    editingCommentId: String?,
    comment: AmityComment,
    onEdit: (String?) -> Unit,
) {
    if (comment.isDeleted()) {
        AmityDeletedCommentView(isReplyComment = true)
    } else {
        val mentionGetter = AmityMentionMetadataGetter(comment.getMetadata() ?: JsonObject())
        val commentText = (comment.getData() as? AmityComment.Data.TEXT)?.getText() ?: ""

        AmitySingleCommentView(
            modifier = modifier,
            componentScope = componentScope,
            allowInteraction = allowInteraction,
            reference = reference,
            isReplyComment = true,
            currentUserId = currentUserId,
            commentId = comment.getCommentId(),
            editingCommentId = editingCommentId,
            commentText = commentText,
            mentionGetter = mentionGetter,
            mentionees = comment.getMentionees(),
            creatorId = comment.getCreatorId(),
            creatorAvatarUrl = comment.getCreator()?.getAvatar()?.getUrl() ?: "",
            creatorDisplayName = comment.getCreator()?.getDisplayName() ?: "",
            createdAt = comment.getCreatedAt(),
            isEdited = comment.isEdited(),
            isCommunityModerator = comment.isCommunityModerator(),
            isReactedByMe = comment.getMyReactions().isNotEmpty(),
            isFlaggedByMe = comment.isFlaggedByMe(),
            isFailed = comment.getState() == AmityComment.State.FAILED,
            reactionCount = comment.getReactionCount(),
            childCount = null,
            replyComments = emptyList(),
            onReply = {},
            onEdit = onEdit,
        )
    }
}
