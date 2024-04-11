package com.amity.socialcloud.uikit.chat.compose.live

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.amity.socialcloud.uikit.chat.compose.live.component.AmityLiveChatHeader
import com.amity.socialcloud.uikit.chat.compose.live.component.AmityLiveChatMessageList
import com.amity.socialcloud.uikit.chat.compose.live.composer.AmityLiveChatMessageComposeBar
import com.amity.socialcloud.uikit.chat.compose.live.composer.AmityLiveChatPageViewModel
import com.amity.socialcloud.uikit.common.eventbus.NetworkConnectionEventPublisher
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityLiveChatPage(
    modifier: Modifier = Modifier,
    channelId: String,
) {
    val subChannelId = channelId
    val viewModel = AmityLiveChatPageViewModel(subChannelId)
    val context = androidx.compose.ui.platform.LocalContext.current
    NetworkConnectionEventPublisher.initPublisher(context = context)
    
    DisposableEffect(Unit) {
        onDispose {
            viewModel.onStop()
        }
    }

    AmityBasePage(pageId = "live_chat") {
        Column(
            modifier = modifier
                .background(Color(0xFF191919))
// TODO uncomment this when implementing dark mode
//                .background(
//                    getPageScope()
//                        .getConfig()
//                        .getBackgroundColor()
//                )
                .fillMaxSize()
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
            ){
                AmityLiveChatHeader(
                    pageScope = getPageScope(),
                    viewModel = viewModel,
                )
            }
            HorizontalDivider(
                color = AmityTheme.colors.baseShade5
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
                color = AmityTheme.colors.primary
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
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 24.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .shadow(4.dp)
                            .background(AmityTheme.colors.baseShade5)
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