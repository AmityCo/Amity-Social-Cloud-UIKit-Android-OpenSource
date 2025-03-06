package com.amity.socialcloud.uikit.community.compose.user.relationship

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityTabRow
import com.amity.socialcloud.uikit.common.ui.elements.AmityTabRowItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.user.relationship.components.AmityUserFollowerListComponent
import com.amity.socialcloud.uikit.community.compose.user.relationship.components.AmityUserFollowingListComponent
import com.amity.socialcloud.uikit.community.compose.user.relationship.elements.AmityUserRelationshipActionsBottomSheet

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmityUserRelationshipPage(
    modifier: Modifier = Modifier,
    userId: String,
    selectedTab: AmityUserRelationshipPageTab,
) {
    val context = LocalContext.current

    val viewModel = viewModel<AmityUserRelationshipPageViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AmityUserRelationshipPageViewModel(userId) as T
            }
        }
    )
    val user by remember(userId) {
        viewModel.getUser()
    }.collectAsState(null)

    val tabs = remember {
        listOf(
            AmityTabRowItem(title = "Following"),
            AmityTabRowItem(title = "Followers"),
        )
    }
    var selectedTabIndex by remember(selectedTab) {
        mutableIntStateOf(if (selectedTab == AmityUserRelationshipPageTab.FOLLOWING) 0 else 1)
    }
    val pagerState = rememberPagerState { tabs.size }

    var targetUser by remember { mutableStateOf<AmityUser?>(null) }
    var showUserActionSheet by remember { mutableStateOf(false) }
    var showBlockUserDialog by remember { mutableStateOf(false) }

    LaunchedEffect(selectedTabIndex) {
        pagerState.scrollToPage(selectedTabIndex)
    }

    AmityBasePage("user_relationship_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            AmityToolBar(
                title = user?.getDisplayName() ?: "",
                onBackClick = {
                    context.closePageWithResult(Activity.RESULT_CANCELED)
                }
            )

            AmityTabRow(tabs = tabs, selectedIndex = selectedTabIndex) {
                selectedTabIndex = it
            }

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                beyondViewportPageCount = 2,
                key = { index -> tabs[index].title ?: index },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                when (index) {
                    0 -> AmityUserFollowingListComponent(
                        viewModel = viewModel,
                        onUserAction = {
                            targetUser = it
                            showUserActionSheet = true
                        }
                    )

                    1 -> AmityUserFollowerListComponent(
                        viewModel = viewModel,
                        onUserAction = {
                            targetUser = it
                            showUserActionSheet = true
                        }
                    )
                }
            }
        }

        if (showUserActionSheet && targetUser != null) {
            AmityUserRelationshipActionsBottomSheet(
                user = targetUser!!,
                allowToBlock = userId == AmityCoreClient.getUserId(),
                onDismiss = {
                    showUserActionSheet = false
                    targetUser = null
                },
                onReportUser = {
                    viewModel.reportUser(
                        userId = it.getUserId(),
                        onSuccess = {
                            getPageScope().showSnackbar(
                                message = "User reported.",
                                drawableRes = R.drawable.amity_ic_snack_bar_success
                            )
                        },
                        onError = {
                            getPageScope().showSnackbar(
                                message = "Failed to report user. Please try again.",
                                drawableRes = R.drawable.amity_ic_snack_bar_warning
                            )
                        }
                    )
                },
                onUnreportUser = {
                    viewModel.unreportUser(
                        userId = it.getUserId(),
                        onSuccess = {
                            getPageScope().showSnackbar(
                                message = "User unreported.",
                                drawableRes = R.drawable.amity_ic_snack_bar_success
                            )
                        },
                        onError = {
                            getPageScope().showSnackbar(
                                message = "Failed to unreport user. Please try again.",
                                drawableRes = R.drawable.amity_ic_snack_bar_warning
                            )
                        }
                    )
                },
                onBlockUser = {
                    targetUser = it
                    showBlockUserDialog = true
                }
            )
        }


        if (showBlockUserDialog && targetUser != null) {
            AmityAlertDialog(
                dialogTitle = "Block user?",
                dialogText = buildAnnotatedString {
                    val displayName = targetUser?.getDisplayName() ?: ""
                    append(displayName)
                    addStyle(
                        style = SpanStyle(AmityTheme.colors.base),
                        start = 0,
                        end = displayName.length,
                    )
                    append(" won't be able to see posts and comments that you've created. They won't be notified that you've blocked them.")
                },
                confirmText = "Block",
                dismissText = "Cancel",
                confirmTextColor = AmityTheme.colors.alert,
                onConfirmation = {
                    showBlockUserDialog = false

                    viewModel.blockUser(
                        userId = targetUser!!.getUserId(),
                        onSuccess = {
                            getPageScope().showSnackbar(
                                message = "User blocked.",
                                drawableRes = R.drawable.amity_ic_snack_bar_success
                            )
                        },
                        onError = {
                            getPageScope().showSnackbar(
                                message = "Failed to block user. Please try again.",
                                drawableRes = R.drawable.amity_ic_snack_bar_warning
                            )
                        }
                    )
                },
                onDismissRequest = {
                    showBlockUserDialog = false
                }
            )
        }

    }
}