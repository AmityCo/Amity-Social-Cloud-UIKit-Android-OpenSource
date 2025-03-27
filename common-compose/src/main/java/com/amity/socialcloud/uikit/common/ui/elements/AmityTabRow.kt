package com.amity.socialcloud.uikit.common.ui.elements


import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.measureTextWidth

@Composable
fun AmityTabRow(
    modifier: Modifier = Modifier,
    tabs: List<AmityTabRowItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (tab, divider) = createRefs()

        HorizontalDivider(
            thickness = 1.dp,
            color = AmityTheme.colors.divider,
            modifier = modifier.constrainAs(divider) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
                .padding(horizontal = 16.dp)
                .nestedScroll(rememberNestedScrollInteropConnection())
                .constrainAs(tab) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            itemsIndexed(tabs) { _: Int, tab: AmityTabRowItem ->
                val isSelected = tab == tabs[selectedIndex]

                val titleStyle = AmityTheme.typography.titleLegacy
                val titleWidth = measureTextWidth(tab.title ?: "", titleStyle)

                val iconWidth = tab.icon?.let { 24.dp } ?: 0.dp

                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .width(iconWidth + titleWidth)
                        .clickableWithoutRipple {
                            if (!isSelected) {
                                onSelect(tabs.indexOf(tab))
                            }
                        }
                ) {
                    val highlightColor = AmityTheme.colors.highlight

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        tab.icon?.let { icon ->
                            Icon(
                                imageVector = ImageVector.vectorResource(id = icon),
                                contentDescription = "",
                                tint = if (isSelected) AmityTheme.colors.base else AmityTheme.colors.secondaryShade3,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        if (tab.icon != null && tab.title != null) {
                            Box(modifier = Modifier.width(4.dp))
                        }

                        tab.title?.let { title ->
                            Text(
                                text = title,
                                style = titleStyle.copy(
                                    color = if (isSelected) highlightColor else AmityTheme.colors.baseShade3
                                ),
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(
                                color = if (isSelected) highlightColor else Color.Transparent,
                                shape = RoundedCornerShape(topStart = 1.dp, topEnd = 1.dp)
                            )
                    )
                }
            }
        }
    }
}

data class AmityTabRowItem(
    @DrawableRes val icon: Int? = null,
    val title: String? = null,
)

@Preview(showBackground = true)
@Composable
fun AmityTabRowPreview() {
    AmityUIKitConfigController.setup(LocalContext.current)
    val tabs = listOf(
        AmityTabRowItem(title = "Members"),
        AmityTabRowItem(title = "Moderators"),
    )

    var selectedIndex by remember { mutableIntStateOf(0) }

    AmityTabRow(
        tabs = tabs,
        selectedIndex = selectedIndex,
        onSelect = {
            selectedIndex = it
        },
    )
}