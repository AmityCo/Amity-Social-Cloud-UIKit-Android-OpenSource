package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple


@Composable
fun AmitySearchBarView(
    modifier: Modifier = Modifier,
    hint: String,
    // Styling
    height: Dp? = null,
    cornerRadius: Dp = 8.dp,
    outerPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 9.dp),
    innerHorizontalPadding: Dp = 12.dp,
    textVerticalPadding: Dp = 10.dp,
    containerColor: Color = AmityTheme.colors.baseShade4,
    searchIconTint: Color = AmityTheme.colors.baseShade2,
    // Trailing clear icon
    clearIconContainerSize: Dp = 20.dp,
    clearIconContainerColor: Color = AmityTheme.colors.baseShade3,
    clearIconPadding: Dp = 5.dp,
    clearIconTint: Color = Color.White,
    requestFocus: Boolean = false,
    onSearch: (String) -> Unit,
) {
    var keyword by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }

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
                painter = painterResource(id = R.drawable.amity_ic_search),
                tint = searchIconTint,
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
                    color = AmityTheme.colors.base,
                ),
                cursorBrush = SolidColor(AmityTheme.colors.highlight),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .padding(vertical = textVerticalPadding),
                decorationBox = { innerTextField ->
                    if (keyword.text.isEmpty()) {
                        Text(
                            text = hint,
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = AmityTheme.colors.baseShade2
                            ),
                        )
                    }
                    innerTextField()
                }
            )

            if (keyword.text.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .size(clearIconContainerSize)
                        .clip(CircleShape)
                        .background(clearIconContainerColor)
                        .clickableWithoutRipple {
                            keyword = TextFieldValue("")
                            onSearch("")
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_close),
                        tint = clearIconTint,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(clearIconPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmitySearchBarViewPreview() {
    AmitySearchBarView(
        hint = "Search category",
        onSearch = {}
    )
}