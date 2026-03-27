package com.amity.socialcloud.uikit.community.compose.comment.query.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.EXPANDABLE_TEXT_MAX_LINES
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.comment.query.AmityReplyCommentView
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.AmityCommentItemShimmer
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.AmityCommentViewReplyBar
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.ThreadLineState
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.trackCollapsedBarPositionAtIndex
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.trackBranchPosition
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.trackSingleReplyPosition
import com.amity.socialcloud.uikit.community.compose.post.detail.bounceEffect

@Composable
fun AmityReplyCommentContainer(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    allowInteraction: Boolean,
    referenceId: String,
    referenceType: AmityCommentReferenceType,
    currentUserId: String,
    commentId: String,
    editingCommentId: String?,
    includeDeleted: Boolean = false,
    replyCount: Int,
    replyTargetId: String? = null,
    l2TargetId: String? = null,
    showEngagementRow: Boolean,
    replies: List<AmityComment>,
    onEdit: (String?) -> Unit,
    previewLines: Int = EXPANDABLE_TEXT_MAX_LINES,
    showBounceEffect: Boolean = false,
    isExpanded: Boolean = false,
    isL2Thread: Boolean = false,
    threadLineState: ThreadLineState? = null,
    fromNonMemberCommunity: Boolean = false,
    onReply: (String) -> Unit,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    val commentViewModel =
        viewModel<AmityCommentTrayComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val context = LocalContext.current
    var shouldShowReplies by rememberSaveable { mutableStateOf(isExpanded) }
    var hideAfterError by rememberSaveable { mutableStateOf(false) }

    val replyLoadErrorSet by commentViewModel.replyLoadErrors.collectAsState()
    LaunchedEffect(replyLoadErrorSet.contains(commentId)) {
        if (replyLoadErrorSet.contains(commentId)) {
            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                context.getString(R.string.amity_no_internet_error_message)
            )
            commentViewModel.clearReplyLoadError(commentId)
            shouldShowReplies = false
            hideAfterError = true
        }
    }

    // Check if a reply is currently being created for this parent
    val creatingSet by commentViewModel.creatingReplyForParent.collectAsState()
    val isCreatingReply = commentId in creatingSet

    // Get optimistic (newly created) comments for this parent while collapsed
    val optimisticIds by commentViewModel.optimisticCommentIds.collectAsState()
    val replyCommentsMap by commentViewModel.replyComments.collectAsState()
    val newReplies = if (!shouldShowReplies) {
        val ids = optimisticIds[commentId].orEmpty().toSet()
        if (ids.isNotEmpty()) {
            replyCommentsMap[commentId]?.filter { it.getCommentId() in ids } ?: emptyList()
        } else emptyList()
    } else emptyList()

    if (shouldShowReplies) {
        AmityReplyCommentListView(
            modifier = modifier,
            componentScope = componentScope,
            allowInteraction = allowInteraction,
            referenceId = referenceId,
            referenceType = referenceType,
            currentUserId = currentUserId,
            commentId = commentId,
            editingCommentId = editingCommentId,
            includeDeleted = includeDeleted,
            replyCount = replyCount,
            replyTargetId = replyTargetId,
            l2TargetId = l2TargetId,
            showBounceEffect = showBounceEffect,
            showEngagementRow = showEngagementRow,
            replies = replies,
            previewLines = previewLines,
            isL2Thread = isL2Thread,
            threadLineState = threadLineState,
            onEdit = onEdit,
            fromNonMemberCommunity = fromNonMemberCommunity,
            onReply = onReply
        )
    } else if (!hideAfterError && (replyCount > 0 || isCreatingReply || newReplies.isNotEmpty())) {
        val density = LocalDensity.current
        val remainingReplyCount = replyCount - newReplies.size

        Column {
            if (replyTargetId != null) {
                val reply = replies.firstOrNull { it.getCommentId() == replyTargetId }
                reply?.let {
                    Box(
                        modifier = if (threadLineState != null) {
                            Modifier.trackSingleReplyPosition(threadLineState, density)
                        } else Modifier
                    ) {
                        AmityReplyCommentView(
                            modifier = modifier.let { if (showBounceEffect) it.bounceEffect() else it },
                            componentScope = componentScope,
                            allowInteraction = allowInteraction,
                            referenceId = referenceId,
                            referenceType = referenceType,
                            currentUserId = currentUserId,
                            editingCommentId = editingCommentId,
                            comment = it,
                            isL2Comment = isL2Thread,
                            onEdit = onEdit,
                            replyCount = replyCount,
                            previewLines = previewLines,
                            showEngagementRow = showEngagementRow,
                            shouldShowReplies = {
                                shouldShowReplies = it
                            },
                            fromNonMemberCommunity = fromNonMemberCommunity,
                            onReply = onReply
                        )
                    }
                }
            } else {
                newReplies.forEachIndexed { index, newComment ->
                    Box(
                        modifier = if (threadLineState != null) {
                            Modifier.trackBranchPosition(threadLineState, index, density)
                        } else Modifier
                    ) {
                        AmityReplyCommentView(
                            modifier = modifier,
                            componentScope = componentScope,
                            allowInteraction = allowInteraction,
                            referenceId = referenceId,
                            referenceType = referenceType,
                            currentUserId = currentUserId,
                            editingCommentId = editingCommentId,
                            showEngagementRow = showEngagementRow,
                            comment = newComment,
                            isL2Comment = isL2Thread,
                            previewLines = previewLines,
                            onEdit = onEdit,
                            fromNonMemberCommunity = fromNonMemberCommunity,
                            onReply = onReply
                        )
                    }
                }

                if (isCreatingReply) {
                    AmityCommentItemShimmer(
                        modifier = modifier.padding(top = 5.dp)
                    )
                }

                if (replyCount > 0) {
                    Box(
                        modifier = if (newReplies.isNotEmpty()) {
                            Modifier
                        } else if (threadLineState != null) {
                            Modifier.trackCollapsedBarPositionAtIndex(threadLineState, newReplies.size)
                        } else Modifier
                    ) {
                        AmityCommentViewReplyBar(
                            modifier = modifier.padding(top = 5.dp),
                            isViewAllReplies = newReplies.isNotEmpty(),
                            replyCount = remainingReplyCount,
                            isReplyComment = isL2Thread,
                        ) {
                            shouldShowReplies = true
                        }
                    }
                }
            }
        }
    }
}