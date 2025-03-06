package com.amity.socialcloud.uikit.community.compose.story.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.allowHardware
import com.amity.socialcloud.sdk.model.core.ad.AmityAd
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.common.ad.AmityAdEngine
import com.amity.socialcloud.uikit.common.ad.AmityAdInfoSheet
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.isVisible
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.story.hyperlink.elements.AmityStoryAdHyperlinkView
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryBodyGestureBox
import kotlinx.coroutines.Dispatchers

@Composable
fun AmityStoryAdView(
    modifier: Modifier = Modifier,
    ad: AmityAd,
    onTap: (Boolean) -> Unit = {},
    onHold: (Boolean) -> Unit = {},
    onSwipeDown: () -> Unit = {},
) {
    val uriHandler = LocalUriHandler.current

    var showAdInfoSheet by remember { mutableStateOf(false) }

    var isVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            AmityAdEngine.markSeen(ad, AmityAdPlacement.STORY)
        }
    }

    LaunchedEffect(showAdInfoSheet) {
        onHold(showAdInfoSheet)
    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(ad.getImage9_16()?.getUrl(AmityImage.Size.LARGE))
            .allowHardware(false)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .isVisible { isVisible = it }
    ) {

        AmityStoryBodyGestureBox(
            modifier = modifier,
            onTap = onTap,
            onHold = onHold,
            onSwipeUp = {},
            onSwipeDown = onSwipeDown,
        )

        Image(
            painter = painter,
            contentScale = ContentScale.Fit,
            contentDescription = "Story Image",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
        )

        if (ad.getCallToAction().isNotEmptyOrBlank()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.amity_ic_story_ad_chevron_top),
                    contentDescription = null,
                    modifier = Modifier.width(30.dp),
                )

                AmityStoryAdHyperlinkView(
                    text = ad.getCallToAction(),
                    onClick = {
                        AmityAdEngine.markClicked(ad, AmityAdPlacement.STORY)
                        uriHandler.openUri(ad.getCallToActionUrl())
                    }
                )
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.amity_ic_more_info),
            contentDescription = null,
            tint = AmityTheme.colors.baseShade3,
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (-4).dp, y = (-4).dp)
                .clickableWithoutRipple {
                    showAdInfoSheet = true
                }
        )

        AmityAdInfoSheet(
            name = ad.getAdvertiser()?.getCompanyName() ?: "",
            showSheet = showAdInfoSheet,
        ) {
            showAdInfoSheet = false
        }
    }
}