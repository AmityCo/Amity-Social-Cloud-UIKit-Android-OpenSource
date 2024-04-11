package com.amity.socialcloud.uikit.common.utils

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.paging.LoadState
import androidx.palette.graphics.Palette.Swatch
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun Context.closePage() {
    getActivity()?.finishAfterTransition()
}

fun Context.closePageWithResult(resultCode: Int) {
    getActivity()?.let {
        it.setResult(resultCode)
        it.finishAfterTransition()
    }
}

fun Swatch.toComposeColor(): Color {
    return Color(
        red = (rgb shr 16 and 0xFF) / 255f,
        green = (rgb shr 8 and 0xFF) / 255f,
        blue = (rgb and 0xFF) / 255f,
    )
}

fun Color.shade(shade: AmityColorShade): Color {
    return Color(
        AmityColorPaletteUtil.getColor(
            color = this.toArgb(),
            shade = shade
        )
    )
}

class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()
    override suspend fun emit(interaction: Interaction) {}
    override fun tryEmit(interaction: Interaction) = true
}

fun Modifier.clickableWithoutRipple(
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier {
    return this.clickable(
        enabled = enabled,
        interactionSource = NoRippleInteractionSource(),
        indication = null
    ) {
        onClick()
    }
}

fun LazyListScope.pagingLoadStateItem(
    loadState: LoadState,
    loading: (@Composable LazyItemScope.() -> Unit)? = null,
    error: (@Composable LazyItemScope.(LoadState.Error) -> Unit)? = null,
) {
    if (loading != null && loadState == LoadState.Loading) {
        item(content = loading)
    }
    if (error != null && loadState is LoadState.Error) {
        item(content = { error(loadState) })
    }
}

fun Modifier.shimmerBackground(
    shape: Shape = RectangleShape,
    widthOfShadowBrush: Int = 700,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
    color: Color = Color.Unspecified
): Modifier = composed {
    val shimmerBaseColor = if (color == Color.Unspecified) {
        AmityTheme.colors.baseShade1
    } else {
        color
    }
    val shimmerColors = listOf(
        shimmerBaseColor.copy(alpha = 0.3f),
        shimmerBaseColor.copy(alpha = 0.5f),
        shimmerBaseColor.copy(alpha = 1.0f),
        shimmerBaseColor.copy(alpha = 0.5f),
        shimmerBaseColor.copy(alpha = 0.3f),
    )
    
    val transition = rememberInfiniteTransition(label = "")
    
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
                animation = tween(
                        durationMillis = durationMillis,
                        easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )
    
    this.background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
            end = Offset(x = translateAnimation.value, y = angleOfAxisY),
        ),
        shape = shape
    )
}