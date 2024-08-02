package com.amity.socialcloud.uikit.community.compose.comment

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.comment.query.AmityCommentAdView
import com.amity.socialcloud.uikit.community.compose.comment.query.AmityCommentView
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.AmityCommentItemShimmer

@Composable
fun amityCommentListComponent(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope,
    referenceId: String,
    referenceType: AmityCommentReferenceType,
    community: AmityCommunity? = null,
    shouldAllowInteraction: Boolean,
    triggerRefresh: Boolean = false,
    onReply: (AmityComment) -> Unit,
): List<@Composable () -> Unit> {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommentTrayComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val comments =
        remember(triggerRefresh, referenceType, referenceId, community?.getCommunityId()) {
            viewModel.getComments(referenceId, referenceType, community?.getCommunityId())
        }.collectAsLazyPagingItems()

    val commentListState by viewModel.commentListState.collectAsState()

    var replyCommentId by remember { mutableStateOf("") }
    var editingCommentId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(replyCommentId) {
        comments.itemSnapshotList.firstOrNull {
            it is AmityListItem.CommentItem &&
                    it.comment.getCommentId() == replyCommentId
        }?.let {
            onReply((it as AmityListItem.CommentItem).comment)
            replyCommentId = ""
        }
    }

    LaunchedEffect(community?.getCommunityId()) {
        viewModel.setCommunity(community)
    }

    LaunchedEffect(comments.loadState.refresh, comments.itemCount) {
        AmityCommentTrayComponentViewModel.CommentListState.from(
            loadState = comments.loadState.refresh,
            itemCount = comments.itemCount,
        ).let(viewModel::setCommentListState)
    }

    return when (commentListState) {
        AmityCommentTrayComponentViewModel.CommentListState.LOADING -> {
            listOf(
                {
                    Spacer(modifier = Modifier.height(16.dp))
                    AmityCommentItemShimmer()
                    Spacer(modifier = Modifier.height(16.dp))
                },
                {
                    AmityCommentItemShimmer()
                    Spacer(modifier = Modifier.height(16.dp))
                },
                {
                    AmityCommentItemShimmer()
                    Spacer(modifier = Modifier.height(16.dp))
                },
            )
        }

        AmityCommentTrayComponentViewModel.CommentListState.SUCCESS -> {
            comments.itemSnapshotList.items.map {
                {
                    if (it is AmityListItem.CommentItem) {
                        AmityCommentView(
                            modifier = modifier,
                            componentScope = componentScope,
                            referenceId = referenceId,
                            referenceType = referenceType,
                            currentUserId = AmityCoreClient.getUserId(),
                            editingCommentId = editingCommentId,
                            comment = it.comment,
                            allowInteraction = shouldAllowInteraction,
                            onReply = {
                                replyCommentId = it
                            },
                            onEdit = {
                                editingCommentId = it
                            },
                        )
                    } else if (it is AmityListItem.AdItem) {
                        AmityCommentAdView(
                            modifier = modifier,
                            componentScope = componentScope,
                            ad = it.ad
                        )
                    }
                }
            }
        }

        else -> {
            listOf()
        }
    }
}