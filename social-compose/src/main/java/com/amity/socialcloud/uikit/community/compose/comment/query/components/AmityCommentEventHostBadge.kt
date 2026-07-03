package com.amity.socialcloud.uikit.community.compose.comment.query.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.amityEventHostBadgeBackground
import com.amity.socialcloud.uikit.common.ui.theme.amityEventHostBadgeContent
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

@Composable
fun AmityCommentEventHostBadge(
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
            .background(
                color = amityEventHostBadgeBackground,
                shape = RoundedCornerShape(size = 20.dp)
            )
            .padding(start = 4.dp, end = 6.dp)
            .height(18.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.amity_ic_event_host_badge),
            contentDescription = null,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = amitySocialString("amity_social_button_host"),
            style = TextStyle(
                fontSize = 10.sp,
                lineHeight = 18.sp,
                color = amityEventHostBadgeContent,
            )
        )
    }
}
