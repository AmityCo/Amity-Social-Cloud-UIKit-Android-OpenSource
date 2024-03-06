package com.amity.socialcloud.uikit.community.compose.ui.elements

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme
import com.google.gson.JsonObject

@Composable
fun AmityExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    mentionGetter: AmityMentionMetadataGetter,
    mentionees: List<AmityMentionee>,
    style: TextStyle = AmityTheme.typography.body,
) {
    BoxWithConstraints(
        modifier = modifier,
    ) {
        val textMeasurer = rememberTextMeasurer()
        val textLayoutResult = remember(text) {
            textMeasurer.measure(
                text = text,
                style = style,
                constraints = Constraints(maxWidth = this.constraints.maxWidth)
            )
        }
        val trimmedText by rememberSaveable(text) {
            mutableStateOf(
                getTrimmedText(
                    text = text,
                    textLayoutResult = textLayoutResult
                )
            )
        }
        var isReadMoreClicked by rememberSaveable(text, trimmedText) {
            mutableStateOf(text == trimmedText)
        }

        val annotatedString = buildAnnotatedString {
            val displayText = if (isReadMoreClicked) text else trimmedText
            append(displayText)

            mentionGetter.getMentionedUsers().forEach { mentionItem ->
                if (mentionees.any { (it as? AmityMentionee.USER)?.getUserId() == mentionItem.getUserId() }
                    && mentionItem.getIndex() < displayText.length
                ) {
                    addStyle(
                        style = SpanStyle(AmityTheme.colors.primary),
                        start = mentionItem.getIndex(),
                        end = mentionItem.getIndex().plus(mentionItem.getLength()).inc(),
                    )
                }
            }

            if (!isReadMoreClicked) {
                withStyle(style = SpanStyle(AmityTheme.colors.primary)) {
                    pushStringAnnotation(tag = readMore, annotation = readMore)
                    append(readMore)
                }
            }
        }

        ClickableText(
            text = annotatedString,
            style = style,
            onClick = { offset ->
                annotatedString.getStringAnnotations(
                    tag = readMore,
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    isReadMoreClicked = true
                }
            },
            modifier = modifier
        )
    }
}

private fun getTrimmedText(text: String, textLayoutResult: TextLayoutResult): String {
    return if (textLayoutResult.lineCount >= visiblePreviewLines) {
        val startIndex = textLayoutResult.getLineStart(visiblePreviewLines - 1)
        val endIndex = textLayoutResult.getLineEnd(visiblePreviewLines - 1)
        val lastLine = text.substring(startIndex, endIndex)
        if (lastLine.length > 25) {
            val lengthToReduce = readMore.length * 3 / 2
            text.substring(0, endIndex - lengthToReduce)
        } else {
            text.substring(0, endIndex)
        }
    } else {
        text
    }
}

private const val readMore = "…See more"
private const val visiblePreviewLines = 8

@Preview(showBackground = true)
@Composable
fun AmityExpandableTextPreview() {
    AmityExpandableText(
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur neque urna, malesuada sit amet mattis sit amet, fringilla vitae eros. Phasellus tristique dolor ut nulla tincidunt sollicitudin. Sed eu bibendum nibh. Cras sed ligula nunc. Fusce mollis hendrerit erat, in tempus nisl rhoncus nec. Vivamus vel dictum lectus. Sed suscipit ante sit amet nulla hendrerit, at tincidunt odio suscipit. Nam cursus malesuada eros, et aliquet sem. Quisque ligula nunc, aliquet sit amet scelerisque eleifend, cursus ut nisl. Sed condimentum eleifend sollicitudin. Nam nec magna egestas, ullamcorper diam in, eleifend justo. Quisque aliquam elit sollicitudin, viverra ex non, ultrices erat. Morbi fermentum, turpis et accumsan ultrices, felis metus posuere sem, at feugiat mi velit quis risus.",
        mentionGetter = AmityMentionMetadataGetter(JsonObject()),
        mentionees = emptyList(),
        style = AmityTheme.typography.body
    )
}