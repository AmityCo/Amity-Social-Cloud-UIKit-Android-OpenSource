package com.amity.socialcloud.uikit.community.compose.search.components

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.search.global.AmityGlobalSearchType
import com.amity.socialcloud.uikit.community.compose.search.global.AmityGlobalSearchViewModel

@Composable
fun AmityTopSearchBarComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityGlobalSearchViewModel,
    shouldShowKeyboard: Boolean? = false
) {
    val context = LocalContext.current
    var keyword by remember { mutableStateOf(TextFieldValue("")) }
    val searchType by viewModel.searchType.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val title by remember(searchType) {
        derivedStateOf {
            if (searchType == AmityGlobalSearchType.MY_COMMUNITY) {
                "Search my communities"
            } else {
                "Search community and user"
            }
        }
    }

    LaunchedEffect(keyword.text) {
        viewModel.setKeyword(keyword.text)
    }

    LaunchedEffect(Unit) {
        if (shouldShowKeyboard == true) {
            focusRequester.requestFocus()
        }
    }

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "top_search_bar"
    ) {
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
                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = getComponentScope(),
                    elementId = "search_icon"
                ) {
                    Icon(
                        painter = painterResource(id = getConfig().getIcon()),
                        tint = AmityTheme.colors.baseShade2,
                        contentDescription = null,
                        modifier = modifier.testTag(getAccessibilityId()),
                    )
                }

                BasicTextField(
                    value = keyword,
                    onValueChange = {
                        keyword = it
                    },
                    singleLine = true,
                    textStyle = AmityTheme.typography.bodyLegacy.copy(
                        color = AmityTheme.colors.base,
                    ),
                    cursorBrush = SolidColor(AmityTheme.colors.highlight),
                    modifier = modifier
                        .weight(1f)
                        .padding(vertical = 14.dp)
                        .focusRequester(focusRequester),
                    decorationBox = { innerTextField ->
                        if (keyword.text.isEmpty()) {
                            Text(
                                text = title,
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
                            }
                    ) {
                        AmityBaseElement(
                            pageScope = pageScope,
                            componentScope = getComponentScope(),
                            elementId = "clear_button"
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.amity_ic_close),
                                tint = Color.White,
                                contentDescription = null,
                                modifier = modifier
                                    .align(Alignment.Center)
                                    .padding(5.dp)
                                    .testTag(getAccessibilityId()),
                            )
                        }
                    }
                }
            }

            AmityBaseElement(
                pageScope = pageScope,
                componentScope = getComponentScope(),
                elementId = "cancel_button"
            ) {
                Text(
                    text = getConfig().getText(),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        color = AmityTheme.colors.primary
                    ),
                    modifier = modifier
                        .align(Alignment.CenterVertically)
                        .clickableWithoutRipple { context.closePage() }
                        .testTag(getAccessibilityId()),
                )
            }
        }
    }
}