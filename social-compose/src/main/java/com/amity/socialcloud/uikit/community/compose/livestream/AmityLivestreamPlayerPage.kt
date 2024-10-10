package com.amity.socialcloud.uikit.community.compose.livestream

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.stream.AmityStream
import com.amity.socialcloud.sdk.video.presentation.AmityVideoPlayer
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamDisconnectedView
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamEndedView
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamLoadingView
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamPostLiveLabel
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamTerminatedView
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamUnavailableView

@Composable
fun AmityLivestreamPlayerPage(
    modifier: Modifier = Modifier,
    post: AmityPost,
    onClose: () -> Unit = {}
) {
    val viewModel = remember { AmityLivestreamPlayerViewModel(post) }
    val state by remember { viewModel.liveStreamFullScreenState }.collectAsState()
    val connection by viewModel
        .getNetworkConnectionStateFlow()
        .collectAsState(initial = NetworkConnectionEvent.Connected)
    AmityBasePage(pageId = "live_stream_page") {
        AmityBaseComponent(
            pageScope = getPageScope(),
            componentId = "stream_player",
            needScaffold = true,
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxSize()
            ) {
                val streamStatus = state.stream?.getStatus()
                val terminateLabels =
                    state.stream?.getStreamModeration()?.getTerminateLabels() ?: emptyList()
                val isUnavailable = state.error != null || state.stream?.isDeleted() == true
                val streamId = state.stream?.getStreamId()
                val isDisconnected = connection == NetworkConnectionEvent.Disconnected && streamStatus == AmityStream.Status.LIVE
                if (terminateLabels.isNotEmpty() || streamStatus == AmityStream.Status.ENDED || isUnavailable || isDisconnected || streamId == null) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = if (streamId == null) {
                            "stream_loading"
                        } else if (terminateLabels.isNotEmpty()) {
                            "stream_terminated"
                        } else if (streamStatus == AmityStream.Status.ENDED) {
                            "stream_ended"
                        } else if (isDisconnected) {
                            "stream_disconnected"
                        } else {
                            "stream_unavailable"
                        }
                    ) {
                        Box(
                            contentAlignment = Alignment.TopStart,
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Green)
                        ) {
                            if (streamId == null) {
                                AmityLivestreamLoadingView()
                            } else if (terminateLabels.isNotEmpty()) {
                                AmityLivestreamTerminatedView()
                            } else if (streamStatus == AmityStream.Status.ENDED) {
                                AmityLivestreamEndedView()
                            } else if (isDisconnected) {
                                AmityLivestreamDisconnectedView()
                            } else {
                                AmityLivestreamUnavailableView()
                            }
                            Box(
                                modifier = Modifier
                                    .padding(top = 52.dp, start = 36.dp)
                                    .wrapContentSize()
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = R.drawable.amity_ic_close
                                    ),
                                    contentDescription = "Close",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(4.dp)
                                        .clickableWithoutRipple {
                                            onClose()
                                        }
                                        .testTag(getAccessibilityId()),
                                )
                            }
                        }
                    }
                } else {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = ""
                    ) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                        ) {
                            AndroidView(
                                modifier = modifier.fillMaxSize(),
                                factory = { context: Context ->
                                    AmityVideoPlayer(context = context).apply {
                                        setOnClose(onClose)
                                    }
                                },
                                update = { amityVideoPlayer: AmityVideoPlayer ->
                                    amityVideoPlayer.enableStopWhenPause()
                                    amityVideoPlayer.play(streamId)
                                },
                            )
                            if (streamStatus == AmityStream.Status.LIVE) {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 16.dp, top = 60.dp)
                                ) {
                                    AmityLivestreamPostLiveLabel()
                                }
                            }
                        }

                    }
                }
            }


        }
    }
}