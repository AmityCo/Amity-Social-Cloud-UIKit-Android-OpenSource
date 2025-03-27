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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.common.readableSocialTimeDiff
import com.amity.socialcloud.uikit.common.reaction.AmityReactionList
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel


@Composable
fun AmityCommentEngagementBar(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    allowInteraction: Boolean,
    isReplyComment: Boolean,
    comment: AmityComment,
    isCreatedByMe: Boolean,
    onReply: (String) -> Unit,
    onEdit: () -> Unit,
) {
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.commentTrayComponentBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommentTrayComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    var isReacted by remember { mutableStateOf(comment.getMyReactions().isNotEmpty()) }
    var localReactionCount by remember { mutableIntStateOf(comment.getReactionCount()) }
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
                    text = comment.getCreatedAt()
                        .readableSocialTimeDiff() + if (comment.isEdited()) " (edited)" else "",
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.baseShade2,
                    ),
                    modifier = modifier.testTag("comment_list/comment_bubble_timestamp")
                )

                Text(
                    text = context.getString(
                        R.string.amity_like
                    ),
                    style = AmityTheme.typography.captionLegacy.copy(
                        color = if (isReacted) AmityTheme.colors.primary
                        else AmityTheme.colors.baseShade2,
                    ),
                    modifier = modifier
                        .clickable {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            isReacted = !isReacted
                            if (isReacted) {
                                localReactionCount++
                                viewModel.addReaction(comment.getCommentId())
                            } else {
                                localReactionCount--
                                viewModel.removeReaction(comment.getCommentId())
                            }
                        }
                        .testTag("comment_list/comment_bubble_reaction_button")
                )

                if (!isReplyComment) {
                    Text(
                        text = context.getString(R.string.amity_reply),
                        style = AmityTheme.typography.captionLegacy.copy(
                            color = AmityTheme.colors.baseShade2,
                        ),
                        modifier = modifier
                            .clickable {
                                onReply(comment.getCommentId())
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
                    style = AmityTheme.typography.captionLegacy.copy(
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
            commentId = comment.getCommentId(),
            shouldShow = showCommentActionSheet,
            isReplyComment = isReplyComment,
            isCommentCreatedByMe = isCreatedByMe,
            isFlaggedByMe = comment.isFlaggedByMe(),
            isFailed = false,
            onEdit = onEdit,
        ) {
            showCommentActionSheet = false
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
}