package com.amity.socialcloud.uikit.community.compose.comment.query.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalContext
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
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.trackBranchPosition
import com.amity.socialcloud.uikit.community.compose.post.detail.bounceEffect

@Composable
fun AmityReplyCommentListView(
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
    showBounceEffect: Boolean = false,
    showEngagementRow: Boolean,
    replies: List<AmityComment>,
    previewLines: Int = EXPANDABLE_TEXT_MAX_LINES,
    isL2Thread: Boolean = false,
    threadLineState: ThreadLineState? = null,
    onEdit: (String?) -> Unit,
    onReply: (String) -> Unit,
    fromNonMemberCommunity: Boolean = false,
) {
    val context = LocalContext.current
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    val commentViewModel =
        viewModel<AmityCommentTrayComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    // Ensure the loader exists & is subscribed (ViewModel caches it)
    remember(commentId) {
        commentViewModel.getOrCreateReplyLoader(
            referenceId = referenceId,
            referenceType = referenceType,
            parentCommentId = commentId,
            includeDeleted = includeDeleted,
            isL2Thread = isL2Thread,
            replyCount = replyCount
        )
    }

    val replyCommentsMap by commentViewModel.replyComments.collectAsState()
    val replyShowLoadMoreMap by commentViewModel.replyShowLoadMore.collectAsState()
    val replyLoadingMap by commentViewModel.replyLoading.collectAsState()
    val creatingReplySet by commentViewModel.creatingReplyForParent.collectAsState()
    val optimisticIdsMap by commentViewModel.optimisticCommentIds.collectAsState()

    val commentsRaw = remember(replyCommentsMap[commentId]) {
        replyCommentsMap[commentId] ?: emptyList()
    }
    val shouldShowLoadMoreButton = replyShowLoadMoreMap[commentId] ?: false
    val isLoadingReplies = replyLoadingMap[commentId] ?: false
    val isCreatingReply = commentId in creatingReplySet

    val hasOptimisticReplies = optimisticIdsMap[commentId].orEmpty().isNotEmpty()

    val comments = remember(commentsRaw, replyTargetId, hasOptimisticReplies) {
        if (replyTargetId != null && !hasOptimisticReplies) {
            commentsRaw.sortedByDescending { it.getCommentId() == replyTargetId }
        } else {
            commentsRaw
        }
    }
    
    val allRepliesDeleted = remember(comments) {
        comments.isNotEmpty() && comments.all { it.isDeleted() }
    }

    val replyUnavailableSet by commentViewModel.replyUnavailable.collectAsState()
    val replyUnavailable = commentId in replyUnavailableSet
    var showOtherL2Replies by remember { mutableStateOf(false) }

    LaunchedEffect(replyUnavailable) {
        if (replyUnavailable) {
            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                context.getString(R.string.amity_reply_no_longer_available_error_message)
            )
            commentViewModel.clearReplyUnavailable(commentId)
        }
    }

    val density = LocalDensity.current

    Column {
        if (!allRepliesDeleted) {
            // Reset positions when comment list changes
            val commentIdKey = remember(comments) {
                comments.joinToString(",") { it.getCommentId() }
            }
            LaunchedEffect(commentIdKey) {
                threadLineState?.branchYPositions?.clear()
            }

            if (isL2Thread && !showOtherL2Replies) {
                comments.find { it.getCommentId() == replyTargetId }?.let { comment ->
                    Box(
                        modifier = if (threadLineState != null) {
                            Modifier.trackBranchPosition(threadLineState, 0, density)
                        } else Modifier
                    ) {
                        AmityReplyCommentView(
                            modifier = modifier,
                            shouldHighlight = true,
                            componentScope = componentScope,
                            allowInteraction = allowInteraction,
                            referenceId = referenceId,
                            referenceType = referenceType,
                            currentUserId = currentUserId,
                            editingCommentId = editingCommentId,
                            showEngagementRow = showEngagementRow,
                            comment = comment,
                            isL2Comment = true,
                            previewLines = previewLines,
                            onEdit = onEdit,
                            fromNonMemberCommunity = fromNonMemberCommunity,
                            onReply = onReply
                        )
                    }
                }
            } else {
                comments.forEachIndexed { index, comment ->
                    key(comment.getCommentId()) {
                        Box(
                            modifier = if (threadLineState != null) {
                                Modifier.trackBranchPosition(threadLineState, index, density)
                            } else Modifier
                        ) {
                            // L1 bounce: only when targeting this specific L1 and no L2 target
                            val shouldBounce = showBounceEffect
                                    && replyTargetId != null
                                    && l2TargetId == null
                                    && !isL2Thread
                                    && comment.getCommentId() == replyTargetId

                            // L2 highlight: only in an L2 thread targeting a specific L2
                            val shouldHighlight = showBounceEffect
                                    && replyTargetId != null
                                    && isL2Thread
                                    && comment.getCommentId() == replyTargetId

                            // Is this L1 the parent of the L2 target?
                            val isL2Parent = l2TargetId != null
                                    && comment.getCommentId() == replyTargetId

                            AmityReplyCommentView(
                                modifier = when {
                                    shouldBounce -> modifier.bounceEffect()
                                    else -> modifier
                                },
                                shouldHighlight = shouldHighlight,
                                componentScope = componentScope,
                                allowInteraction = allowInteraction,
                                referenceId = referenceId,
                                referenceType = referenceType,
                                currentUserId = currentUserId,
                                editingCommentId = editingCommentId,
                                showEngagementRow = showEngagementRow,
                                comment = comment,
                                isL2Comment = isL2Thread,
                                previewLines = previewLines,
                                onEdit = onEdit,
                                replyTargetId = if (isL2Parent) l2TargetId else null,
                                showBounceEffect = if (isL2Parent) showBounceEffect else false,
                                expandReplies = isL2Parent,
                                fromNonMemberCommunity = fromNonMemberCommunity,
                                onReply = onReply
                            )
                        }
                    }
                }
            }

        }

        if (isLoadingReplies || isCreatingReply) {
            AmityCommentItemShimmer(
                modifier = modifier.padding(top = 5.dp)
            )
        } else if (shouldShowLoadMoreButton || (isL2Thread && !showOtherL2Replies)) {
            Box {
                AmityCommentViewReplyBar(
                    modifier = modifier.padding(top = 5.dp),
                    isViewAllReplies = true,
                    replyCount = 0,
                    isReplyComment = isL2Thread,
                ) {
                    if (isL2Thread && !showOtherL2Replies) {
                        showOtherL2Replies = true
                    } else {
                        commentViewModel.loadMoreReplies(commentId)
                    }
                }
            }
        }
    }
}
