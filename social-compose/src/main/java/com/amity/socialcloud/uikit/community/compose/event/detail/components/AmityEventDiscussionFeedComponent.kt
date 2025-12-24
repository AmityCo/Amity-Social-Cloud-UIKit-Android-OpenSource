package com.amity.socialcloud.uikit.community.compose.event.detail.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.LazyPagingItems
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.sdk.model.core.pin.AmityPinnedPost
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityAdInjector
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageBehavior
import com.amity.socialcloud.uikit.community.compose.event.detail.AmityEventDetailViewModel
import com.amity.socialcloud.uikit.community.compose.paging.feed.event.amityEventDiscussionFeedLLS
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.catch
import java.util.concurrent.TimeUnit

@Composable
fun AmityEventDiscussionFeedComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityEventDetailViewModel,
    lazyListState: LazyListState,
    header: LazyListScope.() -> Unit,
    shouldRefresh: Boolean = false,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.communityProfilePageBehavior
    }

    val communityId by viewModel.communityId.collectAsState()
    val excludedPostIds by viewModel.excludedPostIds.collectAsState()

    val announcementPosts = remember(communityId) {
        if (!communityId.isNullOrEmpty()) {
            AmitySocialClient.newPostRepository()
                .getPinnedPosts(
                    communityId = communityId!!,
                    placement = AmityPinnedPost.PinPlacement.ANNOUNCEMENT.value
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .asFlow()
                .catch { }
        } else {
            kotlinx.coroutines.flow.flowOf(androidx.paging.PagingData.empty())
        }
    }.collectAsLazyPagingItems()

    val pinPosts = remember(communityId) {
        if (!communityId.isNullOrEmpty()) {
            AmitySocialClient.newPostRepository()
                .getPinnedPosts(
                    communityId = communityId!!,
                    placement = AmityPinnedPost.PinPlacement.DEFAULT.value
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .asFlow()
                .catch { }
        } else {
            kotlinx.coroutines.flow.flowOf(androidx.paging.PagingData.empty())
        }
    }.collectAsLazyPagingItems()

    val communityPosts = remember(communityId) {
        if (!communityId.isNullOrEmpty()) {
            val injector = AmityAdInjector<AmityPost>(
                placement = AmityAdPlacement.FEED,
                communityId = communityId!!,
            )

            AmitySocialClient.newFeedRepository()
                .getCommunityFeed(communityId!!)
                .includeDeleted(false)
                .dataTypes(AmitySocialBehaviorHelper.supportedPostTypes)
                .matchingOnlyParentPosts(true)
                .build()
                .query()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                .throttleLatest(2000, TimeUnit.MILLISECONDS)
                .map { injector.inject(it) }
                .asFlow()
                .catch {}
        } else {
            kotlinx.coroutines.flow.flowOf(androidx.paging.PagingData.empty())
        }
    }.collectAsLazyPagingItems()

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            communityPosts.refresh()
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .background(AmityTheme.colors.background)
    ) {
        // Render header content provided by caller
        header()

        // Render feed items directly in this LazyColumn
        if (!communityId.isNullOrEmpty()) {
            amityEventDiscussionFeedItems(
                modifier = Modifier,
                pageScope = pageScope,
                announcementPosts = announcementPosts,
                pinPosts = pinPosts,
                communityPosts = communityPosts,
                excludedPostIds = excludedPostIds,
                eventHostId = null,
                onClick = { post, _ ->
                    behavior.goToPostDetailPage(
                        AmityCommunityProfilePageBehavior.Context(
                            pageContext = context,
                        ),
                        postId = post.getPostId(),
                        category = AmityPostCategory.GENERAL
                    )
                }
            )
        }
    }
}

/**
 * LazyListScope extension for rendering event discussion feed items
 * This allows the feed to be embedded directly in the parent LazyColumn
 */
fun LazyListScope.amityEventDiscussionFeedItems(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    announcementPosts: LazyPagingItems<AmityPinnedPost>,
    pinPosts: LazyPagingItems<AmityPinnedPost>,
    communityPosts: LazyPagingItems<AmityListItem>,
    excludedPostIds: List<String> = emptyList(),
    eventHostId: String? = null,
    onClick: (AmityPost, AmityPostCategory) -> Unit
) {
    // Render feed items directly in the parent LazyListScope
    amityEventDiscussionFeedLLS(
        modifier = modifier,
        pageScope = pageScope,
        communityPosts = communityPosts,
        pinPosts = pinPosts,
        announcementPosts = announcementPosts,
        excludedPostIds = excludedPostIds,
        eventHostId = eventHostId,
        onClick = onClick
    )
}
