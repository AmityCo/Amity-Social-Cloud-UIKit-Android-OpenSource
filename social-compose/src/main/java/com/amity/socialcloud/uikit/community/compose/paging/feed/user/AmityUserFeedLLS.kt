package com.amity.socialcloud.uikit.community.compose.paging.feed.user

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostShimmer
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityBlockedUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityEmptyUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityPrivateUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityUnknownUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityUserFeedType


fun LazyListScope.amityUserFeedLLS(
    modifier: Modifier = Modifier,
    context: Context,
    pageScope: AmityComposePageScope? = null,
    userPosts: LazyPagingItems<AmityPost>,
    isBlockedByMe: Boolean,
) {
    val behavior by lazy {
        AmitySocialBehaviorHelper.userFeedComponentBehavior
    }

    if (isBlockedByMe) {
        item {
            AmityBaseComponent(
                pageScope = pageScope,
                componentId = "user_feed"
            ) {
                Box(modifier = Modifier.height(480.dp)) {
                    AmityBlockedUserFeed(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        feedType = AmityUserFeedType.POST,
                    )
                }
            }
        }
    } else if (userPosts.loadState.mediator?.refresh is LoadState.Error) {
        item {
            val error = (userPosts.loadState.mediator?.refresh as LoadState.Error)
            val amityError = AmityError.from(error.error)

            if (amityError == AmityError.UNAUTHORIZED_ERROR) {
                AmityBaseComponent(
                    pageScope = pageScope,
                    componentId = "user_feed"
                ) {
                    Box(modifier = Modifier.height(480.dp)) {
                        AmityPrivateUserFeed(
                            pageScope = pageScope,
                            componentScope = getComponentScope(),
                            feedType = AmityUserFeedType.POST
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.height(480.dp)) {
                    AmityUnknownUserFeed()
                }
            }
        }
    } else if (userPosts.itemCount == 0) {
        item {
            AmityBaseComponent(
                pageScope = pageScope,
                componentId = "user_feed"
            ) {
                Box(modifier = Modifier.height(480.dp)) {
                    AmityEmptyUserFeed(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        feedType = AmityUserFeedType.POST
                    )
                }
            }
        }
    } else {
        items(
            count = userPosts.itemCount,
            key = { "user_${userPosts[it]?.getPostId() ?: it}" }
        ) { index ->
            val post = userPosts[index]
            if (post == null) {
                AmityPostShimmer()
                AmityNewsFeedDivider()
            } else {
                // TODO: 3/6/24 currently only support text, image, and video post
                when (post.getData()) {
                    is AmityPost.Data.TEXT,
                    is AmityPost.Data.IMAGE,
                    is AmityPost.Data.VIDEO -> {
                    }

                    else -> return@items
                }

                AmityPostContentComponent(
                    modifier = modifier,
                    post = post,
                    style = AmityPostContentComponentStyle.FEED,
                    hideMenuButton = false,
                    hideTarget = true,
                    onTapAction = {
                        behavior.goToPostDetailPage(
                            context = context,
                            postId = post.getPostId(),
                        )
                    },
                )
                AmityNewsFeedDivider()
            }
        }
    }
}