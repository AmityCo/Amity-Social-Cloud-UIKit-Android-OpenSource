package com.amity.socialcloud.uikit.chat.compose.message.element

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityChatMessageSkeleton(
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "msg_shimmer")
    val shimmerTranslate = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "msg_shimmer_translate",
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            AmityTheme.colors.baseShade4,
            AmityTheme.colors.baseShade4.copy(alpha = 0.4f),
            AmityTheme.colors.baseShade4,
        ),
        start = Offset(shimmerTranslate.value - 200f, 0f),
        end = Offset(shimmerTranslate.value, 0f),
    )

    // Layout mimics a chat conversation: alternating left/right bubbles,
    // bottom-aligned (reversed layout) so they appear from the bottom up.
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        ReceivedBubbleSkeleton(shimmerBrush, widthFraction = 0.35f)
        Spacer(modifier = Modifier.height(8.dp))

        SentBubbleSkeleton(shimmerBrush, widthFraction = 0.50f)
        Spacer(modifier = Modifier.height(8.dp))

        ReceivedBubbleSkeleton(shimmerBrush, widthFraction = 0.60f)
        Spacer(modifier = Modifier.height(8.dp))

        SentBubbleSkeleton(shimmerBrush, widthFraction = 0.40f)
        Spacer(modifier = Modifier.height(8.dp))

        ReceivedBubbleSkeleton(shimmerBrush, widthFraction = 0.45f)
        Spacer(modifier = Modifier.height(8.dp))

        SentBubbleSkeleton(shimmerBrush, widthFraction = 0.55f)
        Spacer(modifier = Modifier.height(8.dp))

        ReceivedBubbleSkeleton(shimmerBrush, widthFraction = 0.65f)
        Spacer(modifier = Modifier.height(8.dp))

        SentBubbleSkeleton(shimmerBrush, widthFraction = 0.35f)
        Spacer(modifier = Modifier.height(8.dp))

        ReceivedBubbleSkeleton(shimmerBrush, widthFraction = 0.50f)
        Spacer(modifier = Modifier.height(8.dp))

        SentBubbleSkeleton(shimmerBrush, widthFraction = 0.45f)
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun ReceivedBubbleSkeleton(shimmerBrush: Brush, widthFraction: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(shimmerBrush),
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            // Name
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(shimmerBrush),
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Bubble
            Box(
                modifier = Modifier
                    .fillMaxWidth(widthFraction)
                    .height(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(shimmerBrush),
            )
        }
    }
}

@Composable
private fun SentBubbleSkeleton(shimmerBrush: Brush, widthFraction: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 260.dp)
                .fillMaxWidth(widthFraction)
                .height(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(shimmerBrush),
        )
    }
}

/**
 * Skeleton for the chat page top bar — shows a shimmer avatar and display name
 * matching the layout of ConversationChatHeader / GroupChatHeader.
 */
@Composable
fun AmityChatHeaderSkeleton(
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "header_shimmer")
    val shimmerTranslate = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "header_shimmer_translate",
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            AmityTheme.colors.baseShade4,
            AmityTheme.colors.baseShade4.copy(alpha = 0.4f),
            AmityTheme.colors.baseShade4,
        ),
        start = Offset(shimmerTranslate.value - 200f, 0f),
        end = Offset(shimmerTranslate.value, 0f),
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Avatar skeleton
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(shimmerBrush),
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Display name skeleton
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(shimmerBrush),
        )
    }
}
