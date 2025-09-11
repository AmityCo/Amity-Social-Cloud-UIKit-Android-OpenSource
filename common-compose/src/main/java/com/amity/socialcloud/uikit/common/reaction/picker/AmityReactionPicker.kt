package com.amity.socialcloud.uikit.common.reaction.picker

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.model.AmitySocialReactions
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import kotlin.math.roundToInt

@Composable
fun AmityReactionPicker(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    selectedReaction: String?,
    onAddReaction: (String) -> Unit,
    onRemoveReaction: (String) -> Unit,
    highlightedIndex: Int? = null,
    onDismiss: () -> Unit,
) {
    val density = LocalDensity.current
    val reactions = remember { AmitySocialReactions.getList() }

    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = "reaction_picker"
    ) {
        Row(
            modifier = modifier
                .shadow(
                    shape = RoundedCornerShape(32.dp),
                    elevation = 4.dp,
                    clip = false // Changed from true to false to avoid clipping shadow
                )
                .background(
                    color = AmityTheme.colors.background,
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(4.dp)
        ) {
            reactions
                .mapIndexed { index, reaction ->
                    val isSelected = selectedReaction == reaction.name
                    val isHighlighted = highlightedIndex == index

                    val targetScale = if (isHighlighted) 1.25f else 1.0f
                    val scale by animateFloatAsState(
                        targetValue = targetScale,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "reactionScale"
                    )
                    val targetOffsetY = if (isHighlighted) (-12).dp else 0.dp
                    val yOffset by animateDpAsState(targetValue = targetOffsetY, label = "reactionYOffset")

                    // Box wrapper to handle positioning of both elements
                    Box(
                        contentAlignment = Alignment.Center,
                    ) {
                        // Reaction name box - positioned above
                        if (isHighlighted) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .graphicsLayer {
                                        translationY = with(density) { yOffset.toPx() - 40.dp.toPx() } // Adjusted to position above the icon
                                    }
                                    .background(
                                        color = Color(0xAA333333), // semi-transparent dark background
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                                    .widthIn(min = 0.dp, max = 64.dp)
                            ) {
                                Text(
                                    text = reaction.name.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase() else it.toString()
                                    },
                                    color = Color.White,
                                    style = AmityTheme.typography.caption,
                                )
                            }
                        }

                        // Reaction icon box
                        Box(
                            modifier = Modifier
                                .background(
                                    color = when {
                                        isSelected -> AmityTheme.colors.secondaryShade4
                                        else -> Color.Transparent
                                    },
                                    shape = RoundedCornerShape(28.dp)
                                )
//                                .padding(4.dp)
                                // Use graphicsLayer at the outermost level to allow full scaling
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    translationY = with(density) { yOffset.toPx() }
                                }
                        ) {
                            AmityReactionItem(
                                icon = ImageVector.vectorResource(id = reaction.icon),
                                onDismiss = onDismiss,
                                action = {
                                    if (isSelected) {
                                        onRemoveReaction.invoke(reaction.name)
                                    } else {
                                        onAddReaction.invoke(reaction.name)
                                    }
                                }
                            )
                        }
                    }
                }
        }
    }
}

@Composable
fun AmityReactionItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onDismiss: () -> Unit,
    action: () -> Unit,
) {
    Row(
        modifier = modifier
            .clickableWithoutRipple {
                action.invoke()
                onDismiss()
            }
            .clip(RoundedCornerShape(28.dp))
            .padding(4.dp)
    ){
        Column {
            Image(
                imageVector = icon,
                contentDescription = "",
                modifier = Modifier
                    .size(32.dp)
            )
        }
    }
}

// Helper function to get index by x position
fun getReactionIndexByX(x: Float, popupWidthPx: Int, reactionsCount: Int): Int? {
    // Return null if x is outside the popup width
    if (x < 0 || x > popupWidthPx) {
        return null
    }

    val percent = if (popupWidthPx > 0) (x / popupWidthPx).coerceIn(0f, 1f) else 0f
    val idx = (percent * reactionsCount).toInt().coerceIn(0, reactionsCount - 1)
    return idx
}
