package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText

@Composable
fun AmityEmptyNewsFeedComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    onExploreClicked: () -> Unit = {},
    onCreateClicked: () -> Unit = {},
) {
    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "empty_newsfeed"
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(600.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.align(Alignment.Center)
            ) {
                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = getComponentScope(),
                    elementId = "illustration"
                ) {
                    Image(
                        painter = painterResource(id = getConfig().getIcon()),
                        contentDescription = null,
                        modifier = modifier
                            .size(200.dp)
                            .testTag(getAccessibilityId()),
                    )
                }

                Spacer(modifier = modifier.height(4.dp))

                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = getComponentScope(),
                    elementId = "title"
                ) {
                    Text(
                        text = getConfig().getText(),
                        style = AmityTheme.typography.titleLegacy.copy(
                            color = AmityTheme.colors.secondaryShade3
                        ),
                        modifier = modifier.testTag(getAccessibilityId()),
                    )
                }
                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = getComponentScope(),
                    elementId = "description"
                ) {
                    Text(
                        text = getConfig().getText(),
                        style = AmityTheme.typography.captionLegacy.copy(
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.colors.secondaryShade3
                        ),
                        modifier = modifier.testTag(getAccessibilityId()),
                    )
                }

                Spacer(modifier = modifier.height(17.dp))

                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = getComponentScope(),
                    elementId = "explore_communities_button"
                ) {
                    Row(
                        modifier = modifier
                            .background(
                                color = AmityTheme.colors.primary,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .clickableWithoutRipple {
                                onExploreClicked()
                            }
                            .testTag(getAccessibilityId()),
                    ) {
                        Icon(
                            painter = painterResource(id = getConfig().getIcon()),
                            contentDescription = null,
                            tint = Color.White
                        )

                        Spacer(modifier = modifier.width(8.dp))

                        Text(
                            text = getConfig().getText(),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = Color.White
                            ),
                        )
                    }
                }

                Spacer(modifier = modifier.height(4.dp))

                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = getComponentScope(),
                    elementId = "create_community_button"
                ) {
                    Text(
                        text = "Create community",
                        style = AmityTheme.typography.bodyLegacy.copy(
                            color = AmityTheme.colors.primary
                        ),
                        modifier = modifier
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .clickableWithoutRipple {
                                onCreateClicked()
                            }
                            .testTag(getAccessibilityId()),
                    )
                }
            }
        }
    }
}