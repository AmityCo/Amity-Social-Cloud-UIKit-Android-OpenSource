package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme


@Composable
fun AmityMentionTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    maxLines: Int = 8,
    mentionedUser: AmityUser? = null,
    shouldClearText: Boolean = false,
    mentionMetadata: List<AmityMentionMetadata> = emptyList(),
    mentionees: List<AmityMentionee> = emptyList(),
    onValueChange: (String) -> Unit = {},
    onMentionAdded: () -> Unit = {},
    onQueryToken: (String?) -> Unit = {},
    onMention: (List<AmityMentionMetadata>) -> Unit = {},
) {
    AndroidView(
        modifier = modifier
            .background(
                color = AmityTheme.colors.secondaryShade4,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp),
        factory = {
            AmityMessageComposeView(it).apply {
                this.maxLines = maxLines
                setTextCompose(value)

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

                    onMention(getMentions())
                }
            }
        },
        update = { view ->
            if (shouldClearText) {
                view.setTextCompose("")
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