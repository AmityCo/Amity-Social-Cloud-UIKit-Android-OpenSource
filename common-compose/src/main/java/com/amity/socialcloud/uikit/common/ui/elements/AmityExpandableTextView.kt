package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.uikit.common.extionsions.extractUrls
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.google.gson.JsonObject

@Composable
fun AmityExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    mentionGetter: AmityMentionMetadataGetter = AmityMentionMetadataGetter(JsonObject()),
    mentionees: List<AmityMentionee> = emptyList(),
    boldWhenMatches: List<String> = emptyList(),
    style: TextStyle = AmityTheme.typography.bodyLegacy,
    previewLines: Int = 8,
    intialExpand: Boolean = false,
    onClick: () -> Unit = {},
    onMentionedUserClick: (String) -> Unit = {},
) {
    val uriHandler = LocalUriHandler.current

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
                    textLayoutResult = textLayoutResult,
                    visiblePreviewLines = previewLines,
                )
            )
        }
        var isReadMoreClicked by rememberSaveable(text, trimmedText) {
            mutableStateOf(intialExpand || text == trimmedText)
        }

        val annotatedString = buildAnnotatedString {
            val displayText = if (isReadMoreClicked) text else trimmedText
            append(displayText)

            mentionGetter.getMentionedUsers().forEach { mentionItem ->
                if (mentionees.any { (it as? AmityMentionee.USER)?.getUserId() == mentionItem.getUserId() }
                    && mentionItem.getIndex() < displayText.length
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
                    style = SpanStyle(
                        color = AmityTheme.colors.highlight
                    ),
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

            boldWhenMatches.forEach {
                val matches = findMatchIndices(displayText, it)
                matches.forEach { match ->
                    addStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = AmityTheme.colors.base
                        ),
                        start = match.first,
                        end = match.second,
                    )
                    addStringAnnotation(
                        tag = "BOLDED",
                        annotation = it,
                        start = match.first,
                        end = match.second
                    )
                }
            }

            if (!isReadMoreClicked) {
                append("...")
                withStyle(style = SpanStyle(AmityTheme.colors.primary)) {
                    pushStringAnnotation(tag = readMore, annotation = readMore)
                    append(readMore)
                }
            }
        }

        val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
        val pressIndicator = Modifier.pointerInput(annotatedString) {
            detectTapGestures(
                onTap = { offset ->
                    layoutResult.value?.let {
                        val position = it.getOffsetForPosition(offset)
                        val mentionedUser = annotatedString.getStringAnnotations(
                            tag = "USER_MENTION",
                            start = position,
                            end = position
                        )

                        if (mentionedUser.isNotEmpty()) {
                            val userId = mentionedUser.first().item
                            onMentionedUserClick(userId)
                            return@let
                        }

                        val annotations = annotatedString.getStringAnnotations(
                            tag = "URL",
                            start = position,
                            end = position
                        )
                        if (annotations.isNotEmpty()) {
                            val url = annotations.first().item
                            uriHandler.openUri(url)
                            return@let
                        }

                        val seeMoreAnnotation = annotatedString.getStringAnnotations(
                            tag = readMore,
                            start = position,
                            end = position
                        )
                        if (seeMoreAnnotation.isNotEmpty()) {
                            isReadMoreClicked = true
                            return@let
                        }

                        onClick()
                    }
                }
            )
        }

        Text(
            text = annotatedString,
            style = style,
            modifier = modifier.then(pressIndicator),
            onTextLayout = {
                layoutResult.value = it
            }
        )
    }
}

private fun findMatchIndices(input: String, pattern: String): List<Pair<Int, Int>> {
    val regex = Regex(pattern)
    return regex.findAll(input).map { matchResult ->
        matchResult.range.first to matchResult.range.last + 1 // +1 for exclusive end
    }.toList()
}

private fun getTrimmedText(
    text: String,
    textLayoutResult: TextLayoutResult,
    visiblePreviewLines: Int
): String {
    return if (textLayoutResult.lineCount >= visiblePreviewLines) {
        val startIndex = textLayoutResult.getLineStart(visiblePreviewLines - 1)
        val endIndex = textLayoutResult.getLineEnd(visiblePreviewLines - 1)
        val lastLine = text.substring(startIndex, endIndex)

        val newText = if (lastLine.length > 25) {
            val lengthToReduce = readMore.length * 3 / 2
            text.substring(0, endIndex - lengthToReduce)
        } else {
            text.substring(0, endIndex)
        }

        if (newText.endsWith("\n")) {
            newText.replaceRange(endIndex - 1, endIndex, "")
        } else {
            newText
        }
    } else {
        text
    }
}

private const val readMore = "See more"

@Preview(showBackground = true)
@Composable
fun AmityExpandableTextPreview() {
    AmityExpandableText(
        text = "www.google.com Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur neque urna, malesuada sit amet mattis sit amet, fringilla vitae eros. Phasellus tristique dolor ut nulla tincidunt sollicitudin. Sed eu bibendum nibh. Cras sed ligula nunc. Fusce mollis hendrerit erat, in tempus nisl rhoncus nec. Vivamus vel dictum lectus. Sed suscipit ante sit amet nulla hendrerit, at tincidunt odio suscipit. Nam cursus malesuada eros, et aliquet sem. Quisque ligula nunc, aliquet sit amet scelerisque eleifend, cursus ut nisl. Sed condimentum eleifend sollicitudin. Nam nec magna egestas, ullamcorper diam in, eleifend justo. Quisque aliquam elit sollicitudin, viverra ex non, ultrices erat. Morbi fermentum, turpis et accumsan ultrices, felis metus posuere sem, at feugiat mi velit quis risus.",
        mentionGetter = AmityMentionMetadataGetter(JsonObject()),
        mentionees = emptyList(),
        style = AmityTheme.typography.bodyLegacy,
        onClick = {}
    )
}