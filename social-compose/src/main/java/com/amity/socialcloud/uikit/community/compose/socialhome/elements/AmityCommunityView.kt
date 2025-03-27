package com.amity.socialcloud.uikit.community.compose.socialhome.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityCommunityAvatarWithLabelView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.community.compose.community.membership.element.AmityCommunityJoinButton

@Composable
fun AmityCommunityView(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    community: AmityCommunity,
    onClick: (AmityCommunity) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(80.dp)
            .clickableWithoutRipple {
                onClick(community)
            }
    ) {
        AmityBaseElement(
            pageScope = pageScope,
            componentScope = componentScope,
            elementId = "community_avatar"
        ) {
            AmityCommunityAvatarWithLabelView(
                community = community,
                modifier = modifier.testTag(getAccessibilityId()),
                label = null
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                if (!community.isPublic()) {
                    AmityBaseElement(
                        pageScope = pageScope,
                        componentScope = componentScope,
                        elementId = "community_private_badge"
                    ) {
                        Icon(
                            painter = painterResource(id = getConfig().getIcon()),
                            tint = AmityTheme.colors.baseShade1,
                            contentDescription = "Private Community",
                            modifier = Modifier
                                .size(16.dp)
                                .testTag(getAccessibilityId()),
                        )
                    }
                }

                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = componentScope,
                    elementId = "community_display_name"
                ) {
                    Text(
                        text = community.getDisplayName().trim(),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = modifier.testTag(getAccessibilityId()),
                    )
                }

                if (community.isOfficial()) {
                    AmityBaseElement(
                        pageScope = pageScope,
                        componentScope = componentScope,
                        elementId = "community_official_badge"
                    ) {
                        Image(
                            painter = painterResource(id = getConfig().getIcon()),
                            contentDescription = "Verified Community",
                            modifier = Modifier
                                .size(16.dp)
                                .testTag(getAccessibilityId()),
                        )
                    }
                }
            }

            if (community.getCategories().isNotEmpty()) {
                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = componentScope,
                    elementId = "community_category_name"
                ) {
                    AmityCommunityCategoryView(
                        categories = community.getCategories(),
                        modifier = modifier
                            .padding(top = 6.dp, end = 4.dp)
                            .testTag(getAccessibilityId()),
                        maxPreview = 3
                    )
                }
            }

            AmityBaseElement(
                pageScope = pageScope,
                componentScope = componentScope,
                elementId = "community_members_count"
            ) {
                Text(
                    text = "${community.getMemberCount().readableNumber()} members",
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.baseShade1,
                    ),
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .testTag(getAccessibilityId()),
                )
            }
        }
    }
}


@Composable
fun AmityJoinCommunityView(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    community: AmityCommunity,
    label: String? = null,
    onClick: (AmityCommunity) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(80.dp)
            .clickableWithoutRipple {
                onClick(community)
            }
    ) {
        AmityBaseElement(
            pageScope = pageScope,
            componentScope = componentScope,
            elementId = "community_avatar"
        ) {
            AmityCommunityAvatarWithLabelView(
                community = community,
                modifier = modifier.testTag(getAccessibilityId()),
                label = label
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp)
                .fillMaxHeight()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!community.isPublic()) {
                    AmityBaseElement(
                        pageScope = pageScope,
                        componentScope = componentScope,
                        elementId = "community_private_badge"
                    ) {
                        Icon(
                            painter = painterResource(id = getConfig().getIcon()),
                            tint = AmityTheme.colors.baseShade1,
                            contentDescription = "Private Community",
                            modifier = Modifier
                                .size(16.dp)
                                .testTag(getAccessibilityId()),
                        )
                    }
                }

                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = componentScope,
                    elementId = "community_display_name"
                ) {
                    Text(
                        text = community.getDisplayName(),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .testTag(getAccessibilityId()),
                    )
                }

                if (community.isOfficial()) {
                    AmityBaseElement(
                        pageScope = pageScope,
                        componentScope = componentScope,
                        elementId = "community_official_badge"
                    ) {
                        Image(
                            painter = painterResource(id = getConfig().getIcon()),
                            contentDescription = "Verified Community",
                            modifier = Modifier
                                .size(16.dp)
                                .testTag(getAccessibilityId()),
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        //.padding(end = 2.dp)
                        .fillMaxHeight()
                ) {
                    if (community.getCategories().isNotEmpty()) {
                        AmityBaseElement(
                            pageScope = pageScope,
                            componentScope = componentScope,
                            elementId = "community_category_name"
                        ) {
                            AmityCommunityCategoryView(
                                categories = community.getCategories(),
                                modifier = modifier
                                    .padding(top = 6.dp, end = 4.dp)
                                    .testTag(getAccessibilityId()),
                                maxPreview = 2
                            )
                        }
                    }

                    AmityBaseElement(
                        pageScope = pageScope,
                        componentScope = componentScope,
                        elementId = "community_members_count"
                    ) {
                        Text(
                            text = "${community.getMemberCount().readableNumber()} members",
                            style = AmityTheme.typography.captionLegacy.copy(
                                fontWeight = FontWeight.Normal,
                                color = AmityTheme.colors.baseShade1,
                            ),
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .testTag(getAccessibilityId()),
                        )
                    }
                }

                Column(

                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    AmityCommunityJoinButton(
                        community = community
                    )
                }

            }
        }
    }
}