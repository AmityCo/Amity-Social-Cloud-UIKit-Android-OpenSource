package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple


@Composable
fun AmitySearchBarView(
    modifier: Modifier = Modifier,
    hint: String,
    onSearch: (String) -> Unit,
) {
    var keyword by remember { mutableStateOf(TextFieldValue("")) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 9.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .weight(1f)
                .background(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_search),
                tint = AmityTheme.colors.baseShade2,
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
                modifier = modifier
                    .weight(1f)
                    .padding(vertical = 10.dp),
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
                    modifier = modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(AmityTheme.colors.baseShade3)
                        .clickableWithoutRipple {
                            keyword = TextFieldValue("")
                            onSearch("")
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_close),
                        tint = Color.White,
                        contentDescription = null,
                        modifier = modifier
                            .align(Alignment.Center)
                            .padding(5.dp)
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
    ) {}
}