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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
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
import com.amity.socialcloud.uikit.chat.compose.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageAvatarView
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageReactionPicker
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageReactionPreview
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityAvatarFullScreenDialog
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.service.AmityFileService
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
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
import com.amity.socialcloud.uikit.chat.compose.config.AmityChatConfigHelper
import com.amity.socialcloud.uikit.common.ui.theme.AmityColors
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack
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

    val pageId = if (isGroupChat) "group_chat_page" else "chat_page"
    val themeColors = AmityTheme.colors
    val bubbleColors = remember(pageId, themeColors) { AmityChatConfigHelper.getBubbleColors(pageId, themeColors) }

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
            delay(2000) // Hold optimistic state to outlast any stale server emissions
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

    // Highlight animation for jump-to-message
    val highlightAlpha = remember { Animatable(0f) }
    LaunchedEffect(isHighlighted) {
        if (isHighlighted) {
            highlightAlpha.animateTo(0.15f, animationSpec = tween(200))
            highlightAlpha.animateTo(0f, animationSpec = tween(800))
        }
    }

    // Swipe-to-reply gesture state
    val swipeOffsetX = remember { Animatable(0f) }
    val defaultUser = amityChatString("chat.unknown.user")

    // Hoist reaction display count so avatar alignment can use it before the outer Box
    val hasReactions = (reactionDisplay?.reactionCount ?: message.getReactionCount()) > 0

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
                            .offset(x = 2.dp, y = 2.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(
                                R.drawable.amity_ic_chat_group_moderator,
                            ),
                            contentDescription = "Moderator",
                            modifier = Modifier.size(16.dp),
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
                        color = AmityTheme.colors.baseShade1,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            // Quoted parent message (reply)
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

            // Reaction picker — anchored above the bubble
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

            // Bubble + timestamp side-by-side (timestamp outside widthIn constraint)
            // Use snapshot display state when available to prevent blink during SDK transitions.
            Row(
                horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
                modifier = Modifier.widthIn(max = 280.dp),
            ) {
                // Timestamp on the LEFT for current user
                if (isCurrentUser) {
                    AmityChatMessageTimestamp(
                        modifier = Modifier.align(
                            if (message.getData() is AmityMessage.Data.TEXT) Alignment.Bottom
                            else Alignment.CenterVertically
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

                // Bubble Box
                Box(modifier = Modifier.weight(1f, fill = false).widthIn(max = 260.dp)) {
                    val senderDisplayName = message.getCreator()?.getDisplayName() ?: ""
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
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
                                    isCurrentUser && isPressed -> bubbleColors.rightBubblePressedColor
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
                            isLongPressed = showReactionPicker,
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

                    // Reaction preview — overlaps bottom of bubble
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
                        modifier = Modifier.align(Alignment.Bottom),
                        message = message,
                        isCurrentUser = isCurrentUser,
                        onFailedClick = if (onResend != null || onDelete != null) {
                            { showFailedActionSheet = true }
                        } else null,
                        isCancelledUpload = isCancelledUpload && (message.getData() is AmityMessage.Data.IMAGE || message.getData() is AmityMessage.Data.VIDEO),
                    )
                }
            }

            // Bottom spacing to account for reaction overlap
            if (hasReactions) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
    } // end swipe Box

    // Failed message action sheet
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

    // Full-screen avatar viewer
    if (showAvatarFullScreen) {
        AmityAvatarFullScreenDialog(
            avatarUrl = message.getCreator()?.resolvedAvatarUrl(AmityImage.Size.LARGE),
            onDismiss = { showAvatarFullScreen = false },
        )
    }

    // Parent message media preview (from quoted message tap)
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

@Composable
private fun MessageContent(
    message: AmityMessage,
    isCurrentUser: Boolean,
    onLongClick: () -> Unit = {},
    onCancelUpload: ((AmityMessage) -> Unit)? = null,
    onSeeMore: (text: String, displayName: String) -> Unit = { _, _ -> },
    senderDisplayName: String = "",
    onDelete: ((AmityMessage) -> Unit)? = null,
    bubbleColors: AmityChatBubbleColors = AmityChatBubbleColors.DEFAULTS,
    isLongPressed: Boolean = false,
    isCancelledUpload: Boolean = false
) {
    val isFailed = message.getState() == AmityMessage.State.FAILED
    when (val data = message.getData()) {
        is AmityMessage.Data.TEXT -> {
            val text = data.getText()
            val firstUrl = remember(text) { AmityLinkPreviewFetcher.extractFirstUrl(text) }

            Column {
                AmityChatTextContent(
                    text = text,
                    isCurrentUser = isCurrentUser,
                    hasLinks = firstUrl != null,
                    mentionGetter = AmityMentionMetadataGetter(message.getMetadata() ?: com.google.gson.JsonObject()),
                    mentionees = message.getMentionees(),
                    onLongClick = onLongClick,
                    onSeeMore = { t -> onSeeMore(t, senderDisplayName) },
                    bubbleColors = bubbleColors,
                    isEdited = message.isEdited()
                )
                if (firstUrl != null) {
                    AmityChatLinkPreview(
                        url = firstUrl,
                        isCurrentUser = isCurrentUser,
                        modifier = Modifier.padding(
                            start = 4.dp,
                            end = 4.dp,
                            bottom = 4.dp,
                        ),
                    )
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
                    isLongPressed = isLongPressed,
                )
                if (isFailed && !isCancelledUpload) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = amityChatString("chat.message.failed.to.send"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 10.sp,
                            color = AmityTheme.colors.alert,
                        ),
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 8.dp, bottom = 8.dp, start = 8.dp)
                    )
                }
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
                if (isFailed && !isCancelledUpload) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = amityChatString("chat.message.failed.to.send"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 10.sp,
                            color = AmityTheme.colors.alert,
                        ),
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 8.dp, bottom = 8.dp, start = 8.dp)
                    )
                }
            }
        }
        else -> {
            AmityChatTextContent(
                text = amityChatString("chat.unsupported.message"),
                isCurrentUser = isCurrentUser,
                bubbleColors = bubbleColors,
            )
        }
    }
}

private val URL_REGEX = Regex(
    """https?://[^\s<>"{}|\\^`\[\]]+""",
    RegexOption.IGNORE_CASE,
)

@Composable
fun AmityChatTextContent(
    modifier: Modifier = Modifier,
    text: String,
    isCurrentUser: Boolean,
    hasLinks: Boolean = false,
    mentionGetter: AmityMentionMetadataGetter? = null,
    mentionees: List<AmityMentionee> = emptyList(),
    onLongClick: () -> Unit = {},
    onSeeMore: ((text: String) -> Unit)? = null,
    bubbleColors: AmityChatBubbleColors = AmityChatBubbleColors.DEFAULTS,
    isEdited: Boolean = false,
) {
    val context = LocalContext.current
    val textColor = if (isCurrentUser) bubbleColors.rightBubbleTextColor else bubbleColors.leftBubbleTextColor
    val linkColor = if (isCurrentUser) bubbleColors.rightBubbleTextColor else AmityTheme.colors.highlight
    val mentionColor = if (isCurrentUser) bubbleColors.rightBubbleTextColor else AmityTheme.colors.highlight
    val subtleColor = if (isCurrentUser) bubbleColors.rightBubbleSubtleTextColor else bubbleColors.leftBubbleSubtleTextColor

    val maxLinesCollapsed = if (hasLinks) 5 else 10
    var expanded by remember { mutableStateOf(false) }
    var isOverflowing by remember { mutableStateOf(false) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    val annotatedString = remember(text, textColor, linkColor, mentionColor) {
        buildAnnotatedString {
            append(text)

            // Highlight @mentioned users
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

            // Highlight @All channel mentions
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

            // Highlight URLs
            URL_REGEX.findAll(text).forEach { match ->
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

    Column(modifier = modifier.widthIn(min = 0.dp)) {
        Text(
            text = annotatedString,
            modifier = Modifier
                .padding(12.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            textLayoutResult?.let { layout ->
                                val charOffset = layout.getOffsetForPosition(offset)
                                annotatedString.getStringAnnotations("URL", charOffset, charOffset)
                                    .firstOrNull()?.let { annotation ->
                                        val url = annotation.item
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
                if (!expanded) {
                    isOverflowing = result.hasVisualOverflow
                }
            },
        )

        if (isOverflowing && !expanded) {
            HorizontalDivider(
                color = if (isCurrentUser) bubbleColors.rightBubbleTextColor.copy(alpha = 0.15f)
                else bubbleColors.bubbleDividerColor,
                thickness = 1.dp,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (onSeeMore != null) {
                            onSeeMore(text)
                        } else {
                            expanded = true
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = amityChatString("chat.see.more"),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 13.sp,
                        color = subtleColor,
                    ),
                )
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_chevron_right,
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = subtleColor,
                )
            }
        }

        if (isEdited) {
            Text(
                modifier = Modifier.align(if (isCurrentUser) Alignment.End else Alignment.Start)
                    .padding(end = 12.dp, bottom = 12.dp, start = 12.dp),
                text = amityChatString("chat.status.edited"),
                style = AmityTheme.typography.caption.copy(
                    color = subtleColor,
                ),
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
    isLongPressed: Boolean = false,
) {
    val image = imageData.getImage()
    val imageUrl = image?.getUrl(AmityImage.Size.MEDIUM)
    val smallImageUrl = image?.getUrl(AmityImage.Size.SMALL)
    // Prefer local URI (available during upload) over network URL
    val localUri = image?.getUri()
    val dataSource = localUri ?: imageUrl

    val context = LocalContext.current
    
    // Dialog state for image preview
    var showPreviewDialog by remember { mutableStateOf(false) }

    // Low-res placeholder painter (SMALL size loads fast)
    val placeholderPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(localUri ?: smallImageUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val placeholderState by placeholderPainter.state.collectAsState()

    // Full-res painter (MEDIUM size)
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

    // Container modifier: max 240dp in both dimensions, preserve aspect ratio when known, square otherwise.
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
    
    // Track upload progress
    var uploadProgress by remember { mutableStateOf(0) }
    LaunchedEffect(message.getMessageId(), isUploading) {
        if (isUploading) {
            try {
                val disposable = AmityFileService().getUploadInfo(message.getMessageId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { uploadInfo ->
                            uploadProgress = uploadInfo.getProgressPercentage()
                        },
                        { error ->
                            // Fallback to default behavior if upload info fails
                        }
                    )
            } catch (e: Exception) {
                // Handle any errors gracefully without crashing
            }
        }
    }

    Box(
        modifier = imageContainerModifier,
        contentAlignment = Alignment.Center,
    ) {
        // Show image if available, otherwise grey placeholder
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
            // Show low-res SMALL image while MEDIUM is still loading
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
            // Dark overlay during upload on top of visible image
            if (isUploading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(amityColorBlack.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center,
                ) {
                    // Circular progress ring with percentage text and centered cancel button
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        // Progress ring (background track)
                        CircularProgressIndicator(
                            progress = { uploadProgress / 100f },
                            color = amityColorWhite,
                            trackColor = amityColorWhite.copy(alpha = 0.2f),
                            strokeWidth = 3.dp,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier.size(100.dp),
                        )
                        // Progress percentage text
                        Text(
                            text = "$uploadProgress%",
                            color = amityColorWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    // Cancel button (centered) over progress ring
                    if (onCancelUpload != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp)
                                .background(amityColorBlack.copy(alpha = 0.6f), CircleShape)
                                .clip(CircleShape)
                                .clickable { onCancelUpload(message) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_close_reply),
                                contentDescription = "Cancel upload",
                                tint = amityColorWhite,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                }
            }
        } else if (painterState is AsyncImagePainter.State.Loading) {
            // Grey placeholder with indeterminate loading spinner while loading thumbnail
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AmityTheme.colors.baseShade4),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    color = AmityTheme.colors.baseShade1,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(40.dp),
                )
            }
        } else {
            // Grey placeholder with icon for failed or error states
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AmityTheme.colors.baseShade4),
                contentAlignment = Alignment.Center,
            ) {
                if (isUploading) {
                    // Circular progress ring with percentage text and centered cancel button
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        // Progress ring (background track)
                        CircularProgressIndicator(
                            progress = { uploadProgress / 100f },
                            color = amityColorWhite,
                            trackColor = amityColorWhite.copy(alpha = 0.2f),
                            strokeWidth = 3.dp,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier.size(100.dp),
                        )
                        // Progress percentage text
                        Text(
                            text = "$uploadProgress%",
                            color = amityColorWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    // Cancel button (centered) over progress ring
                    if (onCancelUpload != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp)
                                .background(amityColorBlack.copy(alpha = 0.6f), CircleShape)
                                .clip(CircleShape)
                                .clickable { onCancelUpload(message) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_close_reply),
                                contentDescription = "Cancel upload",
                                tint = amityColorWhite,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.amity_ic_chat_placeholder),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                    )
                }
            }
        }

        // Long-press highlight overlay
        if (isLongPressed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AmityTheme.colors.secondary.copy(alpha = 0.4f))
            )
        }
    }
    
    // Show image preview dialog when clicked
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

    // Dialog state for video preview
    var showPreviewDialog by remember { mutableStateOf(false) }

    // Extract video thumbnail from local file during upload
    var thumbnailBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isThumbnailLoading by remember { mutableStateOf(false) }
    val video = videoData.getVideo()
    val videoUri = video?.getUri()
    @Suppress("DEPRECATION")
    val videoFilePath = video?.getFilePath()
    
    // Get sent video URIs from CompositionLocal for immediate thumbnail extraction
    val sentVideoUris = LocalSentVideoUris.current
    
    // Server-side thumbnail (available for synced messages)
    val thumbnailImage = videoData.getThumbnailImage()
    val thumbnailUrl = thumbnailImage?.getUrl(AmityImage.Size.MEDIUM)
    
    // Load server thumbnail via Coil
    val thumbnailPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(thumbnailUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val thumbnailPainterState by thumbnailPainter.state.collectAsState()
    
    // Track upload progress and local file path
    var uploadProgress by remember { mutableStateOf(0) }
    var uploadFilePath by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(message.getMessageId(), isUploading) {
        if (isUploading) {
            try {
                val disposable = AmityFileService().getUploadInfo(message.getMessageId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { uploadInfo ->
                            uploadProgress = uploadInfo.getProgressPercentage()
                            val filePath = uploadInfo.getFilePath()
                            if (!filePath.isNullOrEmpty() && uploadFilePath == null) {
                                uploadFilePath = filePath
                            }
                        },
                        { error ->
                            // Fallback to default behavior if upload info fails
                        }
                    )
            } catch (e: Exception) {
                // Handle any errors gracefully without crashing
            }
        }
    }

    // Generate thumbnail from local video file
    // Priority: SDK videoUri > uploadInfo filePath > file path from SDK > sentVideoUris (most recent)
    val effectiveVideoSource: Any? = videoUri
        ?: videoFilePath
        ?: uploadFilePath
    
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
        // Show thumbnail: prefer server thumbnail, then local bitmap, then placeholder
        if (thumbnailPainterState is AsyncImagePainter.State.Success) {
            // Server-side thumbnail loaded via Coil
            Image(
                painter = thumbnailPainter,
                contentDescription = "Video thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(240.dp),
            )
            
            if (isUploading) {
                // Dark overlay + progress ring on top of thumbnail
                Box(
                    modifier = Modifier
                        .size(100.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    // Circular progress ring with percentage text and centered cancel button
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        // Progress ring (background track)
                        CircularProgressIndicator(
                            progress = { uploadProgress / 100f },
                            color = amityColorWhite,
                            trackColor = amityColorWhite.copy(alpha = 0.2f),
                            strokeWidth = 3.dp,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier.size(100.dp),
                        )
                        // Progress percentage text
                        Text(
                            text = "$uploadProgress%",
                            color = amityColorWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    // Cancel button (centered) over progress ring
                    if (onCancelUpload != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp)
                                .background(amityColorBlack.copy(alpha = 0.6f), CircleShape)
                                .clip(CircleShape)
                                .clickable { onCancelUpload(message) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_close_reply),
                                contentDescription = "Cancel upload",
                                tint = amityColorWhite,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                }
            } else {
                // Play button overlay for synced videos
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .background(amityColorBlack.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(amityColorBlack.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.amity_ic_chat_video_play),
                            contentDescription = "Play video",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
        } else if (thumbnailBitmap != null) {
            // Show extracted thumbnail
            Image(
                bitmap = thumbnailBitmap!!.asImageBitmap(),
                contentDescription = "Video thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(240.dp),
            )
            
            if (isUploading) {
                // Dark overlay + progress ring on top of thumbnail
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .background(amityColorBlack.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center,
                ) {
                    // Circular progress ring with percentage text and centered cancel button
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        // Progress ring (background track)
                        CircularProgressIndicator(
                            progress = { uploadProgress / 100f },
                            color = amityColorWhite,
                            trackColor = amityColorWhite.copy(alpha = 0.2f),
                            strokeWidth = 3.dp,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier.size(40.dp),
                        )
                    }
                    // Cancel button (centered) over progress ring
                    if (onCancelUpload != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(24.dp)
                                .clip(CircleShape)
                                .clickable { onCancelUpload(message) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_close_reply),
                                contentDescription = "Cancel upload",
                                tint = amityColorWhite,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                }
            } else {
                // Play button overlay for synced videos
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .background(amityColorBlack.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(amityColorBlack.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.amity_ic_chat_video_play),
                            contentDescription = "Play video",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
        } else {
            // Grey placeholder when no thumbnail available
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .background(AmityTheme.colors.baseShade4),
                contentAlignment = Alignment.Center,
            ) {
                if (isUploading) {
                    // Circular progress ring with percentage text and centered cancel button
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        // Progress ring (background track)
                        CircularProgressIndicator(
                            progress = { uploadProgress / 100f },
                            color = amityColorWhite,
                            trackColor = amityColorWhite.copy(alpha = 0.2f),
                            strokeWidth = 3.dp,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier.size(100.dp),
                        )
                        // Progress percentage text
                        Text(
                            text = "$uploadProgress%",
                            color = amityColorWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    // Cancel button (centered) over progress ring
                    if (onCancelUpload != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp)
                                .background(amityColorBlack.copy(alpha = 0.6f), CircleShape)
                                .clip(CircleShape)
                                .clickable { onCancelUpload(message) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_close_reply),
                                contentDescription = "Cancel upload",
                                tint = amityColorWhite,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                } else if (isThumbnailLoading) {
                    // Show indeterminate loading spinner while extracting thumbnail
                    CircularProgressIndicator(
                        color = AmityTheme.colors.baseShade1,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(40.dp),
                    )
                } else {
                    // Play icon placeholder for synced video without thumbnail
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(amityColorBlack.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.amity_ic_chat_video_play),
                            contentDescription = "Play video",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
        }
    }
    
    // Show video preview dialog when clicked
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
    val contentColor = if(isCurrentUser) AmityTheme.colors.primary
    else AmityTheme.colors.baseShade2
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
        Row(
            modifier = Modifier
                .background(Color.Transparent, RoundedCornerShape(20.dp))
                .border(
                    width = 1.dp,
                    color = if (isCurrentUser) AmityTheme.colors.primary
                        else AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(vertical = 4.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.amity_ic_delete_message),
                contentDescription = "deleted message",
                tint = contentColor
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = amityChatString("chat.message.deleted"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = contentColor,
                ),
            )
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

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AmityTheme.colors.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Title
            Text(
                text = amityChatString("chat.message.not.sent"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AmityTheme.colors.baseShade1,
                ),
                modifier = Modifier.padding(bottom = 16.dp),
            )

            HorizontalDivider(color = AmityTheme.colors.baseShade4, thickness = 1.dp)

            // Resend
            if (onResend != null) {
                Text(
                    text = amityChatString("chat.message.resend"),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.primary,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onResend() }
                        .padding(vertical = 14.dp),
                    textAlign = TextAlign.Center,
                )
                HorizontalDivider(color = AmityTheme.colors.baseShade4, thickness = 1.dp)
            }

            // Delete
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
                HorizontalDivider(color = AmityTheme.colors.baseShade4, thickness = 1.dp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cancel
            Text(
                text = amityChatString("chat.cancel"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AmityTheme.colors.primary,
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