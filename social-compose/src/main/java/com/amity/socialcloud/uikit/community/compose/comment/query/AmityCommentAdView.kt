package com.amity.socialcloud.uikit.community.compose.comment.query

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.core.ad.AmityAd
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.common.ad.AmityAdBadge
import com.amity.socialcloud.uikit.common.ad.AmityAdEngine
import com.amity.socialcloud.uikit.common.ad.AmityAdInfoSheet
import com.amity.socialcloud.uikit.common.asset.ImageFromAsset
import com.amity.socialcloud.uikit.common.ui.elements.AmityAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityExpandableText
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.isVisible
import com.amity.socialcloud.uikit.community.compose.R
import com.google.gson.JsonObject


@Composable
fun AmityCommentAdView(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    ad: AmityAd
) {
    var showAdInfoSheet by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    var isVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            AmityAdEngine.markSeen(ad, AmityAdPlacement.COMMENT)
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                top = 4.dp,
                bottom = 8.dp,
                start = 12.dp,
                end = 12.dp
            )
            .isVisible { isVisible = it }
            .testTag("comment_list/*")
    ) {
        AmityAvatarView(
            image = ad.getAdvertiser()?.getAvatar(),
            placeholder = R.drawable.amity_ic_default_advertiser,
            iconPadding = 8.dp,
            modifier = modifier.testTag("comment_list/comment_bubble_avatar")
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .background(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(
                                topEnd = 12.dp,
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp,
                            )
                        )
                        .padding(
                            top = 0.dp,
                            end = 0.dp,
                            bottom = 12.dp,
                            start = 12.dp
                        )

                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = ad.getAdvertiser()?.getName() ?: "",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = AmityTheme.typography.captionLegacy,
                            modifier = Modifier
                                .testTag("comment_list/comment_bubble_creator_display_name")
                                .padding(top = 12.dp, end = 24.dp)
                                .weight(1f)
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.amity_ic_more_info),
                            contentDescription = null,
                            tint = AmityTheme.colors.baseShade3,
                            modifier = Modifier
                                .size(16.dp)
                                .offset(x = (-4).dp, y = 4.dp)
                                .clickableWithoutRipple {
                                    showAdInfoSheet = true
                                }
                        )
                    }

                    AmityAdBadge()

                    if (ad.getBody().isNotEmpty()) {
                        AmityExpandableText(
                            text = ad.getBody(),
                            mentionGetter = AmityMentionMetadataGetter(JsonObject()),
                            mentionees = listOf(),
                            style = AmityTheme.typography.bodyLegacy,
                            modifier = Modifier
                                .testTag("comment_list/comment_bubble_comment_text_view")
                                .padding(end = 12.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .height(118.dp)
                            .padding(end = 12.dp)
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(
                                    topStart = 8.dp,
                                    topEnd = 8.dp,
                                    bottomStart = 8.dp,
                                    bottomEnd = 8.dp,
                                )
                            )
                            .background(AmityTheme.colors.background)
                            .clickableWithoutRipple {
                                AmityAdEngine.markClicked(ad, AmityAdPlacement.COMMENT)
                                uriHandler.openUri(ad.getCallToActionUrl())
                            }
                    ) {
                        ImageFromAsset(
                            url = ad.getImage1_1()?.getUrl(AmityImage.Size.LARGE) ?: "",
                            scale = ContentScale.Crop,
                            modifier = Modifier
                                .width(100.dp)
                                .fillMaxHeight()
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .background(AmityTheme.colors.baseShade4)
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(12.dp)
                        ) {
                            Text(
                                text = ad.getDescription(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = AmityTheme.typography.captionLegacy.copy(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = AmityTheme.colors.baseShade1
                                ),
                                modifier = Modifier.testTag("comment_list/comment_bubble_ad_description")
                            )
                            Text(
                                text = ad.getHeadline(),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = AmityTheme.typography.captionLegacy.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier
                                    .testTag("comment_list/comment_bubble_ad_headline")
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            if (
                                ad.getCallToAction().isNotEmpty()
                                && ad.getCallToActionUrl().isNotEmpty()
                            ) {
                                Button(
                                    onClick = {
                                        AmityAdEngine.markClicked(ad, AmityAdPlacement.COMMENT)
                                        uriHandler.openUri(ad.getCallToActionUrl())
                                    },
                                    shape = RoundedCornerShape(4.dp),
                                    colors = ButtonDefaults.textButtonColors(
                                        containerColor = AmityTheme.colors.highlight,
                                    ),
                                    modifier = Modifier.wrapContentWidth()
                                ) {
                                    Text(
                                        text = ad.getCallToAction(),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        ),
                                        modifier = Modifier
                                            .testTag("comment_list/comment_bubble_ad_headline")
                                    )
                                }
                            }
                        }
                    }
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