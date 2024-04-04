package com.amity.socialcloud.uikit.community.compose.comment.query.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.uikit.common.common.readableTimeDiff
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.reaction.AmityReactionListBottomSheet
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.utils.clickableWithoutRipple
import org.joda.time.DateTime


@Composable
fun AmityCommentEngagementBar(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    allowInteraction: Boolean,
    isReplyComment: Boolean,
    commentId: String,
    createdAt: DateTime,
    isEdited: Boolean,
    isCreatedByMe: Boolean,
    isFlaggedByMe: Boolean,
    isReactedByMe: Boolean,
    reactionCount: Int,
    onReply: (String) -> Unit,
    onEdit: () -> Unit,
) {
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommentTrayComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    var isReacted by remember { mutableStateOf(isReactedByMe) }
    var localReactionCount by remember { mutableIntStateOf(reactionCount) }
    var showCommentActionSheet by remember { mutableStateOf(false) }
    var showReactionListSheet by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        if (allowInteraction) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = createdAt.readableTimeDiff() + if (isEdited) " (edited)" else "",
                    style = AmityTheme.typography.caption.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.baseShade2,
                    ),
                    modifier = modifier.testTag("comment_list/comment_bubble_timestamp")
                )

                Text(
                    text = context.getString(
                        if (isReacted) R.string.amity_liked
                        else R.string.amity_like
                    ),
                    style = AmityTheme.typography.caption.copy(
                        color = if (isReacted) AmityTheme.colors.primary
                        else AmityTheme.colors.baseShade2,
                    ),
                    modifier = modifier
                        .clickable {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            isReacted = !isReacted
                            if (isReacted) {
                                localReactionCount++
                                viewModel.addReaction(commentId)
                            } else {
                                localReactionCount--
                                viewModel.removeReaction(commentId)
                            }
                        }
                        .testTag("comment_list/comment_bubble_reaction_button")
                )

                if (!isReplyComment) {
                    Text(
                        text = context.getString(R.string.amity_reply),
                        style = AmityTheme.typography.caption.copy(
                            color = AmityTheme.colors.baseShade2,
                        ),
                        modifier = modifier
                            .clickable {
                                onReply(commentId)
                            }
                            .testTag("comment_list/comment_bubble_reply_button")
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.amity_ic_more_horiz),
                    contentDescription = null,
                    tint = AmityTheme.colors.secondaryShade2,
                    modifier = modifier
                        .size(20.dp)
                        .clickable {
                            showCommentActionSheet = true
                        }
                        .testTag("comment_list/comment_bubble_meat_balls_button")
                )
            }
        }

        if (localReactionCount > 0) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.clickableWithoutRipple {
                    showReactionListSheet = true
                }
            ) {
                Text(
                    text = localReactionCount.toString(),
                    style = AmityTheme.typography.caption.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.secondaryShade2,
                    ),
                    modifier = modifier.testTag("comment_list/comment_bubble_reaction_count_text_view")
                )

                Image(
                    painter = painterResource(id = R.drawable.amity_ic_story_liked_pressed),
                    contentDescription = null,
                    modifier = modifier.size(20.dp)
                )
            }
        }

        AmityCommentActionsBottomSheet(
            modifier = modifier,
            componentScope = componentScope,
            commentId = commentId,
            shouldShow = showCommentActionSheet,
            isReplyComment = isReplyComment,
            isCommentCreatedByMe = isCreatedByMe,
            isFlaggedByMe = isFlaggedByMe,
            isFailed = false,
            onEdit = onEdit,
        ) {
            showCommentActionSheet = false
        }

        if (showReactionListSheet) {
            AmityReactionListBottomSheet(
                modifier = modifier,
                referenceType = AmityReactionReferenceType.COMMENT,
                referenceId = commentId,
            ) {
                showReactionListSheet = false
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AmityCommentEngagementBarPreview() {
    AmityCommentEngagementBar(
        allowInteraction = true,
        isReplyComment = false,
        commentId = "",
        createdAt = DateTime.now(),
        isEdited = true,
        isCreatedByMe = true,
        isFlaggedByMe = false,
        isReactedByMe = true,
        reactionCount = 1,
        onReply = {},
        onEdit = {},
    )
}