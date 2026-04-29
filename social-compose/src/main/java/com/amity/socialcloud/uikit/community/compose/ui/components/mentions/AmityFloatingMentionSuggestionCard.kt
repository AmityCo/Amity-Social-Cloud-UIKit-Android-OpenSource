package com.amity.socialcloud.uikit.community.compose.ui.components.mentions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityFloatingMentionSuggestionCard(
    modifier: Modifier = Modifier,
    community: AmityCommunity? = null,
    keyword: String = "",
    maxHeight: Dp = 220.dp,
    productEnabled: Boolean = true,
    alreadyTaggedProductIds: Set<String> = emptySet(),
    onDismiss: () -> Unit = {},
    onUserSelected: (AmityUser) -> Unit = {},
    onProductSelected: (AmityProduct) -> Unit = {},
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val cardShape = RoundedCornerShape(12.dp)

    // IMPORTANT: don't clip the outer container, otherwise the overlapping close button gets clipped.
    Box(modifier = modifier) {
        // Card surface
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 12.dp, shape = cardShape, clip = false)
                .clip(cardShape)
                .background(Color.White)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                // Header: icon-only tabs filling the whole width (only show if product is enabled)
                if (productEnabled) {
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        modifier = Modifier
                            .fillMaxWidth(),
                        containerColor = Color.Transparent,
                        contentColor = AmityTheme.colors.primary,
                        divider = {},
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                height = 3.dp,
                                color = AmityTheme.colors.primary
                            )
                        }
                    ) {
                        Tab(
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 },
                            selectedContentColor = AmityTheme.colors.base,
                            unselectedContentColor = AmityTheme.colors.baseShade2,
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.amity_ic_user_mention_tab),
                                    contentDescription = "Mention Users Tab",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                        Tab(
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 },
                            selectedContentColor = AmityTheme.colors.base,
                            unselectedContentColor = AmityTheme.colors.baseShade2,
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.amity_ic_product_tag),
                                    contentDescription = "Mention Products Tab",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                    }

                    // Divider between tabs and list content (as per design)
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color(0xFFE5E7EB)
                    )
                }

                // Body - force user mention only if product is disabled
                if (productEnabled && selectedTabIndex == 1) {
                    AmityProductMentionSuggestionView(
                        keyword = keyword,
                        heightIn = maxHeight,
                        shape = RoundedCornerShape(0.dp),
                        alreadyTaggedProductIds = alreadyTaggedProductIds,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = maxHeight)
                            .background(Color.White),
                        onProductSelected = onProductSelected,
                    )
                } else {
                    AmityMentionSuggestionView(
                        community = community,
                        keyword = keyword,
                        heightIn = maxHeight,
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = maxHeight)
                            .background(Color.White),
                        onUserSelected = onUserSelected
                    )
                }
             }
         }

        // Close button: overlapping the top-right corner (like the design)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 10.dp, y = (-10).dp)
                .size(26.dp)
                .shadow(elevation = 6.dp, shape = CircleShape, clip = false)
                .clip(CircleShape)
                .background(Color.White)
                .border(width = 1.dp, color = Color(0xFFE5E7EB), shape = CircleShape)
                .clickableWithoutRipple { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_close),
                contentDescription = "Close",
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(12.dp)
            )
        }
    }
}
