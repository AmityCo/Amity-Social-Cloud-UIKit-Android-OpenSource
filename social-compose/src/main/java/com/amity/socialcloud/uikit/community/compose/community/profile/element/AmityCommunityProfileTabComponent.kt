package com.amity.socialcloud.uikit.community.compose.community.profile.element

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityCommunityProfileTabComponent(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	selectedIndex: Int,
	onSelect: (Int) -> Unit
) {
	AmityBaseComponent(
		pageScope = pageScope,
		componentId = "community_profile_tab",
	) {
		val tabs = listOf(
			CommunityProfileTab("community_feed", R.drawable.amity_ic_community_feed),
			CommunityProfileTab("community_pin", R.drawable.amity_ic_community_pin),
		)
		LazyRow(
			modifier = modifier
				.height(48.dp)
				.fillMaxWidth()
				.background(color = AmityTheme.colors.background)
				.padding(horizontal = 16.dp)
				.nestedScroll(rememberNestedScrollInteropConnection())
		) {
			itemsIndexed(tabs) { index: Int, tab: CommunityProfileTab ->
				val isSelected = tab == tabs[selectedIndex]
				AmityBaseElement(
					pageScope = pageScope,
					componentScope = getComponentScope(),
					elementId =
					when (tab.title) {
						"community_feed" -> "community_feed_tab_button"
						"community_pin" -> "community_pin_tab_button"
						else -> ""
					}
				) {
					Column(
						modifier = Modifier.clickableWithoutRipple {
							if (!isSelected) {
								onSelect(tabs.indexOf(tab))
							}
						}
					) {
						val highlightColor = AmityTheme.colors.highlight
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.width(75.dp)
								.height(48.dp)
								.padding(end = 16.dp)
								.drawBehind {
									val strokeWidth = 8f
									val x = size.width - 2f
									val y = size.height - 2f
									drawLine(
										color = if (isSelected) highlightColor else Color.Transparent,
										start = Offset(0f, y),// bottom-left point of the box
										end = Offset(x, y),// bottom-right point of the box
										strokeWidth = strokeWidth
									)
								}
						) {
							Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
								val icon = try {
									getConfig().getIcon()
								} catch(e: Exception) {
									R.drawable.amity_ic_community_feed
								}
								Icon(
									imageVector = ImageVector.vectorResource(id = tab.icon),
									contentDescription = "",
									tint = if (isSelected) AmityTheme.colors.base else AmityTheme.colors.secondaryShade3,
									modifier = Modifier
										.size(24.dp)
								)
							}
						}
					}
				}
			}
		}
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(0.dp)
				.height(1.dp)
				.background(color = AmityTheme.colors.baseShade4)
		)
	}
}

data class CommunityProfileTab(val title: String, val icon: Int)