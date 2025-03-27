package com.amity.socialcloud.uikit.community.compose.user.pending

import android.app.Activity
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserListItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.user.pending.elements.AmityUserEmptyPendingFollowRequestView
import com.amity.socialcloud.uikit.community.compose.user.pending.elements.AmityUserPendingFollowRequestActionRow

@Composable
fun AmityUserPendingFollowRequestsPage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.userPendingFollowRequestsPageBehavior
    }

    val viewModel = viewModel<AmityUserPendingFollowRequestsPageViewModel>()

    val lazyPagingItems = remember {
        viewModel.getPendingRequests()
    }.collectAsLazyPagingItems()

    AmityBasePage("user_pending_follow_request_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            AmityToolBar(
                title = "Follow requests (${lazyPagingItems.itemCount})",
                onBackClick = {
                    context.closePageWithResult(Activity.RESULT_CANCELED)
                }
            )

            if (lazyPagingItems.itemCount > 0) {
                Text(
                    text = "Declining a follow request is irreversible. The user must send a new request if declined.",
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.baseShade1
                    ),
                    modifier = modifier
                        .background(color = AmityTheme.colors.baseShade4)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )

                LazyColumn {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = { index ->
                            lazyPagingItems[index]?.getSourceUser()?.getUserId() ?: index
                        }
                    ) { index ->
                        val follower = lazyPagingItems[index]?.getSourceUser() ?: return@items

                        Column(
                            modifier = modifier.fillParentMaxWidth()
                        ) {
                            AmityUserListItem(
                                user = follower,
                                showRightMenu = false,
                                onUserClick = {
                                    behavior.goToUserProfilePage(
                                        context = context,
                                        userId = it.getUserId(),
                                    )
                                },
                            )

                            AmityUserPendingFollowRequestActionRow(
                                onAccept = {
                                    viewModel.acceptFollow(
                                        userId = follower.getUserId(),
                                        onSuccess = {
                                            lazyPagingItems.refresh()
                                            getPageScope().showSnackbar(
                                                message = "${follower.getDisplayName()} is now following you.",
                                                drawableRes = R.drawable.amity_ic_snack_bar_success
                                            )
                                        },
                                        onError = {
                                            lazyPagingItems.refresh()
                                            getPageScope().showSnackbar(
                                                message = "Failed to accept follow request. Please try again.",
                                                drawableRes = R.drawable.amity_ic_snack_bar_warning
                                            )
                                        }
                                    )
                                },
                                onDecline = {
                                    viewModel.declineFollow(
                                        userId = follower.getUserId(),
                                        onSuccess = {
                                            lazyPagingItems.refresh()
                                            getPageScope().showSnackbar(
                                                message = "Following request declined.",
                                                drawableRes = R.drawable.amity_ic_snack_bar_success
                                            )
                                        },
                                        onError = {
                                            lazyPagingItems.refresh()
                                            getPageScope().showSnackbar(
                                                message = "Failed to decline follow request. Please try again.",
                                                drawableRes = R.drawable.amity_ic_snack_bar_warning
                                            )
                                        }
                                    )
                                }
                            )
                            AmityNewsFeedDivider()
                        }
                    }
                }
            } else {
                AmityUserEmptyPendingFollowRequestView()
            }
        }
    }
}