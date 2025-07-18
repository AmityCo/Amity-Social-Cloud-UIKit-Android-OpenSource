package com.amity.socialcloud.uikit.community.compose.livestream.create.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityCreateLivestreamPendingApprovalView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.5f)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.amity_ic_base_hidden),
            contentDescription = null,
            modifier = Modifier
                .width(48.dp)
                .height(48.dp),
            tint = Color.White
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Waiting for approval",
            color = Color.White,
            style = AmityTheme.typography.titleLegacy.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "This live stream has started. However, it will have limited visibility until your post has been approved.",
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = AmityTheme.typography.captionLegacy.copy(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        )
        Spacer(Modifier.weight(1f))
    }
}

@Composable
@Preview
fun AmityCreateLivestreamPendingApprovalViewPreview() {
    MaterialTheme {
        AmityCreateLivestreamPendingApprovalView()
    }
}