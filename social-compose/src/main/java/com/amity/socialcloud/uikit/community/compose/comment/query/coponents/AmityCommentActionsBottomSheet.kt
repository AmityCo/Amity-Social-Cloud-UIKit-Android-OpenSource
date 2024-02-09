package com.amity.socialcloud.uikit.community.compose.comment.query.coponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityCommentActionsBottomSheet(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    commentId: String,
    shouldShow: Boolean,
    isReplyComment: Boolean,
    isCommentCreatedByMe: Boolean,
    isFlaggedByMe: Boolean,
    onEdit: () -> Unit,
    onClose: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (shouldShow) {
        ModalBottomSheet(
            onDismissRequest = {
                onClose()
            },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            AmityCommentActionsContainer(
                modifier = modifier,
                componentScope = componentScope,
                commentId = commentId,
                isReplyComment = isReplyComment,
                isCommentCreatedByMe = isCommentCreatedByMe,
                isFlaggedByMe = isFlaggedByMe,
                onEdit = onEdit,
            ) {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onClose()
                    }
                }
            }
        }
    }
}

@Composable
fun AmityCommentActionsContainer(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    commentId: String,
    isReplyComment: Boolean,
    isCommentCreatedByMe: Boolean,
    isFlaggedByMe: Boolean,
    onEdit: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommentTrayComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    val openDeleteAlertDialog = remember { mutableStateOf(false) }

    if (openDeleteAlertDialog.value) {
        AmityAlertDialog(
            dialogTitle = context.getString(
                if (isReplyComment) R.string.amity_delete_reply_title
                else R.string.amity_delete_comment_title
            ),
            dialogText = context.getString(
                if (isReplyComment) R.string.amity_delete_reply_warning_message
                else R.string.amity_delete_comment_warning_message
            ),
            confirmText = context.getString(R.string.amity_delete),
            dismissText = context.getString(R.string.amity_cancel),
            onConfirmation = {
                onClose()
                openDeleteAlertDialog.value = false

                viewModel.deleteComment(
                    commentId = commentId,
                    onSuccess = {
                        componentScope?.showSnackbar(
                            message = context.getString(
                                if (isReplyComment) R.string.amity_reply_deleted_message
                                else R.string.amity_comment_deleted_message
                            ),
                            drawableRes = R.drawable.amity_ic_check_circle
                        )
                    },
                    onError = {
                        it.message?.let { message ->
                            componentScope?.showSnackbar(
                                message = message,
                            )
                        }
                    }
                )
            },
            onDismissRequest = { openDeleteAlertDialog.value = false }
        )
    }

    Column(
        modifier = modifier
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, bottom = 50.dp)
    ) {
        if (isCommentCreatedByMe) {
            AmityBottomSheetActionItem(
                icon = R.drawable.amity_ic_edit_profile,
                text = context.getString(
                    if (isReplyComment) R.string.amity_edit_reply
                    else R.string.amity_edit_comment
                ),
            ) {
                onClose()
                onEdit()
            }

            AmityBottomSheetActionItem(
                icon = R.drawable.amity_ic_delete_story,
                text = context.getString(
                    if (isReplyComment) R.string.amity_delete_reply
                    else R.string.amity_delete_comment
                ),
            ) {
                openDeleteAlertDialog.value = true
            }
        } else {
            AmityBottomSheetActionItem(
                icon = R.drawable.amity_ic_report_comment,
                text = context.getString(
                    if (isFlaggedByMe) {
                        R.string.amity_undo_report
                    } else {
                        if (isReplyComment) R.string.amity_report_reply
                        else R.string.amity_report_comment
                    }
                ),
            ) {
                onClose()
                if (isFlaggedByMe) {
                    viewModel.unflagComment(
                        commentId = commentId,
                        onSuccess = {
                            componentScope?.showSnackbar(
                                message = context.getString(R.string.amity_comment_unreported_toast_message),
                                drawableRes = R.drawable.amity_ic_check_circle
                            )
                        },
                        onError = {
                            it.message?.let { message ->
                                componentScope?.showSnackbar(
                                    message = message,
                                )
                            }
                        }
                    )
                } else {
                    viewModel.flagComment(
                        commentId = commentId,
                        onSuccess = {
                            componentScope?.showSnackbar(
                                message = context.getString(R.string.amity_comment_reported_toast_message),
                                drawableRes = R.drawable.amity_ic_check_circle
                            )
                        },
                        onError = {
                            it.message?.let { message ->
                                componentScope?.showSnackbar(
                                    message = message,
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityCommentActionsContainerPreview() {
    AmityCommentActionsContainer(
        commentId = "",
        isReplyComment = false,
        isCommentCreatedByMe = false,
        isFlaggedByMe = false,
        onEdit = {},
        onClose = {},
    )
}