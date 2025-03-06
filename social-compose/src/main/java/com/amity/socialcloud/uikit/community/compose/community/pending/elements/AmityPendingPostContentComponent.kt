package com.amity.socialcloud.uikit.community.compose.community.pending.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityPostPreviewLinkView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostContentElement
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostLivestreamElement
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostMediaElement
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostPollElement
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuDialogUIState
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuSheetUIState
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuViewModel

@Composable
fun AmityPendingPostContentComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    post: AmityPost,
    onAcceptAction: (AmityPost) -> Unit,
    onDeclineAction: (AmityPost) -> Unit,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.pendingPostContentComponentBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostMenuViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val dialogState by viewModel.dialogUIState.collectAsState()

    when (dialogState) {
        is AmityPostMenuDialogUIState.OpenConfirmDeleteDialog -> {
            val data = dialogState as AmityPostMenuDialogUIState.OpenConfirmDeleteDialog
            if (data.postId == post.getPostId()) {
                viewModel.deletePost(
                    postId = data.postId,
                    onSuccess = {
                        AmityUIKitSnackbar.publishSnackbarMessage("Post deleted")
                        viewModel.updateDialogUIState(AmityPostMenuDialogUIState.CloseDialog)
                    },
                    onError = {
                        AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to delete post")
                        viewModel.updateDialogUIState(AmityPostMenuDialogUIState.CloseDialog)
                    }
                )
                /*
                AmityAlertDialog(
                    dialogTitle = context.getString(R.string.amity_delete_post_title),
                    dialogText = context.getString(R.string.amity_delete_post_warning_message),
                    confirmText = context.getString(R.string.amity_delete),
                    dismissText = context.getString(R.string.amity_cancel),
                    onConfirmation = {
                        viewModel.deletePost(
                            postId = data.postId,
                            onSuccess = {
                                context.showToast("Post deleted")
                                viewModel.updateDialogUIState(AmityPostMenuDialogUIState.CloseDialog)
                            },
                            onError = {
                                context.showToast("Failed to delete post")
                                viewModel.updateDialogUIState(AmityPostMenuDialogUIState.CloseDialog)
                            }
                        )
                    },
                    onDismissRequest = {
                        viewModel.updateDialogUIState(AmityPostMenuDialogUIState.CloseDialog)
                    }
                )
                 */
            }
        }

        is AmityPostMenuDialogUIState.OpenConfirmEditDialog -> {

        }

        is AmityPostMenuDialogUIState.OpenConfirmClosePollDialog -> {

        }

        is AmityPostMenuDialogUIState.CloseDialog -> {
            viewModel.updateDialogUIState(AmityPostMenuDialogUIState.CloseDialog)
        }
    }

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "pending_post_content"
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(AmityTheme.colors.background)
        ) {
            AmityPendingPostHeaderElement(
                modifier = modifier,
                pageScope = pageScope,
                componentScope = getComponentScope(),
                post = post,
                hideMenuButton = post.getCreatorId() != AmityCoreClient.getUserId(),
                onMenuClick = {
                    viewModel.updateSheetUIState(AmityPostMenuSheetUIState.OpenSheet(it.getPostId()))
                }
            )

            if (post.getChildren().any { it.getData() is AmityPost.Data.LIVE_STREAM }) {
                AmityPostLivestreamElement(
                    modifier = modifier,
                    post = post
                )
            } else if (post.getChildren().any { it.getData() is AmityPost.Data.POLL }) {

                AmityPostPollElement(
                    modifier = modifier,
                    componentScope = getComponentScope(),
                    post = post,
                    style = AmityPostContentComponentStyle.FEED,
                    onClick = {},
                    onMentionedUserClick = {},
                )
            } else {
                AmityPostContentElement(
                    modifier = modifier,
                    post = post,
                    style = AmityPostContentComponentStyle.FEED,
                    onClick = {},
                    onMentionedUserClick = {
                        behavior.goToUserProfilePage(context = context, userId = it)
                    }
                )
                AmityPostPreviewLinkView(
                    modifier = modifier,
                    post = post,
                )
                AmityPostMediaElement(
                    modifier = modifier,
                    post = post
                )
            }

            AmityPendingPostMenuBottomSheet(
                post = post
            )

            if (post.getCreatorId() != AmityCoreClient.getUserId()) {
                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp)
                )

                AmityPendingPostActionRow(
                    pageScope = pageScope,
                    onAccept = {
                        onAcceptAction(post)
                    },
                    onDecline = {
                        onDeclineAction(post)
                    },
                )
            }
        }
    }
}