package com.amity.socialcloud.uikit.community.compose.story.hyperlink.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

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
                color = Color(0xFFA5A9B5)
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = TextStyle(
            fontSize = 15.sp,
            color = Color(0xFF292B32),
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