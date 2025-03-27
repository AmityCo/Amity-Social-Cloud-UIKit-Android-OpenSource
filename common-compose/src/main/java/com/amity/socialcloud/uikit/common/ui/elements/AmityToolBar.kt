package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText

@Composable
fun AmityToolBar(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    title: String = "",
    onBackClick: (() -> Unit) = {},
    rightMenuAction: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AmityTheme.colors.background)
            .padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        AmityBaseElement(
            pageScope = pageScope,
            elementId = "back_button"
        ) {
            val icon = getConfig().getIcon().takeIf {
                it != R.drawable.amity_empty
            } ?: R.drawable.amity_ic_back

            Icon(
                painter = painterResource(icon),
                contentDescription = "Close",
                tint = AmityTheme.colors.base,
                modifier = modifier
                    .size(24.dp)
                    .align(Alignment.CenterStart)
                    .clickableWithoutRipple {
                        onBackClick()
                    }
            )
        }

        AmityBaseElement(
            pageScope = pageScope,
            elementId = "title"
        ) {
            val text = getConfig().getText().takeIf { it.isNotEmptyOrBlank() } ?: title
            Text(
                text = text,
                style = AmityTheme.typography.titleLegacy,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp)
            )
        }

        if (rightMenuAction != null) {
            Box(
                modifier = modifier.align(Alignment.CenterEnd)
            ) {
                rightMenuAction()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityToolBarPreview() {
    AmityToolBar(
        title = "All members"
    )
}