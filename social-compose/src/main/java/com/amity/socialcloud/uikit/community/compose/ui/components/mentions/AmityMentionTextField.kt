package com.amity.socialcloud.uikit.community.compose.ui.components.mentions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtag
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import kotlinx.coroutines.delay
import kotlin.text.compareTo

/**
 * A pure Compose implementation of mention text field
 */
@Composable
fun AmityMentionTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    hintText: String = "Share something...",
    maxLines: Int = 8,
    maxChar: Int = Int.MAX_VALUE,
    mentionedUser: AmityUser? = null,
    mentionMetadata: List<AmityMentionMetadata.USER> = emptyList(),
    mentionees: List<AmityMentionee> = emptyList(),
    hashtagMetadata: List<AmityHashtag> = emptyList(),
    onValueChange: (String) -> Unit = {},
    isEnabled: Boolean = true,
    onMentionAdded: () -> Unit = {},
    onHashtagAdded: (String) -> Unit = {},
    onQueryToken: (String?) -> Unit = {},
    onHashtagToken: (String?) -> Unit = {},
    onUserMentions: (List<AmityMentionMetadata.USER>) -> Unit = {},
    onHashtags: (List<AmityHashtag>) -> Unit = {},
    autoFocus: Boolean = false,
    shouldClearText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.Sentences
    ),
    // New customization parameters
    textStyle: TextStyle = LocalTextStyle.current.copy(
        color = AmityTheme.colors.base,
        fontSize = 15.sp
    ),
    verticalPadding: Dp = 12.dp,
    horizontalPadding: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(vertical = verticalPadding, horizontal = horizontalPadding),
    backgroundColor: Color = Color.Transparent,
    hintColor: Color = AmityTheme.colors.baseShade2,
    mentionColor: Color = AmityTheme.colors.highlight,
    hashtagColor: Color = AmityTheme.colors.highlight,
    cursorColor: Color = AmityTheme.colors.primary,
) {
    val maxHashtag = 30

    // Setup focus
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    // Remember if focus was requested to ensure we only do it once
    var hasFocusBeenRequested by remember { mutableStateOf(false) }

    // Track mentions internally
    var mentions by remember { mutableStateOf<List<MentionData>>(emptyList()) }

    // Track hashtags internally
    var hashtags by remember { mutableStateOf<List<AmityHashtag>>(emptyList()) }

    // Convert external mentions to our internal format
    val initialMentions by remember(mentionMetadata, mentionees) {
        derivedStateOf {
            mentionMetadata.mapNotNull { metadata ->
                val user = mentionees.filterIsInstance<AmityMentionee.USER>()
                    .find { it.getUser()?.getUserId() == metadata.getUserId() }
                    ?.getUser()

                if (user != null) {
                    MentionData(
                        userId = user.getUserId(),
                        displayName = user.getDisplayName() ?: "",
                        startPosition = metadata.getIndex(),
                        length = (user.getDisplayName() ?: "").length
                    )
                } else null
            }
        }
    }

    // Convert external hashtags to our internal format
    val initialHashtags by remember(hashtagMetadata) {
        derivedStateOf {
            hashtagMetadata.map { metadata ->
                AmityHashtag(
                    text = metadata.getText(),
                    index = metadata.getIndex(),
                    length = metadata.getLength()
                )
            }
        }
    }

    var showHashtagExceedDialog by remember { mutableStateOf(false) }

    // Initialize mentions from metadata
    LaunchedEffect(initialMentions) {
        if (initialMentions.isNotEmpty()) {
            mentions = initialMentions
        }
    }

    // Initialize hashtags from metadata
    LaunchedEffect(initialHashtags) {
        if (initialHashtags.isNotEmpty()) {
            hashtags = initialHashtags
        }
    }

    // Use TextFieldValue to maintain cursor position during updates
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length) // Initialize cursor at the end of text
            )
        )
    }

    // Auto focus logic with cursor position at end of text
    LaunchedEffect(autoFocus) {
        if (autoFocus && !hasFocusBeenRequested) {
            // Small delay to ensure the view is ready to receive focus
            delay(100)
            focusRequester.requestFocus()
            // Ensure cursor is at the end when gaining focus
            textFieldValue = TextFieldValue(
                text = textFieldValue.text,
                selection = TextRange(textFieldValue.text.length)
            )
            hasFocusBeenRequested = true
        }
    }

    // When value changes from outside (like initial setup)
    LaunchedEffect(value) {
        if (value != textFieldValue.text) {
            textFieldValue = TextFieldValue(
                text = value,
                selection = TextRange(value.length) // Keep cursor at end when text changes
            )
        }
    }

    // Capture theme colors once at composition time to pass to non-composable functions
    val mentionColorValue = mentionColor
    val hashtagColorValue = hashtagColor

    // Format the text with mention and hashtag highlighting
    val formattedText by remember(textFieldValue.text, mentions, hashtags, mentionColorValue, hashtagColorValue) {
        derivedStateOf {
            formatTextWithMentionsAndHashtags(
                text = textFieldValue.text,
                mentions = mentions,
                hashtags = if (onHashtags == {}) emptyList() else hashtags,
                mentionColor = mentionColorValue,
                hashtagColor = hashtagColorValue
            )
        }
    }

    // Process new mention
    LaunchedEffect(mentionedUser) {
        mentionedUser?.let { user ->
            val text = textFieldValue.text
            val selection = textFieldValue.selection.start

            // Find the @ character that triggered this mention
            val mentionStart = text.lastIndexOf('@', selection - 1)
            if (mentionStart >= 0) {
                // Calculate the positions
                val beforeMention = text.substring(0, mentionStart)
                val afterMention = if (selection < text.length) text.substring(selection) else ""
                val displayName = user.getDisplayName() ?: ""

                // Create new text with the mention inserted
                val newText = "$beforeMention@$displayName $afterMention"
                val newPosition = mentionStart + displayName.length + 2 // +2 for @ and space

                // Add to tracked mentions
                val newMention = MentionData(
                    userId = user.getUserId(),
                    displayName = displayName,
                    startPosition = mentionStart,
                    length = displayName.length
                )

                // Update mentions list with the new mention
                mentions = updateMentionPositions(mentions + newMention, mentionStart,
                    displayName.length + 1 - (selection - mentionStart))

                // Update text field value
                textFieldValue = TextFieldValue(
                    text = newText,
                    selection = TextRange(newPosition)
                )
                onValueChange(newText)

                // Update external mentions - important to pass back the updated mentions
                val mentionMetadata = mentions.map { mention ->
                    AmityMentionMetadata.USER(
                        mention.userId,
                        mention.startPosition,
                        mention.length
                    )
                }
                onUserMentions(mentionMetadata)

                delay(100) // Small delay to ensure UI updates
                onMentionAdded()
                onQueryToken(null)
            }
        }
    }

    // Handle text clearing when shouldClearText becomes true
    LaunchedEffect(shouldClearText) {
        if (shouldClearText) {
            textFieldValue = TextFieldValue(
                text = "",
                selection = TextRange(0)
            )
            mentions = emptyList()
            hashtags = emptyList()
            onValueChange("") // Notify parent about the cleared text
            onUserMentions(emptyList()) // Clear mentions in parent
            onHashtags(emptyList()) // Clear hashtags in parent
        }
    }

    // Main text field
    Box(modifier = modifier.fillMaxWidth().background(backgroundColor)) {
        BasicTextField(
            value = TextFieldValue(
                annotatedString = formattedText,
                selection = textFieldValue.selection
            ),
            onValueChange = { newValue ->
                // Enforce the character limit
                val limitedValue = if (newValue.text.length <= maxChar) {
                    newValue
                } else {
                    newValue.copy(
                        text = newValue.text.substring(0, maxChar),
                        selection = TextRange(maxChar)
                    )
                }

                // Only update text content, preserve the annotated string formatting
                if (limitedValue.text != textFieldValue.text) {
                    // Text has changed, update mentions
                    val oldText = textFieldValue.text
                    val newText = newValue.text

                    val diff = calculateTextDiff(oldText, limitedValue)

                    // Update mention positions if text changed
                    val updatedMentions = if (diff != null) {
                        updateMentionPositions(mentions, diff.position, diff.change)
                    } else mentions

                    // Update hashtag positions if text changed
                    var updatedHashtags = if (diff != null) {
                        updateHashtagPositions(hashtags, diff.position, diff.change)
                    } else hashtags

                    mentions = updatedMentions
                    hashtags = updatedHashtags

                    // Improved mention token detection logic
                    val hasActiveMention = detectActiveMentionToken(limitedValue)
                    val hasActiveHashtag = detectActiveHashtagToken(limitedValue)

                    if (hasActiveMention != null) {
                        // We found an active @ query token
                        onQueryToken(hasActiveMention)
                    } else {
                        // No active @ token, ensure suggestion list is hidden
                        onQueryToken(null)
                    }

                    if (hasActiveHashtag != null) {
                        // We found an active # query token
                        onHashtagToken(hasActiveHashtag)
                    } else {
                        // No active # token, ensure suggestion list is hidden
                        onHashtagToken(null)
                    }

                    textFieldValue = limitedValue
                    onValueChange(limitedValue.text)

                    // Update external mentions after text changes
                    val mentionMetadata = mentions.map { mention ->
                        AmityMentionMetadata.USER(
                            mention.userId,
                            mention.startPosition,
                            mention.length
                        )
                    }
                    onUserMentions(mentionMetadata)

                    // Update external hashtags after text changes
                    onHashtags(hashtags)

                    // --- Improved hashtag auto-detection on paste or any change ---
                    val hashtagRegex = "#\\w+".toRegex()
                    val detectedHashtags = hashtagRegex.findAll(limitedValue.text).map { it.value.removePrefix("#") to it.range.first }.toList()
                    if (detectedHashtags.isNotEmpty()) {
                        detectedHashtags.forEach { (tag, index) ->
                            if (hashtags.size < maxHashtag) {
                                if (index >= 0) {
                                    val newHashtag = AmityHashtag(tag, index, tag.length)
                                    if (hashtags.none { it.getText() == newHashtag.getText() && it.getIndex() == newHashtag.getIndex() }) {
                                        hashtags = hashtags + newHashtag
                                        onHashtags(hashtags)
                                        onHashtagAdded(tag)
                                    }
                                }
                            } else {
                                showHashtagExceedDialog = true
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            enabled = isEnabled,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            maxLines = maxLines,
            cursorBrush = SolidColor(cursorColor),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.padding(contentPadding)
                ) {
                    if (textFieldValue.text.isEmpty()) {
                        Text(
                            text = hintText,
                            color = hintColor,
                            style = textStyle
                        )
                    }
                    innerTextField()
                }
            }
        )
    }

    if (showHashtagExceedDialog) {
        HashtagsExceedDialog(
            onDismiss = {
                showHashtagExceedDialog = false
            }
        )
    }
}

/**
 * Format text with mentions and hashtags highlighted - NOT a composable function
 */
private fun formatTextWithMentionsAndHashtags(
    text: String,
    mentions: List<MentionData>,
    hashtags: List<AmityHashtag>,
    mentionColor: Color,
    hashtagColor: Color
): AnnotatedString {
    if (mentions.isEmpty() && hashtags.isEmpty()) return AnnotatedString(text)

    return buildAnnotatedString {
        var lastIndex = 0

        // Combine mentions and hashtags and sort by position
        val allHighlights = mutableListOf<TextHighlight>()
        mentions.forEach { mention ->
            allHighlights.add(
                TextHighlight(
                    start = mention.startPosition,
                    end = mention.startPosition + mention.length + 1, // +1 for @
                    color = mentionColor
                )
            )
        }
        hashtags.forEach { hashtag ->
            allHighlights.add(
                TextHighlight(
                    start = hashtag.getIndex(),
                    end = hashtag.getIndex() + hashtag.getLength() + 1, // +1 for #
                    color = hashtagColor
                )
            )
        }

        // Remove overlapping highlights before sorting
        val nonOverlappingHighlights = allHighlights.sortedBy { it.start }.fold(mutableListOf<TextHighlight>()) { acc, highlight ->
            if (acc.isEmpty() || highlight.start >= acc.last().end) {
                acc.add(highlight)
            }
            acc
        }

        // Sort by start position
        val sortedHighlights = nonOverlappingHighlights

        for (highlight in sortedHighlights) {
            // Validate indices
            if (highlight.end > text.length || highlight.start < 0 || highlight.start >= text.length) {
                continue
            }

            // Add text before this highlight
            if (highlight.start > lastIndex) {
                append(text.substring(lastIndex, highlight.start))
            }

            // Add highlighted text with styling
            withStyle(SpanStyle(
                color = highlight.color,
                textDecoration = TextDecoration.None
            )) {
                append(text.substring(highlight.start, highlight.end))
            }

            lastIndex = highlight.end
        }

        // Add any remaining text
        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
    }
}

/**
 * Helper class for text highlighting
 */
private data class TextHighlight(
    val start: Int,
    val end: Int,
    val color: Color
)

/**
 * Update hashtag positions when text changes
 */
private fun updateHashtagPositions(
    hashtags: List<AmityHashtag>,
    changePosition: Int,
    change: Int
): List<AmityHashtag> {
    if (change == 0 || hashtags.isEmpty()) return hashtags

    return hashtags.map { hashtag ->
        when {
            // If change happens before hashtag, adjust position
            hashtag.getIndex() > changePosition ->
                hashtag.copy(index = hashtag.getIndex() + change)

            // If change happens inside hashtag, remove that hashtag
            changePosition > hashtag.getIndex() &&
                    changePosition <= hashtag.getIndex() + hashtag.getLength() + 1 ->
                hashtag.copy(index = -1) // Mark for removal

            // Otherwise keep as is
            else -> hashtag
        }
    }.filter { it.getIndex() >= 0 } // Remove invalid hashtags
}

private fun detectActiveHashtagToken(value: TextFieldValue): String? {
    val text = value.text
    val cursorPosition = value.selection.start

    if (cursorPosition <= 0) return null

    // Check if cursor is right after an # character
    if (text.getOrNull(cursorPosition - 1) == '#') {
        return ""
    }

    // Look backwards from cursor for # token
    var index = cursorPosition - 1
    var foundAt = false
    var tokenStart = -1

    // Search backwards from cursor until we find # or a space/newline
    while (index >= 0) {
        val char = text[index]
        if (char == '#') {
            // Found # character
            foundAt = true
            tokenStart = index + 1
            break
        } else if (char == ' ' || char == '\n') {
            // Hit a whitespace before finding #, no active token
            break
        }
        index--
    }

    // If we found # and cursor is after it, return the token text
    return if (foundAt && cursorPosition > tokenStart) {
        text.substring(tokenStart, cursorPosition)
    } else {
        null
    }
}

/**
 * Detect completed hashtag based on space, newline, or other separator
 * Returns the newly completed hashtag if one is found
 */
private fun detectCompletedHashtag(value: TextFieldValue): AmityHashtag? {
    val text = value.text
    val cursorPosition = value.selection.start

    if (cursorPosition <= 1) return null

    // Check if a space, newline, or other separator was just added after a hashtag
    val currentChar = text.getOrNull(cursorPosition - 1)
    if (currentChar == '#' ||currentChar == ' ' || currentChar == '\n' || currentChar == null) {
        // Look backwards to find the start of a potential hashtag
        var index = cursorPosition - 2  // Skip the separator
        var hashIndex = -1

        // Go back until we find a # or another separator
        while (index >= 0) {
            val char = text[index]
            if (char == '#') {
                // Found the # character - this is our hashtag start
                // We simply take the first # we encounter going backward
                hashIndex = index
                break
            } else if (char == ' ' || char == '\n') {
                // Another separator, no hashtag
                break
            }
            index--
        }

        // If we found a hashtag, create and return it
        if (hashIndex >= 0) {
            if (hashIndex > 0) {
                if (text.getOrNull(hashIndex - 1)?.isWhitespace() != true) {
                    // If the previous character is also a #, this is not a valid hashtag
                    return null
                }
            }
            val text = text.substring(hashIndex + 1, cursorPosition - 1)
            if (text.isNotEmpty()) {
                return AmityHashtag(
                    text = text,
                    index = hashIndex,
                    length = text.length
                )
            }
        }
    }

    return null
}

/**
 * Format text with mentions highlighted - NOT a composable function
 */
private fun formatTextWithMentions(text: String, mentions: List<MentionData>, mentionColor: Color): AnnotatedString {
    if (mentions.isEmpty()) return AnnotatedString(text)

    return buildAnnotatedString {
        var lastIndex = 0
        val sortedMentions = mentions.sortedBy { it.startPosition }

        for (mention in sortedMentions) {
            val mentionStart = mention.startPosition
            val mentionEnd = mentionStart + mention.length + 1 // +1 for @

            // Validate indices
            if (mentionEnd > text.length || mentionStart < 0 || mentionStart >= text.length) {
                continue
            }

            // Add text before mention
            if (mentionStart > lastIndex) {
                append(text.substring(lastIndex, mentionStart))
            }

            // Add mention with styling - using the passed-in color
            withStyle(SpanStyle(
                color = mentionColor,
                textDecoration = TextDecoration.None
            )) {
                append(text.substring(mentionStart, mentionEnd))
            }

            lastIndex = mentionEnd
        }

        // Add any remaining text
        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
    }
}

/**
 * Calculate text changes between old and new text
 */
private data class TextDiff(val position: Int, val change: Int)

private fun calculateTextDiff(oldText: String, newValue: TextFieldValue): TextDiff? {
    val newText = newValue.text

    if (oldText == newText) return null

    // Find the position where text changed
    val minLength = minOf(oldText.length, newText.length)
    var diffStart = 0
    while (diffStart < minLength && oldText[diffStart] == newText[diffStart]) {
        diffStart++
    }

    // Calculate the change in length
    val change = newText.length - oldText.length

    return TextDiff(diffStart, change)
}

/**
 * Improved detection logic that handles all cases for @ token detection
 * Returns the query string if an active mention is being typed, null otherwise
 */
private fun detectActiveMentionToken(value: TextFieldValue): String? {
    val text = value.text
    val cursorPosition = value.selection.start

    if (cursorPosition <= 0) return null

    // Check if cursor is right after an @ character
    if (text.getOrNull(cursorPosition - 1) == '@') {
        return ""
    }

    // Look backwards from cursor for @ token
    var index = cursorPosition - 1
    var foundAt = false
    var tokenStart = -1

    // Search backwards from cursor until we find @ or a space/newline
    while (index >= 0) {
        val char = text[index]
        if (char == '@') {
            // Found @ character
            foundAt = true
            tokenStart = index + 1
            break
        } else if (char == ' ' || char == '\n') {
            // Hit a whitespace before finding @, no active token
            break
        }
        index--
    }

    // If we found @ and cursor is after it, return the token text
    return if (foundAt && cursorPosition > tokenStart) {
        text.substring(tokenStart, cursorPosition)
    } else {
        null
    }
}

/**
 * Update mention positions when text changes
 */
private fun updateMentionPositions(
    mentions: List<MentionData>,
    changePosition: Int,
    change: Int
): List<MentionData> {
    if (change == 0 || mentions.isEmpty()) return mentions

    return mentions.map { mention ->
        when {
            // If change happens before mention, adjust position
            mention.startPosition > changePosition ->
                mention.copy(startPosition = mention.startPosition + change)

            // If change happens inside mention, remove that mention
            changePosition > mention.startPosition &&
                    changePosition <= mention.startPosition + mention.length + 1 ->
                mention.copy(startPosition = -1) // Mark for removal

            // Otherwise keep as is
            else -> mention
        }
    }.filter { it.startPosition >= 0 } // Remove invalid mentions
}

/**
 * Data class to track mention information
 */
data class MentionData(
    val userId: String,
    val displayName: String, // Never null, use empty string as fallback
    val startPosition: Int,
    val length: Int // Derived from displayName.length
)


@Composable
fun HashtagsExceedDialog(
    onDismiss: () -> Unit = {},
) {
    AmityAlertDialog(
        dialogTitle = "Hashtags limit reached",
        dialogText = "You can only add hashtag up to 30 hashtags to per post.",
        dismissText = stringResource(R.string.amity_ok),
        onDismissRequest = onDismiss,
    )
}