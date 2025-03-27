package com.amity.socialcloud.uikit.community.compose.livestream.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.stream.AmityStream
import com.amity.socialcloud.sdk.video.presentation.AmityVideoPlayer
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.DisposableEffectWithLifeCycle
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamErrorScreenType
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity.Companion.EXTRA_PARAM_LIVESTREAM_ERROR_TYPE
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamDisconnectedView
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamEndedView
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamLoadingView
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityLivestreamPostLiveLabel

@Composable
fun AmityLivestreamPlayerPage(
    modifier: Modifier = Modifier,
    post: AmityPost,
) {
    val context = LocalContext.current
    val activity = context as Activity
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel = viewModel<AmityLivestreamPlayerViewModel>(
        factory = AmityLivestreamPlayerViewModel.create(post),
        viewModelStoreOwner = viewModelStoreOwner
    )
    //val viewModel = remember { AmityLivestreamPlayerViewModel(post) }
    val state by remember { viewModel.liveStreamFullScreenState }.collectAsState()

    DisposableEffectWithLifeCycle(
        onStart = {
            viewModel.subscribePostRT(state.post)
        },
        onDestroy = {
            viewModel.unSubscribePostRT(state.post)
        }
    )

    LaunchedEffect(state.post.isDeleted()) {
        if (state.post.isDeleted()) {
            closePageWithLivestreamError(
                context,
                LivestreamErrorScreenType.DELETED
            )
        }
    }

    val connection by viewModel
        .getNetworkConnectionStateFlow()
        .collectAsState(initial = NetworkConnectionEvent.Connected)
    AmityBasePage(pageId = "live_stream_page") {
        AmityBaseComponent(
            pageScope = getPageScope(),
            componentId = "stream_player",
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {
                val streamStatus = state.stream?.getStatus()

                val terminateLabels =
                    state.stream?.getStreamModeration()?.getTerminateLabels() ?: emptyList()
                val isUnavailable = state.error != null || state.stream?.isDeleted() == true
                val streamId = state.stream?.getStreamId()
                val isDisconnected =
                    connection == NetworkConnectionEvent.Disconnected && streamStatus == AmityStream.Status.LIVE
                if (terminateLabels.isNotEmpty() || streamStatus == AmityStream.Status.ENDED || isUnavailable || streamId == null) {
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
                                .fillMaxSize()
                                .background(Color.Black)
                        ) {
                            if (streamId == null) {
                                AmityLivestreamLoadingView()
                            } else if (terminateLabels.isNotEmpty()) {
                                closePageWithLivestreamError(
                                    context,
                                    LivestreamErrorScreenType.TERMINATED
                                )
                            } else if (streamStatus == AmityStream.Status.ENDED) {
                                AmityLivestreamEndedView()
                            } else if (isUnavailable) {
                                closePageWithLivestreamError(
                                    context,
                                    LivestreamErrorScreenType.DELETED
                                )
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
                                            context.closePageWithResult(Activity.RESULT_OK)
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
                                        setOnClose {
                                            context.closePageWithResult(Activity.RESULT_OK)
                                        }
                                    }
                                },
                                update = { amityVideoPlayer: AmityVideoPlayer ->
                                    amityVideoPlayer.enableStopWhenPause()
                                    amityVideoPlayer.play(streamId)
                                },
                                onRelease = {
                                    it.stop()
                                }
                            )
                            if (streamStatus == AmityStream.Status.LIVE) {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 16.dp, top = 60.dp)
                                ) {
                                    AmityLivestreamPostLiveLabel()
                                }
                            }
                            if (isDisconnected) {
                                AmityLivestreamDisconnectedView()
                            }
                        }

                    }
                }
            }
        }
    }
}

private fun closePageWithLivestreamError(context: Context, errorType: LivestreamErrorScreenType) {
    val resultIntent = Intent().apply {
        putExtra(EXTRA_PARAM_LIVESTREAM_ERROR_TYPE, errorType.name)
    }
    context.closePageWithResult(Activity.RESULT_OK, resultIntent)
}