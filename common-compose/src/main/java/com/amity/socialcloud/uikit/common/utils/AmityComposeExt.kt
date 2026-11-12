package com.amity.socialcloud.uikit.common.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.paging.LoadState
import androidx.palette.graphics.Palette.Swatch
import com.amity.socialcloud.uikit.common.common.toDp
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

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

fun Context.closePageWithResult(resultCode: Int, intent: Intent) {
    getActivity()?.let {
        it.setResult(resultCode, intent)
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

// The down event of the last tap that landed on a keyboardTapOutsideSkip() area.
// Pointer input runs on the main thread, so a plain object is enough.
private object KeyboardTapSkip {
    var pointerId: PointerId? = null
    var uptimeMillis: Long = -1L
}

// Closes the keyboard and clears focus when the user taps anywhere except a
// keyboardTapOutsideSkip() area. It reads taps in the Initial pass and does not consume them,
// so the tapped button still works. A scroll or a drag does not close the keyboard.
// Hiding the keyboard directly also covers View-based inputs, which clearFocus() does not close.
fun Modifier.clearFocusOnTapOutside(): Modifier = composed {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val view = LocalView.current
    this.pointerInput(Unit) {
        awaitEachGesture {
            val down = awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
            waitForUpOrCancellation(pass = PointerEventPass.Initial) ?: return@awaitEachGesture
            val skipped = KeyboardTapSkip.pointerId == down.id &&
                KeyboardTapSkip.uptimeMillis == down.uptimeMillis
            if (skipped || !view.isKeyboardOpen()) return@awaitEachGesture
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }
}

// Marks an area where a tap keeps the keyboard open: an input, a toolbar, a composer bar
// or a search bar. It does not consume the tap.
fun Modifier.keyboardTapOutsideSkip(): Modifier = this.pointerInput(Unit) {
    awaitEachGesture {
        val down = awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
        KeyboardTapSkip.pointerId = down.id
        KeyboardTapSkip.uptimeMillis = down.uptimeMillis
    }
}

// Returns true when the window reports no insets, so the caller still closes the keyboard.
private fun View.isKeyboardOpen(): Boolean {
    val insets = ViewCompat.getRootWindowInsets(this) ?: return true
    return insets.isVisible(WindowInsetsCompat.Type.ime())
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

fun Modifier.isVisible(
    threshold: Int = 60,
    onVisibilityChange: (Boolean) -> Unit
): Modifier {
    return this.onGloballyPositioned { layoutCoordinates: LayoutCoordinates ->
        val layoutHeight = layoutCoordinates.size.height
        val thresholdHeight = layoutHeight * threshold / 100
        val layoutTop = layoutCoordinates.positionInRoot().y
        val layoutBottom = layoutTop + layoutHeight

        val parent = layoutCoordinates.parentLayoutCoordinates

        parent?.boundsInRoot()?.let { rect: Rect ->
            val parentTop = rect.top
            val parentBottom = rect.bottom

            if (
                parentBottom - layoutTop > thresholdHeight &&
                (parentTop < layoutBottom - thresholdHeight)
            ) {
                onVisibilityChange(true)
            } else {
                onVisibilityChange(false)

            }
        }
    }
}

@Composable
fun copyText(text: String) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    clipboardManager.setText(AnnotatedString((text)))
}

@Composable
fun isKeyboardVisible(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

@Composable
fun getKeyboardHeight(): State<Dp> {
    val bottomHeight = WindowInsets.ime.getBottom(LocalDensity.current).toDp().dp
    return rememberUpdatedState(bottomHeight)
}

@Composable
fun measureTextWidth(text: String, style: TextStyle): Dp {
    val textMeasurer = rememberTextMeasurer()
    val widthInPixels = textMeasurer.measure(text, style).size.width
    return with(LocalDensity.current) { widthInPixels.toDp() }
}

@Composable
@ReadOnlyComposable
fun amityStringResource(
    configString: String = "",
    @StringRes id: Int = R.string.amity_common_empty_state_empty_string,
): String {
    LocalConfiguration.current
    val resources = LocalContext.current.resources
    return configString.ifEmpty { resources.getString(id) }
}

fun Context.amityStringResource(
    configString: String = "",
    @StringRes id: Int = R.string.amity_common_empty_state_empty_string,
): String {
    val resources = this.resources
    return configString.ifEmpty { resources.getString(id) }
}