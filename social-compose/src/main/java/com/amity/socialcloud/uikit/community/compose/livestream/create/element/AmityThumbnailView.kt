package com.amity.socialcloud.uikit.community.compose.livestream.create.element

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityThumbnailView(
    modifier: Modifier = Modifier,
    thumbnailsUri: Uri? = null,
    isShowProgressIndicator: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .width(70.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(size = 4.dp))
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(size = 4.dp)
            )
            .clickableWithoutRipple {
                onClick()
            }

    ) {
        AsyncImage(
            model = thumbnailsUri,
            contentDescription = "thumbnail image",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (isShowProgressIndicator) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(size = 4.dp)
                    )
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    color = Color.White,
                    trackColor = Color.Gray,
                    strokeWidth = 2.dp,
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }


}


@Composable
@Preview
private fun AmityThumbnailViewPreview() {
    MaterialTheme {
        AmityThumbnailView(modifier = Modifier)
    }
}