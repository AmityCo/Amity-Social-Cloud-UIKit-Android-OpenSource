package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.uikit.common.extionsions.extractUrls
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme


@Composable
fun AmityAnnotatedText(
    modifier: Modifier = Modifier,
    text: String,
    mentionGetter: AmityMentionMetadataGetter,
    mentionees: List<AmityMentionee>,
    style: TextStyle = AmityTheme.typography.bodyLegacy,
    onLongPress: () -> Unit = {},
) {
    val uriHandler = LocalUriHandler.current
    val annotatedText = buildAnnotatedString {
        append(text)
        mentionGetter.getMentionedUsers().forEach { mentionItem ->
            if (mentionees.any { (it as? AmityMentionee.USER)?.getUserId() == mentionItem.getUserId() }
                && mentionItem.getIndex() < text.length
            ) {
                val start = mentionItem.getIndex()
                val end = mentionItem.getIndex().plus(mentionItem.getLength()).inc()
                addStyle(
                    style = SpanStyle(AmityTheme.colors.highlight),
                    start = start,
                    end = end,
                )
                addStringAnnotation(
                    tag = "USER_MENTION",
                    annotation = mentionItem.getUserId(),
                    start = start,
                    end = end
                )
            }
        }
        mentionGetter.getMentionedChannels().forEach { mentionItem ->
            val start = mentionItem.getIndex()
            val end = mentionItem.getIndex().plus(mentionItem.getLength()).inc()
            addStyle(
                style = SpanStyle(AmityTheme.colors.highlight),
                start = start,
                end = end,
            )
            addStringAnnotation(
                tag = "CHANNEL_MENTION",
                annotation = "",
                start = start,
                end = end
            )
        }
        text.extractUrls().forEach {
            addStyle(
                style = SpanStyle(color = AmityTheme.colors.highlight, textDecoration = TextDecoration.Underline),
                start = it.start,
                end = it.end,
            )
            addStringAnnotation(
                tag = "URL",
                annotation = it.url,
                start = it.start,
                end = it.end
            )
        }
    }
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val pressIndicator = Modifier.pointerInput(onLongPress) {
        detectTapGestures(
            onPress = { offset ->
                layoutResult.value?.let {
                    val position = it.getOffsetForPosition(offset)
                    val annotations = annotatedText.getStringAnnotations(
                        tag = "URL",
                        start = position,
                        end = position
                    )
                    if (annotations.isNotEmpty()) {
                        val url = annotations.first().item
                        uriHandler.openUri(url)
//                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                        startActivity(context, intent, null)
                    }

                }
            },
            onLongPress = { offset ->
                onLongPress.invoke()
            }
        )
    }
    Text(
        text = annotatedText,
        style = style,
        modifier = modifier.then(pressIndicator),
        onTextLayout = {
            layoutResult.value = it
        }
    )
}