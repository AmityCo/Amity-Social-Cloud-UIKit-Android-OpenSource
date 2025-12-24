package com.amity.socialcloud.uikit.community.compose.post.detail.elements

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityPostEventHostBadge(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
) {
    AmityBaseElement(
        componentScope = componentScope,
        elementId = "event_host_badge"
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = modifier
                .background(
                    color = Color(0xFFEAE2FF),
                    shape = RoundedCornerShape(size = 20.dp)
                )
                .padding(start = 4.dp, end = 6.dp)
                .height(18.dp)
                .testTag(getAccessibilityId()),
        ) {
            Image(
                painter = painterResource(id = R.drawable.amity_ic_event_host_badge),
                contentDescription = null,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = "Host",
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 18.sp,
                    color = Color(0xFF4B1BD0),
                )
            )
        }
    }
}
