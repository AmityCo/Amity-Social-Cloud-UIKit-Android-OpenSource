package com.amity.socialcloud.uikit.community.compose.user.profile.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.paging.feed.user.amityUserVideoFeedLLS
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.catch

@Composable
fun AmityUserVideoFeedComponent(
    modifier: Modifier = Modifier,
    userId: String,
    shouldRefresh: Boolean = false,
) {
    val videoPosts = remember(userId) {
        AmitySocialClient.newPostRepository()
            .getPosts()
            .targetUser(userId)
            .types(listOf(AmityPost.DataType.sealedOf(AmityPost.DataType.VIDEO.getApiKey())))
            .includeDeleted(false)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {}
    }.collectAsLazyPagingItems()

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            videoPosts.refresh()
        }
    }

    LazyColumn {
        amityUserVideoFeedLLS(
            modifier = modifier,
            videoPosts = videoPosts,
            isBlockedByMe = false,
        )
    }
}