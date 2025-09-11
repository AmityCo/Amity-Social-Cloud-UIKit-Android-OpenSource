package com.amity.socialcloud.uikit.community.compose.paging.feed.community

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.delay
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileEmptyImageFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileImageFeedItem
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityImageFeedContainer
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory

fun LazyListScope.amityCommunityImageFeedLLS(
    modifier: Modifier = Modifier,
    imagePosts: LazyPagingItems<AmityPost>,
    onViewPost: ((String, AmityPostCategory) -> Unit)? = null,
) {
    item {

        var debouncedAvailablePostIds by remember { mutableStateOf(emptySet<String>()) }
        
        val currentPostIds = (0 until imagePosts.itemCount).mapNotNull { index ->
            imagePosts[index]?.getPostId()
        }.toSet()
        
        LaunchedEffect(currentPostIds) {
            delay(300)
            debouncedAvailablePostIds = currentPostIds
        }

        AmityImageFeedContainer(
            availablePostIds = debouncedAvailablePostIds
        ) { openDialog ->
            AmityCommunityImageFeedContent(
                modifier = modifier,
                imagePosts = imagePosts,
                onViewPost = onViewPost,
                openDialog = openDialog
            )
        }
    }
}

@Composable
private fun AmityCommunityImageFeedContent(
    modifier: Modifier = Modifier,
    imagePosts: LazyPagingItems<AmityPost>,
    onViewPost: ((String, AmityPostCategory) -> Unit)? = null,
    openDialog: (AmityImage?, String?, onBottomSheetRequest: (() -> Unit)?) -> Unit
) {
    Column {
        Spacer(modifier.height(12.dp))
        
        /*  count calculation logic
         *  showing 2 items in a row
         *  check if the item count is even or odd
         *  if even, show 2 items in a row
         *  if odd, show last item in a new row
         */
        for (index in 0 until (imagePosts.itemCount / 2 + imagePosts.itemCount % 2)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .aspectRatio(2f)
            ) {
                //  check first image index is valid
                val isFirstImageIndexValid =
                    index * 2 < imagePosts.itemCount && index >= 0
                val firstImage = if (isFirstImageIndexValid) {
                    imagePosts[index * 2]
                } else {
                    null
                }
                AmityProfileImageFeedItem(
                    modifier = modifier.weight(1f),
                    data = firstImage?.getData() as? AmityPost.Data.IMAGE,
                    postId = firstImage?.getPostId(), // For deletion detection
                    parentPostId = firstImage?.getParentPostId(), // For navigation
                    isPostCreator = firstImage?.getCreatorId() == AmityCoreClient.getUserId(),
                    onPostClick = { postId ->
                        onViewPost?.invoke(postId, AmityPostCategory.GENERAL)
                    },
                    openDialog = openDialog
                )

                //  check second image index is valid
                val isSecondImageIndexValid =
                    index * 2 + 1 < imagePosts.itemCount && index >= 0
                val secondImage = if (isSecondImageIndexValid) {
                    imagePosts[index * 2 + 1]
                } else {
                    null
                }

                //  show image thumbnail if index is valid
                //  if not, it's one last item in a new row and show empty box
                if (isSecondImageIndexValid) {
                    AmityProfileImageFeedItem(
                        modifier = modifier.weight(1f),
                        data = secondImage?.getData() as? AmityPost.Data.IMAGE,
                        postId = secondImage?.getPostId(), // For deletion detection
                        parentPostId = secondImage?.getParentPostId(), // For navigation
                        isPostCreator = secondImage?.getCreatorId() == AmityCoreClient.getUserId(),
                        onPostClick = { postId ->
                            onViewPost?.invoke(postId, AmityPostCategory.GENERAL)
                        },
                        openDialog = openDialog
                    )
                } else {
                    Box(modifier = modifier.weight(1f))
                }
            }
        }

        if (imagePosts.itemCount == 0) {
            Box(modifier = Modifier.height(480.dp)) {
                AmityProfileEmptyImageFeed()
            }
        }
    }
}