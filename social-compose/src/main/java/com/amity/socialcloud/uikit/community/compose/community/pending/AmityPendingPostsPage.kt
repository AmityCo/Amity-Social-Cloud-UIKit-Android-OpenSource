package com.amity.socialcloud.uikit.community.compose.community.pending

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.community.compose.community.pending.elements.AmityEmptyPendingPostsElement
import com.amity.socialcloud.uikit.community.compose.community.pending.elements.AmityPendingPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostShimmer

@Composable
fun AmityPendingPostsPage(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
) {
    val context = LocalContext.current

    val viewModel = remember(community.getCommunityId()) {
        AmityPendingPostsPageViewModel(community.getCommunityId())
    }

    val isModerator by AmityCoreClient.hasPermission(AmityPermission.EDIT_COMMUNITY)
        .atCommunity(community.getCommunityId())
        .check()
        .asFlow()
        .collectAsState(initial = false)

    val pendingPosts = remember(community.getCommunityId()) {
        viewModel.getPendingPosts()
    }.collectAsLazyPagingItems()

    val postListState by viewModel.postListState.collectAsState()

    AmityBasePage(pageId = "pending_posts_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            AmityToolBar(
                title = "Pending posts" + if (isModerator) " (${pendingPosts.itemCount})" else "",
                onBackClick = {
                    context.closePage()
                }
            )

            if (isModerator && postListState == AmityPendingPostsPageViewModel.PostListState.SUCCESS) {
                Text(
                    text = "Decline pending post will permanently delete the selected post from community.",
                    style = AmityTheme.typography.bodyLegacy.copy(
                        color = AmityTheme.colors.baseShade1
                    ),
                    modifier = modifier
                        .background(color = AmityTheme.colors.baseShade4)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            LazyColumn {
                AmityPendingPostsPageViewModel.PostListState.from(
                    loadState = pendingPosts.loadState.refresh,
                    itemCount = pendingPosts.itemCount,
                ).let(viewModel::setPostListState)

                when (postListState) {
                    AmityPendingPostsPageViewModel.PostListState.ERROR,
                    AmityPendingPostsPageViewModel.PostListState.EMPTY -> {
                        item {
                            AmityEmptyPendingPostsElement()
                        }
                    }

                    AmityPendingPostsPageViewModel.PostListState.LOADING -> {
                        items(2) {
                            AmityPostShimmer()
                            AmityNewsFeedDivider()
                        }
                    }

                    AmityPendingPostsPageViewModel.PostListState.SUCCESS -> {
                        items(
                            count = pendingPosts.itemCount,
                            key = { pendingPosts[it]?.getPostId() ?: it }
                        ) {
                            val post = pendingPosts[it] ?: return@items

                            AmityPendingPostContentComponent(
                                pageScope = getPageScope(),
                                post = post,
                                onAcceptAction = {
                                    viewModel.approvePost(
                                        postId = post.getPostId(),
                                        onApproved = {
                                            getPageScope().showSnackbar(
                                                message = "Post accepted.",
                                                drawableRes = R.drawable.amity_ic_snack_bar_success,
                                            )
                                        },
                                        onAlreadyApproved = {
                                            getPageScope().showSnackbar(
                                                message = "Post has been reviewed by another moderator",
                                                drawableRes = R.drawable.amity_ic_snack_bar_success,
                                            )
                                        },
                                        onError = {
                                            getPageScope().showSnackbar(
                                                message = "Failed to review post. Please try again!",
                                                drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                            )
                                        }
                                    )
                                },
                                onDeclineAction = {
                                    viewModel.declinePost(
                                        postId = post.getPostId(),
                                        onApproved = {
                                            getPageScope().showSnackbar(
                                                message = "Post declined.",
                                                drawableRes = R.drawable.amity_ic_snack_bar_success,
                                            )
                                        },
                                        onAlreadyDeclined = {
                                            getPageScope().showSnackbar(
                                                message = "Post has been reviewed by another moderator.",
                                                drawableRes = R.drawable.amity_ic_snack_bar_success,
                                            )
                                        },
                                        onError = {
                                            getPageScope().showSnackbar(
                                                message = "Failed to review post. Please try again!",
                                                drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                            )
                                        }
                                    )
                                }
                            )
                            AmityNewsFeedDivider()
                        }
                    }
                }
            }
        }
    }
}