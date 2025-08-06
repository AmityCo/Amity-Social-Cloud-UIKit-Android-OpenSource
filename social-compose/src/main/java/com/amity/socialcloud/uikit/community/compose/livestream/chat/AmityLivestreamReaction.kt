package com.amity.socialcloud.uikit.community.compose.livestream.chat

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.model.AmityReactionType
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun ReactionPicker(
    modifier: Modifier = Modifier,
    onReactionSelected: (AmityReactionType) -> Unit,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    onDismiss: () -> Unit,
) {
    val reactions = AmityMessageReactions.getList()
    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = "message_reaction"
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clickable { onDismiss() }
        ) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .shadow(
                        elevation = 6.dp,
                        spotColor = Color(0x0A000000),
                        ambientColor = Color(0x0A000000)
                    )
                    .shadow(
                        elevation = 64.dp,
                        spotColor = Color(0x14000000),
                        ambientColor = Color(0x14000000)
                    )
                    .shadow(
                        elevation = 24.dp,
                        spotColor = Color(0x1A000000),
                        ambientColor = Color(0x1A000000)
                    )
                    .background(
                        color = Color(0x66FFFFFF),
                        shape = RoundedCornerShape(size = 9999.dp)
                    )
                    .clickable(enabled = false) { }, // Prevent dismiss when clicking card
                shape = RoundedCornerShape(size = 9999.dp),
            ) {
                LazyRow(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(reactions) { reaction ->
                            AmityLivestreamReactionItem(
                                icon = ImageVector.vectorResource(id = reaction.icon),
                                onDismiss = onDismiss,
                                action = {
                                    onReactionSelected(reaction)
                                }
                            )
                    }
                }
            }
        }
    }
}


@Composable
fun AmityLivestreamReactionItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onDismiss: () -> Unit,
    action: () -> Unit,
) {
        Column(
            modifier = modifier
                .clickableWithoutRipple {
                    action.invoke()
                }
        ) {
            Image(
                imageVector = icon,
                contentDescription = "",
                modifier = Modifier
                    .size(32.dp)
            )
        }
}

data class FloatingReaction(
    val reaction: AmityReactionType,
    val id: Long,
    val startTime: Long = System.currentTimeMillis()
)

@Composable
fun FloatingReactionsOverlay1(
    reactions: List<FloatingReaction>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        reactions.forEach { reaction ->
            FloatingReactionItem(
                reaction = reaction,
                onAnimationEnd = {
                    // Remove reaction after animation
                }
            )
        }
    }
}

@Composable
fun FloatingReactionsOverlay(
    reactions: MutableList<FloatingReaction>,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(reactions.size) {
        println("FloatingReactionsOverlay: ${reactions.size} reactions")
    }

    Box(modifier = modifier) {
        reactions.forEach { reaction ->
            key(reaction.id) {
                FloatingReactionItem(
                    reaction = reaction,
                    onAnimationEnd = {
                        println("Removing reaction: ${reaction.id}")
                        reactions.remove(reaction)
                    }
                )
            }
        }
    }
}

@Composable
fun FloatingReactionItem(
    reaction: FloatingReaction,
    onAnimationEnd: () -> Unit
) {
    val animationDuration = 4000 // 4 seconds
    val startTime = reaction.startTime
    val currentTime by produceState(0L) {
        while (true) {
            value = System.currentTimeMillis()
            delay(16) // ~60fps
        }
    }

    val progress = ((currentTime - startTime) / animationDuration.toFloat()).coerceIn(0f, 1f)

    if (progress >= 1f) {
        LaunchedEffect(reaction.id) {
            onAnimationEnd()
        }
        return
    }

    // Smooth upward movement
    val animatedY by animateFloatAsState(
        targetValue = if (progress > 0) 0.2f else 0.8f, // Move from bottom to top
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = LinearEasing
        ),
        label = "floating_y"
    )

    // Scale animation - starts normal, gets smaller
    val scale by animateFloatAsState(
        targetValue = if (progress < 0.2f) 1f else 0.6f, // Shrink after 30% of animation
        animationSpec = tween(
            durationMillis = (animationDuration * 0.97f).toInt(),
            delayMillis = (animationDuration * 0.03f).toInt(),
            easing = FastOutSlowInEasing
        ),
        label = "floating_scale"
    )

    // Fade out animation
    val alpha by animateFloatAsState(
        targetValue = if (progress < 0.3f) 1f else 0f, // Start fading at 70%
        animationSpec = tween(
            durationMillis = (animationDuration * 0.97f).toInt(),
            delayMillis = (animationDuration * 0.03f).toInt(),
            easing = LinearEasing
        ),
        label = "floating_alpha"
    )

    val startX = remember { Random.nextFloat() * 0.6f + 0.2f } // Random X position

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Image(
            imageVector = ImageVector.vectorResource(id = reaction.reaction.icon),
            contentDescription = "",
            modifier = Modifier
                .size(36.dp)
                .offset(
                    x = (maxWidth * startX),
                    y = (maxHeight * animatedY)
                )
                .scale(scale)
                .alpha(alpha)
        )
    }
}