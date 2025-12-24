package com.amity.socialcloud.uikit.community.compose.story.target.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.story.target.elements.AmityStoryGradientRingElement

@Composable
fun AmityCommunityLiveRoomTarget(
    modifier: Modifier = Modifier,
    post: AmityPost? = null,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .padding(top = 16.dp, bottom = 16.dp)
            .width(52.dp)
            .clickableWithoutRipple {
                onClick()
            }
            .testTag("story_target_list/*")
    ) {
        Box(
            modifier = Modifier.size(48.dp)
        ) {

            AmityUserAvatarView(
                user = post?.getCreator(),
                size = 40.dp,
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
                    .size(24.dp) // Total size of the badge including white border
                    .clip(CircleShape) // Clip the outer box to a circle
                    .background(Color.White) // This provides the white border
                    .padding(2.dp) // Inner padding to create the white border thickness
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(AmityTheme.colors.alert),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_live_badge),
                        contentDescription = "Live Icon",
                        modifier = Modifier.size(12.dp),
                        tint = Color.White
                    )
                }
            }

        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = post?.getCreator()?.getDisplayName() ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AmityTheme.typography.captionLegacy.copy(
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier
                    .weight(1f, fill = false)
                    .testTag("story_target_list/target_display_name")
            )
            if (post?.getCreator()?.isBrand() == true) {
                Image(
                    painter = painterResource(id = R.drawable.amity_ic_brand_badge),
                    contentDescription = "Brand badge",
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}