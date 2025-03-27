package com.amity.socialcloud.uikit.community.compose.story.target.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityCommunityAvatarView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.asColorList
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getValueAsList
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.story.target.utils.AmityStoryTargetRingUiState

@Composable
fun AmityStoryTargetElement(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope,
    isCommunityTarget: Boolean = false,
    community: AmityCommunity?,
    ringUiState: AmityStoryTargetRingUiState,
    hasManageStoryPermission: Boolean = false,
    onClick: () -> Unit
) {
    val isPublicCommunity by remember(community?.getCommunityId()) {
        mutableStateOf(community?.isPublic() == true)
    }
    val isOfficialCommunity by remember(community?.getCommunityId()) {
        mutableStateOf(community?.isOfficial() == true)
    }

    AmityBaseElement(
        componentScope = componentScope,
        elementId = "story_ring"
    ) {
        val colors = when (ringUiState) {
            AmityStoryTargetRingUiState.SEEN -> {
                getConfig().getValueAsList("background_color").asColorList()
            }

            AmityStoryTargetRingUiState.SYNCING,
            AmityStoryTargetRingUiState.HAS_UNSEEN -> {
                getConfig().getValueAsList("progress_color").asColorList()
            }

            AmityStoryTargetRingUiState.FAILED -> {
                listOf(AmityTheme.colors.alert)
            }
        }

        val displayName = if (isCommunityTarget) {
            "Story"
        } else {
            community?.getDisplayName()?.trim() ?: ""
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier
                .width(if (isCommunityTarget) 52.dp else 72.dp)
                .padding(top = 16.dp, bottom = 16.dp)
                .clickableWithoutRipple {
                    onClick()
                }
                .testTag("story_target_list/*")
        ) {
            Box(
                modifier = Modifier.size(if (isCommunityTarget) 48.dp else 64.dp)
            ) {
                AmityCommunityAvatarView(
                    community = community,
                    size = if (isCommunityTarget) 40.dp else 56.dp,
                    modifier = Modifier
                        .testTag("story_target_list/target_avatar")
                        .align(Alignment.Center)
                )

                AmityStoryGradientRingElement(
                    colors = colors,
                    isIndeterminate = ringUiState == AmityStoryTargetRingUiState.SYNCING,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("story_target_list/target_ring_view"),
                )

                val badge = when {
                    ringUiState == AmityStoryTargetRingUiState.FAILED -> R.drawable.amity_ic_error_circle
                    hasManageStoryPermission -> R.drawable.amity_ic_plus_circle
                    isOfficialCommunity -> R.drawable.amity_ic_verified
                    else -> null
                }

                if (badge != null) {
                    Image(
                        painter = painterResource(id = badge),
                        contentDescription = "",
                        modifier = Modifier
                            .size(if (isCommunityTarget) 16.dp else 20.dp)
                            .align(Alignment.BottomEnd)
                            .testTag("story_target_list/target_create_story_icon")
                    )
                }
            }

            //Spacer(modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (!isPublicCommunity && !isCommunityTarget) {
                    Image(
                        painter = painterResource(id = R.drawable.amity_ic_lock1),
                        contentDescription = "",
                        modifier = Modifier.size(12.dp)
                    )
                }
                Text(
                    text = displayName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.testTag("story_target_list/target_display_name")
                )
            }
        }
    }
}