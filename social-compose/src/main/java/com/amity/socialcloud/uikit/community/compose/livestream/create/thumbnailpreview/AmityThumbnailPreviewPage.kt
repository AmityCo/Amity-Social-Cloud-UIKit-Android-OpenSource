package com.amity.socialcloud.uikit.community.compose.livestream.create.thumbnailpreview

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getBackgroundColor
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityThumbnailPreviewPage(
    mediaUri: String,
) {
    val context = LocalContext.current
    AmityBasePage("thumbnail_preview_page") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(getConfig().getBackgroundColor()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(58.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.clickableWithoutRipple {
                        context.closePageWithResult(Activity.RESULT_CANCELED)
                    },
                    painter = painterResource(R.drawable.amity_ic_back),
                    tint = Color.White,
                    contentDescription = "back"
                )
            }

        }

    }
}