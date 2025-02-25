package com.amity.socialcloud.uikit.community.compose.post.detail.elements

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.stream.AmityStream
import com.amity.socialcloud.uikit.common.ui.elements.AmityExpandableText
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.AmityLivestreamPlayerPageActivity
import kotlinx.coroutines.flow.Flow

@Composable
fun AmityPostLivestreamElement(
    modifier: Modifier = Modifier,
    post: AmityPost
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
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column {
                    stream.getTitle()?.let { title ->
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = title,
                            textAlign = TextAlign.Left,
                            style = TextStyle(
                                fontSize = 15.sp,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Left,
                                fontWeight = FontWeight(800),
                                color = AmityTheme.colors.base,
                            )
                        )
                    }
                    stream.getDescription()?.let { description ->
                        if (description.isNotBlank()) {
                            AmityExpandableText(
                                modifier = modifier,
                                text = "\n$description",
                                style = AmityTheme.typography.body,
                                onClick = {},
                            )
                        }
                    }
                }
            }

        }

        if (streamState?.value?.getStatus() == AmityStream.Status.ENDED) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(203.dp)
            ) {
                AmityLivestreamEndedView(modifier = modifier)
            }
        } else {
            Box(
                modifier = modifier
                    .clickable {
                        AmityLivestreamPlayerPageActivity
                            .newIntent(
                                context = context,
                                post = post
                            )
                            .let {
                                context.startActivity(it)
                            }
                    }
            ) {
                streamState?.value?.let { stream ->
                    if (image != null) {
                        AmityPostMediaImageChildrenOne(
                            modifier = modifier
                                .height(219.dp)
                                .fillMaxWidth(),
                            isVideoPost = false,
                            image = image!!
                        ) {
                            AmityLivestreamPlayerPageActivity
                                .newIntent(
                                    context = context,
                                    post = post
                                )
                                .let {
                                    context.startActivity(it)
                                }
                        }
                    } else {
                        Image(
                            modifier = modifier
                                .height(219.dp)
                                .fillMaxWidth(),
                            painter = painterResource(id = R.drawable.amity_ic_default_stream_thumbnail),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                        )
                    }
                    AmityPostMediaPlayButton(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                    val status = stream.getStatus()
                    if (status == AmityStream.Status.RECORDED) {
                        AmityLivestreamPostRecordedLabel(
                            modifier = Modifier
                                .padding(start = 12.dp, top = 12.dp)
                        )
                    } else if (status == AmityStream.Status.LIVE) {
                        AmityLivestreamPostLiveLabel(
                            modifier = Modifier
                                .padding(start = 12.dp, top = 12.dp)
                        )
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
fun AmityLivestreamPostRecordedLabel(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(color = Color.Black.copy(0.5f), shape = RoundedCornerShape(size = 4.dp))
            .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
    ) {
        Text(
            text = "RECORDED",
            style = TextStyle(
                fontSize = 13.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight(600),
                color = AmityTheme.colors.background,
            )
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
            style = TextStyle(
                fontSize = 13.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight(600),
                color = AmityTheme.colors.background,
            )
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
        description = "Please try again later.",
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
    description: String,
    shouldShowLoading: Boolean = false,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
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
                ) {  LivestreamLoadingIndicator() }
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
                    style = TextStyle(
                        fontSize = 17.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .wrapContentSize()
            ) {
                Text(
                    text = description,
                    style = TextStyle(
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight(400),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                )
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