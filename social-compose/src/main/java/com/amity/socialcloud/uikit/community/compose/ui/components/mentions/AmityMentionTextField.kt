package com.amity.socialcloud.uikit.community.compose.ui.components.mentions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
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
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.producttag.AmityProductTag
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.extionsions.extractUrls
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.text.compareTo
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize

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
    mentionedProduct: AmityProduct? = null,
    mentionMetadata: List<AmityMentionMetadata.USER> = emptyList(),
    mentionees: List<AmityMentionee> = emptyList(),
    productMetadata: List<AmityProductTag.Text> = emptyList(), // For edit mode - product tags from existing post
    hashtagMetadata: List<AmityHashtag> = emptyList(),
    onValueChange: (String) -> Unit = {},
    isEnabled: Boolean = true,
    onMentionAdded: () -> Unit = {},
    onHashtagAdded: (String) -> Unit = {},
    onQueryToken: (String?) -> Unit = {},
    onHashtagToken: (String?) -> Unit = {},
    onUserMentions: (List<AmityMentionMetadata.USER>) -> Unit = {},
    onProductMentions: (List<ProductMentionData>) -> Unit = {},
    onHashtags: (List<AmityHashtag>) -> Unit = {},
    autoFocus: Boolean = false,
    focusTrigger: Any? = null,
    shouldClearText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.Sentences
    ),
    // Product tag limit enforcement
    totalProductTagCount: Int = 0,
    maxProductTags: Int = Int.MAX_VALUE,
    onProductTagLimitReached: () -> Unit = {},
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
    enableUrlHighlighting: Boolean = false,
    urlColor: Color = AmityTheme.colors.primary,
    urlHighlights: List<UrlHighlight> = emptyList(),
    onUrlsDetected: (List<UrlHighlight>) -> Unit = {},
    // Floating mention suggestion anchor (caret bounds in window coordinates)
    onMentionAnchorChanged: (Rect?) -> Unit = {},
    // Optional: render a floating mention suggestions UI anchored to the caret
    mentionSuggestions: (@Composable (onDismiss: () -> Unit) -> Unit)? = null,
) {
    val maxHashtag = 30

    // Setup focus
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    // Track mentions internally
    var mentions by remember { mutableStateOf<List<MentionData>>(emptyList()) }

    // Track product mentions (for highlighting only; no metadata)
    var productMentions by remember { mutableStateOf<List<ProductMentionData>>(emptyList()) }

    // Track hashtags internally
    var hashtags by remember { mutableStateOf<List<AmityHashtag>>(emptyList()) }

    // Track detected URLs for highlighting
    var detectedUrls by remember(urlHighlights) { mutableStateOf<List<UrlHighlight>>(urlHighlights) }

    // When true the mention suggestion popup has been explicitly dismissed by the user and should
    // stay hidden until a fresh '@' character is typed.
    var mentionDismissed by remember { mutableStateOf(false) }

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

    // Convert external product metadata to our internal format (for edit mode)
    val initialProductMentions by remember(productMetadata) {
        derivedStateOf {
            productMetadata.map { tag ->
                ProductMentionData(
                    productId = tag.productId,
                    displayName = tag.text,
                    startPosition = tag.index,
                    length = tag.length,
                    product = tag.product
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

    // Initialize product mentions from metadata (for edit mode)
    LaunchedEffect(initialProductMentions) {
        if (initialProductMentions.isNotEmpty()) {
            productMentions = initialProductMentions
            // Emit product mentions to parent for tag aggregation
            onProductMentions(productMentions)
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
    LaunchedEffect(autoFocus, focusTrigger) {
        if (autoFocus) {
            // Small delay to ensure the view is ready to receive focus
            delay(100)
            focusRequester.requestFocus()
            // Ensure cursor is at the end when gaining focus
            textFieldValue = TextFieldValue(
                text = textFieldValue.text,
                selection = TextRange(textFieldValue.text.length)
            )
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
    val urlColorValue = urlColor

    // Format the text with mention, hashtag, and URL highlighting
    val formattedText by remember(textFieldValue.text, mentions, productMentions, hashtags, detectedUrls, mentionColorValue, hashtagColorValue, urlColorValue, enableUrlHighlighting) {
        derivedStateOf {
            formatTextWithMentionsHashtagsAndUrls(
                text = textFieldValue.text,
                mentions = mentions,
                productMentions = productMentions,
                hashtags = if (onHashtags == {}) emptyList() else hashtags,
                urls = if (enableUrlHighlighting) detectedUrls else emptyList(),
                mentionColor = mentionColorValue,
                hashtagColor = hashtagColorValue,
                urlColor = urlColorValue
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

    // Process new product mention.
    // Contract:
    // - Replace the currently active @token (from last '@' to cursor) with the product name.
    // - Remove the '@' trigger for products.
    // - Do NOT add mention metadata (keeps previous user-mention behavior intact).
    LaunchedEffect(mentionedProduct) {
        mentionedProduct?.let { product ->
            // Check if adding this product would exceed the global product tag limit.
            // Only block if this product is not already tagged (would be a duplicate, not a new tag).
            val isAlreadyTagged = productMentions.any { it.productId == product.getProductId() }
            if (!isAlreadyTagged && totalProductTagCount >= maxProductTags) {
                onProductTagLimitReached()
                onQueryToken(null)
                return@LaunchedEffect
            }

            val text = textFieldValue.text
            val selection = textFieldValue.selection.start

            val mentionStart = text.lastIndexOf('@', selection - 1)
            if (mentionStart >= 0) {
                val beforeMention = text.substring(0, mentionStart)
                val afterMention = if (selection < text.length) text.substring(selection) else ""

                val productName = product.getProductName()
                val inserted = "$productName "

                // Remove '@' by not including it in the replacement.
                val newText = beforeMention + inserted + afterMention
                val newCursor = (beforeMention.length + inserted.length).coerceIn(0, newText.length)

                // Update positions for existing user mentions/hashtags/urls if needed.
                val replacedLength = selection - mentionStart
                val change = inserted.length - replacedLength

                mentions = updateMentionPositions(mentions, mentionStart, change)
                productMentions = updateProductMentionPositions(productMentions, mentionStart, change)
                hashtags = updateHashtagPositions(hashtags, mentionStart, change)
                detectedUrls = updateUrlPositions(detectedUrls, mentionStart, change)

                // Add the product mention highlight span (without '@', so start at mentionStart)
                productMentions = productMentions + ProductMentionData(
                    productId = product.getProductId(),
                    displayName = productName,
                    startPosition = mentionStart,
                    length = productName.length,
                    product = product,
                )

                // Emit updated product mentions
                onProductMentions(productMentions)

                textFieldValue = TextFieldValue(
                    text = newText,
                    selection = TextRange(newCursor)
                )
                onValueChange(newText)

                // Hide suggestions.
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
            productMentions = emptyList()
            hashtags = emptyList()
            onValueChange("") // Notify parent about the cleared text
            onUserMentions(emptyList()) // Clear mentions in parent
            onProductMentions(emptyList())
            onHashtags(emptyList()) // Clear hashtags in parent
        }
    }

    // Track the latest TextLayoutResult and LayoutCoordinates for anchor position reporting
    var latestTextLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    var latestCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

    // Cached caret rect in window coordinates for popup anchoring
    var mentionAnchorRectInWindow by remember { mutableStateOf<Rect?>(null) }

    // Main text field
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(backgroundColor)
            .onGloballyPositioned { latestCoordinates = it }
    ) {
        BasicTextField(
            value = TextFieldValue(
                annotatedString = formattedText,
                selection = textFieldValue.selection
            ),
            onValueChange = { newValue ->
                // --- Product mention: delete whole mention with a single backspace (like user mentions) ---
                // Detect a single-character deletion (backspace) and if the cursor was right after
                // a product mention span, remove the whole span (and its trailing space).
                val oldValue = textFieldValue
                val isSingleCharDeletion = newValue.text.length == oldValue.text.length - 1

                if (isSingleCharDeletion && productMentions.isNotEmpty()) {
                    // On some keyboards the new selection can be unchanged or off-by-one.
                    // Decide based on where the cursor was before deletion.
                    val oldCursor = oldValue.selection.start
                    val hit = productMentions.firstOrNull { pm ->
                        val spanEnd = pm.startPosition + pm.length
                        // Cursor at end of product name (| after name)
                        oldCursor == spanEnd ||
                                // Cursor after trailing space (| after "Product ")
                                (oldCursor == spanEnd + 1 && oldValue.text.getOrNull(spanEnd) == ' ')
                    }

                    if (hit != null) {
                        val start = hit.startPosition
                        var endExclusive = hit.startPosition + hit.length

                        // If a trailing space exists, remove it too so it behaves like user mention deletion.
                        if (endExclusive < oldValue.text.length && oldValue.text[endExclusive] == ' ') {
                            endExclusive += 1
                        }

                        val newText = oldValue.text.removeRange(start, endExclusive)
                        val newCursor = start.coerceIn(0, newText.length)
                        val removedLen = endExclusive - start
                        val change = -removedLen

                        // Update existing highlight positions.
                        mentions = updateMentionPositions(mentions, start, change)
                        productMentions = updateProductMentionPositions(
                            productMentions.filterNot { it == hit },
                            start,
                            change
                        )
                        hashtags = updateHashtagPositions(hashtags, start, change)
                        detectedUrls = updateUrlPositions(detectedUrls, start, change)

                        // Emit updated product mentions (after deletion)
                        onProductMentions(productMentions)

                        textFieldValue = TextFieldValue(
                            text = newText,
                            selection = TextRange(newCursor)
                        )
                        onValueChange(newText)

                        // Ensure suggestion is dismissed on deletion.
                        onQueryToken(null)
                        return@BasicTextField
                    }
                }

                // Enforce the character limit
                val limitedValue = if (newValue.text.length <= maxChar) {
                    newValue
                } else {
                    newValue.copy(
                        text = newValue.text.substring(0, maxChar),
                        selection = TextRange(maxChar)
                    )
                }

                // Update selection even if text hasn't changed (for cursor positioning)
                if (limitedValue.selection != textFieldValue.selection) {
                    textFieldValue = textFieldValue.copy(selection = limitedValue.selection)
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

                    val updatedProductMentions = if (diff != null) {
                        updateProductMentionPositions(productMentions, diff.position, diff.change)
                    } else productMentions

                    // Update hashtag positions if text changed
                    var updatedHashtags = if (diff != null) {
                        updateHashtagPositions(hashtags, diff.position, diff.change)
                    } else hashtags

                    // Update URL positions immediately if text changed
                    val updatedUrls = if (diff != null) {
                        updateUrlPositions(detectedUrls, diff.position, diff.change)
                    } else detectedUrls

                    mentions = updatedMentions
                    productMentions = updatedProductMentions
                    hashtags = updatedHashtags
                    detectedUrls = updatedUrls

                    // If the user just typed (or pasted) an '@' character, lift the dismiss lock so
                    // the mention suggestion popup can reappear for the new token.
                    if (diff != null && diff.change > 0) {
                        val addedText = limitedValue.text.substring(
                            diff.position,
                            (diff.position + diff.change).coerceAtMost(limitedValue.text.length)
                        )
                        if (addedText.contains('@')) {
                            mentionDismissed = false
                        }
                    }

                    // Improved mention token detection logic
                    val existingMentionPositions = mentions.map { it.startPosition }.toSet()
                    val hasActiveMention = detectActiveMentionToken(limitedValue, existingMentionPositions)
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

                    // Emit updated product mentions
                    onProductMentions(productMentions)

                    // Update external hashtags after text changes
                    onHashtags(hashtags)

                    // --- Improved hashtag auto-detection on paste or any change ---
                    val hashtagRegex = "(?:^|\\s)#(\\w+)".toRegex()
                    val detectedHashtags = hashtagRegex.findAll(limitedValue.text).map {
                        val matchResult = it
                        val hashtagText = matchResult.groupValues[1] // Get the captured group (hashtag without #)
                        val hashtagIndex = matchResult.range.last - hashtagText.length // Position of # character
                        hashtagText to hashtagIndex
                    }.toList()
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
            singleLine = maxLines <= 1,
            cursorBrush = SolidColor(cursorColor),
            onTextLayout = { layoutResult ->
                latestTextLayoutResult = layoutResult
            },
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

    // Report the mention anchor bounds (window coords) when suggestions are visible.
    LaunchedEffect(textFieldValue.text, textFieldValue.selection, latestTextLayoutResult, latestCoordinates) {
        val coords = latestCoordinates
        val layout = latestTextLayoutResult
        if (coords == null || layout == null) {
            mentionAnchorRectInWindow = null
            onMentionAnchorChanged(null)
            return@LaunchedEffect
        }

        val cursorPosition = textFieldValue.selection.start
        val mentionStart = textFieldValue.text.lastIndexOf('@', cursorPosition - 1)
        if (mentionStart < 0) {
            mentionAnchorRectInWindow = null
            onMentionAnchorChanged(null)
            return@LaunchedEffect
        }

        // Only show anchor when we're actively typing a mention token
        val existingMentionPositions = mentions.map { it.startPosition }.toSet()
        val isActiveToken = detectActiveMentionToken(textFieldValue, existingMentionPositions) != null
        if (!isActiveToken) {
            mentionAnchorRectInWindow = null
            onMentionAnchorChanged(null)
            return@LaunchedEffect
        }

        // Guard against stale layout: textFieldValue can update 1 frame before onTextLayout fires,
        // so cursorPosition may exceed the layout's valid range and cause getCursorRect to throw
        // IllegalArgumentException: offset(x) is out of bounds [0, y].
        val layoutTextLength = layout.layoutInput.text.length
        if (cursorPosition > layoutTextLength) {
            // Layout is stale; LaunchedEffect will re-run once latestTextLayoutResult catches up.
            return@LaunchedEffect
        }

        // Use the caret position (cursor) as the anchor so the popup follows the cursor while typing.
        val caretRectLocal = layout.getCursorRect(cursorPosition)
        val topLeftWindow = coords.localToWindow(Offset(caretRectLocal.left, caretRectLocal.top))
        val bottomRightWindow = coords.localToWindow(Offset(caretRectLocal.right, caretRectLocal.bottom))
        val rect = Rect(
            left = topLeftWindow.x,
            top = topLeftWindow.y,
            right = bottomRightWindow.x,
            bottom = bottomRightWindow.y,
        )

        mentionAnchorRectInWindow = rect
        onMentionAnchorChanged(rect)
    }

    // Render suggestions popup (optional) anchored to caret.
    val anchorRect = mentionAnchorRectInWindow
    val existingMentionPositionsForPopup = remember(mentions) { mentions.map { it.startPosition }.toSet() }
    val hasActiveMentionToken = remember(textFieldValue.text, textFieldValue.selection, mentionDismissed, existingMentionPositionsForPopup) {
        detectActiveMentionToken(textFieldValue, existingMentionPositionsForPopup) != null && !mentionDismissed
    }
    if (mentionSuggestions != null && anchorRect != null && hasActiveMentionToken) {
        AmityMentionSuggestionPopupFullWidth(
            anchorInWindow = anchorRect,
            onDismissRequest = {
                mentionDismissed = true
                onQueryToken(null)
            },
        ) {
            val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp

            // Force the popup to measure at full screen width (Popup otherwise measures wrap-content)
            // and apply the insets used in the new design.
            Box(
                modifier = Modifier
                    .width(screenWidthDp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    mentionSuggestions {
                        mentionDismissed = true
                        onQueryToken(null)
                    }
                }
            }
        }
    }

    // Debounced URL detection (2 seconds after user stops typing)
    // Only auto-detect if no urlHighlights are provided
    LaunchedEffect(value, enableUrlHighlighting, urlHighlights) {
        if (urlHighlights.isNotEmpty()) {
            // Use provided URL highlights from link objects
            detectedUrls = urlHighlights
            onUrlsDetected(detectedUrls)
        } else if (enableUrlHighlighting && value.isNotEmpty()) {
            delay(2000) // Wait 2 seconds after text changes
            val urlPositions = value.extractUrls()
            detectedUrls = urlPositions.map { urlPos ->
                UrlHighlight(
                    start = urlPos.start,
                    end = urlPos.end,
                    url = urlPos.url
                )
            }
            onUrlsDetected(detectedUrls)
        } else if (value.isEmpty()) {
            // Clear URLs when text is empty
            detectedUrls = emptyList()
            onUrlsDetected(emptyList())
        }
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
                append(text.substring(highlight.start, minOf(highlight.end, text.length)))
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

/**
 * Update URL positions when text changes
 */
private fun updateUrlPositions(
    urls: List<UrlHighlight>,
    changePosition: Int,
    change: Int
): List<UrlHighlight> {
    if (change == 0 || urls.isEmpty()) return urls

    return urls.mapNotNull { url ->
        when {
            // If change happens before URL, adjust position
            url.start > changePosition ->
                url.copy(start = url.start + change, end = url.end + change)

            // If change happens inside URL, remove that URL highlight
            changePosition >= url.start && changePosition < url.end ->
                null // Mark for removal

            // Otherwise keep as is
            else -> url
        }
    }
}

private fun detectActiveHashtagToken(value: TextFieldValue): String? {
    val text = value.text
    val cursorPosition = value.selection.start

    if (cursorPosition <= 0) return null

    // Check if cursor is right after an # character
    if (text.getOrNull(cursorPosition - 1) == '#') {
        // Check if the # is at the beginning or preceded by whitespace
        val hashPosition = cursorPosition - 1
        val isValidHashtag = hashPosition == 0 || text.getOrNull(hashPosition - 1)?.isWhitespace() == true
        return if (isValidHashtag) "" else null
    }

    // Look backwards from cursor for # token
    var index = cursorPosition - 1
    var foundAt = false
    var tokenStart = -1

    // Search backwards from cursor until we find # or a space/newline
    while (index >= 0) {
        val char = text[index]
        if (char == '#') {
            // Found # character - check if it's valid (at beginning or after whitespace)
            val isValidHashtag = index == 0 || text.getOrNull(index - 1)?.isWhitespace() == true
            if (isValidHashtag) {
                foundAt = true
                tokenStart = index + 1
            }
            break
        } else if (char == ' ' || char == '\n') {
            // Hit a whitespace before finding #, no active token
            break
        }
        index--
    }

    // If we found a valid # and cursor is after it, return the token text
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
            // Check if the # is at the beginning or preceded by whitespace
            val isValidHashtag = hashIndex == 0 || text.getOrNull(hashIndex - 1)?.isWhitespace() == true
            if (!isValidHashtag) {
                // If the # is not at the beginning and not preceded by whitespace, it's not a valid hashtag
                return null
            }
            val hashtagText = text.substring(hashIndex + 1, cursorPosition - 1)
            if (hashtagText.isNotEmpty()) {
                return AmityHashtag(
                    text = hashtagText,
                    index = hashIndex,
                    length = hashtagText.length
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
 *
 * @param existingMentionPositions start positions (where '@' sits in the text) of
 *   already-completed user mentions. Any '@' found at one of these positions is
 *   treated as consumed and will NOT be reported as an active token.
 */
internal fun detectActiveMentionToken(
    value: TextFieldValue,
    existingMentionPositions: Set<Int> = emptySet(),
): String? {
    val text = value.text
    val cursorPosition = value.selection.start

    if (cursorPosition <= 0) return null

    // Check if cursor is right after an @ character
    if (text.getOrNull(cursorPosition - 1) == '@') {
        // If this '@' belongs to a completed mention, do not treat it as a new token.
        if ((cursorPosition - 1) in existingMentionPositions) return null
        return ""
    }

    // Look backwards from cursor for @ token
    var index = cursorPosition - 1
    var foundAt = false
    var tokenStart = -1

    // Search backwards from cursor until we find '@'.
    // Allow spaces inside the token so multi-word product names remain an active mention query.
    // Stop at newlines — a mention token should not span across lines.
    while (index >= 0) {
        val char = text[index]
        if (char == '\n') break
        if (char == '@') {
            // Skip '@' that belongs to an already-completed mention.
            if (index in existingMentionPositions) break
            foundAt = true
            tokenStart = index + 1
            break
        }
        index--
    }

    // If we found @ and cursor is after it, return the token text (may include spaces)
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

            // If change happens strictly inside the mention name, remove that mention
            changePosition > mention.startPosition &&
                    changePosition <= mention.startPosition + mention.length ->
                mention.copy(startPosition = -1) // Mark for removal

            // Otherwise keep as is
            else -> mention
        }
    }.filter { it.startPosition >= 0 } // Remove invalid mentions
}

private fun updateProductMentionPositions(
    mentions: List<ProductMentionData>,
    changePosition: Int,
    change: Int
): List<ProductMentionData> {
    if (change == 0 || mentions.isEmpty()) return mentions

    return mentions.mapNotNull { mention ->
        when {
            // If change happens before the product span, adjust position
            mention.startPosition > changePosition -> mention.copy(startPosition = mention.startPosition + change)

            // If change happens strictly inside the product name, remove it (no longer reliable)
            // Allow deleting/pasting after the product name (trailing space) without removing the mention
            changePosition > mention.startPosition &&
                changePosition <= mention.startPosition + mention.length -> null

            else -> mention
        }
    }
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

/**
 * Product mention highlight (no metadata, styling only).
 */
data class ProductMentionData(
    val productId: String,
    val displayName: String,
    val startPosition: Int,
    val length: Int,
    val product: AmityProduct? = null, // Store the product object for tag aggregation
)

/**
 * Data class to track URL information
 */
data class UrlHighlight(
    val start: Int,
    val end: Int,
    val url: String
)

/**
 * Format text with mentions, hashtags, and URLs highlighted
 */
private fun formatTextWithMentionsHashtagsAndUrls(
    text: String,
    mentions: List<MentionData>,
    productMentions: List<ProductMentionData>,
    hashtags: List<AmityHashtag>,
    urls: List<UrlHighlight>,
    mentionColor: Color,
    hashtagColor: Color,
    urlColor: Color
): AnnotatedString {
    if (mentions.isEmpty() && productMentions.isEmpty() && hashtags.isEmpty() && urls.isEmpty()) return AnnotatedString(text)

    return buildAnnotatedString {
        var lastIndex = 0

        // Combine mentions, hashtags, and URLs into highlights
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

        // Products: highlight just the product name (no '@')
        productMentions.forEach { product ->
            allHighlights.add(
                TextHighlight(
                    start = product.startPosition,
                    end = product.startPosition + product.length,
                    color = mentionColor
                )
            )
        }

        hashtags.forEach { hashtag ->
            // Highlight only up to 100 characters of the hashtag
            val hashtagStart = hashtag.getIndex()
            val maxHighlightLength = minOf(hashtag.getLength() + 1, 100) // +1 for #, max 100 chars
            allHighlights.add(
                TextHighlight(
                    start = hashtagStart,
                    end = hashtagStart + maxHighlightLength,
                    color = hashtagColor
                )
            )
        }

        urls.forEach { url ->
            allHighlights.add(
                TextHighlight(
                    start = url.start,
                    end = url.end,
                    color = urlColor
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
            withStyle(
                SpanStyle(
                    color = highlight.color,
                    // Match user mention highlight style (no underline)
                    textDecoration = TextDecoration.None
                )
            ) {
                append(text.substring(highlight.start, minOf(highlight.end, text.length)))
            }

            lastIndex = highlight.end
        }

        // Add any remaining text
        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
    }
}

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