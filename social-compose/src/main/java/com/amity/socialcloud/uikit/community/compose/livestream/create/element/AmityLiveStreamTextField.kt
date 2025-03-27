package com.amity.socialcloud.uikit.community.compose.livestream.create.element

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityLivestreamTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeHolder: String? = null,
    singleLine: Boolean = false,
    enable: Boolean = true,
    maxCharLength: Int = Int.MAX_VALUE,
    textStyle: TextStyle = AmityTheme.typography.bodyLegacy,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    BasicTextField(
        value = value,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        onValueChange = {
            if (maxCharLength == -1) {
                onValueChange(it)
            } else if (it.length <= maxCharLength) {
                onValueChange(it)
            }
        },
        textStyle = textStyle,
        cursorBrush = SolidColor(Color.White),
        singleLine = singleLine,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                singleLine = singleLine,
                enabled = enable,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                interactionSource = interactionSource,
                visualTransformation = visualTransformation,
                contentPadding = OutlinedTextFieldDefaults.contentPadding(
                    start = 0.dp,
                    top = 0.dp,
                    end = 0.dp,
                    bottom = 0.dp
                ),
                container = {
                    OutlinedTextFieldDefaults.ContainerBox(
                        enabled = enable,
                        isError = false,
                        interactionSource = interactionSource,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        focusedBorderThickness = 0.dp,
                        unfocusedBorderThickness = 0.dp
                    )
                },
                placeholder = {
                    placeHolder?.let {
                        Text(
                            text = placeHolder,
                            color = Color.White,
                            style = textStyle
                        )
                    }
                }
            )
        }
    )
}

@Composable
@Preview
private fun DefaultAmityLivestreamTextFieldPreview() {
    MaterialTheme {
        AmityLivestreamTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier,
            placeHolder = "PlaceHolder",
            singleLine = false,
            maxCharLength = 30,
            textStyle = AmityTheme.typography.titleLegacy.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                lineHeight = 24.sp,
            ),
        )

    }
}