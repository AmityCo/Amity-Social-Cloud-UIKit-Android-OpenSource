package com.amity.socialcloud.uikit.community.compose.comment.query.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getBackgroundColor
import com.amity.socialcloud.uikit.common.utils.getValue
import com.amity.socialcloud.uikit.common.utils.shade
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.ui.components.mentions.AmityMentionTextField
import com.amity.socialcloud.uikit.community.compose.ui.components.mentions.AmityMentionSuggestionView
import com.google.gson.JsonObject

@Composable
fun AmityEditCommentContainer(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    comment: AmityComment,
    onEditFinished: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val mentionGetter = remember {
        AmityMentionMetadataGetter(comment.getMetadata() ?: JsonObject())
    }
    val commentText = remember {
        (comment.getData() as? AmityComment.Data.TEXT)?.getText() ?: ""
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommentTrayComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    var localCommentText by remember { mutableStateOf(commentText) }
    val isAllowedToSave by remember {
        derivedStateOf { localCommentText != commentText && localCommentText.isNotEmpty() }
    }

    var shouldShowSuggestion by remember { mutableStateOf(false) }
    var queryToken by remember { mutableStateOf("") }

    var selectedUserToMention by remember { mutableStateOf<AmityUser?>(null) }
    var mentionedUsers by remember { mutableStateOf<List<AmityMentionMetadata.USER>>(emptyList()) }
    val context = LocalContext.current

    // Define character limit constant for comments
    val COMMENT_MAX_CHAR_LIMIT = 50000

    AmityBaseComponent(
        modifier = modifier,
        componentId = "edit_comment_component"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .background(AmityTheme.colors.background)
                .padding(top = 4.dp),
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(
                            topEnd = 12.dp,
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp,
                        )
                    )
            ) {
                AmityMentionTextField(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp) // Match exact padding from original component
                        .testTag(getAccessibilityId("text_field")),
                    value = localCommentText,
                    mentionedUser = selectedUserToMention,
                    mentionMetadata = mentionGetter.getMentionedUsers(),
                    mentionees = comment.getMentionees(),
                    onValueChange = {
                        localCommentText = it
                    },
                    onMentionAdded = {
                        selectedUserToMention = null
                    },
                    onQueryToken = {
                        queryToken = it ?: ""
                        shouldShowSuggestion = (it != null) // This will hide suggestions when token is null
                    },
                    onUserMentions = {
                        mentionedUsers = it
                    },
                    autoFocus = true, // Enable auto-focus for edit container
                )
            }
            if (shouldShowSuggestion) {
                AmityMentionSuggestionView(
                    modifier = modifier,
                    community = viewModel.community,
                    keyword = queryToken,
                ) {
                    selectedUserToMention = it
                    shouldShowSuggestion = false
                }
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
                            containerColor = Color.Transparent,
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
                            style = AmityTheme.typography.captionLegacy.copy(
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
                            // Check character limit before proceeding
                            if (localCommentText.length > COMMENT_MAX_CHAR_LIMIT) {
                                AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                    context.getString(R.string.amity_add_comment_exceed_error_message, COMMENT_MAX_CHAR_LIMIT)
                                )
                                return@Button
                            }

                            onEditFinished()

                            viewModel.editComment(
                                commentId = comment.getCommentId(),
                                commentText = localCommentText,
                                userMentions = mentionedUsers,
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
                            style = AmityTheme.typography.captionLegacy.copy(
                                color = Color.White,
                            ),
                            modifier = modifier.testTag(getAccessibilityId())
                        )
                    }
                }
            }
        }
    }
}