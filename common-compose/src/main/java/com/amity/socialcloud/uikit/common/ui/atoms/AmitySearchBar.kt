package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.compose.R as ComposeR
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

/**
 * Search bar ATOM — the design-token search input. Features still on the legacy color system keep
 * using `AmitySearchBarView`; this atom is its token-bound replacement.
 *
 * Anatomy (Input-family tokens): boxed container `Surface/Input/BoxedInput/Default`, `search-r`
 * leading glyph + `clear-r` trailing glyph tinted `Icon/Input/TextInput/Default`, text
 * `Text/Input/TextInput/Placeholder/Enabled(Filled)`, cursor `Text/Input/TextInput/TextCursor/Default`.
 */
@Composable
fun AmitySearchBar(
    modifier: Modifier = Modifier,
    hint: String,
    height: Dp? = null,
    cornerRadius: Dp = 8.dp,
    outerPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 9.dp),
    innerHorizontalPadding: Dp = 12.dp,
    textVerticalPadding: Dp = 10.dp,
    requestFocus: Boolean = false,
    onSearch: (String) -> Unit,
) {
    var keyword by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }

    val containerColor = AmityTheme.token(AmityColorToken.SurfaceInputBoxedInputDefault)
    val iconTint = AmityTheme.token(AmityColorToken.IconInputTextInputDefault)
    val textColor = AmityTheme.token(AmityColorToken.TextInputTextInputPlaceholderEnabledFilled)
    val hintColor = AmityTheme.token(AmityColorToken.TextInputTextInputPlaceholderEnabled)
    val cursorColor = AmityTheme.token(AmityColorToken.TextInputTextInputTextCursorDefault)

    LaunchedEffect(requestFocus) {
        if (requestFocus) {
            focusRequester.requestFocus()
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(outerPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
                .then(if (height != null) Modifier.heightIn(min = height) else Modifier)
                .background(
                    color = containerColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .padding(horizontal = innerHorizontalPadding)
        ) {
            Icon(
                painter = painterResource(id = ComposeR.drawable.amity_ic_search_r),
                tint = iconTint,
                contentDescription = null,
            )

            BasicTextField(
                value = keyword,
                onValueChange = {
                    keyword = it
                    onSearch(it.text)
                },
                singleLine = true,
                textStyle = AmityTheme.typography.bodyLegacy.copy(
                    color = textColor,
                ),
                cursorBrush = SolidColor(cursorColor),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .padding(vertical = textVerticalPadding),
                decorationBox = { innerTextField ->
                    if (keyword.text.isEmpty()) {
                        Text(
                            text = hint,
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = hintColor
                            ),
                        )
                    }
                    innerTextField()
                }
            )

            if (keyword.text.isNotEmpty()) {
                Icon(
                    painter = painterResource(id = ComposeR.drawable.amity_ic_clear_r),
                    tint = iconTint,
                    contentDescription = "Clear search",
                    modifier = Modifier
                        .size(20.dp)
                        .clickableWithoutRipple {
                            keyword = TextFieldValue("")
                            onSearch("")
                        }
                )
            }
        }
    }
}
