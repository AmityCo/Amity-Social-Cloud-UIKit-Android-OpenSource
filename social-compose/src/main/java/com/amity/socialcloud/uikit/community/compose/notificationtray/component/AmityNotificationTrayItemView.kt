package com.amity.socialcloud.uikit.community.compose.notificationtray.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.core.notificationtray.AmityNotificationTrayItem
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.common.readableSocialTimeDiff
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityNotificationTrayItemView(
    modifier: Modifier = Modifier,
    isSeen: Boolean = false,
    data: AmityNotificationTrayItem? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if (isSeen) AmityTheme.colors.background else AmityTheme.colors.primaryShade3.copy(
                    alpha = 0.3f
                )
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar shimmer
        data?.getUsers()?.firstOrNull()?.let {
            AmityUserAvatarView(
                user = it,
            )
        }

        Spacer(Modifier.width(12.dp))

        //create annotate string for highlight text
        HighlightText(
            modifier = Modifier.weight(1f),
            text = data?.getText() ?: "",
            templatedText = data?.getTemplatedText() ?: ""
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = data?.getLastOccurredAt()?.readableSocialTimeDiff() ?: "",
            style = AmityTheme.typography.caption,
            color = AmityTheme.colors.baseShade2
        )

    }
}

@Composable
@Preview
private fun AmityNotificationTrayItemReview() {
    AmityNotificationTrayItemView(isSeen = true)
}

@Composable
fun HighlightText(
    modifier: Modifier = Modifier,
    text: String,
    templatedText: String,
) {
    // Improved regex to match {{key:value}} patterns without escaped backslashes
    val regex = "\\{\\{[^}]*\\}\\}".toRegex()
    val placeholders = regex.findAll(templatedText).toList()
    val literalParts = regex.split(templatedText)

    if (placeholders.isNotEmpty()) {
        // Find values that replace placeholders in the rendered text
        val placeholderValues = mutableListOf<String>()
        var remainingText = text
        var currentPos = 0

        for (i in literalParts.indices) {
            if (i < literalParts.size - 1 || literalParts.last().isNotEmpty()) {
                val part = literalParts[i]
                val partPos = remainingText.indexOf(part, currentPos)

                if (partPos >= 0) {
                    if (partPos > currentPos) {
                        // Text between current position and start of literal part is a placeholder value
                        placeholderValues.add(remainingText.substring(currentPos, partPos))
                    }
                    currentPos = partPos + part.length
                }
            }
        }

        // Catch any trailing placeholder value
        if (currentPos < remainingText.length) {
            placeholderValues.add(remainingText.substring(currentPos))
        }

        // Build annotated string with highlighted placeholder values
        val annotatedString = buildAnnotatedString {
            var valueIndex = 0

            for (i in literalParts.indices) {
                append(literalParts[i])

                if (i < placeholderValues.size) {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append(placeholderValues[i])
                    }
                }
            }
        }

        Text(
            modifier = modifier,
            text = annotatedString,
            style = AmityTheme.typography.body,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3
        )
    } else {
        Text(
            text = text,
            style = AmityTheme.typography.body,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HighlightTextPreview() {
    HighlightText(
        text = "Alice and 5 others reacted to your post.",
        templatedText = "{{ userId: 67f4e40030a5dea19d8a187f }} and {{5 others}} reacted to your post."
    )
}