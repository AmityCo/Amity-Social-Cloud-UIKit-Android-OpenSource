package com.amity.socialcloud.uikit.community.compose.paging.comment

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.comment.query.AmityCommentAdView
import com.amity.socialcloud.uikit.community.compose.comment.query.AmityCommentView
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.AmityCommentItemShimmer
import com.amity.socialcloud.uikit.community.compose.post.detail.bounceEffect

fun LazyListScope.amityCommentListLLS(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope,
    comments: LazyPagingItems<AmityListItem>,
    commentTarget: AmityComment? = null,
    replyTargetId: String? = null,
    commentListState: AmityCommentTrayComponentViewModel.CommentListState,
    referenceId: String,
    referenceType: AmityCommentReferenceType,
    editingCommentId: String?,
    shouldAllowInteraction: Boolean,
    onReply: (String) -> Unit,
    onEdit: (String?) -> Unit,
    showBounceEffect: Boolean = false,
) {
    commentTarget?.let {
        item(key = "highlighted_comment_${it.getCommentId()}") {
            AmityCommentView(
                modifier = modifier
                    .let { if (showBounceEffect && replyTargetId == null) it.bounceEffect() else it },
                componentScope = componentScope,
                referenceId = referenceId,
                referenceType = referenceType,
                currentUserId = AmityCoreClient.getUserId(),
                editingCommentId = editingCommentId,
                comment = it,
                allowInteraction = shouldAllowInteraction,
                onReply = onReply,
                onEdit = onEdit,
                replyTargetId = replyTargetId,
                showBounceEffect = showBounceEffect
            )
        }
    }

    when (commentListState) {
        AmityCommentTrayComponentViewModel.CommentListState.SUCCESS -> {
            items(
                count = comments.itemCount,
                key = { "comment_${(comments[it] as? AmityListItem.CommentItem)?.comment?.getCommentId() ?: it}" }
            ) { index ->
                when (val data = comments[index]) {
                    is AmityListItem.CommentItem -> {
                        // Skip this item if it matches our highlighted comment ID
                        if (commentTarget == null || data.comment.getCommentId() != commentTarget.getCommentId()) {
                            AmityCommentView(
                                modifier = modifier,
                                componentScope = componentScope,
                                referenceId = referenceId,
                                referenceType = referenceType,
                                currentUserId = AmityCoreClient.getUserId(),
                                editingCommentId = editingCommentId,
                                comment = data.comment,
                                allowInteraction = shouldAllowInteraction,
                                onReply = onReply,
                                onEdit = onEdit,
                            )
                        }
                    }

                    is AmityListItem.AdItem -> {
                        AmityCommentAdView(
                            modifier = modifier,
                            componentScope = componentScope,
                            ad = data.ad
                        )
                    }

                    else -> {
                        AmityCommentItemShimmer()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        AmityCommentTrayComponentViewModel.CommentListState.LOADING -> {
            items(3) {
                Spacer(modifier = Modifier.height(16.dp))
                AmityCommentItemShimmer()
            }
        }

        AmityCommentTrayComponentViewModel.CommentListState.EMPTY,
        AmityCommentTrayComponentViewModel.CommentListState.ERROR -> {

        }
    }
}