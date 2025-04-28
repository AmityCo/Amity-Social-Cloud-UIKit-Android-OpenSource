package com.amity.socialcloud.uikit.community.compose.post.detail.components

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.await
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityPostPreviewLinkView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.isVisible
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerHelper
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostContentElement
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostEngagementView
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostHeaderElement
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostLivestreamElement
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostMediaElement
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostNonMemberSection
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityPostPollElement
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuBottomSheet
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuDialogUIState
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuSheetUIState
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuViewModel
import kotlinx.coroutines.launch

@Composable
fun AmityPostContentComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    post: AmityPost,
    style: AmityPostContentComponentStyle,
    category: AmityPostCategory = AmityPostCategory.GENERAL,
    boldedText: String? = null,
    hideMenuButton: Boolean,
    hideTarget: Boolean = false,
    onTapAction: () -> Unit = {},
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.postContentComponentBehavior
    }
    val isPostDetailPage = remember(style) {
        style == AmityPostContentComponentStyle.DETAIL
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostMenuViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val dialogState by viewModel.dialogUIState.collectAsState()

    var scope = rememberCoroutineScope()

    when (dialogState) {
        is AmityPostMenuDialogUIState.OpenConfirmDeleteDialog -> {
            val data = dialogState as AmityPostMenuDialogUIState.OpenConfirmDeleteDialog
            if (data.postId == post.getPostId()) {
                AmityAlertDialog(
                    dialogTitle = context.getString(R.string.amity_delete_post_title),
                    dialogText = context.getString(R.string.amity_v4_delete_post_warning_message),
                    confirmText = context.getString(R.string.amity_delete),
                    dismissText = context.getString(R.string.amity_cancel),
                    onConfirmation = {
                        AmityPostComposerHelper.deletePost(data.postId)
                        viewModel.deletePost(
                            postId = data.postId,
                            onSuccess = {
                                if (isPostDetailPage) {
                                    context.closePageWithResult(Activity.RESULT_OK)
                                }
                                AmityUIKitSnackbar.publishSnackbarMessage(
                                    message = "Post deleted",
                                    offsetFromBottom = 52
                                )
                                viewModel.updateDialogUIState(AmityPostMenuDialogUIState.CloseDialog)
                            },
                            onError = {
                                val text =
                                    "Failed to delete post. Please try again."
                                AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                    message = text,
                                            offsetFromBottom = 52
                                )
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

        is AmityPostMenuDialogUIState.OpenConfirmEditDialog -> {
            val data = dialogState as AmityPostMenuDialogUIState.OpenConfirmEditDialog
            if (data.postId == post.getPostId()) {
                AmityAlertDialog(
                    dialogTitle = "Edit globally featured post?",
                    dialogText = "The post you're editing has been featured globally. If you edit your post, it would need to be re-approved, and will no longer be globally featured.",
                    confirmText = "Edit",
                    dismissText = context.getString(R.string.amity_cancel),
                    onConfirmation = {
                        viewModel.updateDialogUIState(AmityPostMenuDialogUIState.CloseDialog)
                        behavior.goToPostComposerPage(
                            context = context,
                            post = post,
                        )
                    },
                    onDismissRequest = {
                        viewModel.updateDialogUIState(AmityPostMenuDialogUIState.CloseDialog)
                    }
                )
            }
        }

        is AmityPostMenuDialogUIState.OpenConfirmClosePollDialog -> {
            val data = dialogState as AmityPostMenuDialogUIState.OpenConfirmClosePollDialog
            AmityAlertDialog(
                dialogTitle = "Close poll?",
                dialogText = "The Poll duration you've set will be ignored and your poll will be closed immediately.",
                confirmText = "Close poll",
                dismissText = context.getString(R.string.amity_cancel),
                onConfirmation = {
                    scope.launch {
                        try {
                            AmitySocialClient.newPollRepository()
                                .closePoll(data.pollId)
                                .await()

                        } catch (e: Exception) {
                            val text =
                                "Oops, something went wrong."
                            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                message = text,
                                offsetFromBottom = 52
                            )
                        }
                    }
                    viewModel.updateDialogUIState(AmityPostMenuDialogUIState.CloseDialog)
                },
                onDismissRequest = {
                    viewModel.updateDialogUIState(AmityPostMenuDialogUIState.CloseDialog)
                }
            )
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
                .testTag(getAccessibilityId())
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
                    style = style,
                    onClick = {
                        if (!isPostDetailPage) {
                            onTapAction()
                        }
                    },
                    onMentionedUserClick = {
                        behavior.goToUserProfilePage(
                            context = context,
                            userId = it,
                        )
                    },
                )
            } else {
                AmityPostContentElement(
                    modifier = modifier,
                    post = post,
                    style = style,
                    boldedText = boldedText,
                    onClick = {
                        if (!isPostDetailPage) {
                            onTapAction()
                        }
                    },
                    onMentionedUserClick = {
                        behavior.goToUserProfilePage(
                            context = context,
                            userId = it,
                        )
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
            }
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
                post = post,
                category = category
            )
        }
    }
}

@Composable
fun AmityPostShimmer(
	modifier: Modifier = Modifier,
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

@Composable
fun AmityMediaPostShimmer(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .aspectRatio(2f)
    ) {
        Box(
            modifier = modifier
                .weight(1f)
                .aspectRatio(1f)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(4.dp)
                )
        )
        Box(
            modifier = modifier
                .weight(1f)
                .aspectRatio(1f)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(4.dp)
                )
        )
    }
}