package com.amity.socialcloud.uikit.community.compose.paging.feed.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityMediaPostShimmer
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
                item { Spacer(modifier.height(12.dp)) }
                /*  count calculation logic
                 *  showing 2 items in a row
                 *  check if the item count is even or odd
                 *  if even, show 2 items in a row
                 *  if odd, show last item in a new row
                 */
                items(
                    count = imagePosts.itemCount / 2 + imagePosts.itemCount % 2,
                    key = { "image_${imagePosts[it]?.getPostId() ?: it}" }
                ) { index ->
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
                            data = firstImage?.getData() as? AmityPost.Data.IMAGE
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
                                data = secondImage?.getData() as? AmityPost.Data.IMAGE
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