package com.amity.socialcloud.uikit.community.compose.comment.query.coponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.comment.elements.AmityCommentComposerTextField
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.utils.getBackgroundColor
import com.amity.socialcloud.uikit.community.compose.utils.getValue
import com.amity.socialcloud.uikit.community.compose.utils.shade

@Composable
fun AmityEditCommentContainer(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    commentId: String,
    commentText: String,
    onEditFinished: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommentTrayComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    var localCommentText by remember { mutableStateOf(commentText) }
    val isAllowedToSave by remember {
        derivedStateOf { localCommentText != commentText && localCommentText.isNotEmpty() }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    AmityBaseComponent(
        modifier = modifier,
        componentId = "edit_comment_component"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.padding(top = 4.dp),
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        color = AmityTheme.colors.secondaryShade4,
                        shape = RoundedCornerShape(
                            topEnd = 12.dp,
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp,
                        )
                    )
            ) {
                AmityCommentComposerTextField(
                    value = localCommentText,
                    onValueChange = {
                        localCommentText = it
                    },
                    modifier = modifier
                        .fillMaxSize()
                        .focusRequester(focusRequester)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.align(Alignment.End)
            ) {
                AmityBaseElement(
                    componentScope = getComponentScope(),
                    elementId = "cancel_button"
                ) {
                    OutlinedButton(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = getElementScope().getConfig().getBackgroundColor(),
                            disabledContainerColor = AmityTheme.colors.primaryShade2,
                        ),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = modifier.height(30.dp),
                        onClick = {
                            onEditFinished()
                        }
                    ) {
                        Text(
                            text = getElementScope().getConfig().getValue("cancel_button_text"),
                            style = AmityTheme.typography.caption.copy(
                                color = AmityTheme.colors.baseShade1,
                            ),
                            modifier = modifier.testTag(getAccessibilityId())
                        )
                    }
                }

                AmityBaseElement(
                    componentScope = getComponentScope(),
                    elementId = "save_button"
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = getElementScope().getConfig().getBackgroundColor(),
                            disabledContainerColor = getElementScope().getConfig()
                                .getBackgroundColor().shade(AmityColorShade.SHADE2),
                        ),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        enabled = isAllowedToSave,
                        modifier = modifier.height(30.dp),
                        onClick = {
                            onEditFinished()

                            viewModel.editComment(
                                commentId = commentId,
                                commentText = localCommentText,
                                onSuccess = {
                                },
                                onError = {
                                    it.message?.let { message ->
                                        componentScope?.showSnackbar(message = message)
                                    }
                                }
                            )
                        }
                    ) {
                        Text(
                            text = getElementScope().getConfig().getValue("save_button_text"),
                            style = AmityTheme.typography.caption.copy(
                                color = AmityTheme.colors.baseInverse,
                            ),
                            modifier = modifier.testTag(getAccessibilityId())
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityEditCommentContainerPreview() {
    AmityUIKitConfigController.setup(LocalContext.current)
    AmityEditCommentContainer(
        commentId = "",
        commentText = "Hello World",
        onEditFinished = {},
    )
}
