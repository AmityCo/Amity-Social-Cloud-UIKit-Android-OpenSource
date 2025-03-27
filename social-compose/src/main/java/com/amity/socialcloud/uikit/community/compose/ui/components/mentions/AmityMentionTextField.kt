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
import androidx.compose.ui.platform.LocalFocusManager
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
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import kotlinx.coroutines.delay

/**
 * A pure Compose implementation of mention text field
 */
@Composable
fun AmityMentionTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    hintText: String = "Say something nice...",
    maxLines: Int = 8,
    mentionedUser: AmityUser? = null,
    mentionMetadata: List<AmityMentionMetadata.USER> = emptyList(),
    mentionees: List<AmityMentionee> = emptyList(),
    onValueChange: (String) -> Unit = {},
    isEnabled: Boolean = true,
    onMentionAdded: () -> Unit = {},
    onQueryToken: (String?) -> Unit = {},
    onUserMentions: (List<AmityMentionMetadata.USER>) -> Unit = {},
    autoFocus: Boolean = false,
    shouldClearText: Boolean = false,  // Add shouldClearText parameter
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
) {
    // Setup focus
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    
    // Remember if focus was requested to ensure we only do it once
    var hasFocusBeenRequested by remember { mutableStateOf(false) }
    
    // Track mentions internally
    var mentions by remember { mutableStateOf<List<MentionData>>(emptyList()) }
    
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
    
    // Initialize mentions from metadata
    LaunchedEffect(initialMentions) {
        if (initialMentions.isNotEmpty()) {
            mentions = initialMentions
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

    // Capture theme color once at composition time to pass to non-composable functions
    val mentionColor = AmityTheme.colors.primary
    
    // Format the text with mention highlighting
    val formattedText by remember(textFieldValue.text, mentions, mentionColor) {
        derivedStateOf {
            formatTextWithMentions(textFieldValue.text, mentions, mentionColor)
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
            onValueChange("") // Notify parent about the cleared text
            onUserMentions(emptyList()) // Clear mentions in parent
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
                // Only update text content, preserve the annotated string formatting
                if (newValue.text != textFieldValue.text) {
                    // Text has changed, update mentions
                    val oldText = textFieldValue.text
                    val newText = newValue.text
                    val diff = calculateTextDiff(oldText, newValue)
                    
                    // Update mention positions if text changed
                    val updatedMentions = if (diff != null) {
                        updateMentionPositions(mentions, diff.position, diff.change)
                    } else mentions
                    
                    mentions = updatedMentions
                }
                
                // Improved mention token detection logic
                val hasActiveQuery = detectActiveMentionToken(newValue)
                
                if (hasActiveQuery != null) {
                    // We found an active @ query token
                    onQueryToken(hasActiveQuery)
                } else {
                    // No active @ token, ensure suggestion list is hidden
                    onQueryToken(null)
                }
                
                textFieldValue = TextFieldValue(
                    text = newValue.text,
                    selection = newValue.selection
                )
                onValueChange(newValue.text)
                
                // Always update external mentions after text changes
                val mentionMetadata = mentions.map { mention ->
                    AmityMentionMetadata.USER(
                        mention.userId,
                        mention.startPosition,
                        mention.length
                    )
                }
                onUserMentions(mentionMetadata)
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            enabled = isEnabled,
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            maxLines = maxLines,
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

