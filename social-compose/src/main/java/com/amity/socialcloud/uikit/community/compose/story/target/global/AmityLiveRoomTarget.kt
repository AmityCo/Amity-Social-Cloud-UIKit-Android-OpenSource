package com.amity.socialcloud.uikit.community.compose.story.target.global

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityCommunityAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.story.target.elements.AmityStoryGradientRingElement

@Composable
fun AmityLiveRoomTarget(
    modifier: Modifier = Modifier,
    post: AmityPost? = null,
    onClick: () -> Unit,
) {
    val community = (post?.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .padding(top = 16.dp, bottom = 16.dp)
            .width(72.dp)
            .clickableWithoutRipple {
                onClick()
            }
            .testTag("story_target_list/*")
    ) {
        Box(
            modifier = Modifier.size(72.dp)
        ) {

            AmityCommunityAvatarView(
                community = community,
                size = 64.dp,
                modifier = Modifier
                    .testTag("story_target_list/target_avatar")
                    .align(Alignment.Center)
            )

            AmityStoryGradientRingElement(
                colors = listOf(Color(0xFFFF305A), Color(0xFFFF0000)),
                isIndeterminate = false,
                modifier = Modifier
                    .fillMaxSize(),
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(2.dp)
            ) {
                AmityUserAvatarView(
                    user = post?.getCreator(),
                    size = 24.dp
                )
            }


            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .border(1.5.dp, Color.White, RoundedCornerShape(6.dp))
                    .background(
                        color = AmityTheme.colors.alert,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LIVE",
                    color = Color.White,
                    style = AmityTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
                )
            }
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = community?.getDisplayName() ?: "",
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