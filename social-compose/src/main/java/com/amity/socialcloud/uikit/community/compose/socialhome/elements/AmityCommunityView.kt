package com.amity.socialcloud.uikit.community.compose.socialhome.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityCommunityView(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    community: AmityCommunity,
    onClick: (AmityCommunity) -> Unit,
) {
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 8.dp)
            .clickableWithoutRipple {
                onClick(community)
            }
    ) {
        AmityBaseElement(
            pageScope = pageScope,
            componentScope = componentScope,
            elementId = "community_avatar"
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(context)
                    .data(community.getAvatar()?.getUrl())
                    .fallback(R.drawable.amity_ic_default_community_avatar_circular)
                    .crossfade(true)
                    .networkCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                placeholder = painterResource(R.drawable.amity_ic_default_community_avatar_circular),
                contentDescription = "Community Avatar Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = AmityTheme.colors.primaryShade3,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .testTag(getAccessibilityId()),
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .weight(1f)
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
                        style = AmityTheme.typography.body.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
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
                        modifier = modifier.testTag(getAccessibilityId()),
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
                    style = AmityTheme.typography.caption.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.baseShade1,
                    ),
                    modifier = modifier.testTag(getAccessibilityId()),
                )
            }
        }
    }
}