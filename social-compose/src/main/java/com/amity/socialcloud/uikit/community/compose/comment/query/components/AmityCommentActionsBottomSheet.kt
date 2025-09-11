package com.amity.socialcloud.uikit.community.compose.comment.query.components

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.sdk.model.core.flag.AmityContentFlagReason
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
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
    onEdit: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
                        onClose = {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                viewModel.updateSheetUIState(CommentBottomSheetState.CloseSheet)
                            }
                        },
                        onReportClick = {
                            if (isFlaggedByMe) {
                                submitUnReport(
                                    viewModel = viewModel,
                                    commentId = commentId,
                                    context = context,
                                    isReplyComment = isReplyComment
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
                                    context = context,
                                    isReplyComment = isReplyComment,
                                    reason = reason,
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
                                    context = context,
                                    isReplyComment = isReplyComment,
                                    reason = AmityContentFlagReason.Others(detail),
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
    onReportClick: () -> Unit,
    onClose: () -> Unit,
) {
    val context = LocalContext.current

    val openDeleteAlertDialog = remember { mutableStateOf(false) }

    if (openDeleteAlertDialog.value) {
        AmityAlertDialog(
            dialogTitle = context.getString(
                if (isReplyComment) R.string.amity_delete_reply_title
                else R.string.amity_delete_comment_title
            ),
            dialogText = context.getString(
                if (isReplyComment) R.string.amity_delete_reply_warning_message
                else R.string.amity_delete_reply_warning_message
            ),
            confirmText = context.getString(R.string.amity_delete),
            dismissText = context.getString(R.string.amity_cancel),
            onConfirmation = {
                onClose()
                openDeleteAlertDialog.value = false

                viewModel.deleteComment(
                    commentId = commentId,
                    onSuccess = {
                        AmityUIKitSnackbar.publishSnackbarMessage(
                            message = context.getString(
                                if (isReplyComment) R.string.amity_reply_deleted_message
                                else R.string.amity_comment_deleted_message
                            )
                        )
                    },
                    onError = {
                        AmityUIKitSnackbar.publishSnackbarErrorMessage(
                            message = context.getString(R.string.amity_comment_delete_failed_toast_message),
                        )
                    }
                )
            },
            onDismissRequest = { openDeleteAlertDialog.value = false }
        )
    }

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
                    text = context.getString(
                        if (isReplyComment) R.string.amity_edit_reply
                        else R.string.amity_edit_comment
                    ),
                    modifier = modifier.testTag("comment_tray_component/bottom_sheet_edit_comment_button")
                ) {
                    onClose()
                    onEdit()
                }
            }

            AmityBottomSheetActionItem(
                icon = R.drawable.amity_ic_delete_story,
                text = context.getString(
                    if (isReplyComment) R.string.amity_delete_reply
                    else R.string.amity_delete_comment
                ),
                color = AmityTheme.colors.alert,
                modifier = modifier.testTag("comment_tray_component/bottom_sheet_delete_comment_button"),
            ) {
                openDeleteAlertDialog.value = true
            }
        } else {
            AmityBottomSheetActionItem(
                icon = if (isFlaggedByMe) R.drawable.amity_ic_unreport else R.drawable.amity_ic_report_comment,
                text = if (isFlaggedByMe) {
                    if (isReplyComment) {
                        context.getString(R.string.amity_unreport_reply)
                    } else {
                        context.getString(R.string.amity_unreport_comment)
                    }
                } else {
                    if (isReplyComment) {
                        context.getString(R.string.amity_report_reply_v4)
                    } else {
                        context.getString(R.string.amity_report_comment)
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
    context: Context,
    isReplyComment: Boolean,
    reason: AmityContentFlagReason,
    onError: () -> Unit = {},
) {
    viewModel.flagComment(
        commentId = commentId,
        reason = reason,
        onSuccess = {
            AmityUIKitSnackbar.publishSnackbarMessage(
                message = if (!isReplyComment) {
                    context.getString(R.string.amity_comment_reported_toast_message)
                } else {
                    context.getString(R.string.amity_reply_reported_toast_message)
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
                            context.getString(R.string.amity_comment_reported_failed_toast_message)
                        } else {
                            context.getString(R.string.amity_reply_reported_failed_toast_message)
                        },
                    )
                }
            } else {
                AmityUIKitSnackbar.publishSnackbarErrorMessage(
                    message = if (!isReplyComment) {
                        context.getString(R.string.amity_comment_reported_failed_toast_message)
                    } else {
                        context.getString(R.string.amity_reply_reported_failed_toast_message)
                    },
                )
            }
        }
    )
}

private fun submitUnReport(
    viewModel: AmityCommentTrayComponentViewModel,
    commentId: String,
    context: Context,
    isReplyComment: Boolean,
) {
    viewModel.unflagComment(
        commentId = commentId,
        onSuccess = {
            AmityUIKitSnackbar.publishSnackbarMessage(
                message = if (!isReplyComment) {
                    context.getString(R.string.amity_comment_unreported_toast_message)
                } else {
                    context.getString(R.string.amity_reply_unreported_toast_message)
                }
            )
        },
        onError = {
            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                message = if (!isReplyComment) {
                    context.getString(R.string.amity_comment_unreported_failed_toast_message)
                } else {
                    context.getString(R.string.amity_reply_unreported_failed_toast_message)
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
        onClose = {},
        onReportClick = {},
        viewModel = AmityCommentTrayComponentViewModel()
    )
}