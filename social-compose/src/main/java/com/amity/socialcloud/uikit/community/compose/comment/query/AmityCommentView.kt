package com.amity.socialcloud.uikit.community.compose.comment.query

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.utils.isCommunityModerator
import com.google.gson.JsonObject

@Composable
fun AmityCommentView(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope,
    reference: AmityComment.Reference,
    currentUserId: String,
    editingCommentId: String?,
    comment: AmityComment,
    allowInteraction: Boolean,
    onReply: (String) -> Unit,
    onEdit: (String?) -> Unit,
) {
    if (comment.isDeleted()) {
        AmityDeletedCommentView(isReplyComment = false)
    } else {
        val mentionGetter = AmityMentionMetadataGetter(comment.getMetadata() ?: JsonObject())
        val commentText = (comment.getData() as? AmityComment.Data.TEXT)?.getText() ?: ""

        AmitySingleCommentView(
            modifier = modifier,
            componentScope = componentScope,
            allowInteraction = allowInteraction,
            reference = reference,
            isReplyComment = false,
            currentUserId = currentUserId,
            commentId = comment.getCommentId(),
            commentText = commentText,
            mentionGetter = mentionGetter,
            mentionees = comment.getMentionees(),
            creatorId = comment.getCreatorId(),
            editingCommentId = editingCommentId,
            creatorAvatarUrl = comment.getCreator()?.getAvatar()?.getUrl() ?: "",
            creatorDisplayName = comment.getCreator()?.getDisplayName() ?: "",
            createdAt = comment.getCreatedAt(),
            isEdited = comment.isEdited(),
            isCommunityModerator = comment.isCommunityModerator(),
            isFlaggedByMe = comment.isFlaggedByMe(),
            isReactedByMe = comment.getMyReactions().isNotEmpty(),
            isFailed = comment.getState() == AmityComment.State.FAILED,
            reactionCount = comment.getReactionCount(),
            childCount = comment.getChildCount(),
            replyComments = comment.getLatestReplies(),
            onReply = onReply,
            onEdit = onEdit,
        )
    }
}
