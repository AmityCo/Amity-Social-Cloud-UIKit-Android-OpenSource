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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.create.AmityCommentComposerBar
import com.amity.socialcloud.uikit.community.compose.comment.elements.AmityDisabledCommentView
import com.amity.socialcloud.uikit.community.compose.comment.query.AmityCommentView
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityPagingEmptyItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityPagingErrorItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityPagingLoadingItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.pagingLoadStateItem

@Composable
fun AmityCommentTrayComponent(
    modifier: Modifier = Modifier,
    reference: AmityComment.Reference,
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

    val comments = remember(viewModel, reference) {
        viewModel.getComments(reference)
    }.collectAsLazyPagingItems()

    val currentUser = remember(viewModel) {
        viewModel.getCurrentUser()
    }.subscribeAsState(null)

    var replyCommentId by remember { mutableStateOf("") }
    var replyComment by remember { mutableStateOf<AmityComment?>(null) }
    var editingCommentId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(replyCommentId) {
        replyComment = comments.itemSnapshotList.firstOrNull {
            it?.getCommentId() == replyCommentId
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
                Text(
                    text = context.getString(R.string.amity_comments),
                    style = AmityTheme.typography.title,
                    modifier = modifier
                        .fillMaxWidth()
                        .testTag(getAccessibilityId("title_text_view")),
                )

                HorizontalDivider(
                    color = AmityTheme.colors.baseShade4,
                    modifier = modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                if (comments.itemCount == 0 && comments.loadState.append is LoadState.NotLoading) {
                    AmityPagingEmptyItem(
                        text = "No comments yet",
                        modifier = Modifier.testTag("comment_tray_component/empty_text_view")
                    )
                }

                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(bottom = 64.dp)
                ) {
                    items(
                        count = comments.itemCount,
                        key = { index -> comments[index]?.getCommentId() ?: index }
                    ) { index ->
                        val comment = comments[index] ?: return@items
                        AmityCommentView(
                            modifier = modifier,
                            componentScope = getComponentScope(),
                            reference = reference,
                            currentUserId = currentUser.value?.getUserId() ?: "",
                            editingCommentId = editingCommentId,
                            comment = comment,
                            allowInteraction = shouldAllowInteraction,
                            onReply = {
                                replyCommentId = it
                            },
                            onEdit = {
                                editingCommentId = it
                            }
                        )
                    }

                    pagingLoadStateItem(
                        loadState = comments.loadState.append,
                        loading = { AmityPagingLoadingItem(modifier) },
                        error = { AmityPagingErrorItem(modifier) },
                    )
                }
            }

            if (shouldAllowInteraction) {
                if (shouldAllowCreation) {
                    AmityCommentComposerBar(
                        componentScope = getComponentScope(),
                        reference = reference,
                        avatarUrl = currentUser.value?.getAvatar()?.getUrl(),
                        replyComment = replyComment,
                        modifier = modifier.align(Alignment.BottomCenter)
                    ) {
                        replyComment = null
                        replyCommentId = ""
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

@Preview
@Composable
fun AmityCommentPagePreview() {
    AmityCommentTrayComponent(
        reference = AmityComment.Reference.STORY(""),
        shouldAllowInteraction = true,
        shouldAllowCreation = true,
    )
}