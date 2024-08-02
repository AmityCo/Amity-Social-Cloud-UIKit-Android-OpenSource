package com.amity.socialcloud.uikit.common.ad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.compose.R

@Composable
fun AmityAdBadge(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
            .background(
                color = Color(0x80636878),
                shape = RoundedCornerShape(size = 20.dp)
            )
            .height(18.dp)
            .padding(start = 4.dp, end = 6.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.amity_ic_sponsor_badge),
            contentDescription = null,
            tint = Color.White,
        )
        Text(
            text = "Sponsored",
            style = TextStyle(
                fontSize = 11.sp,
                lineHeight = 18.sp,
                color = Color.White,
            )
        )
    }
}

@Preview
@Composable
fun AmityAdBadgePreview() {
    AmityAdBadge()
}