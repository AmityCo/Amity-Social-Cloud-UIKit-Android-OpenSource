package com.amity.socialcloud.uikit.chat.compose.live

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.live.component.AmityLiveChatHeader
import com.amity.socialcloud.uikit.chat.compose.live.component.AmityLiveChatMessageList
import com.amity.socialcloud.uikit.chat.compose.live.composer.AmityLiveChatMessageComposeBar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.asColor
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun AmityLiveChatPage(
    modifier: Modifier = Modifier,
    channelId: String,
) {
    val subChannelId = channelId
    val viewModel = AmityLiveChatPageViewModel(subChannelId)
    val membership by remember {
        viewModel.observeMembership()
            .distinctUntilChanged { old, new ->
                old.isBanned() == new.isBanned()
                        && old.isMuted() == new.isMuted()
            }
    }.collectAsState(initial = null)

    val isChannelMuted by remember {
        viewModel.getChannelFlow().map {
            it.isMuted()
        }.distinctUntilChanged()
    }.collectAsState(initial = false)

    val isChannelModerator by remember {
        viewModel.isChannelModerator().distinctUntilChanged()
    }.collectAsState(initial = false)

    val isGlobalBanned by remember {
        viewModel.observeGlobalBanEvent().distinctUntilChanged()
    }.collectAsState(initial = false)

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onStop()
        }
    }
    AmityBasePage(pageId = "live_chat_page") {
        Column(
            modifier = modifier
                .background(
                    getPageScope()
                        .getPageTheme()
                        ?.backgroundColor
                        ?.asColor() ?: AmityTheme.colors.background
                )
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AmityLiveChatHeader(
                    pageScope = getPageScope(),
                    viewModel = viewModel,
                )
            }
            HorizontalDivider(
                color = getPageScope()
                    .getPageTheme()
                    ?.baseShade4Color
                    ?.asColor() ?: AmityTheme.colors.baseShade4,
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AmityLiveChatMessageList(
                    pageScope = getPageScope(),
                    viewModel = viewModel,
                )
            }

            val showMutedLabel =
                !isChannelModerator
                        && membership?.isBanned() == false
                        && (membership?.isMuted() == true || isChannelMuted)
            if (showMutedLabel) {
                HorizontalDivider(
                    color = getPageScope()
                        .getPageTheme()
                        ?.baseShade4Color
                        ?.asColor() ?: AmityTheme.colors.baseShade4,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                ) {

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_muted),
                        contentDescription = "channel muted",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(20.dp),
                        tint = AmityTheme.colors.baseShade1,
                    )
                    val message = if (isChannelMuted) {
                        "This channel has been set to read-only by the channel moderator"
                    } else {
                        "You’ve been muted by the channel moderator"
                    }
                    Text(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        text = message,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = AmityTheme.colors.baseShade1,
                        ),
                    )
                }
            }
            if (membership?.isBanned() == false && !isGlobalBanned)
                AmityLiveChatMessageComposeBar(
                    pageScope = getPageScope(),
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                )
        }

    }
}

@Composable
fun LoadingIndicator(itemCount: Int = 0) {
    if (itemCount == 0) {
        LoadingToast()
    } else {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(20.dp),
                color = AmityTheme.colors.primary,
            )
        }
    }
}

@Composable
fun LoadingToast() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = {}
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .shadow(4.dp)
                            .background(AmityTheme.colors.baseShade4)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Row(Modifier.padding(0.dp, 18.dp)) {
                                Spacer(modifier = Modifier.width(12.dp))
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(20.dp),
                                    color = AmityTheme.colors.primary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Loading chat...",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        fontWeight = FontWeight(400),
                                        color = AmityTheme.colors.baseInverse
                                    )
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}