package com.amity.socialcloud.uikit.community.compose.community.profile.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.sdk.model.core.pin.AmityPinnedPost
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityAdInjector
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageBehavior
import com.amity.socialcloud.uikit.community.compose.paging.feed.community.amityCommunityFeedLLS
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.catch
import java.util.concurrent.TimeUnit

@Composable
fun AmityCommunityFeedComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    communityId: String,
    shouldRefresh: Boolean = false,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.communityProfilePageBehavior
    }

    val announcementPosts = remember(communityId) {
        AmitySocialClient.newPostRepository()
            .getPinnedPosts(
                communityId = communityId,
                placement = AmityPinnedPost.PinPlacement.ANNOUNCEMENT.value
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }.collectAsLazyPagingItems()

    val pinPosts = remember(communityId) {
        AmitySocialClient.newPostRepository()
            .getPinnedPosts(
                communityId = communityId,
                placement = AmityPinnedPost.PinPlacement.DEFAULT.value
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }.collectAsLazyPagingItems()

    val communityPosts = remember(communityId) {
        val injector = AmityAdInjector<AmityPost>(
            placement = AmityAdPlacement.FEED,
            communityId = communityId,
        )

        AmitySocialClient.newFeedRepository()
            .getCommunityFeed(communityId)
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .throttleLatest(2000, TimeUnit.MILLISECONDS)
            .map { injector.inject(it) }
            .asFlow()
            .catch {}
    }.collectAsLazyPagingItems()

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            communityPosts.refresh()
        }
    }

    LazyColumn {
        amityCommunityFeedLLS(
            modifier = modifier,
            pageScope = pageScope,
            communityPosts = communityPosts,
            pinPosts = pinPosts,
            announcementPosts = announcementPosts,
            onClick = { post, _ ->
                behavior.goToPostDetailPage(
                    AmityCommunityProfilePageBehavior.Context(
                        pageContext = context,
                    ),
                    postId = post.getPostId(),
                    category = AmityPostCategory.PIN
                )
            }
        )
    }
}