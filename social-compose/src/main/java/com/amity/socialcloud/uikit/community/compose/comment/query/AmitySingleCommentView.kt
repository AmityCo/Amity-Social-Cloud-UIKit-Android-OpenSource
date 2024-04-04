package com.amity.socialcloud.uikit.community.compose.comment.query

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.elements.AmityCommentAvatarView
import com.amity.socialcloud.uikit.community.compose.comment.query.components.AmityCommentActionsBottomSheet
import com.amity.socialcloud.uikit.community.compose.comment.query.components.AmityCommentContentContainer
import com.amity.socialcloud.uikit.community.compose.comment.query.components.AmityCommentEngagementBar
import com.amity.socialcloud.uikit.community.compose.comment.query.components.AmityEditCommentContainer
import com.amity.socialcloud.uikit.community.compose.comment.query.components.AmityReplyCommentContainer
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.utils.clickableWithoutRipple
import com.google.gson.JsonObject
import org.joda.time.DateTime
import kotlin.math.max

@Composable
fun AmitySingleCommentView(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    allowInteraction: Boolean,
    reference: AmityComment.Reference,
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
    isFailed: Boolean,
    reactionCount: Int,
    childCount: Int?,
    replyComments: List<AmityComment>,
    onReply: (String) -> Unit,
    onEdit: (String?) -> Unit,
) {
    var showCommentActionSheet by remember { mutableStateOf(false) }

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
            .testTag("comment_list/*")
    ) {
        AmityCommentAvatarView(
            size = 32.dp,
            avatarUrl = creatorAvatarUrl,
            modifier = modifier.testTag("comment_list/comment_bubble_avatar")
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
                    mentionGetter = mentionGetter,
                    mentionees = mentionees,
                    onEditFinished = {
                        onEdit(null)
                    },
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    AmityCommentContentContainer(
                        modifier = modifier,
                        displayName = creatorDisplayName,
                        isCommunityModerator = isCommunityModerator,
                        commentText = commentText,
                        mentionGetter = mentionGetter,
                        mentionees = mentionees,
                    )

                    if (isFailed) {
                        Icon(
                            painter = painterResource(id = R.drawable.amity_ic_error),
                            tint = AmityTheme.colors.baseShade2,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(18.dp)
                                .align(Alignment.Bottom)
                                .clickableWithoutRipple {
                                    showCommentActionSheet = true
                                }
                        )
                    }
                }

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
                reference = reference,
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

    AmityCommentActionsBottomSheet(
        modifier = modifier,
        componentScope = componentScope,
        commentId = commentId,
        shouldShow = showCommentActionSheet,
        isReplyComment = isReplyComment,
        isCommentCreatedByMe = true,
        isFlaggedByMe = isFlaggedByMe,
        isFailed = isFailed,
        onEdit = {},
    ) {
        showCommentActionSheet = false
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AmityCommentViewPreview() {
    AmitySingleCommentView(
        allowInteraction = true,
        reference = AmityComment.Reference.STORY(""),
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
        isFailed = true,
        reactionCount = 1,
        childCount = 10,
        replyComments = emptyList(),
        onReply = {},
        onEdit = {},
    )
}