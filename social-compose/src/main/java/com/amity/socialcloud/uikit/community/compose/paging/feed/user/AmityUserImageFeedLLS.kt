package com.amity.socialcloud.uikit.community.compose.paging.feed.user

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.Log
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.delay
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityMediaPostShimmer
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityImageFeedContainer
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileImageFeedItem
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityBlockedUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityEmptyUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityPrivateUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityUnknownUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityUserFeedType
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageViewModel.PostListState

fun LazyListScope.amityUserImageFeedLLS(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    imagePosts: LazyPagingItems<AmityPost>,
    postListState: PostListState,
    isBlockedByMe: Boolean,
    onPostClick: ((String) -> Unit)? = null,
) {
    if (isBlockedByMe) {
        item {
            AmityBaseComponent(
                pageScope = pageScope,
                componentId = "user_image_feed"
            ) {
                Box(modifier = Modifier.height(480.dp)) {
                    AmityBlockedUserFeed(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        feedType = AmityUserFeedType.IMAGE,
                    )
                }
            }
        }
    } else {
        when (postListState) {
            PostListState.EMPTY -> {
                item {
                    AmityBaseComponent(
                        pageScope = pageScope,
                        componentId = "user_image_feed"
                    ) {
                        Box(modifier = Modifier.height(480.dp)) {
                            AmityEmptyUserFeed(
                                pageScope = pageScope,
                                componentScope = getComponentScope(),
                                feedType = AmityUserFeedType.IMAGE
                            )
                        }
                    }
                }
            }

            PostListState.ERROR -> {
                item {
                    val error = (imagePosts.loadState.mediator?.refresh as? LoadState.Error)
                    val amityError = AmityError.from(error?.error)

                    if (
                        amityError == AmityError.UNAUTHORIZED_ERROR ||
                        amityError == AmityError.PERMISSION_DENIED
                    ) {
                        AmityBaseComponent(
                            pageScope = pageScope,
                            componentId = "user_image_feed"
                        ) {
                            Box(modifier = Modifier.height(480.dp)) {
                                AmityPrivateUserFeed(
                                    pageScope = pageScope,
                                    componentScope = getComponentScope(),
                                    feedType = AmityUserFeedType.IMAGE
                                )
                            }
                        }
                    } else {
                        Box(modifier = Modifier.height(480.dp)) {
                            AmityUnknownUserFeed()
                        }
                    }
                }
            }

            PostListState.LOADING -> {
                item {
                    Spacer(modifier.height(16.dp))
                }

                items(4) {
                    AmityMediaPostShimmer()
                }
            }

            PostListState.SUCCESS -> {
                item {
                    // Debounced availablePostIds to prevent excessive updates
                    var debouncedAvailablePostIds by remember { mutableStateOf(emptySet<String>()) }
                    
                    // Calculate current post IDs
                    val currentPostIds = (0 until imagePosts.itemCount).mapNotNull { index ->
                        imagePosts[index]?.getPostId()
                    }.toSet()
                    
                    // Debounce the updates with a 300ms delay
                    LaunchedEffect(currentPostIds) {
                        delay(300) // Wait 300ms before updating
                        debouncedAvailablePostIds = currentPostIds
                    }

                    AmityImageFeedContainer(
                        availablePostIds = debouncedAvailablePostIds
                    ) { openDialog ->
                        Column {
                            Spacer(modifier.height(12.dp))

                            /*  count calculation logic
                             *  showing 2 items in a row
                             *  check if the item count is even or odd
                             *  if even, show 2 items in a row
                             *  if odd, show last item in a new row
                             */
                            (0 until (imagePosts.itemCount / 2 + imagePosts.itemCount % 2)).forEach { index ->
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
                                        postId = firstImage?.getPostId(),
                                        parentPostId = firstImage?.getParentPostId(),
                                        isPostCreator = firstImage?.getCreatorId() == AmityCoreClient.getUserId(),
                                        onPostClick = onPostClick,
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
                                            postId = secondImage?.getPostId(),
                                            parentPostId = secondImage?.getParentPostId(),
                                            isPostCreator = secondImage?.getCreatorId() == AmityCoreClient.getUserId(),
                                            onPostClick = onPostClick,
                                            openDialog = openDialog
                                        )
                                    } else {
                                        Box(modifier = modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}