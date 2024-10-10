package com.amity.socialcloud.uikit.community.compose.user.profile.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.paging.feed.user.amityUserFeedLLS
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.catch

@Composable
fun AmityUserFeedComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    userId: String,
    shouldRefresh: Boolean = false,
) {
    val context = LocalContext.current

    val userPosts = remember(userId) {
        AmitySocialClient.newPostRepository()
            .getPosts()
            .targetUser(userId)
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
            userPosts.refresh()
        }
    }

    LazyColumn {
        amityUserFeedLLS(
            modifier = modifier,
            context = context,
            pageScope = pageScope,
            userPosts = userPosts,
            isBlockedByMe = false,
        )
    }
}