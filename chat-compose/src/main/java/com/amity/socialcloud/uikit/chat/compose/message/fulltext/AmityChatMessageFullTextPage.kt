package com.amity.socialcloud.uikit.chat.compose.message.fulltext

import android.content.Intent
import android.net.Uri
import com.google.gson.JsonParser
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
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.uikit.chat.compose.AmityChatBehaviorHelper
import com.amity.socialcloud.uikit.common.compose.R as CommonComposeR
import com.amity.socialcloud.uikit.common.extionsions.extractUrls
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityChatMessageFullTextPage(
    modifier: Modifier = Modifier,
    displayName: String,
    text: String,
    mentionMetadata: String? = null,
    mentionedUserIds: List<String> = emptyList(),
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val linkColor = AmityTheme.token(AmityColorToken.TextChatBubbleInboundLinkDefault)
    val mentionColor = AmityTheme.token(AmityColorToken.TextChatBubbleInboundMentionedDefault)

    val annotatedString = remember(text, linkColor, mentionColor, mentionMetadata, mentionedUserIds) {
        val mentionGetter = mentionMetadata
            ?.let { runCatching { JsonParser.parseString(it).asJsonObject }.getOrNull() }
            ?.let { AmityMentionMetadataGetter(it) }

        buildAnnotatedString {
            append(text)

            mentionGetter?.getMentionedUsers()?.forEach { mentionItem ->
                if (mentionItem.getUserId() in mentionedUserIds && mentionItem.getIndex() < text.length) {
                    val start = mentionItem.getIndex()
                    val end = minOf(mentionItem.getIndex() + mentionItem.getLength() + 1, text.length)
                    addStyle(
                        style = SpanStyle(
                            color = mentionColor,
                            fontWeight = FontWeight.Bold,
                        ),
                        start = start,
                        end = end,
                    )
                    addStringAnnotation(
                        tag = "MENTION",
                        annotation = mentionItem.getUserId(),
                        start = start,
                        end = end,
                    )
                }
            }

            mentionGetter?.getMentionedChannels()?.forEach { mentionItem ->
                val start = mentionItem.getIndex()
                val end = minOf(mentionItem.getIndex() + mentionItem.getLength() + 1, text.length)
                if (start < text.length) {
                    addStyle(
                        style = SpanStyle(
                            color = mentionColor,
                            fontWeight = FontWeight.Bold,
                        ),
                        start = start,
                        end = end,
                    )
                }
            }
            text.extractUrls().forEach { pos ->
                addStyle(
                    style = SpanStyle(
                        color = linkColor,
                        textDecoration = TextDecoration.Underline,
                    ),
                    start = pos.start,
                    end = pos.end,
                )
                addStringAnnotation(
                    tag = "URL",
                    annotation = pos.url,
                    start = pos.start,
                    end = pos.end,
                )
            }
        }
    }

    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    AmityBasePage("chat_full_text_page", useAmityToast = true) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.token(AmityColorToken.SurfacePageBackgroundDefault)),
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AmityTheme.token(AmityColorToken.SurfaceSheetsBackgroundGeneral))
                    .padding(horizontal = 12.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = CommonComposeR.drawable.amity_ic_chevron_left),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterStart)
                        .clickableWithoutRipple { onBack() },
                    tint = AmityTheme.token(AmityColorToken.IconIconButtonGhostSecondaryDefault),
                )
                Text(
                    text = displayName,
                    style = AmityTheme.typography.titleLegacy.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTitleDefault),
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .padding(horizontal = 44.dp)
                        .align(Alignment.Center),
                )
            }

            AmityDivider(variant = AmityDividerVariant.Post)

            // Full scrollable text body with tappable links
            Text(
                text = annotatedString,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 17.sp,
                    color = AmityTheme.token(AmityColorToken.TextChatBubbleInboundMessagesDefault),
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .pointerInput(annotatedString) {
                        detectTapGestures { offset ->
                            textLayoutResult.value?.let { layout ->
                                val charOffset = layout.getOffsetForPosition(offset)
                                val mentionedUserId = annotatedString
                                    .getStringAnnotations("MENTION", charOffset, charOffset)
                                    .firstOrNull()?.item
                                val linkUrl = annotatedString
                                    .getStringAnnotations("URL", charOffset, charOffset)
                                    .firstOrNull()?.item

                                when {
                                    mentionedUserId != null -> {
                                        AmityChatBehaviorHelper.messageBubbleBehavior
                                            .onMentionUserTap(context, mentionedUserId)
                                    }

                                    linkUrl != null -> {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                                        context.startActivity(intent)
                                    }
                                }
                            }
                        }
                    },
                onTextLayout = { textLayoutResult.value = it },
            )
        }
    }
}
