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
import com.amity.socialcloud.uikit.common.utils.isSupportedDataTypes
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostShimmer
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityBlockedUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityEmptyUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityPrivateUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityUnknownUserFeed
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.user.AmityUserFeedType
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageViewModel.PostListState

fun LazyListScope.amityUserFeedLLS(
    modifier: Modifier = Modifier,
    context: Context,
    pageScope: AmityComposePageScope? = null,
    userPosts: LazyPagingItems<AmityPost>,
    postListState: PostListState,
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
    } else {
        when (postListState) {
            PostListState.EMPTY -> {
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
            }

            PostListState.ERROR -> {
                item {
                    val error = (userPosts.loadState.mediator?.refresh as? LoadState.Error)
                    val amityError = AmityError.from(error?.error)

                    if (
                        amityError == AmityError.UNAUTHORIZED_ERROR ||
                        amityError == AmityError.PERMISSION_DENIED
                    ) {
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
            }

            PostListState.LOADING -> {
                items(4) {
                    AmityPostShimmer()
                    AmityNewsFeedDivider()
                }
            }

            PostListState.SUCCESS -> {
                items(
                    count = userPosts.itemCount,
                    key = { "user_${userPosts[it]?.getPostId() ?: it}" }
                ) { index ->
                    val post = userPosts[index]
                    if (post == null) {
                        AmityPostShimmer()
                        AmityNewsFeedDivider()
                    } else {
                        if (!post.isSupportedDataTypes()) {
                            return@items
                        }

                        AmityPostContentComponent(
                            modifier = modifier,
                            post = post,
                            pageScope = pageScope,
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
    }
}