package com.amity.socialcloud.uikit.community.compose.post.detail.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.waterfall
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
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityPostSettings
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import io.reactivex.rxjava3.core.Flowable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityPostMenuBottomSheet(
    modifier: Modifier = Modifier,
    post: AmityPost,
    category: AmityPostCategory = AmityPostCategory.GENERAL
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
            contentWindowInsets = { WindowInsets.waterfall },
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
                        val isPollPost = post.getChildren().any { it.getData() is AmityPost.Data.POLL }
                        val isLiveStreamPost = post.getChildren().any { it.getData() is AmityPost.Data.LIVE_STREAM }

                        if (post.getCreatorId() == AmityCoreClient.getUserId() && !isPollPost && !isLiveStreamPost) {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_ic_edit_profile,
                                text = "Edit post",
                                modifier = modifier.testTag("bottom_sheet_edit_button"),
                            ) {
                                viewModel.updateSheetUIState(AmityPostMenuSheetUIState.CloseSheet)
                                val target = post.getTarget()
                                if(category == AmityPostCategory.GLOBAL
                                    && target is AmityPost.Target.COMMUNITY
                                    && target.getCommunity()?.getPostSettings() == AmityCommunityPostSettings.ADMIN_REVIEW_POST_REQUIRED
                                    && !AmityCoreClient.hasPermission(AmityPermission.EDIT_COMMUNITY_POST).atCommunity(target.getCommunityId()).check().blockingFirst()
                                    && post.getReviewStatus() == AmityReviewStatus.PUBLISHED) {
                                    viewModel.updateDialogUIState(
                                        AmityPostMenuDialogUIState.OpenConfirmEditDialog(postId = post.getPostId())
                                    )
                                } else {
                                    behavior.goToPostComposerPage(
                                        context = context,
                                        post = post,
                                    )
                                }
                            }
                        }

                        val postData = post.getChildren().firstOrNull()?.getData() as? AmityPost.Data.POLL
                        val poll = postData?.getPoll()?.blockingFirst()
                        val isPollActive = poll?.getClosedAt()?.isAfterNow ?: false
                        if (post.getCreatorId() == AmityCoreClient.getUserId() && post.getReviewStatus() != AmityReviewStatus.UNDER_REVIEW && isPollActive) {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.ic_amity_ic_poll_create,
                                text = "Close poll",
                                modifier = modifier.testTag("bottom_sheet_edit_button"),
                            ) {
                                viewModel.updateSheetUIState(AmityPostMenuSheetUIState.CloseSheet)
                                viewModel.updateDialogUIState(
                                    AmityPostMenuDialogUIState.OpenConfirmClosePollDialog(pollId = poll!!.getPollId())
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
                                            AmityUIKitSnackbar.publishSnackbarMessage("Post unreported")
                                        },
                                        onError = {
                                            AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to unreport post. Please try again.")
                                        }
                                    )
                                } else {
                                    viewModel.flagPost(
                                        postId = post.getPostId(),
                                        onSuccess = {
                                            AmityUIKitSnackbar.publishSnackbarMessage("Post reported")
                                        },
                                        onError = {
                                            AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to report post. Please try again.")
                                        }
                                    )
                                }
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
                    }
                }

                AmityPostMenuSheetUIState.CloseSheet -> {}
            }
        }
    }
}
