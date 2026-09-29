package com.amity.socialcloud.uikit.community.compose.community.profile.element

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageTab

/**
 * Renders exactly the tabs it is given.
 *
 * The caller decides which tabs exist — see
 * [com.amity.socialcloud.uikit.community.compose.community.profile.visibleCommunityProfileTabs].
 * A row with one tab left is still a row of one; a row with none does not
 * render, rather than leaving a divider under an empty strip.
 */
@Composable
fun AmityCommunityProfileTabRow(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    tabs: List<AmityCommunityProfilePageTab>,
    selected: AmityCommunityProfilePageTab,
    onSelect: (AmityCommunityProfilePageTab) -> Unit,
) {
    if (tabs.isEmpty()) {
        return
    }
    AmityBaseElement(
        pageScope = pageScope,
        elementId = "community_profile_tab",
    ) {
        Column(
            modifier = modifier
                .background(color = AmityTheme.colors.background)
                .padding(top = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
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
                                        if (tab == AmityCommunityProfilePageTab.MEDIA) Modifier.padding(2.dp)
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
        }
    }
}
