package com.amity.socialcloud.uikit.common.ui.elements

import android.content.Context
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtag
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtagMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.uikit.common.common.views.text.AmityTextStyle
import com.amity.socialcloud.uikit.common.extionsions.extractUrls
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.google.gson.Gson
import com.google.gson.JsonObject

@Composable
fun AmityExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    mentionGetter: AmityMentionMetadataGetter = AmityMentionMetadataGetter(JsonObject()),
    mentionees: List<AmityMentionee> = emptyList(),
    hashtagGetter: AmityHashtagMetadataGetter = AmityHashtagMetadataGetter(JsonObject()),
    boldWhenMatches: List<String> = emptyList(),
    style: TextStyle = AmityTheme.typography.body,
    previewLines: Int = EXPANDABLE_TEXT_MAX_LINES,
    intialExpand: Boolean = false,
    onClick: () -> Unit = {},
    onMentionedUserClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onUrlClick: ((Context, String) -> Unit)? = null,
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

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
            // Highlight hashtags (not clickable)
            hashtagGetter.getHashtags().forEach { hashtagItem ->
                val start = hashtagItem.getIndex()
                val end = hashtagItem.getIndex().plus(hashtagItem.getLength()).inc()
                addStyle(
                    style = SpanStyle(AmityTheme.colors.highlight),
                    start = start,
                    end = end,
                )
                addStringAnnotation(
                    tag = "HASHTAG",
                    annotation = '#' + hashtagItem.getText(),
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
                val matches = findMatchIndices(
                    input = displayText,
                    pattern = it,
                    ignoreCase = true,
                    findFirstOnly = true,
                    exactMatch = it.startsWith('#')
                )
                val match = matches.firstOrNull()
                if (match == null) return@forEach

                addStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = AmityTheme.colors.highlight,
                        background = AmityTheme.colors.highlight.copy(
                            alpha = 0.1f
                        )
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

            if (!isReadMoreClicked) {
                withStyle(style = SpanStyle(
                    color = AmityTheme.colors.primary,
                    textDecoration = TextDecoration.Underline
                )) {
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

                        val hashtag = annotatedString.getStringAnnotations(
                            tag = "HASHTAG",
                            start = position,
                            end = position
                        )

                        if (hashtag.isNotEmpty()) {
                            onHashtagClick(hashtag.first().item)
                            return@let
                        }

                        val annotations = annotatedString.getStringAnnotations(
                            tag = "URL",
                            start = position,
                            end = position
                        )
                        if (annotations.isNotEmpty()) {
                            val url = annotations.first().item
                            onUrlClick
                                ?.invoke(context, url)
                                ?: uriHandler.openUri(url)
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

private fun findMatchIndices(
    input: String,
    pattern: String,
    ignoreCase: Boolean = true,
    findFirstOnly: Boolean = true,
    exactMatch: Boolean = false,
): List<Pair<Int, Int>> {
    val adjustedPattern = if (exactMatch) {
        "$pattern\\b" // Add word boundaries for exact match
    } else {
        pattern
    }

    val regex = if (ignoreCase) {
        Regex(adjustedPattern, RegexOption.IGNORE_CASE)
    } else {
        Regex(adjustedPattern)
    }

    return if (findFirstOnly) {
        // Find the first match only
        regex.find(input)?.let { matchResult ->
            listOf(matchResult.range.first to matchResult.range.last + 1) // +1 for exclusive end
        } ?: emptyList()
    } else {
        // Find all matches
        regex.findAll(input).map { matchResult ->
            matchResult.range.first to matchResult.range.last + 1
        }.toList()
    }
}

private fun getTrimmedText(
    text: String,
    textLayoutResult: TextLayoutResult,
    visiblePreviewLines: Int,
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
            newText
        } else {
            newText + "\n"
        }
    } else {
        text
    }
}

private const val readMore = "See more"
const val EXPANDABLE_TEXT_MAX_LINES = 8

@Preview(showBackground = true)
@Composable
fun AmityExpandableTextPreview() {
    AmityExpandableText(
        text = "www.google.com Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur neque urna, malesuada sit amet mattis sit amet, fringilla vitae eros. Phasellus tristique dolor ut nulla tincidunt sollicitudin. Sed eu bibendum nibh. Cras sed ligula nunc. Fusce mollis hendrerit erat, in tempus nisl rhoncus nec. Vivamus vel dictum lectus. Sed suscipit ante sit amet nulla hendrerit, at tincidunt odio suscipit. Nam cursus malesuada eros, et aliquet sem. Quisque ligula nunc, aliquet sit amet scelerisque eleifend, cursus ut nisl. Sed condimentum eleifend sollicitudin. Nam nec magna egestas, ullamcorper diam in, eleifend justo. Quisque aliquam elit sollicitudin, viverra ex non, ultrices erat. Morbi fermentum, turpis et accumsan ultrices, felis metus posuere sem, at feugiat mi velit quis risus.",
        mentionGetter = AmityMentionMetadataGetter(JsonObject()),
        mentionees = emptyList(),
        style = AmityTheme.typography.body,
        onClick = {}
    )
}


class HashtagMetadataGetter constructor(private val metadata: JsonObject) {

    fun getHashtags(): List<AmityHashtag> {
        return try {
            metadata.getAsJsonArray("hashtags")
        } catch (e: Exception) {
            // Return null if no hashtags are found or if the metadata is malformed
            null
        }?.let { hashtags ->
            try {
                hashtags.map {
                    Gson().fromJson(it, AmityHashtag::class.java)
                }
            } catch (e: Exception) {
                // If parsing fails, return an empty list
                emptyList()
            }
        } ?: emptyList()
    }
}