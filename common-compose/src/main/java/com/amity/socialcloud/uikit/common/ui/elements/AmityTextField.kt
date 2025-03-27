package com.amity.socialcloud.uikit.common.ui.elements


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hint: String = "",
    maxLines: Int = 1,
    maxCharacters: Int = -1,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit,
) {
    TextField(
        enabled = enabled,
        maxLines = maxLines,
        value = text,
        onValueChange = {
            if (maxCharacters == -1) {
                onValueChange(it)
            } else if (it.length <= maxCharacters) {
                onValueChange(it)
            }
        },
        placeholder = {
            Text(
                text = hint,
                color = AmityTheme.colors.baseShade3
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        textStyle = AmityTheme.typography.bodyLegacy.copy(
            color = if (enabled) AmityTheme.colors.base else AmityTheme.colors.baseShade2,
        ),
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
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