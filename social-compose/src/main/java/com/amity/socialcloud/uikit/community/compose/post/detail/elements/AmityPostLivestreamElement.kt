package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.stream.AmityStream
import com.amity.socialcloud.uikit.common.ui.elements.AmityExpandableText
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.view.AmityLivestreamPlayerPageActivity
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity.Companion.REQUEST_CODE_VIEW_LIVESTREAM
import kotlinx.coroutines.flow.Flow

@Composable
fun AmityPostLivestreamElement(
    modifier: Modifier = Modifier,
    post: AmityPost,
) {
    val postChildren = remember(post.getPostId(), post.getUpdatedAt()) {
        post.getChildren()
    }
    if (postChildren.isEmpty()) return

    when (postChildren.first().getData()) {
        is AmityPost.Data.LIVE_STREAM -> {
        }

        else -> return
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        when (postChildren.first().getData()) {
            is AmityPost.Data.LIVE_STREAM -> AmityChildLivestreamPostElement(
                modifier = modifier
                    .fillMaxWidth(),
                post = post,
            )

            else -> {}
        }
    }
}

@Composable
fun AmityChildLivestreamPostElement(
    modifier: Modifier = Modifier,
    post: AmityPost,
) {
    val context = LocalContext.current
    val activity = context as Activity
    val streamState = remember(post.getPostId(), post.getUpdatedAt()) {
        getLivestreamPostData(post)
    }?.collectAsState(null)

    val image by remember {
        derivedStateOf {
            streamState?.value?.getThumbnailImage()
        }
    }
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        streamState?.value?.let { stream ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(16.dp)
            ) {
                Column {
                    stream.getTitle()?.let { title ->
                        AmityExpandableText(
                            modifier = modifier.fillMaxWidth(),
                            text = title,
                            style = AmityTheme.typography.bodyBold,
                            onClick = {},
                        )
                    }
                    stream.getDescription()?.let { description ->
                        if (description.isNotBlank()) {
                            AmityExpandableText(
                                modifier = modifier,
                                text = "\n$description",
                                style = AmityTheme.typography.bodyLegacy,
                                onClick = {},
                            )
                        }
                    }
                }
            }

            if (stream.getStatus() == AmityStream.Status.ENDED) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(203.dp)
                ) {
                    AmityLivestreamEndedView(modifier = Modifier)
                }
            } else if (stream.isDeleted()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(203.dp)
                ) {
                    AmityLivestreamUnavailableView(modifier = Modifier)
                }
            } else {
                Box(
                    modifier = Modifier
                        .clickable {
                            if (stream.getStatus() != AmityStream.Status.IDLE) {
                                AmityLivestreamPlayerPageActivity
                                    .newIntent(context = context, post = post)
                                    .let {
                                        activity.startActivityForResult(
                                            it,
                                            REQUEST_CODE_VIEW_LIVESTREAM
                                        )
                                    }
                            }
                        }
                ) {
                    if (image != null) {
                        Box(
                            modifier = modifier
                                .height(219.dp)
                                .fillMaxWidth(),
                        ) {
                            AmityPostImageView(
                                post = post.getChildren().first(),
                                onClick = {
                                    if (stream.getStatus() != AmityStream.Status.IDLE) {
                                        AmityLivestreamPlayerPageActivity
                                            .newIntent(context = context, post = post)
                                            .let {
                                                activity.startActivityForResult(
                                                    it,
                                                    REQUEST_CODE_VIEW_LIVESTREAM
                                                )
                                            }
                                    }
                                }
                            )
                        }
                    } else {
                        Image(
                            modifier = modifier
                                .height(219.dp)
                                .fillMaxWidth(),
                            painter = painterResource(id = R.drawable.amity_v4_ic_default_stream_thumbnail),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                        )
                    }
                    if (stream.getStatus() != AmityStream.Status.IDLE) {
                        Image(
                            painter = painterResource(id = R.drawable.amity_ic_play_v4),
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    when (stream.getStatus()) {
                        AmityStream.Status.RECORDED -> {
                            AmityLivestreamPostIdleOrRecordedLabel(
                                modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                                text = "RECORDED"
                            )
                        }

                        AmityStream.Status.LIVE -> {
                            AmityLivestreamPostLiveLabel(
                                modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                            )
                        }

                        AmityStream.Status.IDLE -> {
                            AmityLivestreamPostIdleOrRecordedLabel(
                                modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                                text = "UPCOMING LIVE"
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}

fun getLivestreamPostData(post: AmityPost): Flow<AmityStream>? {
    return post.getChildren().firstOrNull()?.let {
        when (val data = it.getData()) {
            is AmityPost.Data.LIVE_STREAM -> data.getStream().asFlow()
            else -> null
        }
    }
}

@Composable
fun AmityLivestreamPostIdleOrRecordedLabel(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(color = Color.Black.copy(0.5f), shape = RoundedCornerShape(size = 4.dp))
            .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
    ) {
        Text(
            text = text,
            style = AmityTheme.typography.captionBold.copy(
                color = Color.White,
            ),
        )
    }
}

@Composable
fun AmityLivestreamPostLiveLabel(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(color = Color(0xFFFF305A), shape = RoundedCornerShape(size = 4.dp))
            .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
    ) {
        Text(
            text = "LIVE",
            style = AmityTheme.typography.captionLegacy.copy(
                color = AmityTheme.colors.background,
            ),
        )
    }
}

@Composable
fun AmityLivestreamEndedView(modifier: Modifier = Modifier) {
    AmityLivestreamNoticeView(
        modifier = modifier,
        title = "This livestream has ended.",
        description = "Playback will be available for you\nto watch shortly.",
    )
}

@Composable
fun AmityLivestreamUnavailableView(modifier: Modifier = Modifier) {
    AmityLivestreamNoticeView(
        modifier = modifier,
        icon = R.drawable.amity_ic_warning,
        title = "This stream is currently unavailable.",
    )
}

@Composable
fun AmityLivestreamTerminatedView(modifier: Modifier = Modifier) {
    AmityLivestreamNoticeView(
        modifier = modifier,
        icon = R.drawable.amity_ic_warning,
        title = "This stream has been terminated.",
        description = "It looks like this livestream goes against our content moderation guidelines.",
    )
}

@Composable
fun AmityLivestreamDisconnectedView(modifier: Modifier = Modifier) {
    AmityLivestreamNoticeView(
        modifier = modifier,
        title = "Reconnecting",
        description = "Due to poor connection, this livestream has been\npaused. It will resume automatically\nonce the connection is stable.",
        shouldShowLoading = true,
        background = Color.Black.copy(alpha = 0.5f)
    )
}

@Composable
fun AmityLivestreamLoadingView(modifier: Modifier = Modifier) {
    AmityLivestreamNoticeView(
        modifier = modifier,
        title = "",
        description = "",
        shouldShowLoading = true,
    )
}

@Composable
fun AmityLivestreamNoticeView(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    title: String,
    description: String? = null,
    shouldShowLoading: Boolean = false,
    background: Color = Color.Black,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(color = background)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!shouldShowLoading) {
                icon?.let {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                ) { LivestreamLoadingIndicator() }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .wrapContentSize()
            ) {
                Text(
                    text = title,
                    style = AmityTheme.typography.titleLegacy.copy(
                        color = Color.White,
                    ),
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            description?.let {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .wrapContentSize()
                ) {
                    Text(
                        text = description,
                        style = AmityTheme.typography.captionLegacy.copy(
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
fun LivestreamLoadingIndicator() {
    val transition = rememberInfiniteTransition()

    val currentAngle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(
        modifier = Modifier.size(40.dp)
    ) {
        drawCircle(
            color = Color.White.copy(alpha = 0.5f),
            radius = size.width / 2,
            style = Stroke(width = 8.dp.value)
        )

        drawArc(
            color = Color.White,
            startAngle = currentAngle.toFloat(),
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = 8.dp.value)
        )
    }
}

@Preview
@Composable
fun AmityLivestreamPostPreview() {
    AmityLivestreamUnavailableView()
}