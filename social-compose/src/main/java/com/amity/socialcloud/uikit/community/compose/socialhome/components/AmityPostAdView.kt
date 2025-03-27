package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.core.ad.AmityAd
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.common.ad.AmityAdBadge
import com.amity.socialcloud.uikit.common.ad.AmityAdEngine
import com.amity.socialcloud.uikit.common.ad.AmityAdInfoSheet
import com.amity.socialcloud.uikit.common.asset.ImageFromAsset
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.ui.elements.AmityAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.isVisible
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityPostAdView(
    modifier: Modifier = Modifier,
    ad: AmityAd
) {
    val uriHandler = LocalUriHandler.current

    var showAdInfoSheet by remember { mutableStateOf(false) }

    var isVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            AmityAdEngine.markSeen(ad, AmityAdPlacement.FEED)
        }
    }

    Column(
        modifier = modifier
            .background(AmityTheme.colors.background)
            .padding(top = 4.dp)
            .isVisible { isVisible = it }
    ) {
        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)
            ) {
                AmityAvatarView(
                    image = ad.getAdvertiser()?.getAvatar(),
                    placeholder = R.drawable.amity_ic_default_advertiser,
                    iconPadding = 8.dp,
                    modifier = modifier.padding(vertical = 8.dp)
                )

                Column {
                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = ad.getAdvertiser()?.getName() ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    AmityAdBadge()
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.amity_ic_more_info),
                contentDescription = null,
                tint = AmityTheme.colors.baseShade3,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-3).dp, y = (-1).dp)
                    .clickableWithoutRipple {
                        showAdInfoSheet = true
                    }
            )
        }
        if (ad.getBody().isNotEmpty()) {
            Text(
                text = ad.getBody(),
                style = AmityTheme.typography.bodyLegacy,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        ImageFromAsset(
            url = ad.getImage1_1()?.getUrl(AmityImage.Size.LARGE) ?: "",
            scale = ContentScale.Crop,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
        )

        Row(
            modifier = Modifier
                .background(AmityTheme.colors.backgroundShade1)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clickableWithoutRipple(
                    enabled = ad
                        .getCallToAction()
                        .isNotEmptyOrBlank()
                ) {
                    AmityAdEngine.markClicked(ad, AmityAdPlacement.FEED)
                    uriHandler.openUri(ad.getCallToActionUrl())
                }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = modifier.weight(1f)
            ) {
                Text(
                    text = ad.getDescription(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.baseShade1,
                    )
                )
                Text(
                    text = ad.getHeadline(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = AmityTheme.typography.captionLegacy.copy()
                )
            }

            if (ad.getCallToAction().isNotEmptyOrBlank()) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmityTheme.colors.highlight,
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = modifier
                        .height(30.dp)
                        .align(Alignment.CenterVertically),
                    onClick = {
                        AmityAdEngine.markClicked(ad, AmityAdPlacement.FEED)
                        uriHandler.openUri(ad.getCallToActionUrl())
                    }
                ) {
                    Text(
                        text = ad.getCallToAction(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = AmityTheme.typography.captionLegacy.copy(
                            color = Color.White,
                        ),
                        modifier = modifier
                    )
                }
            }
        }

        AmityAdInfoSheet(
            name = ad.getAdvertiser()?.getCompanyName() ?: "",
            showSheet = showAdInfoSheet,
        ) {
            showAdInfoSheet = false
        }
    }
}