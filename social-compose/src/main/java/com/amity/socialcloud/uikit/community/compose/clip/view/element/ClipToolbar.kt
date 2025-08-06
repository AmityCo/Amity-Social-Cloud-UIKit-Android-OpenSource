package com.amity.socialcloud.uikit.community.compose.clip.view.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.R.drawable
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun ClipToolbar(
    modifier: Modifier = Modifier,
    title: String,
    community: AmityCommunity? = null,
    isLoading: Boolean = false,
    isError: Boolean = false,
    onTitleClick: () -> Unit = {},
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back button
            Icon(
                painter = painterResource(R.drawable.amity_ic_back),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickableWithoutRipple {
                        onBackClick()
                    }
            )

            Spacer(Modifier.width(8.dp))
            // Title
            if (!isLoading) {
                Row(
                    modifier = Modifier.weight(1f)
                        .clickableWithoutRipple {
                            onTitleClick()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Private community icon
                    if (community != null && !isError && !community.isPublic()) {
                        Icon(
                            painter = painterResource(id = drawable.amity_ic_lock1),
                            contentDescription = "Private community icon",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                    }

                    Text(
                        text = if (!isError) title else "",
                        style = AmityTheme.typography.titleBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    // Official community badge
                    if (community != null && !isError && community.isOfficial()) {
                        Spacer(Modifier.width(4.dp))
                        Image(
                            painter = painterResource(id = drawable.amity_v4_verified_badge),
                            contentDescription = "Verified community icon",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            } else if (!isError) {
                Box(
                    Modifier
                        .width(120.dp)
                        .height(8.dp)
                        .shimmerBackground(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(12.dp)
                        )
                )
            }

            Spacer(Modifier.width(8.dp))
            // Create button
            Icon(
                painter = painterResource(R.drawable.amity_v4_ic_camera),
                contentDescription = "Create Clip",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickableWithoutRipple {
                        onCreateClick()
                    }
            )
        }
    }
}


//Write clipToolbar Preview
@Preview
@Composable
fun ClipToolbarPreview() {
    ClipToolbar(
        title = "Clip toolbar",
        onBackClick = {},
        onCreateClick = {},
        modifier = Modifier.fillMaxWidth()
    )
}