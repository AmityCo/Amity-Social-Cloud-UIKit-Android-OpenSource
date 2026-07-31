package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * AmityInput — the atomic text-field family.
 *
 * One presentational atom that renders one of four Input sub-types selected via [variant]:
 * Boxed Input, Text Input, Chip Input, User Input. Colour comes exclusively from the
 * semantic color token engine via [AmityTheme.token]; token paths are built from the
 * grammar {Role}/Input/{SubType}/{Part}/{State}.
 *
 * Focus/caret are handled by the native text-field primitive. [state] exposes the
 * Default/Focused/Disabled/Error axis for consumer-driven overrides (e.g. validation).
 *
 * [size]/[boxedStyle] select the Boxed Input geometry ramp (Sm stadium, M Rounded, M Square) —
 * they only apply when [variant] is [AmityInputVariant.BOXED].
 */

enum class AmityInputVariant { BOXED, TEXT, CHIP, USER }

enum class AmityInputState { DEFAULT, FOCUSED, DISABLED, ERROR }

enum class AmityChipLayout { WRAP, OVERFLOW }

/** Size ramp for [AmityInputVariant.BOXED] — Sm is a stadium pill, M supports [AmityBoxedInputStyle]. */
enum class AmityInputSize { SM, M }

/** Corner-radius style for a [AmityInputSize.M] Boxed Input; Sm is always the stadium pill. */
enum class AmityBoxedInputStyle { ROUNDED, SQUARE }

data class AmityInputChip(
    val id: String,
    val label: String,
    val disabled: Boolean = false,
)

data class AmityInputUserData(
    val userId: String,
    val title: String,
    val username: String? = null,
    val description: String? = null,
    val actionLabel: String? = null,
    val disabled: Boolean = false,
)

/* ----------------------------------------------------------------------------------------------
 * Token-segment mapping. Enum values map to the exact token segment strings.
 * NOTE on state naming: BoxedInput surface is invariant (Default only) and its border only
 * paints on Error. Text/Chip Icon and Line use Default.Disabled.Error. TextInput Placeholder
 * uses Enabled (not Default) plus Focused, and content modifiers Filled and Highlight.
 * ---------------------------------------------------------------------------------------------- */

private fun AmityInputState.toIconLineSegment(): String = when (this) {
    AmityInputState.DISABLED -> "Disabled"
    AmityInputState.ERROR -> "Error"
    else -> "Default" // default and focused share the Default icon/line token
}

private fun AmityInputState.toPlaceholderStateSegment(): String = when (this) {
    AmityInputState.DEFAULT -> "Enabled"
    AmityInputState.FOCUSED -> "Focused"
    AmityInputState.DISABLED -> "Disabled"
    AmityInputState.ERROR -> "Error"
}

private fun AmityInputState.toChipPlaceholderStateSegment(): String = when (this) {
    AmityInputState.DISABLED -> "Disabled"
    AmityInputState.ERROR -> "Error"
    else -> "Enabled" // chip has no Focused
}

private fun AmityInputState.toTriStateSegment(): String = when (this) {
    AmityInputState.DISABLED -> "Disabled"
    AmityInputState.ERROR -> "Error"
    else -> "Default"
}

private fun AmityInputState.toBinarySegment(): String =
    if (this == AmityInputState.DISABLED) "Disabled" else "Default"

/**
 * Build the Placeholder token path for the Text Input family, appending the content modifier.
 */
private fun textPlaceholderPath(state: AmityInputState, filled: Boolean, highlight: Boolean): String {
    val base = "Text/Input/TextInput/Placeholder/" + state.toPlaceholderStateSegment()
    return when {
        highlight -> "$base-Highlight"
        filled -> "$base-Filled"
        else -> base
    }
}

@Composable
fun AmityInput(
    variant: AmityInputVariant,
    modifier: Modifier = Modifier,
    value: String = "",
    placeholder: String? = null,
    title: String? = null,
    hintText: String? = null,
    description: String? = null,
    username: String? = null,
    actionLabel: String? = null,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    showCharacterCount: Boolean = false,
    maxLength: Int? = null,
    chips: List<AmityInputChip> = emptyList(),
    chipLayout: AmityChipLayout = AmityChipLayout.WRAP,
    multiline: Boolean = false,
    size: AmityInputSize = AmityInputSize.SM,
    boxedStyle: AmityBoxedInputStyle = AmityBoxedInputStyle.ROUNDED,
    user: AmityInputUserData? = null,
    state: AmityInputState = AmityInputState.DEFAULT,
    highlightMatch: Boolean = false,
    onChangeText: (String) -> Unit = {},
    onChipsChange: (List<AmityInputChip>) -> Unit = {},
    onFocus: () -> Unit = {},
    onBlur: () -> Unit = {},
    onSubmit: (String) -> Unit = {},
    onActionClick: (AmityInputUserData) -> Unit = {},
) {
    when (variant) {
        AmityInputVariant.BOXED -> BoxedInput(
            modifier = modifier,
            value = value,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            multiline = multiline,
            size = size,
            style = boxedStyle,
            state = state,
            highlightMatch = highlightMatch,
            onChangeText = onChangeText,
            onFocus = onFocus,
            onBlur = onBlur,
            onSubmit = onSubmit,
        )

        AmityInputVariant.TEXT -> TextInput(
            modifier = modifier,
            value = value,
            placeholder = placeholder,
            title = title,
            hintText = hintText,
            description = description,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            showCharacterCount = showCharacterCount,
            maxLength = maxLength,
            state = state,
            highlightMatch = highlightMatch,
            onChangeText = onChangeText,
            onFocus = onFocus,
            onBlur = onBlur,
            onSubmit = onSubmit,
        )

        AmityInputVariant.CHIP -> ChipInput(
            modifier = modifier,
            value = value,
            placeholder = placeholder,
            title = title,
            hintText = hintText,
            description = description,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            showCharacterCount = showCharacterCount,
            maxLength = maxLength,
            chips = chips,
            state = state,
            onChangeText = onChangeText,
            onChipsChange = onChipsChange,
            onFocus = onFocus,
            onBlur = onBlur,
            onSubmit = onSubmit,
        )

        AmityInputVariant.USER -> UserInput(
            modifier = modifier,
            user = user,
            state = state,
            onActionClick = onActionClick,
        )
    }
}

/* ---------------------------------------------------------------------------------- BoxedInput */

/** Per-[AmityInputSize]/[AmityBoxedInputStyle] box geometry — height is `H`, radius `H/2` for the
 * stadium Sm and the Rounded M row; Square M keeps the same box but a flat 8dp radius. */
private data class BoxedInputGeometry(
    val height: Dp,
    val paddingVertical: Dp,
    val paddingHorizontal: Dp,
    val radius: Dp,
)

private fun boxedInputGeometry(size: AmityInputSize, style: AmityBoxedInputStyle): BoxedInputGeometry =
    when (size) {
        AmityInputSize.SM -> BoxedInputGeometry(
            height = 40.dp,
            paddingVertical = 10.dp,
            paddingHorizontal = 12.dp,
            radius = 20.dp,
        )

        AmityInputSize.M -> when (style) {
            AmityBoxedInputStyle.ROUNDED -> BoxedInputGeometry(
                height = 48.dp,
                paddingVertical = 14.dp,
                paddingHorizontal = 12.dp,
                radius = 24.dp,
            )

            AmityBoxedInputStyle.SQUARE -> BoxedInputGeometry(
                height = 48.dp,
                paddingVertical = 14.dp,
                paddingHorizontal = 12.dp,
                radius = 8.dp,
            )
        }
    }

/** Icon-to-content gap shared by Boxed Input and Text Input (Chip Input uses its own 16dp gap). */
private val INPUT_ICON_GAP = 8.dp

@Composable
private fun BoxedInput(
    modifier: Modifier,
    value: String,
    placeholder: String?,
    @DrawableRes leadingIcon: Int?,
    @DrawableRes trailingIcon: Int?,
    multiline: Boolean,
    size: AmityInputSize,
    style: AmityBoxedInputStyle,
    state: AmityInputState,
    highlightMatch: Boolean,
    onChangeText: (String) -> Unit,
    onFocus: () -> Unit,
    onBlur: () -> Unit,
    onSubmit: (String) -> Unit,
) {
    // Surface is invariant (Default only); border paints only on Error.
    val surface = AmityTheme.token("Surface/Input/BoxedInput/Default")
    val geometry = boxedInputGeometry(size, style)
    val shape = RoundedCornerShape(geometry.radius)

    // heightIn's min lets multiline content wrap and grow the box past its resting height.
    var boxMod = modifier
        .fillMaxWidth()
        .heightIn(min = geometry.height)
        .clip(shape)
        .background(surface, shape)
    if (state == AmityInputState.ERROR) {
        val border = AmityTheme.token("Border/Input/BoxedInput/Error")
        boxMod = boxMod.border(1.dp, border, shape)
    }

    Row(
        modifier = boxMod.padding(vertical = geometry.paddingVertical, horizontal = geometry.paddingHorizontal),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(INPUT_ICON_GAP),
    ) {
        InputIcon(leadingIcon, "Icon/Input/TextInput/" + state.toIconLineSegment())
        InlineTextField(
            modifier = Modifier.weight(1f),
            value = value,
            placeholder = placeholder,
            state = state,
            highlightMatch = highlightMatch,
            onChangeText = onChangeText,
            onFocus = onFocus,
            onBlur = onBlur,
            onSubmit = onSubmit,
            singleLine = !multiline,
        )
        InputIcon(trailingIcon, "Icon/Input/TextInput/" + state.toIconLineSegment())
    }
}

/* ----------------------------------------------------------------------------------- TextInput */

@Composable
private fun TextInput(
    modifier: Modifier,
    value: String,
    placeholder: String?,
    title: String?,
    hintText: String?,
    description: String?,
    @DrawableRes leadingIcon: Int?,
    @DrawableRes trailingIcon: Int?,
    showCharacterCount: Boolean,
    maxLength: Int?,
    state: AmityInputState,
    highlightMatch: Boolean,
    onChangeText: (String) -> Unit,
    onFocus: () -> Unit,
    onBlur: () -> Unit,
    onSubmit: (String) -> Unit,
) {
    val underline = AmityTheme.token("Line/Input/TextInput/Underlined/" + state.toIconLineSegment())

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        title?.let {
            Text(
                text = it,
                style = AmityTheme.typography.caption.copy(
                    fontSize = 17.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = AmityTheme.token("Text/Input/TextInput/Title/Default"),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(INPUT_ICON_GAP),
        ) {
            InputIcon(leadingIcon, "Icon/Input/TextInput/" + state.toIconLineSegment())
            InlineTextField(
                modifier = Modifier.weight(1f),
                value = value,
                placeholder = placeholder,
                state = state,
                highlightMatch = highlightMatch,
                onChangeText = onChangeText,
                onFocus = onFocus,
                onBlur = onBlur,
                onSubmit = onSubmit,
                singleLine = true,
            )
            InputIcon(trailingIcon, "Icon/Input/TextInput/" + state.toIconLineSegment())
        }

        // Underline rule.
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 1.dp)
                .background(underline)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val hint = hintText ?: description
            if (hint != null) {
                val hintToken = if (state == AmityInputState.ERROR) {
                    "Text/Input/TextInput/HintText/Error"
                } else {
                    "Text/Input/TextInput/HintText/Default"
                }
                Text(
                    text = hint,
                    style = AmityTheme.typography.caption.copy(fontSize = 13.sp, lineHeight = 18.sp),
                    color = AmityTheme.token(hintToken),
                )
            } else {
                Spacer(Modifier.width(0.dp))
            }
            if (showCharacterCount) {
                val count = maxLength?.let { "${value.length}/$it" } ?: "${value.length}"
                Text(
                    text = count,
                    style = AmityTheme.typography.caption.copy(fontSize = 13.sp, lineHeight = 18.sp),
                    color = AmityTheme.token("Text/Input/TextInput/TextCount/Default"),
                )
            }
        }
    }
}

/* ----------------------------------------------------------------------------------- ChipInput */

@Composable
private fun ChipInput(
    modifier: Modifier,
    value: String,
    placeholder: String?,
    title: String?,
    hintText: String?,
    description: String?,
    @DrawableRes leadingIcon: Int?,
    @DrawableRes trailingIcon: Int?,
    showCharacterCount: Boolean,
    maxLength: Int?,
    chips: List<AmityInputChip>,
    state: AmityInputState,
    onChangeText: (String) -> Unit,
    onChipsChange: (List<AmityInputChip>) -> Unit,
    onFocus: () -> Unit,
    onBlur: () -> Unit,
    onSubmit: (String) -> Unit,
) {
    val underline = AmityTheme.token("Line/Input/ChipInput/Underlined/" + state.toTriStateSegment())

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        title?.let {
            Text(
                text = it,
                style = AmityTheme.typography.caption.copy(
                    fontSize = 17.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = AmityTheme.token("Text/Input/ChipInput/Title/" + state.toTriStateSegment()),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            InputIcon(leadingIcon, "Icon/Input/ChipInput/" + state.toTriStateSegment())
            // Committed chips.
            chips.forEach { chip ->
                val chipState = if (chip.disabled) AmityInputState.DISABLED else state
                Chip(
                    chip = chip,
                    trailingIcon = trailingIcon,
                    state = chipState,
                    onRemove = { onChipsChange(chips.filterNot { it.id == chip.id }) },
                )
            }
            InlineTextField(
                modifier = Modifier.weight(1f),
                value = value,
                placeholder = placeholder,
                state = state,
                highlightMatch = false, // Chip Input defines no -Highlight modifier
                placeholderTokenOverride =
                    "Text/Input/ChipInput/Placeholder/" + state.toChipPlaceholderStateSegment(),
                onChangeText = onChangeText,
                onFocus = onFocus,
                onBlur = onBlur,
                onSubmit = onSubmit,
                singleLine = true,
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 1.dp)
                .background(underline)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val hint = hintText ?: description
            if (hint != null) {
                val hintToken = if (state == AmityInputState.ERROR) {
                    "Text/Input/ChipInput/HintText/Error"
                } else {
                    "Text/Input/ChipInput/HintText/Default"
                }
                Text(
                    text = hint,
                    style = AmityTheme.typography.caption.copy(fontSize = 13.sp, lineHeight = 18.sp),
                    color = AmityTheme.token(hintToken),
                )
            } else {
                Spacer(Modifier.width(0.dp))
            }
            if (showCharacterCount) {
                val count = maxLength?.let { "${value.length}/$it" } ?: "${value.length}"
                Text(
                    text = count,
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.token("Text/Input/ChipInput/TextCount/Default"),
                )
            }
        }
    }
}

@Composable
private fun Chip(
    chip: AmityInputChip,
    @DrawableRes trailingIcon: Int?,
    state: AmityInputState,
    onRemove: () -> Unit,
) {
    val seg = state.toTriStateSegment()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = chip.label,
            style = AmityTheme.typography.caption.copy(
                fontSize = 15.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            color = AmityTheme.token("Text/Input/ChipInput/Title/$seg"),
        )
        if (trailingIcon != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = trailingIcon),
                contentDescription = "remove",
                tint = AmityTheme.token("Icon/Input/ChipInput/$seg"),
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(99.dp))
                    // remove affordance
                    .clickableNoRipple(enabled = !chip.disabled, onClick = onRemove),
            )
        }
    }
}

/* ----------------------------------------------------------------------------------- UserInput */

@Composable
private fun UserInput(
    modifier: Modifier,
    user: AmityInputUserData?,
    state: AmityInputState,
    onActionClick: (AmityInputUserData) -> Unit,
) {
    if (user == null) return
    // User Input is binary Default/Disabled only; error/focused fall back to default.
    val disabled = user.disabled || state == AmityInputState.DISABLED
    val seg = if (disabled) "Disabled" else "Default"

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = user.title,
                style = AmityTheme.typography.bodyBold.copy(fontSize = 17.sp, lineHeight = 24.sp),
                color = AmityTheme.token("Text/Input/UserInput/Title/$seg"),
            )
            user.username?.let {
                Text(
                    text = it,
                    style = AmityTheme.typography.caption.copy(fontSize = 13.sp, lineHeight = 18.sp),
                    color = AmityTheme.token("Text/Input/UserInput/UserName/$seg"),
                )
            }
            user.description?.let {
                Text(
                    text = it,
                    style = AmityTheme.typography.caption.copy(fontSize = 13.sp, lineHeight = 18.sp),
                    color = AmityTheme.token("Text/Input/UserInput/TextDescription/$seg"),
                )
            }
        }
        user.actionLabel?.let {
            Text(
                text = it,
                style = AmityTheme.typography.bodyBold.copy(
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal,
                ),
                color = AmityTheme.token("Text/Input/UserInput/Action/$seg"),
                modifier = Modifier.clickableNoRipple(
                    enabled = !disabled,
                    onClick = { onActionClick(user) },
                ),
            )
        }
    }
}

/* ------------------------------------------------------------------------------------- helpers */

@Composable
private fun InputIcon(@DrawableRes icon: Int?, tokenPath: String) {
    if (icon == null) return
    Icon(
        imageVector = ImageVector.vectorResource(id = icon),
        contentDescription = null,
        tint = AmityTheme.token(tokenPath),
        modifier = Modifier.size(20.dp),
    )
}

/**
 * Shared single-line editable field. Resolves the placeholder token from the Text Input family
 * (state + Filled/Highlight modifiers) unless [placeholderTokenOverride] is supplied (Chip Input,
 * which has its own placeholder token set and no content-modifier axis). Cursor uses the
 * TextInput TextCursor token.
 */
@Composable
private fun InlineTextField(
    modifier: Modifier,
    value: String,
    placeholder: String?,
    state: AmityInputState,
    highlightMatch: Boolean,
    placeholderTokenOverride: String? = null,
    onChangeText: (String) -> Unit,
    onFocus: () -> Unit,
    onBlur: () -> Unit,
    onSubmit: (String) -> Unit,
    singleLine: Boolean,
) {
    val enabled = state != AmityInputState.DISABLED
    val cursor = AmityTheme.token("Text/Input/TextInput/TextCursor/Default")
    val filled = value.isNotEmpty()

    val valueColor = AmityTheme.token(
        placeholderTokenOverride
            ?: textPlaceholderPath(state, filled = true, highlight = highlightMatch)
    )
    val placeholderColor = AmityTheme.token(
        placeholderTokenOverride
            ?: textPlaceholderPath(state, filled = false, highlight = highlightMatch)
    )

    BasicTextField(
        value = value,
        onValueChange = onChangeText,
        modifier = modifier.onFocusChangedCompat(onFocus, onBlur),
        enabled = enabled,
        singleLine = singleLine,
        textStyle = AmityTheme.typography.body.copy(color = valueColor, fontSize = 15.sp),
        cursorBrush = SolidColor(cursor),
        keyboardActions = KeyboardActions(
            onDone = { onSubmit(value) },
            onSearch = { onSubmit(value) },
            onSend = { onSubmit(value) },
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
        ),
        decorationBox = { inner ->
            if (!filled && placeholder != null) {
                Text(
                    text = placeholder,
                    style = AmityTheme.typography.body.copy(fontSize = 15.sp),
                    color = placeholderColor,
                )
            }
            inner()
        },
    )
}

/* Small local modifier helpers to keep the atom self-contained. */

private fun Modifier.clickableNoRipple(enabled: Boolean, onClick: () -> Unit): Modifier =
    if (enabled) this.clickable(onClick = onClick) else this

private fun Modifier.onFocusChangedCompat(onFocus: () -> Unit, onBlur: () -> Unit): Modifier =
    this.onFocusChanged { fs ->
        if (fs.isFocused) onFocus() else onBlur()
    }
