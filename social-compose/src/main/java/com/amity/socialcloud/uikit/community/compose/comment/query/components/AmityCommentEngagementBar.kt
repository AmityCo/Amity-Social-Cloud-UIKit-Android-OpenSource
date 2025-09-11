package com.amity.socialcloud.uikit.community.compose.comment.query.components

import android.content.res.Resources
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitLongPressOrCancellation
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.common.readableSocialTimeDiff
import com.amity.socialcloud.uikit.common.model.AmitySocialReactions
import com.amity.socialcloud.uikit.common.reaction.picker.AmityReactionPicker
import com.amity.socialcloud.uikit.common.reaction.picker.getReactionIndexByX
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityConstants.POST_REACTION
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel.CommentBottomSheetState
import kotlinx.coroutines.TimeoutCancellationException
import kotlin.math.roundToInt


@Composable
fun AmityCommentEngagementBar(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    allowInteraction: Boolean,
    allowAction: Boolean = true,
    isReplyComment: Boolean,
    comment: AmityComment,
    isCreatedByMe: Boolean,
    onReply: (String) -> Unit,
    onEdit: () -> Unit,
) {
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.commentTrayComponentBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommentTrayComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val density = LocalDensity.current
    val screenWidthPx = with(density) { Resources.getSystem().displayMetrics.widthPixels }
    var anchorInWindow by remember { mutableStateOf<Rect?>(null) }
    var popupWidthPx by remember { mutableStateOf(0) }
    var initialPointerId by remember { mutableStateOf<PointerId?>(null) }
    var initialDownPos by remember { mutableStateOf<Offset?>(null) }
    var highlightedIndex by remember { mutableStateOf<Int?>(null) }
    var lastHapticIndex by remember { mutableStateOf<Int?>(null) }
    val reactions = remember { AmitySocialReactions.getList() }

    val myReactionState = remember {
        val firstReaction = comment.getMyReactions().firstOrNull()
        mutableStateOf(firstReaction ?: "")
    }
    val myReaction by remember(comment.getMyReactions()) { myReactionState }
    val reactingState = remember {
        mutableStateOf(Pair("",0))
    }
    val reacting by remember { reactingState }
    var localReactionCount by remember(comment.getReactionCount()) {
        mutableIntStateOf(comment.getReactionCount())
    }

    val isReacted = remember(myReaction, reacting) { myReaction.isNotEmpty() || reacting.first.isNotEmpty() }

    var reactionExpanded by remember { mutableStateOf(false) }

    val pickerPaddingStart = if (comment.getParentId() != null) 96.dp else 56.dp

    // Update the reaction state when the post's reactions change
    LaunchedEffect(comment.getMyReactions()) {
        if (myReactionState.value != comment.getMyReactions().firstOrNull()) {
            myReactionState.value = comment.getMyReactions().firstOrNull() ?: ""
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = comment.getCreatedAt()
                    .readableSocialTimeDiff() + if (comment.isEdited()) " (edited)" else "",
                style = AmityTheme.typography.captionLegacy.copy(
                    fontWeight = FontWeight.Normal,
                    color = AmityTheme.colors.baseShade2,
                ),
                modifier = modifier.testTag("comment_list/comment_bubble_timestamp")
            )
            if (allowInteraction) {
                Text(
                    text = reacting
                        .first
                        .ifEmpty { myReaction }
                        .ifEmpty { AmitySocialReactions.toReaction(POST_REACTION).name }
                        .replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase() else it.toString()
                        },
                    style = AmityTheme.typography.captionLegacy.copy(
                        color = if (isReacted) AmityTheme.colors.primary
                        else AmityTheme.colors.baseShade2,
                    ),
                    modifier = modifier
                        .onGloballyPositioned { coords ->
                            anchorInWindow = coords.boundsInWindow()
                        }
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                // Record the initial pointer ID for future drag tracking
                                val down = awaitFirstDown()
                                down.consume()
                                initialPointerId = down.id

                                // Check if it's a long press (to show reactions) or a tap (to like/unlike)
                                var longPressDetected = false

                                // Try to detect long press
                                try {
                                    withTimeout(viewConfiguration.longPressTimeoutMillis) {
                                        // Wait a bit to see if this is a long press
                                        awaitLongPressOrCancellation(down.id)?.let { longPress ->
                                            // Long press detected, show reaction picker
                                            longPressDetected = true
                                            val anchor = anchorInWindow ?: return@let
                                            initialDownPos = Offset(
                                                anchor.left.toFloat() + longPress.position.x,
                                                anchor.top.toFloat() + longPress.position.y
                                            )
                                            reactionExpanded = true
                                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                                            // Start drag tracking for the reaction selection
                                            var lastIdx: Int? = null
                                            drag(longPress.id) { change ->
                                                change.consume()
                                                if (!reactionExpanded) return@drag
                                                val relativeX = change.position.x + pickerPaddingStart.roundToPx()
                                                val idx = getReactionIndexByX(relativeX, popupWidthPx, reactions.size)
                                                if (idx != highlightedIndex) {
                                                    highlightedIndex = idx
                                                    if (idx != null && lastHapticIndex != idx) {
                                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                                        lastHapticIndex = idx
                                                    }
                                                }
                                                lastIdx = idx
                                            }

                                            // On drag end
                                            lastIdx?.let { chosen ->
                                                val chosenReaction = reactions[chosen]
                                                if (myReaction == chosenReaction.name) {
                                                    localReactionCount -= 1
                                                    myReactionState.value = ""
                                                    viewModel.removeReaction(
                                                        commentId = comment.getCommentId(),
                                                        reaction = chosenReaction.name
                                                    )
                                                } else {
                                                    // If user already reacted with different reaction
                                                    if (myReaction.isNotEmpty() && myReaction != chosenReaction.name) {
                                                        reactingState.value = Pair(chosenReaction.name, localReactionCount)
                                                        val previousReaction = myReaction
                                                        viewModel.switchReaction(
                                                            commentId = comment.getCommentId(),
                                                            reaction = chosenReaction.name,
                                                            previousReaction = previousReaction,
                                                            onSuccess = { reactingState.value = Pair("", 0) },
                                                            onError = { reactingState.value = Pair("", 0) }
                                                        )
                                                    } else {
                                                        localReactionCount += 1
                                                        myReactionState.value = chosenReaction.name
                                                        viewModel.addReaction(
                                                            commentId = comment.getCommentId(),
                                                            reaction = chosenReaction.name
                                                        )
                                                    }
                                                }
                                            }

                                            // Reset states
                                            highlightedIndex = null
                                            lastHapticIndex = null
                                            reactionExpanded = false
                                            initialPointerId = null
                                            initialDownPos = null
                                        }
                                    }
                                } catch (e: TimeoutCancellationException) {
                                    // Timeout for long press detection exceeded
                                }

                                // If no long press was detected, check if this was a tap
                                if (!longPressDetected) {
                                    if (reacting.first.isEmpty()) {
                                        // This was a tap, handle the like/unlike action
                                        val previousReaction = myReaction
                                        myReactionState.value = if (myReaction.isNotEmpty()) {
                                            ""
                                        } else {
                                            POST_REACTION
                                        }
                                        if (myReaction.isNotEmpty()) {
                                            localReactionCount += 1
                                        } else {
                                            localReactionCount -= 1
                                        }
                                        val isReacted = myReaction.isNotEmpty() || reacting.first.isNotEmpty()
                                        if (isReacted) {
                                            viewModel.addReaction(
                                                commentId = comment.getCommentId(),
                                                reaction = POST_REACTION,
                                            )
                                        } else {
                                            viewModel.removeReaction(
                                                commentId = comment.getCommentId(),
                                                reaction = previousReaction,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        .testTag("comment_list/comment_bubble_reaction_button")
                )

                if (!isReplyComment) {
                    Text(
                        text = context.getString(R.string.amity_reply),
                        style = AmityTheme.typography.captionLegacy.copy(
                            color = AmityTheme.colors.baseShade2,
                        ),
                        modifier = modifier
                            .clickable {
                                onReply(comment.getCommentId())
                            }
                            .testTag("comment_list/comment_bubble_reply_button")
                    )
                }
                if (allowAction) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_more_horiz),
                        contentDescription = null,
                        tint = AmityTheme.colors.secondaryShade2,
                        modifier = modifier
                            .size(20.dp)
                            .clickable {
                                viewModel.updateSheetUIState(
                                    CommentBottomSheetState.OpenSheet(
                                        comment.getCommentId()
                                    )
                                )
                            }
                            .testTag("comment_list/comment_bubble_meat_balls_button")
                    )
                }
            }
        }

        if (reactionExpanded && anchorInWindow != null) {
            val anchor = anchorInWindow!!
            Popup(
                properties = PopupProperties(
                    focusable = true,
                    clippingEnabled = false
                ),
                popupPositionProvider = object : PopupPositionProvider {
                    override fun calculatePosition(
                        anchorBounds: IntRect,
                        windowSize: IntSize,
                        layoutDirection: LayoutDirection,
                        popupContentSize: IntSize
                    ): IntOffset {
//                        popupWidthPx = popupContentSize.width
                        val spacingPx = with(density) { 8.dp.roundToPx() }
                        val x = (anchor.center.x - popupContentSize.width / 2f).roundToInt()
                        val likeIsInTop20 = anchor.top <= (windowSize.height * 0.2f)
                        val desiredY = if (likeIsInTop20) {
                            (anchor.bottom + spacingPx)
                        } else {
                            (anchor.top - popupContentSize.height - spacingPx)
                        }.roundToInt()
                        return IntOffset(
                            x.coerceIn(0, windowSize.width - popupContentSize.width),
                            desiredY.coerceIn(0, windowSize.height - popupContentSize.height)
                        )
                    }
                },
                onDismissRequest = {
                    reactionExpanded = false
                    highlightedIndex = null
                    lastHapticIndex = null
                }
            ) {
                Box(
                    modifier = Modifier
                        .onGloballyPositioned { coords ->
                            popupWidthPx = coords.size.width
                        },
                ) {
                    AmityReactionPicker(
                        modifier = Modifier.padding(start = pickerPaddingStart, top = 32.dp),
                        componentScope = componentScope,
                        selectedReaction = myReaction,
                        highlightedIndex = highlightedIndex,
                        onAddReaction = { reaction ->
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            // If the user has already reacted with a different reaction, remove that reaction first
                            if (myReaction.isNotEmpty() && myReaction != reaction) {
                                reactingState.value = Pair(reaction, localReactionCount)
                                // If the user is changing their reaction, we need to update the local state
                                val previousReaction = myReaction
                                viewModel.switchReaction(
                                    commentId = comment.getCommentId(),
                                    reaction = reaction,
                                    previousReaction = previousReaction,
                                    onSuccess = {
                                        reactingState.value = Pair("",0)
                                    },
                                    onError = { error->
                                        // If the reaction change fails, revert the local state
                                        reactingState.value = Pair("",0)
                                    },
                                )
                            } else {
                                // If the user is adding a reaction for the first time, we need to update the local state
                                localReactionCount += 1
                                myReactionState.value = reaction
                                viewModel.addReaction(comment.getCommentId(), reaction)
                            }
                        },
                        onRemoveReaction = { reaction ->
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            localReactionCount -= 1
                            myReactionState.value = ""
                            viewModel.removeReaction(comment.getCommentId(), reaction)
                        },
                        onDismiss = {
                            reactionExpanded = false
                            highlightedIndex = null
                            lastHapticIndex = null
                        }
                    )
                }
            }
        }

        AmityCommentActionsBottomSheet(
            modifier = modifier,
            componentScope = componentScope,
            viewModel = viewModel,
            commentId = comment.getCommentId(),
            isReplyComment = isReplyComment,
            isCommentCreatedByMe = isCreatedByMe,
            isFlaggedByMe = comment.isFlaggedByMe(),
            isFailed = false,
            onEdit = onEdit,
        )
    }
}