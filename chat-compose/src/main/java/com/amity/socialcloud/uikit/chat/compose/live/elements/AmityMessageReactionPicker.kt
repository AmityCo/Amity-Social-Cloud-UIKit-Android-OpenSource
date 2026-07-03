package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.common.localization.LocalAmityCommonStringProvider
import com.amity.socialcloud.uikit.common.localization.amitySocialReactionDisplayName
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.reaction.picker.getReactionIndexByX
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityReactionTooltipBase

@Composable
fun AmityMessageReactionPicker(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    message: AmityMessage,
    show: Boolean,
    isCurrentUser: Boolean = false,
    onAddReaction: (AmityMessage, String) -> Unit,
    onRemoveReaction: (AmityMessage, String) -> Unit,
    onDismiss: () -> Unit,
) {
    if (show) {
        AmityBaseElement(
            pageScope = pageScope,
            componentScope = componentScope,
            elementId = "message_reaction"
        ) {
            Popup(
                alignment = if (isCurrentUser) Alignment.BottomEnd else Alignment.BottomStart,
                onDismissRequest = onDismiss,
            ) {
                AmityMessageReactionPickerContent(
                    message = message,
                    onAddReaction = onAddReaction,
                    onRemoveReaction = onRemoveReaction,
                    onDismiss = onDismiss,
                )
            }
        }
    }
}

@Composable
fun AmityMessageReactionPickerContent(
    modifier: Modifier = Modifier,
    message: AmityMessage,
    onAddReaction: (AmityMessage, String) -> Unit,
    onRemoveReaction: (AmityMessage, String) -> Unit,
    onDismiss: () -> Unit,
) {
    val density = LocalDensity.current
    val myReaction = message.getMyReactions().firstOrNull()
    val reactions = remember { AmityMessageReactions.getList() }
    val view = LocalView.current
    val hapticFeedback = LocalHapticFeedback.current

    var highlightedIndex by remember { mutableStateOf<Int?>(null) }
    var lastHapticIndex by remember { mutableStateOf<Int?>(null) }
    var pickerWidthPx by remember { mutableStateOf(0) }

    // Extra top padding gives the tooltip room to animate upward without being clipped.
    Box(modifier = modifier.padding(top = 40.dp)) {
        Row(
            modifier = Modifier
                .shadow(shape = RoundedCornerShape(32.dp), elevation = 4.dp, clip = false)
                .background(color = AmityTheme.colors.background, shape = RoundedCornerShape(32.dp))
                .graphicsLayer { clip = false }
                .padding(4.dp)
                .onGloballyPositioned { coords ->
                    pickerWidthPx = coords.size.width
                    val screenLocation = IntArray(2)
                    view.getLocationOnScreen(screenLocation)
                }
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        // Determine initial highlighted index from touch-down position
                        val initialRelX = down.position.x
                        highlightedIndex = getReactionIndexByX(initialRelX, pickerWidthPx, reactions.size)

                        drag(down.id) { change ->
                            change.consume()
                            val relX = change.position.x
                            val idx = getReactionIndexByX(relX, pickerWidthPx, reactions.size)
                            if (idx != highlightedIndex) {
                                highlightedIndex = idx
                                if (idx != null && lastHapticIndex != idx) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    lastHapticIndex = idx
                                }
                            }
                        }

                        // Finger lifted — fire the highlighted reaction
                        val chosen = highlightedIndex
                        highlightedIndex = null
                        lastHapticIndex = null
                        if (chosen != null) {
                            val chosenReaction = reactions[chosen]
                            if (myReaction == chosenReaction.name) {
                                onRemoveReaction(message, chosenReaction.name)
                            } else {
                                onAddReaction(message, chosenReaction.name)
                            }
                            onDismiss()
                        }
                    }
                }
        ) {
        reactions.mapIndexed { index, reaction ->
            val isSelected = myReaction == reaction.name
            val isHighlighted = highlightedIndex == index

            val scale by animateFloatAsState(
                targetValue = if (isHighlighted) 1.25f else 1.0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow,
                ),
                label = "reactionScale",
            )
            val yOffset by animateDpAsState(
                targetValue = if (isHighlighted) (-12).dp else 0.dp,
                label = "reactionYOffset",
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(40.dp),
            ) {
                if (isHighlighted) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .graphicsLayer {
                                translationY = with(density) { yOffset.toPx() - 40.dp.toPx() }
                            }
                            .background(
                                color = amityReactionTooltipBase.copy(alpha = 0.67f),
                                shape = RoundedCornerShape(16.dp),
                            )
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                            .widthIn(min = 0.dp, max = 64.dp),
                    ) {
                        Text(
                            text = amityChatReactionDisplayName(reaction.name),
                            color = amityColorWhite,
                            style = AmityTheme.typography.captionSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(
                            color = if (isSelected) AmityTheme.colors.secondaryShade4 else Color.Transparent,
                            shape = RoundedCornerShape(28.dp),
                        )
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationY = with(density) { yOffset.toPx() }
                        },
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = reaction.icon),
                        contentDescription = reaction.name,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(32.dp),
                    )
                }
            }
        }
    }
    }
}

@Composable
fun amityChatReactionDisplayName(reactionKey: String): String {
    val stringKey = when(reactionKey) {
        "like" -> "chat.reaction.label.like"
        "love" -> "chat.reaction.label.love"
        "fire" -> "chat.reaction.label.fire"
        "happy" -> "chat.reaction.label.happy"
        "sad" -> "chat.reaction.label.sad"
        else -> "chat.reaction.label.${reactionKey}"
    }
    val resolved = LocalAmityCommonStringProvider.current.getString(stringKey)
    // If getString returns the raw key, it means no translation was found — fall back to title-case
    return if (resolved == stringKey) {
        reactionKey.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    } else {
        resolved
    }
}
