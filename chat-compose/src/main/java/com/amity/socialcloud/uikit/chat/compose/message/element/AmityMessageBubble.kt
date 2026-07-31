package com.amity.socialcloud.uikit.chat.compose.message.element

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri as AndroidUri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageAvatarView
import com.amity.socialcloud.uikit.chat.compose.message.element.reaction.AmityMessageReactionPicker
import com.amity.socialcloud.uikit.chat.compose.message.element.reaction.AmityMessageReactionPreview
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityAvatarFullScreenDialog
import com.amity.socialcloud.uikit.common.extionsions.extractUrls
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadge
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgeVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgeShape
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgeSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgePreset
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBadgeFamily
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoader
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoaderSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoaderVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySheet
import android.util.Log
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadInfo
import com.amity.socialcloud.uikit.common.service.AmityFileService
import io.reactivex.rxjava3.disposables.Disposable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.text.style.TextAlign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import androidx.compose.runtime.DisposableEffect
import io.reactivex.rxjava3.schedulers.Schedulers
import com.google.gson.JsonObject
import androidx.compose.ui.Alignment
import com.amity.socialcloud.sdk.model.core.file.AmityVideo
import com.amity.socialcloud.uikit.chat.compose.message.fulltext.AmityChatMessageFullTextPageActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlin.math.roundToInt
import kotlinx.coroutines.TimeoutCancellationException
import com.amity.socialcloud.uikit.chat.compose.config.AmityChatBubbleColors
import com.amity.socialcloud.uikit.common.ui.theme.AmityColors
import com.amity.socialcloud.uikit.common.ui.theme.amityChatErrorRed
import com.amity.socialcloud.uikit.common.utils.resolvedAvatarUrl

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmityMessageBubble(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    message: AmityMessage,
    isSenderModerator: Boolean = false,
    onAddReaction: (AmityMessage, String) -> Unit = { _, _ -> },
    onRemoveReaction: (AmityMessage, String) -> Unit = { _, _ -> },
    onOpenReactions: (AmityMessage) -> Unit = {},
    optionAction: AmityMessageActionMenuAction = AmityMessageActionMenuAction(),
    onResend: ((AmityMessage) -> Unit)? = null,
    onDelete: ((AmityMessage) -> Unit)? = null,
    onCancelUpload: ((AmityMessage) -> Unit)? = null,
    onSeeMore: ((text: String, displayName: String) -> Unit)? = null,
    parentMessageFlow: Flow<AmityMessage>? = null,
    isGroupChat: Boolean = false,
    onJumpToMessage: ((messageId: String) -> Unit)? = null,
    isHighlighted: Boolean = false,
    isUserMuted: Boolean = false,
) {
    val context = LocalContext.current
    val isCurrentUser = message.getCreatorId() == AmityCoreClient.getUserId()
    val isDeleted = message.isDeleted()
    val repliedMessageTitle = amityChatString("chat.replied.message")

    // The active page/component scope drives any per-scope theme override for these bubble colors.
    val bubbleColors = rememberTokenChatBubbleColors()

    if (isDeleted) {
        DeletedMessageBubble(
            modifier = modifier,
            isCurrentUser = isCurrentUser,
            message = message,
            isSenderModerator = isSenderModerator
        )
        return
    }

    var showReactionPicker by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    var showFailedActionSheet by remember { mutableStateOf(false) }
    var showAvatarFullScreen by remember { mutableStateOf(false) }
    var isCancelledUpload by remember { mutableStateOf(false) }
    var showParentMediaPreview by remember { mutableStateOf(false) }
    var parentPreviewMedia by remember { mutableStateOf<Any?>(null) }

    // Optimistic reaction snapshot — prevents blink during the SDK's sequential remove+add cycle.
    // Holds projected display state until the SDK message settles to the expected reaction.
    data class ReactionDisplayState(
        val reactionMap: Map<String, Int>,
        val reactionCount: Int,
        val hasMyReaction: Boolean,
        val expectedReaction: String?, // null = expect no reaction, non-empty = expect this reaction
    )
    var reactionDisplay by remember { mutableStateOf<ReactionDisplayState?>(null) }

    // Clear optimistic state when the actual message matches the expected outcome,
    // but wait a bit to survive any intermediate server emissions that may follow.
    val currentMyReaction = message.getMyReactions().firstOrNull()
    LaunchedEffect(currentMyReaction, reactionDisplay) {
        val expected = reactionDisplay ?: return@LaunchedEffect
        val settled = when {
            expected.expectedReaction.isNullOrEmpty() -> currentMyReaction == null
            else -> currentMyReaction == expected.expectedReaction
        }
        if (settled) {
            delay(2000)
            reactionDisplay = null
        }
    }

    fun applyReactionDisplay(msg: AmityMessage, newName: String) {
        val oldName = msg.getMyReactions().firstOrNull()
        val projectedMap = msg.getReactionMap().toMutableMap()
        if (oldName != null) {
            val cnt = projectedMap[oldName] ?: 0
            if (cnt <= 1) projectedMap.remove(oldName) else projectedMap[oldName] = cnt - 1
        }
        if (newName.isNotEmpty()) projectedMap[newName] = (projectedMap[newName] ?: 0) + 1
        val projectedCount = when {
            newName.isNotEmpty() && oldName == null -> msg.getReactionCount() + 1
            newName.isEmpty() -> maxOf(0, msg.getReactionCount() - 1)
            else -> msg.getReactionCount()
        }
        reactionDisplay = ReactionDisplayState(
            reactionMap = projectedMap,
            reactionCount = projectedCount,
            hasMyReaction = newName.isNotEmpty(),
            expectedReaction = newName.ifEmpty { null },
        )
    }

    val highlightAlpha = remember { Animatable(0f) }
    LaunchedEffect(isHighlighted) {
        if (isHighlighted) {
            highlightAlpha.animateTo(0.15f, animationSpec = tween(200))
            highlightAlpha.animateTo(0f, animationSpec = tween(800))
        }
    }

    val swipeOffsetX = remember { Animatable(0f) }
    val defaultUser = amityChatString("chat.unknown.user")

    // Hoist reaction display count so avatar alignment can use it before the outer Box
    val hasReactions = (reactionDisplay?.reactionCount ?: message.getReactionCount()) > 0

    // The bubble caps at 60% of the viewport. It is a fraction of the screen, not a fixed size, so
    // it has to be resolved per configuration — a hardcoded dp reproduces one reference device and
    // drifts on every other. Avatar and timestamp sit outside this budget and must not reduce it.
    val bubbleMaxWidth = (LocalConfiguration.current.screenWidthDp * 0.6f).dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AmityTheme.colors.primary.copy(alpha = highlightAlpha.value)),
    ) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(swipeOffsetX.value.roundToInt(), 0) }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isCurrentUser) {
            Box(
                modifier = Modifier
                    .size(if (isSenderModerator) 36.dp else 32.dp)
                    .then(if (hasReactions) Modifier.offset(y = (-16).dp) else Modifier)
                    .clickable { showAvatarFullScreen = true },
            ) {
                AmityMessageAvatarView(
                    pageScope = pageScope,
                    avatarUrl = message.getCreator()?.resolvedAvatarUrl(AmityImage.Size.SMALL) ?: "",
                    size = if (isSenderModerator) 36.dp else 32.dp,
                    displayName = message.getCreator()?.getDisplayName().orEmpty()
                )
                if (isSenderModerator) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 2.dp, y = 2.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        AmityBadge(
                            variant = AmityBadgeVariant.ICON,
                            icon = CommonR.drawable.amity_ic_shield_check_s,
                            shape = AmityBadgeShape.ROUND,
                            size = AmityBadgeSize.SIZE_14,
                            preset = AmityBadgePreset(
                                family = AmityBadgeFamily.USER_STATUS,
                                case = "Moderator",
                            ),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start,
        ) {
            // Display name for received messages in group chat (not on reply messages)
            if (isGroupChat && !isCurrentUser && !(parentMessageFlow != null && message.getParentId() != null)) {
                Text(
                    text = message.getCreator()?.getDisplayName() ?: defaultUser,
                    modifier = Modifier.widthIn(max = 260.dp),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AmityTheme.token(AmityColorToken.TextChatBubbleInboundHeaderUserNameDefault),
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            if (parentMessageFlow != null && message.getParentId() != null) {
                AmityChatQuotedMessage(
                    message = message,
                    parentMessageFlow = parentMessageFlow,
                    isCurrentUser = isCurrentUser,
                    isGroupChat = isGroupChat,
                    onQuotedClick = { parent ->
                        when (val data = parent.getData()) {
                            is AmityMessage.Data.TEXT -> {
                                context.startActivity(
                                    AmityChatMessageFullTextPageActivity.newIntent(
                                        context,
                                        repliedMessageTitle,
                                        data.getText()
                                    )
                                )
                            }
                            is AmityMessage.Data.IMAGE -> {
                                data.getImage()?.let { image ->
                                    parentPreviewMedia = image
                                    showParentMediaPreview = true
                                }
                            }
                            is AmityMessage.Data.VIDEO -> {
                                data.getVideo()?.let { video ->
                                    parentPreviewMedia = video
                                    showParentMediaPreview = true
                                }
                            }
                            else -> {}
                        }
                    },
                    modifier = Modifier.widthIn(max = 260.dp),
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (!isDeleted) {
                Box(modifier = Modifier.padding(top = 8.dp)) {
                    AmityMessageReactionPicker(
                        pageScope = pageScope,
                        componentScope = componentScope,
                        message = message,
                        show = showReactionPicker,
                        isCurrentUser = isCurrentUser,
                        onAddReaction = { msg, name ->
                            applyReactionDisplay(msg, name)
                            onAddReaction(msg, name)
                        },
                        onRemoveReaction = { msg, name ->
                            applyReactionDisplay(msg, "")
                            onRemoveReaction(msg, name)
                        },
                        onDismiss = {
                            showReactionPicker = false
                            menuExpanded = false
                        },
                    )
                }
            }

            // Bubble + timestamp side-by-side. The row is deliberately unconstrained: the cap
            // belongs on the bubble alone, and any cap here is shared with the timestamp, so a
            // wider clock string ("12:34 AM", "Sending…") would silently narrow the bubble.
            // Use snapshot display state when available to prevent blink during SDK transitions.
            Row(
                horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
            ) {
                // Timestamp on the LEFT for current user
                if (isCurrentUser) {
                    AmityChatMessageTimestamp(
                        modifier = Modifier.align(
                            when {
                                message.getState() == AmityMessage.State.FAILED -> Alignment.CenterVertically
                                message.getData() is AmityMessage.Data.TEXT -> Alignment.Bottom
                                else -> Alignment.CenterVertically
                            }
                        ),
                        message = message,
                        isCurrentUser = isCurrentUser,
                        onFailedClick = if (onResend != null || onDelete != null) {
                            { showFailedActionSheet = true }
                        } else null,
                        isCancelledUpload = isCancelledUpload && (message.getData() is AmityMessage.Data.IMAGE || message.getData() is AmityMessage.Data.VIDEO),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

                Box(modifier = Modifier.weight(1f, fill = false).widthIn(max = bubbleMaxWidth)) {
                    val senderDisplayName = message.getCreator()?.getDisplayName() ?: ""
                    val onLongClickBubble: () -> Unit = {
                        if (message.getState() == AmityMessage.State.SYNCED) {
                            showReactionPicker = true
                            menuExpanded = true
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp,
                                    bottomStart = 20.dp,
                                    bottomEnd = 20.dp,
                                )
                            )
                            .background(
                                when {
                                    message.getData() !is AmityMessage.Data.TEXT -> Color.Transparent
                                    isCurrentUser && showReactionPicker -> bubbleColors.rightBubblePressedColor
                                    !isCurrentUser && showReactionPicker -> bubbleColors.leftBubblePressedColor
                                    isCurrentUser -> bubbleColors.rightBubbleColor
                                    else -> bubbleColors.leftBubbleColor
                                }
                            )
                            .pointerInput(message.getState()) {
                                awaitEachGesture {
                                    val down = awaitFirstDown()
                                    down.consume()

                                    val isTap = withTimeoutOrNull(viewConfiguration.longPressTimeoutMillis) {
                                        while (true) {
                                            val event = awaitPointerEvent()
                                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                            if (!change.pressed) break
                                        }
                                        true
                                    }

                                    if (isTap == null) {
                                        if (message.getState() == AmityMessage.State.SYNCED) {
                                            showReactionPicker = true
                                            menuExpanded = true
                                        }
                                    }
                                }
                            }
                            .then(
                                if (hasReactions && message.getData() is AmityMessage.Data.TEXT) {
                                    Modifier.padding(bottom = 8.dp)
                                } else {
                                    Modifier
                                }
                            ),
                    ) {
                        MessageContent(
                            message = message,
                            isCurrentUser = isCurrentUser,
                            onLongClick = onLongClickBubble,
                            onCancelUpload = onCancelUpload?.let { cancel ->
                                { msg: AmityMessage ->
                                    isCancelledUpload = true
                                    cancel(msg)
                                }
                            },
                            onSeeMore = onSeeMore ?: { text, displayName -> context.startActivity(
                                AmityChatMessageFullTextPageActivity.newIntent(context, displayName, text)
                            )},
                            senderDisplayName = senderDisplayName,
                            bubbleColors = bubbleColors,
                            onDelete = onDelete,
                            isCancelledUpload = isCancelledUpload
                        )
                        // Action menu popup — anchored to the bubble Box so calculatePosition
                        // receives the bubble's own bounds (top/bottom), enabling correct
                        // placement above the reaction picker when space below is limited.
                        if (!isDeleted) {
                            AmityMessageActionMenuPopup(
                                show = menuExpanded,
                                message = message,
                                action = optionAction,
                                isUserMuted = isUserMuted,
                                hasReactions = hasReactions,
                                onDismiss = {
                                    menuExpanded = false
                                    showReactionPicker = false
                                },
                            )
                        }
                    }

                    if (hasReactions) {
                        AmityMessageReactionPreview(
                            pageScope = pageScope,
                            componentScope = componentScope,
                            message = message,
                            overrideReactionMap = reactionDisplay?.reactionMap,
                            overrideReactionCount = reactionDisplay?.reactionCount,
                            overrideHasMyReaction = reactionDisplay?.hasMyReaction,
                            modifier = Modifier
                                .align(if (isCurrentUser) Alignment.BottomEnd else Alignment.BottomStart)
                                .offset(y = 24.dp)
                                .clickable { onOpenReactions(message) },
                        )
                    }
                }

                // Timestamp on the RIGHT for other users
                if (!isCurrentUser) {
                    Spacer(modifier = Modifier.width(4.dp))
                    AmityChatMessageTimestamp(
                        modifier = Modifier.align(
                            if (message.getState() == AmityMessage.State.FAILED) Alignment.CenterVertically
                            else Alignment.Bottom
                        ),
                        message = message,
                        isCurrentUser = isCurrentUser,
                        onFailedClick = if (onResend != null || onDelete != null) {
                            { showFailedActionSheet = true }
                        } else null,
                        isCancelledUpload = isCancelledUpload && (message.getData() is AmityMessage.Data.IMAGE || message.getData() is AmityMessage.Data.VIDEO),
                    )
                }
            }

            // Failed-to-send caption sits OUTSIDE the bubble, on the chat background —
            // right-aligned to the bubble's side (outbound only). MEDIA messages only: failed
            // text/link messages already surface their reason via toast (banned word / link not
            // allowed), so the caption would double-report.
            val isFailed = message.getState() == AmityMessage.State.FAILED
            val isMediaMessage = message.getData() is AmityMessage.Data.IMAGE ||
                message.getData() is AmityMessage.Data.VIDEO
            if (isFailed && isMediaMessage && !isCancelledUpload) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = amityChatString("chat.message.failed.to.send"),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 10.sp,
                        lineHeight = 13.sp,
                        color = AmityTheme.token(AmityColorToken.TextChatBubbleOutboundHelperTextDefault),
                    ),
                    modifier = Modifier.align(Alignment.End),
                )
            }

            // Bottom spacing to account for reaction overlap
            if (hasReactions) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
    } // end swipe Box

    if (showFailedActionSheet) {
        AmityChatFailedMessageActionSheet(
            onResend = if (onResend != null) {
                {
                    showFailedActionSheet = false
                    onResend(message)
                }
            } else null,
            onDelete = if (onDelete != null) {
                {
                    showFailedActionSheet = false
                    onDelete(message)
                }
            } else null,
            onDismiss = { showFailedActionSheet = false },
        )
    }

    val avatarUrl = message.getCreator()?.resolvedAvatarUrl(AmityImage.Size.LARGE)
    if (showAvatarFullScreen && avatarUrl != null) {
        AmityAvatarFullScreenDialog(
            avatarUrl = avatarUrl,
            onDismiss = { showAvatarFullScreen = false },
        )
    }

    if (showParentMediaPreview && parentPreviewMedia != null) {
        AmityChatMediaPreviewDialog(
            media = listOf(parentPreviewMedia!!),
            selectedIndex = 0,
            isCurrentUser = false,
            onDeleteMessage = null,
            onDismiss = {
                showParentMediaPreview = false
                parentPreviewMedia = null
            },
        )
    }
}

/**
 * Builds the message-bubble color set from the semantic color token system. Surface, text and
 * divider each bind to their own token in the ChatBubble family, per direction (inbound/
 * outbound) and state (default/pressed). The two preview-link fields sit outside the bubble
 * token table and keep their theme sources.
 */
@Composable
private fun rememberTokenChatBubbleColors(): AmityChatBubbleColors {
    return AmityChatBubbleColors(
        leftBubbleColor = AmityTheme.token(AmityColorToken.SurfaceChatBubbleMessageInboundDefault),
        leftBubblePressedColor = AmityTheme.token(AmityColorToken.SurfaceChatBubbleMessageInboundPressed),
        leftBubbleTextColor = AmityTheme.token(AmityColorToken.TextChatBubbleInboundMessagesDefault),
        leftBubbleSubtleTextColor = AmityTheme.token(AmityColorToken.TextChatBubbleInboundSeeMoreDefault),
        leftBubblePreviewLinkColor = AmityTheme.token(AmityColorToken.SurfaceCardPreviewLinkDefault),
        rightBubbleColor = AmityTheme.token(AmityColorToken.SurfaceChatBubbleMessageOutboundDefault),
        rightBubblePressedColor = AmityTheme.token(AmityColorToken.SurfaceChatBubbleMessageOutboundPressed),
        rightBubbleTextColor = AmityTheme.token(AmityColorToken.TextChatBubbleOutboundMessagesDefault),
        rightBubbleSubtleTextColor = AmityTheme.token(AmityColorToken.TextChatBubbleOutboundSeeMoreDefault),
        rightBubblePreviewLinkColor = AmityTheme.token(AmityColorToken.SurfaceCardPreviewLinkDefault),
        bubbleDividerColor = AmityTheme.token(AmityColorToken.LineChatBubbleInboundDividerDefault),
    )
}

@Composable
private fun MessageContent(
    message: AmityMessage,
    isCurrentUser: Boolean,
    onLongClick: () -> Unit = {},
    onCancelUpload: ((AmityMessage) -> Unit)? = null,
    onSeeMore: (text: String, displayName: String) -> Unit = { _, _ -> },
    senderDisplayName: String = "",
    onDelete: ((AmityMessage) -> Unit)? = null,
    bubbleColors: AmityChatBubbleColors,
    isCancelledUpload: Boolean = false
) {
    val isFailed = message.getState() == AmityMessage.State.FAILED
    when (val data = message.getData()) {
        is AmityMessage.Data.TEXT -> {
            val text = data.getText()
            val firstUrl = remember(text) { AmityLinkPreviewFetcher.extractFirstUrl(text) }
            var isOverflowing by remember(text) { mutableStateOf(false) }

            // Content is edge-to-edge; this column owns the inset and inter-element gap. The
            // clamped-body // divider and "See more" row sit below it, at the bubble bottom,
            // spanning the full bubble width.
            Column {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AmityChatTextContent(
                        text = text,
                        isCurrentUser = isCurrentUser,
                        hasLinks = firstUrl != null,
                        mentionGetter = AmityMentionMetadataGetter(message.getMetadata() ?: com.google.gson.JsonObject()),
                        mentionees = message.getMentionees(),
                        onLongClick = onLongClick,
                        onOverflowChange = { isOverflowing = it },
                        bubbleColors = bubbleColors,
                    )
                    if (firstUrl != null && !isFailed) {
                        AmityChatLinkPreview(
                            url = firstUrl,
                            isCurrentUser = isCurrentUser,
                        )
                    }
                    // Edited sits beneath the content, after the link-preview card. Inset + 12px
                    // gap above come // from the column.
                    if (message.isEdited()) {
                        Text(
                            modifier = Modifier.align(if (isCurrentUser) Alignment.End else Alignment.Start),
                            text = amityChatString("chat.status.edited"),
                            style = AmityTheme.typography.caption.copy(
                                color = if (isCurrentUser) AmityTheme.token(AmityColorToken.TextChatBubbleOutboundEditedLabelDefault)
                                    else AmityTheme.token(AmityColorToken.TextChatBubbleInboundEditedLabelDefault),
                            ),
                        )
                    }
                }
                // Clamped body: a rule + "See more" row sit at the bubble bottom, below the padded
                // content // column. The divider spans the full bubble width edge-to-edge — the
                // underlying divider // component also has an inset-16 child vector, but that is
                // not the visible rule; the full-width // fill is. The row's label/chevron stay
                // inset 16 to align with the text; tap opens the full message.
                if (isOverflowing) {
                    HorizontalDivider(
                        color = if (isCurrentUser) AmityTheme.token(AmityColorToken.LineChatBubbleOutboundDividerDefault)
                            else bubbleColors.bubbleDividerColor,
                        thickness = 1.dp,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSeeMore(text, senderDisplayName) }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = amityChatString("chat.see.more"),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontSize = 13.sp,
                                color = if (isCurrentUser) bubbleColors.rightBubbleSubtleTextColor
                                    else bubbleColors.leftBubbleSubtleTextColor,
                            ),
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_chevron_right),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = if (isCurrentUser) AmityTheme.token(AmityColorToken.IconChatBubbleOutboundSeeMoreDefault)
                                else AmityTheme.token(AmityColorToken.IconChatBubbleInboundSeeMoreDefault),
                        )
                    }
                }
            }
        }
        is AmityMessage.Data.IMAGE -> {
            Column {
                AmityChatImageContent(
                    imageData = data,
                    message = message,
                    isCurrentUser = isCurrentUser,
                    onCancelUpload = onCancelUpload,
                    onDeleteMessage = onDelete,
                )
            }
        }
        is AmityMessage.Data.VIDEO -> {
            Column {
                AmityChatVideoContent(
                    videoData = data,
                    message = message,
                    isCurrentUser = isCurrentUser,
                    onCancelUpload = onCancelUpload,
                    onDeleteMessage = onDelete,
                )
            }
        }
        else -> {
            // AmityChatTextContent is edge-to-edge (see the TEXT branch); a wrapping column supplies
            // its 16/10 inset.
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                AmityChatTextContent(
                    text = amityChatString("chat.unsupported.message"),
                    isCurrentUser = isCurrentUser,
                    bubbleColors = bubbleColors,
                )
            }
        }
    }
}

@Composable
fun AmityChatTextContent(
    modifier: Modifier = Modifier,
    text: String,
    isCurrentUser: Boolean,
    hasLinks: Boolean = false,
    mentionGetter: AmityMentionMetadataGetter? = null,
    mentionees: List<AmityMentionee> = emptyList(),
    onLongClick: () -> Unit = {},
    expanded: Boolean = false,
    onOverflowChange: ((Boolean) -> Unit)? = null,
    bubbleColors: AmityChatBubbleColors,
) {
    val context = LocalContext.current
    val textColor = if (isCurrentUser) bubbleColors.rightBubbleTextColor else bubbleColors.leftBubbleTextColor
    val linkColor = if (isCurrentUser) AmityTheme.token(AmityColorToken.TextChatBubbleOutboundLinkDefault)
        else AmityTheme.token(AmityColorToken.TextChatBubbleInboundLinkDefault)
    val mentionColor = if (isCurrentUser) AmityTheme.token(AmityColorToken.TextChatBubbleOutboundMentionedDefault)
        else AmityTheme.token(AmityColorToken.TextChatBubbleInboundMentionedDefault)

    // Clamp height: all text scenarios clamp at 10 lines, link-bearing messages included — a //
    // generous clamp keeps more of a URL visible so users can spot suspicious links. Overflow is //
    // reported up via onOverflowChange; the divider + "See more" row are owned by the caller (see
    // // the TEXT branch).
    val maxLinesCollapsed = 10
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    val annotatedString = remember(text, textColor, linkColor, mentionColor) {
        buildAnnotatedString {
            append(text)

            mentionGetter?.getMentionedUsers()?.forEach { mentionItem ->
                if (mentionees.any { (it as? AmityMentionee.USER)?.getUserId() == mentionItem.getUserId() }
                    && mentionItem.getIndex() < text.length
                ) {
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

    Column(modifier = modifier.widthIn(min = 0.dp)) {
        Text(
            text = annotatedString,
            // Edge-to-edge: the caller's column owns the 16/10 inset + inter-element gaps.
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            textLayoutResult?.let { layout ->
                                val charOffset = layout.getOffsetForPosition(offset)
                                annotatedString.getStringAnnotations("URL", charOffset, charOffset)
                                    .firstOrNull()?.let { annotation ->
                                        val url = AmityLinkPreviewFetcher.normalizeUrl(annotation.item)
                                        val intent = Intent(Intent.ACTION_VIEW, AndroidUri.parse(url))
                                        context.startActivity(intent)
                                    }
                            }
                        },
                        onLongPress = { onLongClick() },
                    )
                },
            maxLines = if (expanded) Int.MAX_VALUE else maxLinesCollapsed,
            overflow = TextOverflow.Ellipsis,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 15.sp,
                color = textColor,
            ),
            onTextLayout = { result ->
                textLayoutResult = result
                if (!expanded) onOverflowChange?.invoke(result.hasVisualOverflow)
            },
        )
    }
}

private const val UPLOAD_PROGRESS_TAG = "AmityUploadProgress"

/**
 * Percentage to render for this update.
 *
 * The SDK caps its own reporting at 99 and emits nothing once the bytes are up, so neither the
 * value nor a terminal event marks the end of the transfer, and the ring would otherwise sit
 * frozen just short of full for the seconds the message-create round trip takes. Two signals
 * identify completion: bytes-written against content-length, exact whenever both are populated;
 * and the 99 cap itself, which is the highest the SDK will ever report. Either fills the ring —
 * what remains at that point is server-side message creation, not transfer.
 */
private fun AmityUploadInfo.displayProgress(): Int {
    val total = getContentLength()
    val reported = getProgressPercentage()
    return if ((total > 0 && getBytesWritten() >= total) || reported >= 99) 100 else reported
}

/**
 * Observes the SDK's upload-progress stream for [messageId] and hands each update to [onInfo].
 *
 * The stream is keyed on the message id (the SDK uploads message media with uploadId == messageId)
 * and is backed by a get-or-create subject, so subscribing before the upload has actually started
 * is safe — the row subscribes to the same subject the uploader later pushes into. It carries no
 * replay and is never terminated, so a subscription that starts after the transfer finished simply
 * sees nothing, and disposal is the only thing that ends it.
 */
private fun observeUploadInfo(
    messageId: String,
    onInfo: (AmityUploadInfo) -> Unit,
): Disposable = AmityFileService().getUploadInfo(messageId)
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(
        { info -> onInfo(info) },
        { error -> Log.e(UPLOAD_PROGRESS_TAG, "stream error id=$messageId", error) },
    )

/** Grace period before a silent progress stream is read as "the transfer already happened". */
private const val UPLOAD_NO_DATA_GRACE_MS = 250L

/**
 * Percentage to render for a send, given the last [reported] reading (null if the stream has said
 * nothing yet).
 *
 * A send can complete without a single reading: the SDK reports progress per written chunk, so
 * re-sending a file it already holds transfers no bytes and reports nothing at all. Rendering that
 * as 0% claims a measurement we do not have and leaves the ring flat for the whole send. After a
 * grace period — long enough for a real transfer to produce its first reading — a silent stream is
 * therefore read as a transfer that already finished, and the ring fills, matching where a send
 * with readings ends up. The grace period is what stops a genuinely slow upload from flashing full
 * before its first reading arrives.
 */
@Composable
private fun rememberUploadProgress(messageId: String, isUploading: Boolean, reported: Int?): Int {
    var graceElapsed by remember(messageId) { mutableStateOf(false) }
    LaunchedEffect(messageId, isUploading) {
        graceElapsed = false
        if (isUploading) {
            delay(UPLOAD_NO_DATA_GRACE_MS)
            graceElapsed = true
        }
    }
    return reported ?: if (graceElapsed) 100 else 0
}

/**
 * A 40dp determinate progress ring with a 24dp cancel glyph centered in it, drawn over bright
 * media — no dim, no percentage text. The whole ring is the tap target.
 *
 * Shared so it can be drawn once per media tile rather than per render branch. Repeating it inside
 * each branch is how the image path ended up with a branch that showed no controller at all.
 */
@Composable
private fun AmityChatUploadController(
    progress: Int,
    onCancel: (() -> Unit)?,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .then(if (onCancel != null) Modifier.clickableWithoutRipple { onCancel() } else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        AmityLoader(
            variant = AmityLoaderVariant.Upload,
            size = AmityLoaderSize.Medium,
            progress = progress.toFloat(),
            modifier = Modifier.size(40.dp),
        )
        if (onCancel != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_cross_l),
                contentDescription = "Cancel upload",
                tint = AmityTheme.token(AmityColorToken.IconLoadersUploadControllerDefault),
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
fun AmityChatImageContent(
    modifier: Modifier = Modifier,
    imageData: AmityMessage.Data.IMAGE,
    message: AmityMessage,
    isCurrentUser: Boolean,
    onCancelUpload: ((AmityMessage) -> Unit)? = null,
    onDeleteMessage: ((AmityMessage) -> Unit)? = null,
) {
    val image = imageData.getImage()
    val imageUrl = image?.getUrl(AmityImage.Size.MEDIUM)
    val smallImageUrl = image?.getUrl(AmityImage.Size.SMALL)
    // Prefer local URI (available during upload) over network URL
    val localUri = image?.getUri()
    val dataSource = localUri ?: imageUrl

    val context = LocalContext.current
    
    var showPreviewDialog by remember { mutableStateOf(false) }

    val placeholderPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(localUri ?: smallImageUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val placeholderState by placeholderPainter.state.collectAsState()

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(dataSource)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val painterState by painter.state.collectAsState()
    val state = message.getState()
    val isUploading = state != AmityMessage.State.SYNCED && state != AmityMessage.State.FAILED

    // Derive aspect ratio: prefer server metadata (available immediately), fall back to
    // Coil intrinsic size after load (covers local uploads where metadata is still 0).
    val metaWidth = image?.getWidth() ?: 0
    val metaHeight = image?.getHeight() ?: 0
    val metaRatio = if (metaWidth > 0 && metaHeight > 0) metaWidth.toFloat() / metaHeight.toFloat() else null

    val intrinsicRatio = remember(painterState) {
        val size = (painterState as? AsyncImagePainter.State.Success)?.painter?.intrinsicSize
        if (size != null && size.width > 0f && size.height > 0f && !size.width.isInfinite() && !size.height.isInfinite())
            size.width / size.height
        else null
    }
    val aspectRatio = metaRatio ?: intrinsicRatio

    val imageContainerModifier = modifier
        .widthIn(max = 240.dp)
        .heightIn(max = 240.dp)
        .then(
            if (aspectRatio != null && aspectRatio > 0f)
                Modifier.aspectRatio(aspectRatio)
            else
                Modifier.size(240.dp)
        )
        .clip(RoundedCornerShape(20.dp))
    
    // Track upload progress — see observeUploadInfo for the stream's semantics. Null means the
    // stream has not reported anything yet, which is NOT the same as zero percent.
    var uploadProgress by remember { mutableStateOf<Int?>(null) }
    DisposableEffect(message.getMessageId(), isUploading) {
        val messageId = message.getMessageId()
        val disposable = if (isUploading) {
            observeUploadInfo(messageId) { info -> uploadProgress = info.displayProgress() }
        } else null
        onDispose { disposable?.dispose() }
    }

    val displayedProgress = rememberUploadProgress(message.getMessageId(), isUploading, uploadProgress)

    Box(
        modifier = imageContainerModifier,
        contentAlignment = Alignment.Center,
    ) {
        if (painterState is AsyncImagePainter.State.Success) {
            Image(
                painter = painter,
                contentDescription = "Image message",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            // Do NOT consume DOWN — parent bubble's long-press+drag handler
                            // also needs to see an unconsumed DOWN to start its gesture.
                            val down = awaitFirstDown(requireUnconsumed = false)
                            try {
                                withTimeout(viewConfiguration.longPressTimeoutMillis) {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                        // Movement past touch slop is a scroll, not a tap — let the
                                        // list own the gesture instead of // opening the viewer.
                                        if ((change.position - down.position).getDistance() > viewConfiguration.touchSlop) break
                                        if (!change.pressed && change.previousPressed) {
                                            change.consume()
                                            if (image != null && state == AmityMessage.State.SYNCED) showPreviewDialog = true
                                            break
                                        }
                                    }
                                }
                            } catch (_: TimeoutCancellationException) {
                                // Long press — parent bubble handles reaction picker + drag.
                                // Do NOT consume anything here so drag events stay available.
                            }
                        }
                    },
            )
        } else if (placeholderState is AsyncImagePainter.State.Success) {
            Image(
                painter = placeholderPainter,
                contentDescription = "Image message",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            try {
                                withTimeout(viewConfiguration.longPressTimeoutMillis) {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                        // Movement past touch slop is a scroll, not a tap.
                                        if ((change.position - down.position).getDistance() > viewConfiguration.touchSlop) break
                                        if (!change.pressed && change.previousPressed) {
                                            change.consume()
                                            if (image != null && state == AmityMessage.State.SYNCED) showPreviewDialog = true
                                            break
                                        }
                                    }
                                }
                            } catch (_: TimeoutCancellationException) {
                                // Long press — parent handles drag.
                            }
                        }
                    },
            )
        } else if (painterState is AsyncImagePainter.State.Loading) {
            // Grey placeholder while the thumbnail loads. While the message is still UPLOADING
            // this branch is what actually renders (the painter hasn't resolved yet), so it must
            // show the upload progress ring + cancel — the indeterminate Spinner is only for
            // downloading someone else's media.
            //
            // Own media reaches this branch a second time, just after sync: the SDK drops the
            // local uri, so the model switches to the remote url and the painter restarts on
            // bytes we already have. Rendering the download spinner there made a just-sent image
            // flash image -> spinner -> image, so the spinner stays inbound-only.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AmityTheme.token(AmityColorToken.SurfaceMediaImageLoading)),
                contentAlignment = Alignment.Center,
            ) {
                if (!isUploading && !isCurrentUser) {
                    AmityLoader(
                        variant = AmityLoaderVariant.Spinner,
                        size = AmityLoaderSize.Lg,
                        modifier = Modifier.size(40.dp),
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AmityTheme.token(AmityColorToken.SurfaceMediaImageBroken)),
                contentAlignment = Alignment.Center,
            ) {
                if (!isUploading) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_image_slash_r),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = AmityTheme.token(AmityColorToken.IconMediaImageBroken),
                    )
                }
            }
        }

        // One controller for every branch above. The media stays bright beneath it (no dim); it
        // covers the whole send, transfer and message-create alike, since a locally-loaded image
        // resolves to Success within milliseconds and would otherwise show no progress at all.
        if (isUploading) {
            AmityChatUploadController(
                progress = displayedProgress,
                onCancel = onCancelUpload?.let { cancel -> { cancel(message) } },
            )
        }
    }

    if (showPreviewDialog && image != null) {
        AmityChatMediaPreviewDialog(
            media = listOf(image),
            selectedIndex = 0,
            isCurrentUser = isCurrentUser,
            onDeleteMessage = if (onDeleteMessage != null) {
                { onDeleteMessage(message) }
            } else null,
            onDismiss = { showPreviewDialog = false },
        )
    }
}

@Composable
fun AmityChatVideoContent(
    modifier: Modifier = Modifier,
    videoData: AmityMessage.Data.VIDEO,
    message: AmityMessage,
    isCurrentUser: Boolean,
    onCancelUpload: ((AmityMessage) -> Unit)? = null,
    onDeleteMessage: ((AmityMessage) -> Unit)? = null,
) {
    val context = LocalContext.current
    val state = message.getState()
    val isUploading = state != AmityMessage.State.SYNCED && state != AmityMessage.State.FAILED

    var showPreviewDialog by remember { mutableStateOf(false) }

    var thumbnailBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isThumbnailLoading by remember { mutableStateOf(false) }
    val video = videoData.getVideo()
    val videoUri = video?.getUri()
    @Suppress("DEPRECATION")
    val videoFilePath = video?.getFilePath()
    
    val sentVideoUris = LocalSentVideoUris.current
    
    val thumbnailImage = videoData.getThumbnailImage()
    val thumbnailUrl = thumbnailImage?.getUrl(AmityImage.Size.MEDIUM)
    
    val thumbnailPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(thumbnailUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val thumbnailPainterState by thumbnailPainter.state.collectAsState()
    
    // Track upload progress and the local file path — see observeUploadInfo for the semantics.
    // Null progress means the stream has reported nothing yet, which is NOT zero percent.
    var uploadProgress by remember { mutableStateOf<Int?>(null) }
    var uploadFilePath by remember { mutableStateOf<String?>(null) }
    DisposableEffect(message.getMessageId(), isUploading) {
        val messageId = message.getMessageId()
        val disposable = if (isUploading) {
            observeUploadInfo(messageId) { info ->
                uploadProgress = info.displayProgress()
                val filePath = info.getFilePath()
                if (!filePath.isNullOrEmpty() && uploadFilePath == null) {
                    uploadFilePath = filePath
                    // Survives item recycling — the post-sync thumbnail fallback reads it.
                    AmitySentVideoPathCache.put(messageId, filePath)
                }
            }
        } else null
        onDispose { disposable?.dispose() }
    }

    val displayedProgress = rememberUploadProgress(message.getMessageId(), isUploading, uploadProgress)

    // Generate thumbnail from local video file.
    // Priority: SDK videoUri > SDK file path > uploadInfo filePath (this composition) > the
    // recycling-proof per-message path cache > most recent sent uri. The last two are what keep
    // the thumbnail alive right after sync, when the recreated item has no local state and the
    // server thumbnail is not generated yet.
    val effectiveVideoSource: Any? = videoUri
        ?: videoFilePath
        ?: uploadFilePath
        ?: AmitySentVideoPathCache.get(message.getMessageId())
        ?: (if (isCurrentUser) sentVideoUris.lastOrNull() else null)
    
    LaunchedEffect(effectiveVideoSource) {
        if (effectiveVideoSource != null && thumbnailBitmap == null) {
            isThumbnailLoading = true
            withContext(Dispatchers.IO) {
                try {
                    val retriever = MediaMetadataRetriever()
                    when (effectiveVideoSource) {
                        is android.net.Uri -> retriever.setDataSource(context, effectiveVideoSource)
                        is String -> {
                            if (effectiveVideoSource.startsWith("content://") || effectiveVideoSource.startsWith("file://")) {
                                retriever.setDataSource(context, android.net.Uri.parse(effectiveVideoSource))
                            } else {
                                retriever.setDataSource(effectiveVideoSource)
                            }
                        }
                        else -> {
                            // SDK might return java.net.URI — convert to android.net.Uri
                            val uriStr = effectiveVideoSource.toString()
                            if (uriStr.startsWith("content://") || uriStr.startsWith("file://")) {
                                retriever.setDataSource(context, android.net.Uri.parse(uriStr))
                            } else {
                                retriever.setDataSource(uriStr)
                            }
                        }
                    }
                    thumbnailBitmap = retriever.getFrameAtTime(0)
                    retriever.release()
                } catch (_: Exception) {
                    // Thumbnail generation failed, keep null
                }
            }
            isThumbnailLoading = false
        }
    }

    Box(
        modifier = modifier
            .size(240.dp)
            .clip(RoundedCornerShape(20.dp))
            .pointerInput(Unit) {
                awaitEachGesture {
                    // Do NOT consume DOWN — parent bubble's long-press+drag handler needs it.
                    val down = awaitFirstDown(requireUnconsumed = false)
                    try {
                        withTimeout(viewConfiguration.longPressTimeoutMillis) {
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                // Movement past touch slop is a scroll, not a tap.
                                if ((change.position - down.position).getDistance() > viewConfiguration.touchSlop) break
                                if (!change.pressed && change.previousPressed) {
                                    change.consume()
                                    if (video != null && !isUploading && state == AmityMessage.State.SYNCED) {
                                        showPreviewDialog = true
                                    }
                                    break
                                }
                            }
                        }
                    } catch (_: TimeoutCancellationException) {
                        // Long press — parent bubble handles reaction picker + drag.
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        if (thumbnailPainterState is AsyncImagePainter.State.Success) {
            Image(
                painter = thumbnailPainter,
                contentDescription = "Video thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(240.dp),
            )
            
            if (isUploading) {
                Box(
                    modifier = Modifier
                        .size(40.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    // Upload controller (Loading+Cancel): progress ring with the cancel glyph
                    // centered — the media stays bright (no dim, no percentage text). The whole
                    // 40dp ring is the tap target.
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .then(
                                if (onCancelUpload != null) {
                                    Modifier.clickableWithoutRipple { onCancelUpload(message) }
                                } else Modifier
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        AmityLoader(
                            variant = AmityLoaderVariant.Upload,
                            size = AmityLoaderSize.Medium,
                            progress = displayedProgress.toFloat(),
                            modifier = Modifier.size(40.dp),
                        )
                        if (onCancelUpload != null) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_cross_l),
                                contentDescription = "Cancel upload",
                                tint = AmityTheme.token(AmityColorToken.IconLoadersUploadControllerDefault),
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                }
            } else {
                // Play button overlay for synced videos — decorative glyph (no onClick of its own;
                // tap-to-preview is handled by the parent's pointerInput), left as a hand-rolled
                // circle rather than the Icon Button atom (which always installs a click listener).
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .background(AmityTheme.token(AmityColorToken.SurfaceChatBubbleReplyOverlayDefault)),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                // Semantic scrim token — flat 50% black in both light and dark
                                // modes.
                                AmityTheme.token(AmityColorToken.SurfaceMediaOverlayTransparentBlack),
                                CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_video_play_s),
                            contentDescription = "Play video",
                            modifier = Modifier.size(24.dp),
                            tint = AmityTheme.token(AmityColorToken.IconLoadersUploadControllerDefault),
                        )
                    }
                }
            }
        } else if (thumbnailBitmap != null) {
            Image(
                bitmap = thumbnailBitmap!!.asImageBitmap(),
                contentDescription = "Video thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(240.dp),
            )
            
            if (isUploading) {
                Box(
                    modifier = Modifier.size(240.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    // Upload controller (Loading+Cancel): progress ring with the cancel glyph
                    // centered — the media stays bright (no dim, no percentage text). The whole
                    // 40dp ring is the tap target.
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .then(
                                if (onCancelUpload != null) {
                                    Modifier.clickableWithoutRipple { onCancelUpload(message) }
                                } else Modifier
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        AmityLoader(
                            variant = AmityLoaderVariant.Upload,
                            size = AmityLoaderSize.Medium,
                            progress = displayedProgress.toFloat(),
                            modifier = Modifier.size(40.dp),
                        )
                        if (onCancelUpload != null) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_cross_l),
                                contentDescription = "Cancel upload",
                                tint = AmityTheme.token(AmityColorToken.IconLoadersUploadControllerDefault),
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                }
            } else {
                // Play button overlay for synced videos — decorative glyph (no onClick of its own;
                // tap-to-preview is handled by the parent's pointerInput), left as a hand-rolled
                // circle rather than the Icon Button atom (which always installs a click listener).
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .background(AmityTheme.token(AmityColorToken.SurfaceChatBubbleReplyOverlayDefault)),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                // Semantic scrim token — flat 50% black in both light and dark
                                // modes.
                                AmityTheme.token(AmityColorToken.SurfaceMediaOverlayTransparentBlack),
                                CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_video_play_s),
                            contentDescription = "Play video",
                            modifier = Modifier.size(24.dp),
                            tint = AmityTheme.token(AmityColorToken.IconLoadersUploadControllerDefault),
                        )
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .background(AmityTheme.token(AmityColorToken.SurfaceMediaVideoLoading)),
                contentAlignment = Alignment.Center,
            ) {
                if (isUploading) {
                    // Upload controller (Loading+Cancel): progress ring with the cancel glyph
                    // centered — the media stays bright (no dim, no percentage text). The whole
                    // 40dp ring is the tap target.
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .then(
                                if (onCancelUpload != null) {
                                    Modifier.clickableWithoutRipple { onCancelUpload(message) }
                                } else Modifier
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        AmityLoader(
                            variant = AmityLoaderVariant.Upload,
                            size = AmityLoaderSize.Medium,
                            progress = displayedProgress.toFloat(),
                            modifier = Modifier.size(40.dp),
                        )
                        if (onCancelUpload != null) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_cross_l),
                                contentDescription = "Cancel upload",
                                tint = AmityTheme.token(AmityColorToken.IconLoadersUploadControllerDefault),
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                } else if (isThumbnailLoading && !isCurrentUser) {
                    // Indeterminate spinner while a thumbnail resolves — inbound only, same rule as
                    // the image path. Own video re-extracts its thumbnail right after sync (the
                    // local path is lost with the message id), and a spinner there reads as a
                    // second upload; the video-loading surface plus the play disc covers the wait.
                    AmityLoader(
                        variant = AmityLoaderVariant.Spinner,
                        size = AmityLoaderSize.Lg,
                        modifier = Modifier.size(40.dp),
                    )
                } else {
                    // Play icon placeholder for synced video without thumbnail — decorative glyph
                    // (no onClick of its own), left as a hand-rolled circle rather than the Icon
                    // Button atom (which always installs a click listener).
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                // Semantic scrim token — flat 50% black in both light and dark
                                // modes.
                                AmityTheme.token(AmityColorToken.SurfaceMediaOverlayTransparentBlack),
                                CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_video_play_s),
                            contentDescription = "Play video",
                            modifier = Modifier.size(24.dp),
                            tint = AmityTheme.token(AmityColorToken.IconLoadersUploadControllerDefault),
                        )
                    }
                }
            }
        }
    }
    
    if (showPreviewDialog && video != null) {
        AmityChatMediaPreviewDialog(
            media = listOf(video),
            selectedIndex = 0,
            isCurrentUser = isCurrentUser,
            onDeleteMessage = if (onDeleteMessage != null) {
                { onDeleteMessage(message) }
            } else null,
            onDismiss = { showPreviewDialog = false },
        )
    }
}

@Composable
fun DeletedMessageBubble(
    modifier: Modifier = Modifier,
    isSenderModerator: Boolean,
    isCurrentUser: Boolean,
    message: AmityMessage,
) {
    val deletedTextColor = if (isCurrentUser) AmityTheme.token(AmityColorToken.TextChatBubbleOutboundMessagesDeleted)
    else AmityTheme.token(AmityColorToken.TextChatBubbleInboundMessagesDeleted)
    val deletedIconColor = if (isCurrentUser) AmityTheme.token(AmityColorToken.IconChatBubbleOutboundMessagesDeleted)
    else AmityTheme.token(AmityColorToken.IconChatBubbleInboundMessagesDeleted)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isCurrentUser) {
            AmityMessageAvatarView(
                avatarUrl = message.getCreator()?.resolvedAvatarUrl(AmityImage.Size.SMALL) ?: "",
                size = if (isSenderModerator) 36.dp else 32.dp,
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Column(
            horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start,
        ) {
            // The sender name is carried on the deleted placeholder too — without it a deleted
            // message in a group reads as unattributed. Styling is unspecified in the design;
            // it mirrors the live bubble's header until the designer supplies the deleted variant.
            if (!isCurrentUser) {
                Text(
                    text = message.getCreator()?.getDisplayName().orEmpty(),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AmityTheme.token(AmityColorToken.TextChatBubbleInboundHeaderUserNameDefault),
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
            Row(
                modifier = Modifier
                    .background(Color.Transparent, RoundedCornerShape(20.dp))
                    .border(
                        width = 1.dp,
                        color = if (isCurrentUser) AmityTheme.token(AmityColorToken.BorderChatBubbleOutboundDeleted)
                            else AmityTheme.token(AmityColorToken.BorderChatBubbleInboundDeleted),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(CommonR.drawable.amity_ic_trash_r),
                    contentDescription = "deleted message",
                    tint = deletedIconColor
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = amityChatString("chat.message.deleted"),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = deletedTextColor,
                    ),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityChatFailedMessageActionSheet(
    onResend: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    AmitySheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = amityChatString("chat.message.not.sent"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTextDescriptionDefault),
                ),
                modifier = Modifier.padding(bottom = 16.dp),
            )

            AmityDivider(variant = AmityDividerVariant.Content, inset = false)

            if (onResend != null) {
                Text(
                    text = amityChatString("chat.message.resend"),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.token(AmityColorToken.TextBaseHighlight),
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onResend() }
                        .padding(vertical = 14.dp),
                    textAlign = TextAlign.Center,
                )
                AmityDivider(variant = AmityDividerVariant.Content, inset = false)
            }

            if (onDelete != null) {
                Text(
                    text = amityChatString("chat.option.delete"),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Normal,
                        color = amityChatErrorRed,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDelete() }
                        .padding(vertical = 14.dp),
                    textAlign = TextAlign.Center,
                )
                AmityDivider(variant = AmityDividerVariant.Content, inset = false)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = amityChatString("chat.cancel"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AmityTheme.token(AmityColorToken.TextBaseHighlight),
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDismiss() }
                    .padding(vertical = 14.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}