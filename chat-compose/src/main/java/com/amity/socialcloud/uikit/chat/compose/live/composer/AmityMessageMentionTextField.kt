package com.amity.socialcloud.uikit.chat.compose.live.composer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityChannelMention
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageComposeView
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityUserMention
import com.amity.socialcloud.uikit.chat.compose.live.mention.AmityMentionSuggestion
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.linkedin.android.spyglass.mentions.Mentionable


@Composable
fun AmityMessageMentionTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    hint: String = "",
    maxChar: Int = 10000,
    maxLines: Int = 8,
    isEnabled: Boolean = true,
    addedMention: AmityMentionSuggestion? = null,
    shouldClearText: Boolean = false,
    mentionMetadata: List<AmityMentionMetadata> = emptyList(),
    mentionees: List<AmityMentionee> = emptyList(),
    editMentionMetadata: List<AmityMentionMetadata> = emptyList(),
    editMentionees: List<AmityMentionee> = emptyList(),
    onValueChange: (String) -> Unit = {},
    onMentionAdded: () -> Unit = {},
    onQueryToken: (String?) -> Unit = {},
    onMention: (List<AmityMentionMetadata>) -> Unit = {},
    onMentionLimitReached: () -> Unit = {},
    shouldRequestFocus: Boolean = false
) {
    val textColor = AmityTheme.colors.base.toArgb()
    val hintColor = AmityTheme.colors.baseShade1.toArgb()

    AndroidView(
        modifier = modifier
            .background(
                color = AmityTheme.colors.baseShade4,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp),
        factory = {
            AmityMessageComposeView(it).apply {
                this.maxLines = maxLines
                setMaxChars(maxChar)
                setTextCompose(value)
                setHint(hint)
                setMentionMetadata(mentionMetadata, mentionees)
                this.setStyle(textColor, hintColor)
                this.isEnabled = isEnabled
                this.onMentionLimitReached = onMentionLimitReached
                setQueryTokenReceiver { qt ->
                    if (qt.tokenString.startsWith(AmityUserMention.CHAR_MENTION)) {
                        onQueryToken(qt.keywords)
                    } else {
                        onQueryToken(null)
                    }
                    emptyList<String>()
                }

                addTextChangedListener { text ->
                    if (isApplyingMentionMetadata) return@addTextChangedListener
                    text?.let { textNonNull ->
                        if(!textNonNull.contains(AmityUserMention.CHAR_MENTION)) {
                            onQueryToken(null)
                        }
                        onValueChange(textNonNull.toString())
                    }

                    onMention(getMentions())
                }
                requestFocus()
            }
        },
        update = { view ->
            if (shouldClearText) {
                view.setTextCompose("")
            } else if (editMentionMetadata.isNotEmpty() && value.isNotEmpty() && view.text.toString() != value) {
                // Set text with mention highlights (for edit mode only)
                view.isApplyingMentionMetadata = true
                view.setTextCompose(value)
                view.setMentionMetadata(editMentionMetadata, editMentionees)
                view.isApplyingMentionMetadata = false
                // Sync back the actual text after mention metadata is applied
                val actualText = view.text.toString()
                if (actualText != value) {
                    onValueChange(actualText)
                }
                view.setSelection(view.text.length)
            } else if (value.isNotEmpty() && view.text.toString() != value && view.getMentions().isEmpty()) {
                // Set text only when there are no existing mentions (to avoid stripping spans)
                view.setTextCompose(value)
                view.setSelection(view.text.length)
            }

            addedMention?.let {
                val mentionable : Mentionable
                if(it is AmityMentionSuggestion.USER) {
                    mentionable = AmityUserMention(it.user)
                } else {
                    mentionable = AmityChannelMention()
                }
                view.insertMention(mentionable)
                onMentionAdded()
            }

            if (shouldRequestFocus) {
                view.requestFocus()
                val imm = view.context.getSystemService<InputMethodManager>()
                imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AmityMessageMentionTextFieldPreview() {
    AmityMessageMentionTextField()
}
