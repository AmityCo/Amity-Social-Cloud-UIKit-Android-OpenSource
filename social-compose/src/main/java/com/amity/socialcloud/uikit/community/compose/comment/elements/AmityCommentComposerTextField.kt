package com.amity.socialcloud.uikit.community.compose.comment.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityCommentComposerTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    shouldClearText: Boolean = false,
    onValueChange: (String) -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    var text by remember { mutableStateOf(value) }

    LaunchedEffect(shouldClearText) {
        if (shouldClearText) {
            text = ""
        }
    }

    BasicTextField(
        maxLines = 10,
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        textStyle = AmityTheme.typography.body,
        modifier = modifier.background(
            color = AmityTheme.colors.secondaryShade4,
            shape = RoundedCornerShape(20.dp)
        )
    ) {
        TextFieldDefaults.DecorationBox(
            value = text,
            innerTextField = it,
            enabled = true,
            singleLine = false,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            placeholder = {
                Text(
                    text = "Say something nice...",
                    style = AmityTheme.typography.body.copy(
                        color = AmityTheme.colors.baseShade3,
                    ),
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                top = 10.dp,
                bottom = 10.dp,
                start = 12.dp,
                end = 12.dp,
            ),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AmityCommentComposerTextFieldPreview() {
    AmityCommentComposerTextField(
        onValueChange = {},
    )
}