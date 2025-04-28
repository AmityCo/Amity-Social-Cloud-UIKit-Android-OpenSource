package com.amity.socialcloud.uikit.community.compose.notificationtray

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.notificationtray.NotificationTrayViewModel.NotificationTrayListItem
import com.amity.socialcloud.uikit.community.compose.notificationtray.component.AmityNotificationTrayEmptyState
import com.amity.socialcloud.uikit.community.compose.notificationtray.component.AmityNotificationTrayItemView
import com.amity.socialcloud.uikit.community.compose.notificationtray.component.AmityNotificationTrayShimmer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmityNotificationTrayPage(
    modifier: Modifier = Modifier,
) {
    val context: Context = LocalContext.current

    val scrollState = rememberLazyListState()
    val hasScrolled by remember {
        derivedStateOf { scrollState.firstVisibleItemScrollOffset > 0 }
    }

    val behavior = remember {
        AmitySocialBehaviorHelper.notificationTrayPageBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<NotificationTrayViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    val notificationItems = viewModel.getNotificationTrayItem().collectAsLazyPagingItems()

    // Add this state to track if error snackbar has been shown
    var errorSnackbarShown by remember { mutableStateOf(false) }

    var stickyHeaderHeight by remember { mutableIntStateOf(0) }

    AmityBasePage(
        pageId = "notification_tray_page",
    ) {
        LazyColumn(
            state = scrollState,
            verticalArrangement = Arrangement.Top,
            modifier = modifier.fillMaxSize()
        ) {

            stickyHeader {
                Box(
                    modifier = modifier
                        .height(58.dp)
                        .fillMaxWidth()
                        .background(AmityTheme.colors.background)
                        .padding(horizontal = 16.dp)
                        .onGloballyPositioned { coordinates ->
                            stickyHeaderHeight = coordinates.size.height
                        }
                ) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "back_button"
                    ) {
                        Icon(
                            painter = painterResource(getConfig().getIcon()),
                            contentDescription = null,
                            tint = AmityTheme.colors.base,
                            modifier = modifier
                                .size(20.dp)
                                .align(Alignment.CenterStart)
                                .clickableWithoutRipple {
                                    context.closePage()
                                }
                        )
                    }

                    Text(
                        text = "Notifications",
                        style = AmityTheme.typography.titleBold,
                        modifier = modifier.align(Alignment.Center)
                    )

                }
                if (hasScrolled) {
                    HorizontalDivider(
                        color = AmityTheme.colors.baseShade4,
                    )
                }
            }

            when (notificationItems.loadState.refresh) {
                is LoadState.Loading -> {
                    item {
                        AmityNotificationTrayShimmer(
                            modifier = Modifier
                        )
                    }

                }

                is LoadState.Error -> {
                    item {
                        AmityNotificationTrayShimmer(
                            modifier = Modifier
                        )
                    }

                    if (!errorSnackbarShown) {
                        showErrorSnackbar(
                            message = "Oops, something went wrong.",
                            additionalHeight = 20
                        )
                        errorSnackbarShown = true
                    }
                }

                is LoadState.NotLoading -> {
                    if (notificationItems.itemCount == 0) {
                        item {
                            AmityNotificationTrayEmptyState(modifier = Modifier.fillParentMaxSize())
                        }
                    } else {
                        items(
                            count = notificationItems.itemCount,
                            key = { index ->
                                // Use uniqueId for notifications or title string for headers
                                when (val listItem = notificationItems[index]) {
                                    is NotificationTrayListItem.NotificationItem -> listItem.item.uniqueId()
                                    is NotificationTrayListItem.Header -> "header_${listItem.title}"
                                    else -> "unknown $index"
                                }
                            }
                        ) { index ->
                            when (val listItem = notificationItems[index]) {
                                is NotificationTrayListItem.Header -> {
                                    Text(
                                        text = listItem.title,
                                        style = AmityTheme.typography.captionBold,
                                        color = AmityTheme.colors.baseShade1,
                                        modifier = Modifier.padding(16.dp, 8.dp)
                                    )
                                }

                                is NotificationTrayListItem.NotificationItem -> {
                                    AmityNotificationTrayItemView(
                                        modifier = Modifier.clickableWithoutRipple {
                                            viewModel.markNotificationItemAsSeen(listItem.item)
                                            var postId: String? = null
                                            var commentId: String? = null
                                            var parentId: String? = null
                                            var communityId: String? = null

                                            if (listItem.item.getTargetType() == "community") {
                                                communityId = listItem.item.getTargetId()
                                            }

                                            when (listItem.item.getActionType()) {
                                                "post", "poll" -> {
                                                    postId =
                                                        if (listItem.item.getTargetType() != "community")
                                                            listItem.item.getActionReferenceId() else null
                                                }

                                                "comment" -> {
                                                    postId = listItem.item.getReferenceId()
                                                    commentId = listItem.item.getActionReferenceId()
                                                }

                                                "reply" -> {
                                                    postId = listItem.item.getReferenceId()
                                                    commentId = listItem.item.getActionReferenceId()
                                                    parentId = listItem.item.getParentId()
                                                }

                                                "reaction" -> {
                                                    when (listItem.item.getTrayItemCategory()) {
                                                        "reaction_on_comment" -> {
                                                            postId = listItem.item.getReferenceId()
                                                            commentId = listItem.item.getActionReferenceId()
                                                        }

                                                        "reaction_on_reply" -> {
                                                            postId = listItem.item.getReferenceId()
                                                            commentId = listItem.item.getActionReferenceId()
                                                            parentId = listItem.item.getParentId()
                                                        }

                                                        "reaction_on_post" -> {
                                                            postId = listItem.item.getActionReferenceId()
                                                        }
                                                    }
                                                }

                                                "mention" -> {
                                                    when (listItem.item.getTrayItemCategory()) {
                                                        "mention_in_poll", "mention_in_post" -> {
                                                            postId = listItem.item.getActionReferenceId()
                                                        }

                                                        "mention_in_comment" -> {
                                                            postId = listItem.item.getReferenceId()
                                                            commentId = listItem.item.getActionReferenceId()
                                                        }

                                                        "mention_in_reply" -> {
                                                            postId = listItem.item.getReferenceId()
                                                            commentId = listItem.item.getActionReferenceId()
                                                            parentId = listItem.item.getParentId()
                                                        }
                                                    }
                                                }
                                            }

                                            if (postId == null && communityId != null) {
                                                behavior.goToCommunityProfilePage(
                                                    context = context,
                                                    communityId = listItem.item.getTargetId()
                                                )
                                            } else {
                                                postId?.let {
                                                    behavior.goToPostDetailPage(
                                                        context = context,
                                                        postId = postId,
                                                        commentId = commentId,
                                                        parentId = parentId,
                                                    )
                                                }
                                            }
                                        },
                                        isSeen = listItem.item.isSeen() == true,
                                        data = listItem.item,
                                    )
                                }

                                else -> Unit
                            }
                        }

                        when (notificationItems.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    Column(verticalArrangement = Arrangement.SpaceBetween) {
                                        Box(
                                            Modifier
                                                .width(233.dp)
                                                .height(10.dp)
                                                .shimmerBackground(
                                                    color = AmityTheme.colors.baseShade4,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                        )
                                        Spacer(Modifier.height(8.dp))
                                        Box(
                                            Modifier
                                                .width(132.dp)
                                                .height(10.dp)
                                                .shimmerBackground(
                                                    color = AmityTheme.colors.baseShade4,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                        )
                                    }
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }

        }
    }
}