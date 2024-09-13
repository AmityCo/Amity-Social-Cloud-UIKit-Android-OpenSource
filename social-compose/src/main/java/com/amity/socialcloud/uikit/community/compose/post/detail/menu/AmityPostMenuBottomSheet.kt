package com.amity.socialcloud.uikit.community.compose.post.detail.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.showToast
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import io.reactivex.rxjava3.core.Flowable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityPostMenuBottomSheet(
    modifier: Modifier = Modifier,
    post: AmityPost,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.postContentComponentBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel = viewModel<AmityPostMenuViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val hasDeleteCommunityPostPermission by remember(viewModel, post.getPostId()) {
        if (post.getTarget() is AmityPost.Target.COMMUNITY) {
            val communityId = (post.getTarget() as AmityPost.Target.COMMUNITY).getCommunityId()
            viewModel.checkDeleteCommunityPostPermission(communityId)
        } else {
            Flowable.just(false)
        }
    }.subscribeAsState(false)

    val hasDeleteUserFeedPostPermission by remember(viewModel, post.getPostId()) {
        if (post.getTarget() is AmityPost.Target.USER) {
            viewModel.checkDeleteUserFeedPostPermission()
        } else {
            Flowable.just(false)
        }
    }.subscribeAsState(false)

    val shouldShowDeletePostOption by remember(post.getPostId()) {
        derivedStateOf {
            val isCommunityTarget = post.getTarget() is AmityPost.Target.COMMUNITY

            if (post.getCreatorId() == AmityCoreClient.getUserId()) {
                true
            } else {
                if (isCommunityTarget) {
                    hasDeleteCommunityPostPermission
                } else {
                    hasDeleteUserFeedPostPermission
                }
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetUIState by viewModel.sheetUIState.collectAsState()

    val showSheet by remember(viewModel) {
        derivedStateOf {
            sheetUIState != AmityPostMenuSheetUIState.CloseSheet &&
                    sheetUIState.postId == post.getPostId()
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.updateSheetUIState(AmityPostMenuSheetUIState.CloseSheet)
            },
            sheetState = sheetState,
            containerColor = AmityTheme.colors.background,
            windowInsets = WindowInsets(top = 54.dp),
            modifier = modifier
                .semantics {
                    testTagsAsResourceId = true
                },
        ) {
            when (sheetUIState) {
                is AmityPostMenuSheetUIState.OpenSheet -> {
                    val isFlaggedByMe = post.isFlaggedByMe()

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 64.dp)
                    ) {
                        if (post.getCreatorId() == AmityCoreClient.getUserId()) {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_ic_edit_profile,
                                text = "Edit post",
                                modifier = modifier.testTag("bottom_sheet_edit_button"),
                            ) {
                                viewModel.updateSheetUIState(AmityPostMenuSheetUIState.CloseSheet)
                                behavior.goToPostComposerPage(
                                    context = context,
                                    post = post,
                                )
                            }
                        }

                        if (shouldShowDeletePostOption) {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_ic_delete_story,
                                text = "Delete post",
                                color = AmityTheme.colors.alert,
                                modifier = modifier.testTag("bottom_sheet_delete_button"),
                            ) {
                                viewModel.updateSheetUIState(AmityPostMenuSheetUIState.CloseSheet)
                                viewModel.updateDialogUIState(
                                    AmityPostMenuDialogUIState.OpenConfirmDeleteDialog(postId = post.getPostId())
                                )
                            }
                        }

                        if (post.getCreatorId() != AmityCoreClient.getUserId()) {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_ic_report_comment,
                                text = if (isFlaggedByMe) "Unreport post" else "Report post",
                                modifier = modifier.testTag("bottom_sheet_report_button"),
                            ) {
                                viewModel.updateSheetUIState(AmityPostMenuSheetUIState.CloseSheet)
                                if (isFlaggedByMe) {
                                    viewModel.unflagPost(
                                        postId = post.getPostId(),
                                        onSuccess = {
                                            context.showToast("Post unreported")
                                        },
                                        onError = {
                                            it.message?.let { message ->
                                                context.showToast(message)
                                            }
                                        }
                                    )
                                } else {
                                    viewModel.flagPost(
                                        postId = post.getPostId(),
                                        onSuccess = {
                                            context.showToast("Post reported")
                                        },
                                        onError = {
                                            it.message?.let { message ->
                                                context.showToast(message)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                AmityPostMenuSheetUIState.CloseSheet -> {}
            }
        }
    }
}
