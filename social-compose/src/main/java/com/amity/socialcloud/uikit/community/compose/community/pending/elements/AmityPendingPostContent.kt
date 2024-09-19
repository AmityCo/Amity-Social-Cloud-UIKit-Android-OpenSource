package com.amity.socialcloud.uikit.community.compose.community.pending.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityPostPreviewLinkView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.showToast
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostContentElement
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostMediaElement
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuDialogUIState
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuSheetUIState
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuViewModel

@Composable
fun AmityPendingPostContent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    post: AmityPost,
    hideMenuButton: Boolean,
) {
    val context = LocalContext.current

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
                        context.showToast("Post deleted")
                        viewModel.updateDialogUIState(AmityPostMenuDialogUIState.CloseDialog)
                    },
                    onError = {
                        context.showToast("Failed to delete post")
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

        AmityPostMenuDialogUIState.CloseDialog -> {
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
                hideMenuButton = hideMenuButton,
                onMenuClick = {
                    viewModel.updateSheetUIState(AmityPostMenuSheetUIState.OpenSheet(it.getPostId()))
                }
            )
            AmityPostContentElement(
                modifier = modifier,
                post = post,
                onClick = {},
            )
            AmityPostPreviewLinkView(
                modifier = modifier,
                post = post,
            )
            AmityPostMediaElement(
                modifier = modifier,
                post = post
            )

            AmityPendingPostMenuBottomSheet(
                post = post
            )
        }
    }
}