package com.amity.socialcloud.uikit.community.compose.story.hyperlink.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityHyperlinkTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hint: String = "",
    maxCharacters: Int = -1,
    onValueChange: (String) -> Unit,
) {
    TextField(
        singleLine = true,
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
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = AmityTheme.typography.body.copy(
            color = AmityTheme.colors.base,
        ),
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AmityHyperlinkTextFieldPreview() {
    AmityHyperlinkTextField(
        hint = "https://example.com",
        onValueChange = {},
    )
}