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
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityCommunityProfileTabRow(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    AmityBaseElement(
        pageScope = pageScope,
        elementId = "community_profile_tab",
    ) {
        Column(
            modifier = modifier
                .background(color = AmityTheme.colors.background)
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .weight(1f)
                        .clickableWithoutRipple {
                            onSelect(0)
                        }
                ) {
                    Box(
                        modifier = Modifier.padding(bottom = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_community_feed),
                            contentDescription = "",
                            tint = if (selectedIndex == 0) AmityTheme.colors.base else AmityTheme.colors.secondaryShade3,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                color = if (selectedIndex == 0) AmityTheme.colors.highlight else Color.Transparent,
                                shape = RoundedCornerShape(
                                    topStart = 1.dp,
                                    topEnd = 1.dp
                                )
                            )
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .weight(1f)
                        .clickableWithoutRipple {
                            onSelect(1)
                        }
                ) {
                    Box(
                        modifier = Modifier.padding(bottom = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_community_pin),
                            contentDescription = "",
                            tint = if (selectedIndex == 1) AmityTheme.colors.base else AmityTheme.colors.secondaryShade3,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                color = if (selectedIndex == 1) AmityTheme.colors.highlight else Color.Transparent,
                                shape = RoundedCornerShape(
                                    topStart = 1.dp,
                                    topEnd = 1.dp
                                )
                            )
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .weight(1f)
                        .clickableWithoutRipple {
                            onSelect(2)
                        }
                ) {
                    Box(
                        modifier = Modifier.padding(bottom = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_community_image_feed),
                            contentDescription = "",
                            tint = if (selectedIndex == 2) AmityTheme.colors.base else AmityTheme.colors.secondaryShade3,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(2.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                color = if (selectedIndex == 2) AmityTheme.colors.highlight else Color.Transparent,
                                shape = RoundedCornerShape(
                                    topStart = 1.dp,
                                    topEnd = 1.dp
                                )
                            )
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .weight(1f)
                        .clickableWithoutRipple {
                            onSelect(3)
                        }
                ) {
                    Box(
                        modifier = Modifier.padding(bottom = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_community_video_feed),
                            contentDescription = "",
                            tint = if (selectedIndex == 3) AmityTheme.colors.base else AmityTheme.colors.secondaryShade3,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(2.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                color = if (selectedIndex == 3) AmityTheme.colors.highlight else Color.Transparent,
                                shape = RoundedCornerShape(
                                    topStart = 1.dp,
                                    topEnd = 1.dp
                                )
                            )
                    )
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
