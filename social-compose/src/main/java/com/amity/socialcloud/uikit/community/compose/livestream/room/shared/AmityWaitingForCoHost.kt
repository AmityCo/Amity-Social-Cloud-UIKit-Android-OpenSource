package com.amity.socialcloud.uikit.community.compose.livestream.room.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import io.livekit.android.compose.types.TrackReference
import io.livekit.android.compose.ui.ScaleType
import io.livekit.android.compose.ui.VideoTrackView
import io.livekit.android.room.Room


@Composable
fun AmityCoHostVideo(
    trackReference: TrackReference?,
    modifier: Modifier = Modifier,
    user: AmityUser?,
    room: Room? = null,
    isHost: Boolean,
    isMute: Boolean,
    mirror: Boolean = false,
    scaleType: ScaleType = ScaleType.Fill,
    onMenuClick: (() -> Unit)?,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        VideoTrackView(
            trackReference = trackReference,
            modifier = modifier,
            room = room,
            mirror = mirror,
            scaleType = scaleType,
        )
        AmityCoHostOverlay(
            modifier = Modifier.align(Alignment.TopStart),
            user = user,
            isHost = isHost,
            isMute = isMute,
            onMenuClick = onMenuClick,
        )
    }
}

@Composable
fun AmityWaitingForCoHost(
    modifier: Modifier = Modifier,
    isHost: Boolean,
    user: AmityUser?,
    onMenuClick: (() -> Unit)?,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF4A5568))
    ) {
        if (onMenuClick != null) {
            AmityCoHostOverlay(
                modifier = Modifier.align(Alignment.TopStart),
                user = user,
                isHost = isHost,
                isMute = false,
                onMenuClick = onMenuClick,
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AmityUserAvatarView(
                user = user,
                size = 64.dp,
                modifier = Modifier
                .clip(CircleShape)
            )


            // Waiting message
            Text(
                text = if (isHost) {
                    "Co-host is getting ready\nin the backstage."
                } else {
                    "Waiting for host\nto get resume..."
                },
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun AmityCoHostOverlay(
    modifier: Modifier = Modifier,
    user: AmityUser?,
    isHost: Boolean,
    isMute: Boolean,
    onMenuClick: (() -> Unit)? = null,
) {
    Box {
        if (onMenuClick != null) {
            // Top bar with user display name and controls
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User display name with icons
                Row(
                    modifier = Modifier
                        .background(
                            color = Color(0x33000000),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = user?.getDisplayName() ?: "Unknown User",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Light
                    )

                    // Show brand badge if user is a brand
                    val isBrandUser = user?.isBrand() == true
                    if (isBrandUser) {
                        Image(
                            painter = painterResource(id = com.amity.socialcloud.uikit.common.compose.R.drawable.amity_ic_brand_badge),
                            contentDescription = "Brand badge",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    if (isMute && !isHost) {
                        Image(
                            painter = painterResource(id = R.drawable.amity_ic_room_mute_state),
                            contentDescription = "Mute state",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = Color(0x80000000),
                            shape = CircleShape
                        )
                        .clickableWithoutRipple {
                            onMenuClick()
                        }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_more_vertical),
                        contentDescription = "Menu",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
        if (isMute && isHost) {
            Box(
                modifier = Modifier
                    .padding(top = 48.dp, start = 16.dp)
                    .size(32.dp)
                    .background(
                        color = Color(0x33000000),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.amity_ic_room_mute_state),
                    contentDescription = "host mute state icon",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}