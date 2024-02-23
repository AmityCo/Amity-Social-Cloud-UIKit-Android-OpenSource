package com.amity.socialcloud.uikit.community.compose.comment.query

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.community.compose.comment.elements.AmityCommentAvatarView
import com.amity.socialcloud.uikit.community.compose.comment.query.coponents.AmityCommentContentContainer
import com.amity.socialcloud.uikit.community.compose.comment.query.coponents.AmityCommentEngagementBar
import com.amity.socialcloud.uikit.community.compose.comment.query.coponents.AmityEditCommentContainer
import com.amity.socialcloud.uikit.community.compose.comment.query.coponents.AmityReplyCommentContainer
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope
import com.google.gson.JsonObject
import org.joda.time.DateTime
import kotlin.math.max

@Composable
fun AmitySingleCommentView(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    allowInteraction: Boolean,
    storyId: String,
    isReplyComment: Boolean,
    currentUserId: String,
    commentId: String,
    editingCommentId: String?,
    commentText: String,
    mentionGetter: AmityMentionMetadataGetter,
    mentionees: List<AmityMentionee>,
    creatorId: String,
    creatorAvatarUrl: String,
    creatorDisplayName: String,
    createdAt: DateTime,
    isEdited: Boolean,
    isCommunityModerator: Boolean,
    isReactedByMe: Boolean,
    isFlaggedByMe: Boolean,
    reactionCount: Int,
    childCount: Int?,
    replyComments: List<AmityComment>,
    onReply: (String) -> Unit,
    onEdit: (String?) -> Unit,
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                top = 4.dp,
                bottom = 8.dp,
                start = 12.dp,
                end = if (isReplyComment) 0.dp else 12.dp
            )
    ) {
        AmityCommentAvatarView(
            size = 32.dp,
            avatarUrl = creatorAvatarUrl
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.wrapContentSize(),
        ) {
            if (editingCommentId == commentId) {
                AmityEditCommentContainer(
                    modifier = modifier,
                    componentScope = componentScope,
                    commentId = commentId,
                    commentText = commentText,
                    onEditFinished = {
                        onEdit(null)
                    },
                )
            } else {
                AmityCommentContentContainer(
                    modifier = modifier,
                    displayName = creatorDisplayName,
                    isCommunityModerator = isCommunityModerator,
                    commentText = commentText,
                    mentionGetter = mentionGetter,
                    mentionees = mentionees,
                )
                AmityCommentEngagementBar(
                    modifier = modifier,
                    componentScope = componentScope,
                    allowInteraction = allowInteraction,
                    isReplyComment = isReplyComment,
                    commentId = commentId,
                    createdAt = createdAt,
                    isEdited = isEdited,
                    isCreatedByMe = currentUserId == creatorId,
                    isFlaggedByMe = isFlaggedByMe,
                    isReactedByMe = isReactedByMe,
                    reactionCount = reactionCount,
                    onReply = onReply,
                    onEdit = {
                        onEdit(commentId)
                    }
                )
            }

            AmityReplyCommentContainer(
                modifier = modifier,
                componentScope = componentScope,
                allowInteraction = allowInteraction,
                storyId = storyId,
                currentUserId = currentUserId,
                commentId = commentId,
                editingCommentId = editingCommentId,
                // TODO: 31/1/24 child count is not real-time
                replyCount = max(childCount ?: 0, replyComments.size),
                replies = replyComments,
                onEdit = onEdit,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AmityCommentViewPreview() {
    AmitySingleCommentView(
        allowInteraction = true,
        storyId = "",
        isReplyComment = false,
        currentUserId = "",
        commentId = "",
        editingCommentId = null,
        commentText = "This is a comment",
        mentionGetter = AmityMentionMetadataGetter(JsonObject()),
        mentionees = emptyList(),
        creatorId = "",
        creatorAvatarUrl = "",
        creatorDisplayName = "John Doe",
        createdAt = DateTime.now(),
        isEdited = true,
        isCommunityModerator = true,
        isReactedByMe = true,
        isFlaggedByMe = false,
        reactionCount = 1,
        childCount = 10,
        replyComments = emptyList(),
        onReply = {},
        onEdit = {},
    )
}