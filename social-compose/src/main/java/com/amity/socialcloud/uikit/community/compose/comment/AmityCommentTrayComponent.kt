package com.amity.socialcloud.uikit.community.compose.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityPagingEmptyItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityPagingErrorItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityPagingLoadingItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.create.AmityCommentComposerBar
import com.amity.socialcloud.uikit.community.compose.comment.elements.AmityDisabledCommentView
import com.amity.socialcloud.uikit.community.compose.paging.comment.amityCommentListLLS

@Composable
fun AmityCommentTrayComponent(
    modifier: Modifier = Modifier,
    referenceId: String,
    referenceType: AmityCommentReferenceType,
    community: AmityCommunity? = null,
    shouldAllowInteraction: Boolean,
    shouldAllowCreation: Boolean,
) {
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommentTrayComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val comments =
        remember(referenceType, referenceId, community?.getCommunityId()) {
            viewModel.getComments(referenceId, referenceType, community?.getCommunityId())
        }.collectAsLazyPagingItems()
    val commentListState by viewModel.commentListState.collectAsState()

    val currentUser by remember(viewModel) {
        viewModel.getCurrentUser()
    }.subscribeAsState(null)

    var replyComment by remember { mutableStateOf<AmityComment?>(null) }

    var replyCommentId by remember { mutableStateOf("") }
    var editingCommentId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(replyCommentId) {
        comments.itemSnapshotList.firstOrNull {
            it is AmityListItem.CommentItem &&
                    it.comment.getCommentId() == replyCommentId
        }?.let {
            replyComment = (it as AmityListItem.CommentItem).comment
            replyCommentId = ""
        }
    }

    LaunchedEffect(community?.getCommunityId()) {
        viewModel.setCommunity(community)
    }

    AmityBaseComponent(
        modifier = modifier,
        needScaffold = true,
        componentId = "comment_tray_component",
    ) {
        Box(
            modifier = modifier
                .background(AmityTheme.colors.background)
        ) {
            Column(modifier = modifier.fillMaxWidth()) {
                if (referenceType == AmityCommentReferenceType.STORY) {
                    Text(
                        text = context.getString(R.string.amity_comments),
                        style = AmityTheme.typography.titleLegacy,
                        modifier = modifier
                            .fillMaxWidth()
                            .testTag(getAccessibilityId("title_text_view")),
                    )

                    HorizontalDivider(
                        color = AmityTheme.colors.baseShade4,
                        modifier = modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(bottom = 64.dp)
                ) {
                    AmityCommentTrayComponentViewModel.CommentListState.from(
                        loadState = comments.loadState.refresh,
                        itemCount = comments.itemCount,
                    ).let(viewModel::setCommentListState)

                    when (commentListState) {
                        AmityCommentTrayComponentViewModel.CommentListState.EMPTY -> {
                            item {
                                AmityPagingEmptyItem(
                                    text = "No comments yet",
                                    modifier = Modifier.testTag("comment_tray_component/empty_text_view")
                                )
                            }
                        }

                        AmityCommentTrayComponentViewModel.CommentListState.SUCCESS -> {
                            amityCommentListLLS(
                                modifier = modifier,
                                componentScope = getComponentScope(),
                                comments = comments,
                                commentListState = commentListState,
                                referenceId = referenceId,
                                referenceType = referenceType,
                                editingCommentId = editingCommentId,
                                shouldAllowInteraction = true,
                                onReply = {
                                    replyCommentId = it
                                },
                                onEdit = {
                                    editingCommentId = it
                                }
                            )
                        }

                        AmityCommentTrayComponentViewModel.CommentListState.LOADING -> {
                            item {
                                AmityPagingLoadingItem(modifier)
                            }
                        }

                        AmityCommentTrayComponentViewModel.CommentListState.ERROR -> {
                            item {
                                AmityPagingErrorItem(modifier)
                            }
                        }
                    }
                }

            }

            if (shouldAllowInteraction) {
                if (shouldAllowCreation) {
                    AmityCommentComposerBar(
                        componentScope = getComponentScope(),
                        referenceId = referenceId,
                        referenceType = referenceType,
                        currentUser = currentUser,
                        replyComment = replyComment,
                        modifier = modifier.align(Alignment.BottomCenter)
                    ) {
                        replyComment = null
                    }
                } else {
                    AmityDisabledCommentView(
                        modifier = modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}