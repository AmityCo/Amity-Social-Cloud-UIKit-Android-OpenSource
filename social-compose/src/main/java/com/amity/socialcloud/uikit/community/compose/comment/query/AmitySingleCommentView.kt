package com.amity.socialcloud.uikit.community.compose.comment.query

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.uikit.common.reaction.AmityReactionList
import com.amity.socialcloud.uikit.common.reaction.preview.AmityReactionPreview
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.EXPANDABLE_TEXT_MAX_LINES
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.query.components.AmityCommentContentContainer
import com.amity.socialcloud.uikit.community.compose.comment.query.components.AmityCommentEngagementBar
import com.amity.socialcloud.uikit.community.compose.comment.query.components.AmityEditCommentContainer
import com.amity.socialcloud.uikit.community.compose.comment.query.components.AmityReplyCommentContainer
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.AmityCommentViewReplyBar
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.math.max

@Composable
fun AmitySingleCommentView(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    allowInteraction: Boolean,
    referenceId: String,
    referenceType: AmityCommentReferenceType,
    comment: AmityComment,
    isReplyComment: Boolean,
    currentUserId: String,
    editingCommentId: String?,
    includeDeleted: Boolean = true,
    showEngagementRow: Boolean,
    onReply: (String) -> Unit,
    onEdit: (String?) -> Unit,
    replyTargetId: String? = null,
    showBounceEffect: Boolean = false,
    replyCount: Int? = null,
    shouldShowReplies: (Boolean) -> Unit = {},
    allowAction: Boolean = true,
    previewLines: Int = EXPANDABLE_TEXT_MAX_LINES,
    expandReplies: Boolean = false,
    fromNonMemberCommunity: Boolean,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.commentTrayComponentBehavior
    }
    val commentBehavior = remember {
        AmitySocialBehaviorHelper.notificationTrayPageBehavior
    }
    var showDeleteBannedWordCommentDialog by remember { mutableStateOf(false) }
    var showReactionListSheet by remember { mutableStateOf(false) }

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
        //User avatar
        AmityUserAvatarView(
            user = comment.getCreator(),
            modifier = modifier
                .clickableWithoutRipple {
                    behavior.goToUserProfilePage(context, comment.getCreatorId())
                }
                .testTag("comment_list/comment_bubble_avatar")
        )

        //Comment content
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.wrapContentSize(),
        ) {
            if (editingCommentId == comment.getCommentId()) {
                AmityEditCommentContainer(
                    modifier = modifier,
                    componentScope = componentScope,
                    comment = comment,
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
                        comment = comment,
                        previewLines = previewLines,
                        onClick = {
                            if (!allowAction) {
                                if (referenceType == AmityCommentReferenceType.POST) {
                                    commentBehavior.goToPostDetailPage(
                                        context = context,
                                        postId = referenceId,
                                        commentId = comment.getCommentId(),
                                        parentId = comment.getParentId(),
                                    )
                                }
                            }
                        }
                    )

                    if (comment.getState() == AmityComment.State.FAILED) {
                        Icon(
                            painter = painterResource(id = R.drawable.amity_ic_error),
                            tint = AmityTheme.colors.baseShade2,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(18.dp)
                                .align(Alignment.Bottom)
                                .clickableWithoutRipple {
                                    showDeleteBannedWordCommentDialog = true
                                }
                        )
                    }
                }
                if (comment.getReactionCount() > 0) {
                    Layout(
                        content = {
                            AmityReactionPreview(
                                modifier = Modifier
                                    .offset(y = (-16).dp)
                                    .clickableWithoutRipple {
                                        if (AmityCoreClient.isVisitor()) {
                                            behavior.handleVisitorUserAction()
                                        } else {
                                            showReactionListSheet = true
                                        }
                                    },
                                componentScope = componentScope,
                                myReaction = comment.getMyReactions().firstOrNull(),
                                reactions = comment.getReactionMap(),
                                reactionCount = comment.getReactionCount(),
                                showContainer = true,
                            )
                        },
                        measurePolicy = { measurables, constraints ->
                            // Measure each child
                            val placeables = measurables.map { measurable ->
                                measurable.measure(constraints)
                            }

                            // Determine the height of the item
                            val height =
                                Math.max(placeables.sumOf { it.height } - (8.dp.roundToPx()), 0)

                            // Set the size of the item
                            layout(
                                height = height,
                                width = placeables.maxOfOrNull { it.width } ?: 0) {
                                var xPosition = 0
                                placeables.forEach { placeable ->
                                    placeable.placeRelative(IntOffset(xPosition, 0))
                                    xPosition += placeable.width
                                }
                            }
                        }
                    )
                }

                // TODO: 17/6/24 enable comment link preview once ready
                /*
                AmityCommentPreviewLinkView(
                    modifier = modifier,
                    comment = comment,
                )
                 */

                if (showEngagementRow) {
                    AmityCommentEngagementBar(
                        modifier = modifier,
                        componentScope = componentScope,
                        allowInteraction = allowInteraction,
                        allowAction = allowAction,
                        isReplyComment = isReplyComment,
                        comment = comment,
                        isCreatedByMe = currentUserId == comment.getCreatorId(),
                        fromNonMemberCommunity = fromNonMemberCommunity,
                        onReply = onReply,
                        onEdit = {
                            onEdit(comment.getCommentId())
                        }
                    )
                }
            }

            AmityReplyCommentContainer(
                modifier = if (allowAction) {
                    modifier
                } else {
                    modifier.clickableWithoutRipple {
                        if (referenceType == AmityCommentReferenceType.POST) {
                            val latestReply = comment.getLatestReplies().firstOrNull()
                            commentBehavior.goToPostDetailPage(
                                context = context,
                                postId = referenceId,
                                commentId = latestReply?.getCommentId() ?: comment.getCommentId(),
                                parentId = latestReply?.getParentId(),
                            )
                        }
                    }
                },
                componentScope = componentScope,
                allowInteraction = allowInteraction,
                referenceId = referenceId,
                referenceType = referenceType,
                currentUserId = currentUserId,
                commentId = comment.getCommentId(),
                editingCommentId = editingCommentId,
                includeDeleted = includeDeleted,
                replyCount = max(comment.getChildCount(), comment.getLatestReplies().let { if(includeDeleted) it else it.filter { it.isDeleted() == false } }.size),
                showEngagementRow = showEngagementRow,
                replies = comment.getLatestReplies(),
                onEdit = onEdit,
                replyTargetId = replyTargetId,
                showBounceEffect = false,
                previewLines = previewLines,
                isExpanded = expandReplies,
                fromNonMemberCommunity = fromNonMemberCommunity,
            )

            if (isReplyComment) {
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
            }
        }
    }

    if (showDeleteBannedWordCommentDialog) {
        AmityAlertDialog(
            dialogTitle = "",
            dialogText = "Your comment was not post",
            confirmText = "Delete",
            dismissText = "Cancel",
            confirmTextColor = AmityTheme.colors.alert,
            dismissTextColor = AmityTheme.colors.highlight,
            onConfirmation = {
                AmitySocialClient.newCommentRepository()
                    .softDeleteComment(comment.getCommentId())
                    .subscribeOn(Schedulers.io())
                    .subscribe()
                showDeleteBannedWordCommentDialog = false
            },
            onDismissRequest = { showDeleteBannedWordCommentDialog = false }
        )
    }

    if (showReactionListSheet) {
        AmityReactionList(
            modifier = modifier,
            referenceType = AmityReactionReferenceType.COMMENT,
            referenceId = comment.getCommentId(),
            onClose = {
                showReactionListSheet = false
            },
            onUserClick = {
                behavior.goToUserProfilePage(
                    context = context,
                    userId = it,
                )
            }
        )
    }
}