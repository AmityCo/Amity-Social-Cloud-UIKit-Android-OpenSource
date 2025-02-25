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
import com.amity.socialcloud.sdk.model.core.pin.AmityPinnedPost
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageBehavior
import com.amity.socialcloud.uikit.community.compose.paging.feed.community.amityCommunityPinnedFeedLLS
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.catch

@Composable
fun AmityCommunityPinnedPostComponent(
    modifier: Modifier = Modifier, communityId: String,
    pageScope: AmityComposePageScope? = null,
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

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            pinPosts.refresh()
            announcementPosts.refresh()
        }
    }

    LazyColumn {
        amityCommunityPinnedFeedLLS(
            modifier = modifier,
            pageScope = pageScope,
            pinPosts = pinPosts,
            announcementPosts = announcementPosts,
            onClick = {
                behavior.goToPostDetailPage(
                    AmityCommunityProfilePageBehavior.Context(
                        pageContext = context,
                    ),
                    postId = it.getPostId(),
                    category = AmityPostCategory.PIN
                )
            }
        )
    }
}