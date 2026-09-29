package com.amity.socialcloud.uikit.community.compose.user.profile.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageTab


@Composable
fun AmityUserProfileTabRow(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    tabs: List<AmityUserProfilePageTab>,
    selected: AmityUserProfilePageTab,
    onSelect: (AmityUserProfilePageTab) -> Unit,
    currentFilter: String,
    onFilterLaunch: () -> Unit,
    showFilter: Boolean = true
) {
    // Both tabs belong to Feed. With none left there is no row to head, no rule
    // to draw under it and nothing for the filter to filter.
    if (tabs.isEmpty()) {
        return
    }
    Column(
        modifier = modifier
            .background(AmityTheme.colors.background)
            .padding(top = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            tabs.forEach { tab ->
                val isSelected = tab == selected
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .weight(1f)
                        .clickableWithoutRipple {
                            onSelect(tab)
                        }
                ) {
                    Box(
                        modifier = Modifier.padding(bottom = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = tab.iconRes),
                            contentDescription = "",
                            tint = if (isSelected) AmityTheme.colors.base else AmityTheme.colors.secondaryShade3,
                            modifier = Modifier
                                .size(24.dp)
                                .then(
                                    if (tab == AmityUserProfilePageTab.MEDIA) Modifier.padding(2.dp)
                                    else Modifier
                                )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                color = if (isSelected) AmityTheme.colors.primary else Color.Transparent,
                                shape = RoundedCornerShape(
                                    topStart = 1.dp,
                                    topEnd = 1.dp
                                )
                            )
                    )
                }
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = AmityTheme.colors.divider,
            modifier = modifier,
        )

        if (!showFilter) return@Column

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.testTag("user_feed_filter")
                .height(42.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickableWithoutRipple {
                    onFilterLaunch.invoke()
                }
        ) {
            Text(
                text = currentFilter, // Public community & profile posts
                style = AmityTheme.typography.captionBold.copy(
                    color = AmityTheme.colors.baseShade1
                ),
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                tint = AmityTheme.colors.baseShade1,
                contentDescription = "Toggle visibility"
            )

        }
        HorizontalDivider(
            thickness = 1.dp,
            color = AmityTheme.colors.divider,
            modifier = modifier,
        )
    }
}
