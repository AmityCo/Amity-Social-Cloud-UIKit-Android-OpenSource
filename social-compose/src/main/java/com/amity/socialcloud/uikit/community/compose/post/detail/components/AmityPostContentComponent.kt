package com.amity.socialcloud.uikit.community.compose.post.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityPostPreviewLinkView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.isVisible
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import com.amity.socialcloud.uikit.common.utils.showToast
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerHelper
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostContentElement
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostEngagementView
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostHeaderElement
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostMediaElement
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostNonMemberSection
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuBottomSheet
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuDialogUIState
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuSheetUIState
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuViewModel

@Composable
fun AmityPostContentComponent(
    modifier: Modifier = Modifier,
    post: AmityPost,
    style: AmityPostContentComponentStyle,
    category: AmityPostCategory = AmityPostCategory.GENERAL,
    hideMenuButton: Boolean,
    hideTarget: Boolean = false,
    onTapAction: () -> Unit = {},
) {
    val context = LocalContext.current
    val isPostDetailPage = remember(style) {
        style == AmityPostContentComponentStyle.DETAIL
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
                AmityAlertDialog(
                    dialogTitle = context.getString(R.string.amity_delete_post_title),
                    dialogText = context.getString(R.string.amity_delete_post_warning_message),
                    confirmText = context.getString(R.string.amity_delete),
                    dismissText = context.getString(R.string.amity_cancel),
                    onConfirmation = {
                        AmityPostComposerHelper.deletePost(data.postId)
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
            }
        }

        AmityPostMenuDialogUIState.CloseDialog -> {
            viewModel.updateDialogUIState(AmityPostMenuDialogUIState.CloseDialog)
        }
    }

    var isVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isVisible) {
        if (!isPostDetailPage && isVisible) {
            post
                .analytics()
                .markAsViewed()
        }
    }

    AmityBaseComponent(componentId = "post_content") {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(AmityTheme.colors.background)
                .clickableWithoutRipple {
                    if (!isPostDetailPage) {
                        onTapAction()
                    }
                }
                .isVisible { isVisible = it }
        ) {
            AmityPostHeaderElement(
                modifier = modifier,
                componentScope = getComponentScope(),
                post = post,
                hideMenuButton = hideMenuButton,
                style = style,
                category = category,
                hideTarget = hideTarget,
                onMenuClick = {
                    viewModel.updateSheetUIState(AmityPostMenuSheetUIState.OpenSheet(it.getPostId()))
                }
            )
            AmityPostContentElement(
                modifier = modifier,
                post = post,
                onClick = {
                    // TODO: 8/9/24 disabled click on post content to fix long text post can't scroll
//                    if (!isPostDetailPage) {
//                        onTapAction()
//                    }
                },
            )
            AmityPostPreviewLinkView(
                modifier = modifier,
                post = post,
            )
            AmityPostMediaElement(
                modifier = modifier,
                post = post
            )

            if (viewModel.isNotMember(post)) {
                AmityPostNonMemberSection()
            } else {
                AmityPostEngagementView(
                    modifier = modifier,
                    componentScope = getComponentScope(),
                    post = post,
                    isPostDetailPage = isPostDetailPage,
                )
            }

            AmityPostMenuBottomSheet(
                post = post
            )
        }
    }
}

@Composable
fun AmityPostShimmer(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row {
            Box(
                modifier = modifier
                    .size(40.dp)
                    .shimmerBackground(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(40.dp)
                    )
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .height(40.dp)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Box(
                    Modifier
                        .width(120.dp)
                        .height(8.dp)
                        .shimmerBackground(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(12.dp)
                        )
                )
                Box(
                    Modifier
                        .width(88.dp)
                        .height(8.dp)
                        .shimmerBackground(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(12.dp)
                        )
                )
            }
        }

        Box(
            Modifier
                .width(240.dp)
                .height(8.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(12.dp)
                )
        )
        Box(
            Modifier
                .width(300.dp)
                .height(8.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(12.dp)
                )
        )
        Box(
            Modifier
                .width(180.dp)
                .height(8.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(12.dp)
                )
        )
    }
}