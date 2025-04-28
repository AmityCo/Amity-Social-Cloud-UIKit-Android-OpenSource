package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageTab
import com.amity.socialcloud.uikit.community.compose.socialhome.elements.AmitySocialHomeNavigationButton

@Composable
fun AmitySocialHomeTopNavigationComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    selectedTab: AmitySocialHomePageTab,
    isSeen: Boolean = true,
    searchButtonAction: () -> Unit,
    notificationButton: () -> Unit,
) {
    val context = LocalContext.current

    val behavior by lazy {
        AmitySocialBehaviorHelper.socialHomeTopNavigationComponentBehavior
    }
    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "top_navigation",
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 13.dp)
        ) {
            AmityBaseElement(
                pageScope = pageScope,
                componentScope = getComponentScope(),
                elementId = "header_label"
            ) {
                Text(
                    text = getConfig().getText(),
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontSize = 20.sp,
                    ),
                    modifier = modifier
                        .align(Alignment.CenterStart)
                        .testTag(getAccessibilityId())
                )
            }

            Row(
                modifier = modifier.align(Alignment.CenterEnd)
            ) {
                when (selectedTab) {
                    AmitySocialHomePageTab.NEWSFEED,
                    AmitySocialHomePageTab.EXPLORE,
                    AmitySocialHomePageTab.MY_COMMUNITIES,
                        -> {
                        AmityBaseElement(
                            pageScope = pageScope,
                            componentScope = getComponentScope(),
                            elementId = "notification_tray_button"
                        ) {
                            Box() {
                                Image(
                                    painter = painterResource(getConfig().getIcon()),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clickableWithoutRipple {
                                            notificationButton()
                                        }
                                )
                                if (isSeen == false) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(12.dp)
                                            .background(Color.White, shape = CircleShape)
                                            .padding(2.dp)
                                            .background(color = AmityTheme.colors.alert, shape = CircleShape)
                                    )
                                }
                            }

                        }
                    }
                }

                Spacer(modifier = modifier.width(10.dp))

                when (selectedTab) {
                    AmitySocialHomePageTab.NEWSFEED,
                    AmitySocialHomePageTab.EXPLORE,
                    AmitySocialHomePageTab.MY_COMMUNITIES,
                        -> {
                        AmityBaseElement(
                            pageScope = pageScope,
                            componentScope = getComponentScope(),
                            elementId = "global_search_button"
                        ) {
                            AmitySocialHomeNavigationButton(
                                icon = getConfig().getIcon(),
                                background = AmityTheme.colors.baseShade4,
                                iconSize = 20.dp,
                                modifier = modifier
                                    .size(32.dp)
                                    .testTag(getAccessibilityId()),
                                onClick = searchButtonAction
                            )
                        }
                    }
                }

                Spacer(modifier = modifier.width(10.dp))

                var expanded by remember { mutableStateOf(false) }
                when (selectedTab) {
                    AmitySocialHomePageTab.NEWSFEED,
                    AmitySocialHomePageTab.MY_COMMUNITIES,
                        -> {
                        AmityBaseElement(
                            pageScope = pageScope,
                            componentScope = getComponentScope(),
                            elementId = "post_creation_button"
                        ) {
                            AmitySocialHomeNavigationButton(
                                icon = getConfig().getIcon(),
                                background = AmityTheme.colors.baseShade4,
                                iconSize = 16.dp,
                                modifier = modifier
                                    .size(32.dp)
                                    .testTag(getAccessibilityId()),
                                onClick = {
                                    when (selectedTab) {
                                        AmitySocialHomePageTab.NEWSFEED -> {
                                            expanded = true
                                        }

                                        AmitySocialHomePageTab.MY_COMMUNITIES -> {
                                            behavior.goToCreateCommunityPage(
                                                AmitySocialHomeTopNavigationComponentBehavior.Context(
                                                    componentContext = context,
                                                )
                                            )
                                        }

                                        else -> {}
                                    }
                                },
                            )
                        }
                    }

                    else -> {}
                }

                AmityCreatePostMenuComponent(
                    modifier = modifier,
                    pageScope = pageScope,
                    expanded = expanded,
                    onDismiss = { expanded = false },
                )
            }
        }
    }
}


@Preview
@Composable
private fun TestNotificationIconPreview() {
    Box {
        Image(
            painter = painterResource(R.drawable.amity_ic_notification_tray_default),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)

        )

    }
}