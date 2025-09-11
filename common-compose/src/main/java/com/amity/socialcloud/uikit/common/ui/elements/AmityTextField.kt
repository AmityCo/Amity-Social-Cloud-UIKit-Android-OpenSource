package com.amity.socialcloud.uikit.common.ui.elements


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hint: String = "",
    maxLines: Int = 1,
    maxCharacters: Int = -1,
    enabled: Boolean = true,
    textStyle: TextStyle = AmityTheme.typography.bodyLegacy.copy(
        color = if (enabled) AmityTheme.colors.base else AmityTheme.colors.baseShade2,
    ),
    hintColor: Color = AmityTheme.colors.baseShade3,
    shape: Shape = TextFieldDefaults.shape,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    innerPadding: PaddingValues? = null,
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        value = text,
        onValueChange = {
            if (maxCharacters == -1) {
                onValueChange(it)
            } else if (it.length <= maxCharacters) {
                onValueChange(it)
            } else {
                onValueChange(it.take(maxCharacters))
            }
        },
        enabled = enabled,
        maxLines = maxLines,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        cursorBrush = androidx.compose.ui.graphics.SolidColor(AmityTheme.colors.highlight),
        decorationBox = { innerTextField ->
           Box(
                modifier = Modifier
                    .background(Color.Transparent, shape)
                    .let {
                        if(innerPadding != null) {
                            it.padding(innerPadding)
                        } else {
                            it
                        }
                    }
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = hint,
                        color = hintColor,
                        style = textStyle
                    )
                }
                innerTextField()
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AmityTextFieldPreview() {
    AmityTextField(
        hint = "testing",
        onValueChange = {},
    )
}