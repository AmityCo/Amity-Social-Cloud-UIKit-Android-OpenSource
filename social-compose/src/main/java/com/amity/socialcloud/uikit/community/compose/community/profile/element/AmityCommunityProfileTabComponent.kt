package com.amity.socialcloud.uikit.community.compose.community.profile.element

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon

@Composable
fun AmityCommunityProfileTabComponent(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	selectedIndex: Int,
	onSelect: (Int) -> Unit
) {
	AmityBaseComponent(
		pageScope = pageScope,
		componentId = "community_profile_tab",
	) {
		val tabs = listOf(
			CommunityProfileTab("community_feed"),
			CommunityProfileTab("community_pin"),
		)
		Column {
			LazyRow(
				modifier = modifier
					.height(56.dp)
					.fillMaxWidth()
					.background(color = AmityTheme.colors.background)
					.padding(start = 16.dp, top = 16.dp, end = 16.dp)
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
							else -> "community_feed_tab_button"
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
									.width(71.dp)
									.height(40.dp)
									.padding(end = 16.dp)
							) {
								Column {
									Box(
										modifier = Modifier
											.fillMaxWidth()
											.padding(bottom = 12.dp),
										contentAlignment = Alignment.Center,
									) {
										Icon(
											imageVector = ImageVector.vectorResource(id = getConfig().getIcon()),
											contentDescription = "",
											tint = if (isSelected) AmityTheme.colors.base else AmityTheme.colors.secondaryShade3,
											modifier = Modifier
												.size(24.dp)
										)
									}
									Box(modifier = Modifier
										.fillMaxWidth()
										.height(2.dp)
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
			}
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(1.dp)
					.background(color = AmityTheme.colors.baseShade4)
			)
		}
	}
}

data class CommunityProfileTab(val title: String)