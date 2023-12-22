package com.amity.socialcloud.uikit.community.compose.story.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.utils.asColorList
import com.amity.socialcloud.uikit.community.compose.utils.getValueAsList

@Composable
fun AmityStoryTargetElement(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope,
    communityDisplayName: String = "",
    avatarUrl: String = "",
    isAlreadyViewed: Boolean = false,
    isPrivateCommunity: Boolean = false,
    isOfficialCommunity: Boolean = false,
    isSingleCommunityTarget: Boolean = false,
    hasManageStoryPermission: Boolean = false,
    onClick: () -> Unit
) {
    AmityBaseElement(
        componentScope = componentScope,
        elementId = "story_ring"
    ) {
        val colors = if (isAlreadyViewed) {
            getConfig().getValueAsList("background_color").asColorList()
        } else {
            getConfig().getValueAsList("progress_color").asColorList()
        }

        val displayName = if (isSingleCommunityTarget) {
            "Story"
        } else {
            communityDisplayName
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .width(if (isSingleCommunityTarget) 52.dp else 72.dp)
                .clickable {
                    onClick()
                }
        ) {
            Box(
                modifier = Modifier.size(if (isSingleCommunityTarget) 48.dp else 64.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(if (isSingleCommunityTarget) 40.dp else 56.dp)
                        .align(Alignment.Center)
                ) {
                    if (avatarUrl.isEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.amity_ic_default_profile1),
                            contentScale = ContentScale.Fit,
                            contentDescription = "Avatar Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                    } else {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(avatarUrl)
                                .crossfade(true)
                                .build(),
                            contentScale = ContentScale.Crop,
                            contentDescription = "Avatar Image",
                            modifier = modifier
                                .clip(CircleShape)
                                .align(Alignment.Center)
                        )
                    }
                }

                AmityStoryGradientRingElement(
                    colors = colors,
                    modifier = Modifier.fillMaxSize(),
                )

                val badge = when {
                    isSingleCommunityTarget && hasManageStoryPermission -> R.drawable.amity_ic_plus_circle
                    isOfficialCommunity -> R.drawable.amity_ic_verified
                    else -> null
                }

                if (badge != null) {
                    Image(
                        painter = painterResource(id = badge),
                        contentDescription = "",
                        modifier = Modifier
                            .size(if (isSingleCommunityTarget) 16.dp else 20.dp)
                            .align(Alignment.BottomEnd)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (isPrivateCommunity && !isSingleCommunityTarget) {
                    Image(
                        painter = painterResource(id = R.drawable.amity_ic_lock1),
                        contentDescription = "",
                        modifier = Modifier.size(12.dp)
                    )
                }
                Text(
                    text = displayName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityStoryTargetElementPreview() {
    AmityUIKitConfigController.setup(LocalContext.current)
    AmityBaseComponent(componentId = "story_tab_component") {
        AmityStoryTargetElement(
            componentScope = getComponentScope(),
            communityDisplayName = "Meow",
            avatarUrl = "",
            isAlreadyViewed = false,
            isPrivateCommunity = false,
            isOfficialCommunity = true,
            isSingleCommunityTarget = true,
            hasManageStoryPermission = true,
        ) {}
    }
}