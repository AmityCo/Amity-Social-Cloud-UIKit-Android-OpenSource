package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityPostMediaElement(
    modifier: Modifier = Modifier,
    post: AmityPost
) {
    val postChildren = remember(post.getPostId(), post.getUpdatedAt()) {
        post.getChildren()
    }
    if (postChildren.isEmpty()) return

    when (postChildren.first().getData()) {
        is AmityPost.Data.IMAGE,
        is AmityPost.Data.VIDEO -> {
        }

        else -> return
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(328.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {

        when (postChildren.first().getData()) {
            is AmityPost.Data.IMAGE -> AmityChildPostMediaElement(
                modifier = modifier,
                post = post,
                isVideoPost = false
            )

            is AmityPost.Data.VIDEO -> AmityChildPostMediaElement(
                modifier = modifier,
                post = post,
                isVideoPost = true
            )

            else -> {}
        }
    }
}

@Composable
fun AmityChildPostMediaElement(
    modifier: Modifier = Modifier,
    post: AmityPost,
    isVideoPost: Boolean,
) {
    val childPosts = remember(post.getPostId(), post.getUpdatedAt(), isVideoPost) {
        getChildPostData(post)
    }

    val images by remember {
        derivedStateOf {
            childPosts.mapNotNull { data ->
                when (data) {
                    is AmityPost.Data.IMAGE -> data.getImage()
                    is AmityPost.Data.VIDEO -> data.getThumbnailImage()
                    else -> null
                }
            }
        }
    }
    val showMediaDialog = remember { mutableStateOf(false) }
    val selectedFileId = remember { mutableStateOf("") }

    if (showMediaDialog.value && selectedFileId.value.isNotEmptyOrBlank()) {
        AmityPostMediaPreviewDialog(
            childPosts = childPosts,
            isVideoPost = isVideoPost,
            selectedFileId = selectedFileId.value,
            onDismiss = { showMediaDialog.value = false }
        )
    }

    when (images.size) {
        0 -> {}
        1 -> AmityPostMediaImageChildrenOne(
            modifier = modifier,
            isVideoPost = isVideoPost,
            image = images[0]
        ) {
            selectedFileId.value = it.getFileId()
            showMediaDialog.value = true
        }

        2 -> AmityPostMediaImageChildrenTwo(
            modifier = modifier,
            images = images,
            isVideoPost = isVideoPost,
        ) {
            selectedFileId.value = it.getFileId()
            showMediaDialog.value = true
        }

        3 -> AmityPostMediaImageChildrenThree(
            modifier = modifier,
            images = images,
            isVideoPost = isVideoPost,
        ) {
            selectedFileId.value = it.getFileId()
            showMediaDialog.value = true
        }

        else -> AmityPostMediaImageChildrenFour(
            modifier = modifier,
            images = images,
            isVideoPost = isVideoPost,
        ) {
            selectedFileId.value = it.getFileId()
            showMediaDialog.value = true
        }
    }
}

@Composable
fun AmityPostMediaImageChildrenOne(
    modifier: Modifier = Modifier,
    image: AmityImage,
    isVideoPost: Boolean,
    onClick: (AmityImage) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AmityPostImageView(
            image = image,
            onClick = { onClick(image) }
        )
        if (isVideoPost) {
            AmityPostMediaPlayButton(
                modifier = modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun AmityPostMediaImageChildrenTwo(
    modifier: Modifier = Modifier,
    images: List<AmityImage>,
    isVideoPost: Boolean,
    onClick: (AmityImage) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            AmityPostImageView(
                image = images[0],
                onClick = { onClick(images[0]) }
            )
            if (isVideoPost) {
                AmityPostMediaPlayButton(
                    modifier = modifier.align(Alignment.Center)
                )
            }
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            AmityPostImageView(
                image = images[1],
                onClick = { onClick(images[1]) }
            )
            if (isVideoPost) {
                AmityPostMediaPlayButton(
                    modifier = modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun AmityPostMediaImageChildrenThree(
    modifier: Modifier = Modifier,
    images: List<AmityImage>,
    isVideoPost: Boolean,
    onClick: (AmityImage) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            AmityPostImageView(
                image = images[0],
                onClick = { onClick(images[0]) }
            )
            if (isVideoPost) {
                AmityPostMediaPlayButton(
                    modifier = modifier.align(Alignment.Center)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                AmityPostImageView(
                    image = images[1],
                    onClick = { onClick(images[1]) }
                )
                if (isVideoPost) {
                    AmityPostMediaPlayButton(
                        modifier = modifier.align(Alignment.Center)
                    )
                }
            }

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                AmityPostImageView(
                    image = images[2],
                    onClick = { onClick(images[2]) }
                )
                if (isVideoPost) {
                    AmityPostMediaPlayButton(
                        modifier = modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun AmityPostMediaImageChildrenFour(
    modifier: Modifier = Modifier,
    images: List<AmityImage>,
    isVideoPost: Boolean,
    onClick: (AmityImage) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .weight(2f)
        ) {
            AmityPostImageView(
                image = images[0],
                onClick = { onClick(images[0]) }
            )
            if (isVideoPost) {
                AmityPostMediaPlayButton(
                    modifier = modifier.align(Alignment.Center)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier
                .fillMaxWidth()
                .height(110.dp)
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                AmityPostImageView(
                    image = images[1],
                    onClick = { onClick(images[1]) }
                )
                if (isVideoPost) {
                    AmityPostMediaPlayButton(
                        modifier = modifier.align(Alignment.Center)
                    )
                }
            }
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                AmityPostImageView(
                    image = images[2],
                    onClick = { onClick(images[2]) }
                )
                if (isVideoPost) {
                    AmityPostMediaPlayButton(
                        modifier = modifier.align(Alignment.Center)
                    )
                }
            }

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                AmityPostImageView(
                    image = images[3],
                    onClick = { onClick(images[3]) }
                )

                if (images.size > 4) {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(0.5f))
                    ) {
                        Text(
                            text = "+${images.size - 3}",
                            style = AmityTheme.typography.title.copy(
                                fontSize = 20.sp,
                                lineHeight = 24.sp,
                                color = Color.White
                            ),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

fun getChildPostData(post: AmityPost): List<AmityPost.Data> {
    return post.getChildren().mapNotNull {
        when (val data = it.getData()) {
            is AmityPost.Data.IMAGE -> data
            is AmityPost.Data.VIDEO -> data
            else -> null
        }
    }
}

@Composable
fun AmityPostMediaPlayButton(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(
                color = Color.Black.copy(0.5f),
                shape = CircleShape
            )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.amity_ic_play),
            contentDescription = null,
            tint = Color.White,
            modifier = modifier
                .size(24.dp)
                .align(Alignment.Center)
                .padding(start = 6.dp, end = 4.dp)
        )
    }
}