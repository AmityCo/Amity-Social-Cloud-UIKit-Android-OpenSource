package com.amity.socialcloud.uikit.community.compose.livestream.create.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.amity.socialcloud.uikit.community.compose.R
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityCreateLivestreamNoInternetView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.5f)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        CircularProgressIndicator(
            modifier = Modifier
                .width(40.dp)
                .height(40.dp),
            color = Color.White,
            trackColor = Color.Gray,
            strokeWidth = 2.dp,
            strokeCap = StrokeCap.Round
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.amity_v4_create_livestream_no_internet_view_title),
            color = Color.White,
            style = AmityTheme.typography.titleLegacy.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.amity_v4_create_livestream_no_internet_view_desc),
            color = Color.White,
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
fun AmityCreateLivestreamNoInternetViewPreview() {
    MaterialTheme {
        AmityCreateLivestreamNoInternetView()
    }
}