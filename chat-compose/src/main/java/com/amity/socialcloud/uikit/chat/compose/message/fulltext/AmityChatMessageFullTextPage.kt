package com.amity.socialcloud.uikit.chat.compose.message.fulltext

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

private val URL_REGEX_FULL = Regex(
    """https?://[^\s<>"{}|\\^`\[\]]+""",
    RegexOption.IGNORE_CASE,
)

@Composable
fun AmityChatMessageFullTextPage(
    modifier: Modifier = Modifier,
    displayName: String,
    text: String,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val linkColor = AmityTheme.colors.highlight

    val annotatedString = remember(text, linkColor) {
        buildAnnotatedString {
            append(text)
            URL_REGEX_FULL.findAll(text).forEach { match ->
                addStyle(
                    style = SpanStyle(
                        color = linkColor,
                        textDecoration = TextDecoration.Underline,
                    ),
                    start = match.range.first,
                    end = match.range.last + 1,
                )
                addStringAnnotation(
                    tag = "URL",
                    annotation = match.value,
                    start = match.range.first,
                    end = match.range.last + 1,
                )
            }
        }
    }

    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    AmityBasePage("chat_full_text_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background),
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterStart)
                        .clickableWithoutRipple { onBack() },
                    tint = AmityTheme.colors.base,
                )
                Text(
                    text = displayName,
                    style = AmityTheme.typography.titleLegacy.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .padding(horizontal = 44.dp)
                        .align(Alignment.Center),
                )
            }

            HorizontalDivider(color = AmityTheme.colors.baseShade4)

            // Full scrollable text body with tappable links
            Text(
                text = annotatedString,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 17.sp,
                    color = AmityTheme.colors.base,
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .pointerInput(annotatedString) {
                        detectTapGestures { offset ->
                            textLayoutResult.value?.let { layout ->
                                val charOffset = layout.getOffsetForPosition(offset)
                                annotatedString
                                    .getStringAnnotations("URL", charOffset, charOffset)
                                    .firstOrNull()?.let { annotation ->
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                        context.startActivity(intent)
                                    }
                            }
                        }
                    },
                onTextLayout = { textLayoutResult.value = it },
            )
        }
    }
}
