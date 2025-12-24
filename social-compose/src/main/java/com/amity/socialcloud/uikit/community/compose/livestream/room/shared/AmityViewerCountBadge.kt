package com.amity.socialcloud.uikit.community.compose.livestream.room.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityNumberUtil.getNumberAbbreveation
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityRoomViewerCountBadge(
    modifier: Modifier = Modifier,
    viewerCount: Int? = null,
    isHost: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                Color(0x80000000), // Pink/Red color for LIVE badge
                RoundedCornerShape(4.dp)
            )
            .padding(start = 4.dp, top = 4.dp, end = 6.dp, bottom = 4.dp)
    ) {
        if (!isHost) {
            Image(
                painter = painterResource(id = R.drawable.amity_ic_live_badge_dot),
                contentDescription = "Live badge dot",
                modifier = Modifier.size(16.dp),
            )
        }
        if (viewerCount != null && viewerCount > 0) {
            Image(
                painter = painterResource(id = R.drawable.amity_ic_cohost_chat_badge),
                contentDescription = "viewer icon",
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = viewerCount.let(::getNumberAbbreveation),
                style = AmityTheme.typography.captionBold.copy(
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.5.sp,
                ),
                color = Color.White,
            )
        } else {
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "LIVE",
                style = AmityTheme.typography.captionBold.copy(
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.5.sp,
                ),
                color = Color.White,
            )
        }
    }
}