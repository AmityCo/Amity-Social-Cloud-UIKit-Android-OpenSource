package com.amity.socialcloud.uikit.community.compose.comment.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.comment.elements.AmityCommentAvatarView
import com.amity.socialcloud.uikit.community.compose.comment.elements.AmityCommentComposerTextField
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme

@Composable
fun AmityCommentComposerBar(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    reference: AmityComment.Reference,
    avatarUrl: String? = null,
    replyComment: AmityComment? = null,
    onClose: () -> Unit
) {
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommentTrayComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    var commentText by remember { mutableStateOf("") }
    val shouldAllowToPost by remember {
        derivedStateOf { commentText.isNotEmpty() }
    }
    var isCommentPosted by remember { mutableStateOf(false) }
    var isReplyingToComment by remember { mutableStateOf(false) }

    LaunchedEffect(replyComment) {
        isReplyingToComment = replyComment != null
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            color = AmityTheme.colors.divider
        )

        if (replyComment != null && isReplyingToComment) {
            AmityCommentComposeReplyLabel(
                displayName = replyComment.getCreator()?.getDisplayName() ?: "",
            ) {
                isReplyingToComment = false
                onClose()
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AmityCommentAvatarView(
                avatarUrl = avatarUrl,
                size = 32.dp,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(bottom = 6.dp)
                    .testTag("comment_tray_component/comment_composer_avatar")
            )

            AmityCommentComposerTextField(
                shouldClearText = isCommentPosted,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .testTag("comment_tray_component/comment_composer_text_field"),
                onValueChange = {
                    commentText = it
                }
            )

            Text(
                text = "Post",
                style = AmityTheme.typography.body.copy(
                    color = if (shouldAllowToPost) AmityTheme.colors.primary
                    else AmityTheme.colors.primaryShade2,
                ),
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(start = 4.dp, bottom = 12.dp)
                    .testTag("comment_tray_component/comment_composer_post_button")
                    .clickable(enabled = shouldAllowToPost) {
                        isCommentPosted = true

                        viewModel.addComment(
                            reference = reference,
                            commentText = commentText,
                            replyCommentId = if (isReplyingToComment) replyComment?.getCommentId() else null,
                            onSuccess = {
                                commentText = ""
                                isReplyingToComment = false
                                isCommentPosted = false
                                onClose()
                            },
                            onError = {
                                commentText = ""
                                isReplyingToComment = false
                                isCommentPosted = false
                                onClose()

                                val errorMessage =
                                    if (AmityError.from(it) == AmityError.BAN_WORD_FOUND) {
                                        context.getString(R.string.amity_add_blocked_words_comment_error_message)
                                    } else {
                                        if (isReplyingToComment) {
                                            context.getString(R.string.amity_add_reply_error_message)
                                        } else {
                                            context.getString(R.string.amity_add_comment_error_message)
                                        }
                                    }
                                componentScope?.showSnackbar(errorMessage)
                            }
                        )
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityCommentComposerBarPreview() {
    AmityCommentComposerBar(
        reference = AmityComment.Reference.STORY(""),
    ) {}
}