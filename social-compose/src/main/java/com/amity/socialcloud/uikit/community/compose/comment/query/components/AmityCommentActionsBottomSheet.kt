package com.amity.socialcloud.uikit.community.compose.comment.query.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.sdk.model.core.flag.AmityContentFlagReason
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel.CommentBottomSheetState
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityReportOtherReasonScreen
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityReportErrorScreen
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityReportReasonListScreen
import com.ekoapp.ekosdk.internal.api.socket.request.FlagContentRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityCommentActionsBottomSheet(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    viewModel: AmityCommentTrayComponentViewModel,
    commentId: String,
    isReplyComment: Boolean,
    isCommentCreatedByMe: Boolean,
    isFlaggedByMe: Boolean,
    isFailed: Boolean,
    fromNonMemberCommunity: Boolean,
    onEdit: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val behavior by lazy {
        AmitySocialBehaviorHelper.commentTrayComponentBehavior
    }

    val sheetUiState by viewModel.sheetUiState.collectAsState()

    val sheetModifier = modifier.navigationBarsPadding().semantics {
        testTagsAsResourceId = true
    }.let { baseModifier ->
        when (sheetUiState) {
            is CommentBottomSheetState.OpenReportSheet,
            is CommentBottomSheetState.OpenReportCustomReasonSheet,
            is CommentBottomSheetState.OpenErrorSheet,
                -> {
                baseModifier
                    .fillMaxSize()
                    .statusBarsPadding()
            }

            else -> baseModifier
        }
    }

    val showSheet by remember(viewModel) {
        derivedStateOf {
            sheetUiState != CommentBottomSheetState.CloseSheet &&
                    sheetUiState.commentId == commentId
        }
    }

    val openDeleteAlertDialog = remember { mutableStateOf(false) }

    val commentDeleteFailedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_comment_delete_failed_toast_message")
    val commentReportedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_comment_reported_toast_message")
    val replyReportedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_reply_reported_toast_message")
    val commentReportFailedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_comment_reported_failed_toast_message")
    val replyReportFailedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_reply_reported_failed_toast_message")
    val commentUnreportedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_comment_unreported_toast_message")
    val replyUnreportedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_reply_unreported_toast_message")
    val commentUnreportFailedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_comment_unreported_failed_toast_message")
    val replyUnreportFailedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_reply_unreported_failed_toast_message")

    if (openDeleteAlertDialog.value) {
        AmityAlertDialog(
            dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString(
                if (isReplyComment) "amity_social_button_delete_reply_title"
                else "amity_social_button_delete_comment_title"
            ),
            dialogText = DefaultAmitySocialStringProvider.getInstance().getString(
                if (isReplyComment) "amity_social_button_delete_reply_warning_message"
                else "amity_social_button_delete_comment_warning_message"
            ),
            confirmText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_delete"),
            dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_cancel"),
            onConfirmation = {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    viewModel.updateSheetUIState(CommentBottomSheetState.CloseSheet)
                }
                openDeleteAlertDialog.value = false

                viewModel.deleteComment(
                    commentId = commentId,
                    onSuccess = {
                        // Do nothing
                    },
                    onError = {
                        AmityUIKitSnackbar.publishSnackbarErrorMessage(
                            message = commentDeleteFailedStr,
                        )
                    }
                )
            },
            onDismissRequest = { openDeleteAlertDialog.value = false }
        )
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.updateSheetUIState(CommentBottomSheetState.CloseSheet)
            },
            sheetState = sheetState,
            containerColor = AmityTheme.colors.background,
            contentWindowInsets = { WindowInsets.waterfall },
            modifier = sheetModifier
        ) {
            when (sheetUiState) {
                is CommentBottomSheetState.OpenSheet -> {
                    AmityCommentActionsContainer(
                        modifier = modifier,
                        componentScope = componentScope,
                        viewModel = viewModel,
                        commentId = commentId,
                        isReplyComment = isReplyComment,
                        isCommentCreatedByMe = isCommentCreatedByMe,
                        isFlaggedByMe = isFlaggedByMe,
                        isFailed = isFailed,
                        onEdit = onEdit,
                        onDeleteClick = { openDeleteAlertDialog.value = true },
                        onClose = {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                viewModel.updateSheetUIState(CommentBottomSheetState.CloseSheet)
                            }
                        },
                        onReportClick = {
                            if (AmityCoreClient.isVisitor()) {
                                behavior.handleVisitorUserAction()
                                viewModel.updateSheetUIState(CommentBottomSheetState.CloseSheet)
                            } else if (fromNonMemberCommunity) {
                                behavior.handleNonMemberAction()
                                viewModel.updateSheetUIState(CommentBottomSheetState.CloseSheet)
                            } else if (isFlaggedByMe) {
                                submitUnReport(
                                    viewModel = viewModel,
                                    commentId = commentId,
                                    isReplyComment = isReplyComment,
                                    unreportedStr = commentUnreportedStr,
                                    replyUnreportedStr = replyUnreportedStr,
                                    unreportFailedStr = commentUnreportFailedStr,
                                    replyUnreportFailedStr = replyUnreportFailedStr,
                                )
                                viewModel.updateSheetUIState(CommentBottomSheetState.CloseSheet)
                            } else {
                                viewModel.updateSheetUIState(
                                    CommentBottomSheetState.OpenReportSheet(
                                        commentId
                                    )
                                )
                            }
                        }
                    )
                }

                is CommentBottomSheetState.OpenReportSheet -> {
                    AmityBaseComponent(
                        componentId = "",
                        needScaffold = true
                    ) {
                        // Same Component as Post
                        AmityReportReasonListScreen(
                            onCloseSheetClick = {
                                viewModel.updateSheetUIState(CommentBottomSheetState.CloseSheet)
                            },
                            onOtherClick = {
                                viewModel.updateSheetUIState(
                                    CommentBottomSheetState.OpenReportCustomReasonSheet(commentId)
                                )
                            },
                            onSubmitClick = { reason, enableButtonCallback ->
                                submitReport(
                                    viewModel = viewModel,
                                    commentId = commentId,
                                    isReplyComment = isReplyComment,
                                    reason = reason,
                                    reportedStr = commentReportedStr,
                                    replyReportedStr = replyReportedStr,
                                    reportFailedStr = commentReportFailedStr,
                                    replyReportFailedStr = replyReportFailedStr,
                                    onError = enableButtonCallback  // Pass the callback
                                )
                            }
                        )
                    }
                }

                is CommentBottomSheetState.OpenReportCustomReasonSheet -> {
                    AmityBaseComponent(
                        componentId = "",
                        needScaffold = true
                    ) {
                        // Same Component as Post
                        AmityReportOtherReasonScreen(
                            onBackClick = {
                                viewModel.updateSheetUIState(
                                    CommentBottomSheetState.OpenReportSheet(commentId)
                                )
                            },
                            onSubmitClick = { detail, enableButtonCallback ->
                                submitReport(
                                    viewModel = viewModel,
                                    commentId = commentId,
                                    isReplyComment = isReplyComment,
                                    reason = AmityContentFlagReason.Others(detail),
                                    reportedStr = commentReportedStr,
                                    replyReportedStr = replyReportedStr,
                                    reportFailedStr = commentReportFailedStr,
                                    replyReportFailedStr = replyReportFailedStr,
                                    onError = enableButtonCallback
                                )
                            },
                            onCloseSheetClick = {
                                viewModel.updateSheetUIState(CommentBottomSheetState.CloseSheet)
                            }
                        )
                    }
                }

                is CommentBottomSheetState.OpenErrorSheet -> {
                    // Same Component as Post
                    AmityReportErrorScreen(
                        onCloseSheetClick = {
                            viewModel.updateSheetUIState(CommentBottomSheetState.CloseSheet)
                        }
                    )
                }

                is CommentBottomSheetState.CloseSheet -> {}

            }

        }
    }
}

@Composable
fun AmityCommentActionsContainer(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    viewModel: AmityCommentTrayComponentViewModel,
    commentId: String,
    isReplyComment: Boolean,
    isCommentCreatedByMe: Boolean,
    isFlaggedByMe: Boolean,
    isFailed: Boolean,
    onEdit: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
    onClose: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(AmityTheme.colors.background)
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
    ) {
        if (isCommentCreatedByMe) {
            if (!isFailed) {
                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_edit_profile,
                    text = DefaultAmitySocialStringProvider.getInstance().getString(
                        if (isReplyComment) "amity_social_button_edit_reply"
                        else "amity_social_button_edit_comment"
                    ),
                    modifier = modifier.testTag("comment_tray_component/bottom_sheet_edit_comment_button")
                ) {
                    onClose()
                    onEdit()
                }
            }

            AmityBottomSheetActionItem(
                icon = R.drawable.amity_ic_delete_story,
                text = DefaultAmitySocialStringProvider.getInstance().getString(
                    if (isReplyComment) "amity_social_button_delete_reply"
                    else "amity_social_button_delete_comment"
                ),
                color = AmityTheme.colors.alert,
                modifier = modifier.testTag("comment_tray_component/bottom_sheet_delete_comment_button"),
            ) {
                onDeleteClick()
            }
        } else {
            AmityBottomSheetActionItem(
                icon = if (isFlaggedByMe) R.drawable.amity_ic_unreport else R.drawable.amity_ic_report_comment,
                text = if (isFlaggedByMe) {
                    if (isReplyComment) {
                        DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_unreport_reply")
                    } else {
                        DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_unreport_comment")
                    }
                } else {
                    if (isReplyComment) {
                        DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_report_reply_v4")
                    } else {
                        DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_report_comment")
                    }
                },
                modifier = modifier.testTag("comment_tray_component/bottom_sheet_report_comment_button"),
            ) {
                onReportClick()
            }
        }
    }
}


private fun submitReport(
    viewModel: AmityCommentTrayComponentViewModel,
    commentId: String,
    isReplyComment: Boolean,
    reason: AmityContentFlagReason,
    reportedStr: String,
    replyReportedStr: String,
    reportFailedStr: String,
    replyReportFailedStr: String,
    onError: () -> Unit = {},
) {
    viewModel.flagComment(
        commentId = commentId,
        reason = reason,
        onSuccess = {
            AmityUIKitSnackbar.publishSnackbarMessage(
                message = if (!isReplyComment) {
                    reportedStr
                } else {
                    replyReportedStr
                },
            )
            viewModel.updateSheetUIState(CommentBottomSheetState.CloseSheet)
        },
        onError = { error ->
            onError()
            if (error is AmityException) {
                if (error.code == AmityError.ITEM_NOT_FOUND.code) {
                    viewModel.updateSheetUIState(CommentBottomSheetState.OpenErrorSheet(commentId))
                } else {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                        message = if (!isReplyComment) {
                            reportFailedStr
                        } else {
                            replyReportFailedStr
                        },
                    )
                }
            } else {
                AmityUIKitSnackbar.publishSnackbarErrorMessage(
                    message = if (!isReplyComment) {
                        reportFailedStr
                    } else {
                        replyReportFailedStr
                    },
                )
            }
        }
    )
}

private fun submitUnReport(
    viewModel: AmityCommentTrayComponentViewModel,
    commentId: String,
    isReplyComment: Boolean,
    unreportedStr: String,
    replyUnreportedStr: String,
    unreportFailedStr: String,
    replyUnreportFailedStr: String,
) {
    viewModel.unflagComment(
        commentId = commentId,
        onSuccess = {
            AmityUIKitSnackbar.publishSnackbarMessage(
                message = if (!isReplyComment) {
                    unreportedStr
                } else {
                    replyUnreportedStr
                }
            )
        },
        onError = {
            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                message = if (!isReplyComment) {
                    unreportFailedStr
                } else {
                    replyUnreportFailedStr
                }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AmityCommentActionsContainerPreview() {
    AmityCommentActionsContainer(
        commentId = "",
        isReplyComment = false,
        isCommentCreatedByMe = true,
        isFlaggedByMe = false,
        isFailed = true,
        onEdit = {},
        onDeleteClick = {},
        onClose = {},
        onReportClick = {},
        viewModel = AmityCommentTrayComponentViewModel()
    )
}