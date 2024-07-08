package com.amity.socialcloud.uikit.community.compose.ui.components.mentions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme


@Composable
fun AmityMentionTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    hintText: String = "Say something nice...",
    maxLines: Int = 8,
    mentionedUser: AmityUser? = null,
    shouldClearText: Boolean = false,
    mentionMetadata: List<AmityMentionMetadata.USER> = emptyList(),
    mentionees: List<AmityMentionee> = emptyList(),
    onValueChange: (String) -> Unit = {},
    onMentionAdded: () -> Unit = {},
    onQueryToken: (String?) -> Unit = {},
    onUserMentions: (List<AmityMentionMetadata.USER>) -> Unit = {},
) {
    val baseColor = AmityTheme.colors.base.toArgb()
    val baseShade2Color = AmityTheme.colors.baseShade2.toArgb()

    AndroidView(
        modifier = modifier,
        factory = {
            AmityCommentComposeView(it).apply {
                this.maxLines = maxLines
                setTextCompose(value)
                setStyle(
                    textColor = baseColor,
                    hintColor = baseShade2Color,
                )
                setHintText(hintText)

                setMentionMetadata(mentionMetadata, mentionees)

                setQueryTokenReceiver { qt ->
                    if (qt.tokenString.startsWith(AmityUserMention.CHAR_MENTION)) {
                        onQueryToken(qt.keywords)
                    } else {
                        onQueryToken(null)
                    }
                    emptyList<String>()
                }

                addTextChangedListener { text ->
                    text?.let { textNonNull ->
                        onValueChange(textNonNull.toString())
                    }

                    onUserMentions(getUserMentions())
                }
            }
        },
        update = { view ->
            if (shouldClearText) {
                view.setTextCompose("")
            }

            if (value.isNotEmptyOrBlank() && view.text.toString() != value) {
                view.setTextCompose(value)
                view.setMentionMetadata(mentionMetadata, mentionees)
            }

            mentionedUser?.let {
                view.insertMention(AmityUserMention(it))
                onMentionAdded()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AmityMentionTextFieldPreview() {
    AmityMentionTextField()
}
