package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
    val postChildren = remember(post.getPostId(), post.getEditedAt(), post.getUpdatedAt(), post.getChildren().size) {
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

    when (post.getChildren().size) {
        0 -> {}
        1 -> AmityPostMediaImageChildrenOne(
            modifier = modifier,
            isVideoPost = isVideoPost,
            postChild = post.getChildren().first(),
        ) {
            selectedFileId.value = it.getPostId()
            showMediaDialog.value = true
        }

        2 -> AmityPostMediaImageChildrenTwo(
            modifier = modifier,
            postChildren = post.getChildren(),
            images = images,
            isVideoPost = isVideoPost,
        ) {
            selectedFileId.value = it.getPostId()
            showMediaDialog.value = true
        }

        3 -> AmityPostMediaImageChildrenThree(
            modifier = modifier,
            postChildren = post.getChildren(),
            images = images,
            isVideoPost = isVideoPost,
        ) {
            selectedFileId.value = it.getPostId()
            showMediaDialog.value = true
        }

        else -> AmityPostMediaImageChildrenFour(
            modifier = modifier,
            postChildren = post.getChildren(),
            images = images,
            isVideoPost = isVideoPost,
        ) {
            selectedFileId.value = it.getPostId()
            showMediaDialog.value = true
        }
    }
}

@Composable
fun AmityPostMediaImageChildrenOne(
    modifier: Modifier = Modifier,
    postChild: AmityPost,
    isVideoPost: Boolean,
    onClick: (AmityPost) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AmityPostImageView(
            post = postChild,
            onClick = { onClick(postChild) }
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
    postChildren: List<AmityPost>,
    images: List<AmityImage>,
    isVideoPost: Boolean,
    onClick: (AmityPost) -> Unit,
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
                post = postChildren[0],
                onClick = { onClick(postChildren[0]) }
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
                post = postChildren[1],
                onClick = { onClick(postChildren[1]) }
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
    postChildren: List<AmityPost>,
    images: List<AmityImage>,
    isVideoPost: Boolean,
    onClick: (AmityPost) -> Unit,
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
                post = postChildren[0],
                onClick = { onClick(postChildren[0]) }
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
                .weight(1f)
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                AmityPostImageView(
                    post = postChildren[1],
                    onClick = { onClick(postChildren[1]) }
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
                    post = postChildren[2],
                    onClick = { onClick(postChildren[2]) }
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
    postChildren: List<AmityPost>,
    images: List<AmityImage>,
    isVideoPost: Boolean,
    onClick: (AmityPost) -> Unit,
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
                post = postChildren[0],
                onClick = { onClick(postChildren[0]) }
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
                    post = postChildren[1],
                    onClick = { onClick(postChildren[1]) }
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
                    post = postChildren[2],
                    onClick = { onClick(postChildren[2]) }
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
                    post = postChildren[3],
                    onClick = { onClick(postChildren[3]) }
                )

                if (postChildren.size > 4) {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(0.5f))
                    ) {
                        Text(
                            text = "+${postChildren.size - 3}",
                            style = AmityTheme.typography.titleLegacy.copy(
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
    Image(
        painter = painterResource(id = R.drawable.amity_ic_play_v4),
        contentDescription = null,
        modifier = modifier.size(40.dp)
    )
}