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
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.AmityProfileVideoFeedItem
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityBlockedUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityEmptyUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityPrivateUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityUnknownUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityUserFeedType
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageViewModel.PostListState


fun LazyListScope.amityUserVideoFeedLLS(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    videoPosts: LazyPagingItems<AmityPost>,
    postListState: PostListState,
    isBlockedByMe: Boolean,
) {
    if (isBlockedByMe) {
        item {
            AmityBaseComponent(
                pageScope = pageScope,
                componentId = "user_video_feed"
            ) {
                Box(modifier = Modifier.height(480.dp)) {
                    AmityBlockedUserFeed(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        feedType = AmityUserFeedType.VIDEO,
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
                        componentId = "user_video_feed"
                    ) {
                        Box(modifier = Modifier.height(480.dp)) {
                            AmityEmptyUserFeed(
                                pageScope = pageScope,
                                componentScope = getComponentScope(),
                                feedType = AmityUserFeedType.VIDEO
                            )
                        }
                    }
                }
            }

            PostListState.ERROR -> {
                item {
                    val error = (videoPosts.loadState.mediator?.refresh as? LoadState.Error)
                    val amityError = AmityError.from(error?.error)

                    if (
                        amityError == AmityError.UNAUTHORIZED_ERROR ||
                        amityError == AmityError.PERMISSION_DENIED
                    ) {
                        AmityBaseComponent(
                            pageScope = pageScope,
                            componentId = "user_video_feed"
                        ) {
                            Box(modifier = Modifier.height(480.dp)) {
                                AmityPrivateUserFeed(
                                    pageScope = pageScope,
                                    componentScope = getComponentScope(),
                                    feedType = AmityUserFeedType.VIDEO
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
                    count = videoPosts.itemCount / 2 + videoPosts.itemCount % 2,
                    key = { "video_${videoPosts[it]?.getPostId() ?: it}" }
                ) { index ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                            .aspectRatio(2f)
                    ) {
                        val isFirstVideoIndexValid =
                            index * 2 < videoPosts.itemCount && index >= 0

                        if (isFirstVideoIndexValid) {
                            val firstVideo = videoPosts[index * 2]
                            if (firstVideo == null) {
                                Box(modifier.weight(1f))
                            } else {
                                AmityProfileVideoFeedItem(
                                    modifier = modifier.weight(1f),
                                    data = firstVideo.getData() as AmityPost.Data.VIDEO
                                )
                            }
                        } else {
                            Box(modifier.weight(1f))
                        }

                        val isSecondVideoIndexValid =
                            index * 2 + 1 < videoPosts.itemCount && index >= 0

                        if (isSecondVideoIndexValid) {
                            val secondVideo = videoPosts.peek(index * 2 + 1)
                            if (secondVideo == null) {
                                Box(modifier = modifier.weight(1f))
                            } else {
                                AmityProfileVideoFeedItem(
                                    modifier = modifier.weight(1f),
                                    data = secondVideo.getData() as AmityPost.Data.VIDEO
                                )
                            }
                        } else {
                            Box(modifier = modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}