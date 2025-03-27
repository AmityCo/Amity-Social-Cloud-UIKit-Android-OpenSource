package com.amity.socialcloud.uikit.community.compose.user.blocked

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserListItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityUserListShimmer

@Composable
fun AmityBlockedUsersPage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.blockedUsersPageBehavior
    }
    val viewModel = viewModel<AmityBlockedUsersPageViewModel>()

    val loadState by viewModel.blockedListState.collectAsState()
    val lazyPagingItems = remember {
        viewModel.getBlockedUsers()
    }.collectAsLazyPagingItems()

    var targetUser by remember { mutableStateOf<AmityUser?>(null) }
    var showUnblockUserDialog by remember { mutableStateOf(false) }

    AmityBasePage("blocked_users_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            AmityToolBar(
                pageScope = getPageScope(),
                onBackClick = {
                    context.closePageWithResult(Activity.RESULT_CANCELED)
                }
            )

            LazyColumn(modifier.fillMaxSize()) {
                AmityBlockedUsersPageViewModel.BlockedListState.from(
                    loadState = lazyPagingItems.loadState.refresh,
                    itemCount = lazyPagingItems.itemCount,
                ).let(viewModel::setBlockedListState)

                when (loadState) {
                    AmityBlockedUsersPageViewModel.BlockedListState.EMPTY -> {
                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(600.dp)
                                    .padding(32.dp),
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.amity_ic_empty_list),
                                    contentDescription = null,
                                    modifier = modifier.size(60.dp)
                                )
                                Spacer(modifier = modifier.size(8.dp))
                                Text(
                                    "Nothing here to see yet",
                                    style = AmityTheme.typography.titleLegacy.copy(
                                        color = AmityTheme.colors.baseShade3
                                    )
                                )
                            }
                        }
                    }

                    AmityBlockedUsersPageViewModel.BlockedListState.LOADING -> {
                        item {
                            AmityUserListShimmer(
                                modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    AmityBlockedUsersPageViewModel.BlockedListState.SUCCESS -> {
                        items(
                            count = lazyPagingItems.itemCount,
                            key = { index -> lazyPagingItems[index]?.getUserId() ?: index }
                        ) { index ->
                            val user = lazyPagingItems[index] ?: return@items

                            AmityUserListItem(
                                user = user,
                                onUserClick = {
                                    behavior.goToUserProfilePage(
                                        context = context,
                                        userId = user.getUserId(),
                                    )
                                },
                                rightMenuContent = {
                                    AmityBaseElement(
                                        pageScope = getPageScope(),
                                        elementId = "unblock_user_button"
                                    ) {
                                        Box(
                                            modifier = modifier
                                                .border(
                                                    border = BorderStroke(
                                                        width = 1.dp,
                                                        color = AmityTheme.colors.baseShade4
                                                    ),
                                                    shape = RoundedCornerShape(4.dp),
                                                )
                                                .padding(horizontal = 8.dp, vertical = 5.dp)
                                                .clickableWithoutRipple {
                                                    targetUser = user
                                                    showUnblockUserDialog = true
                                                },
                                        ) {
                                            Text(
                                                text = "Unblock",
                                                style = AmityTheme.typography.captionLegacy.copy(
                                                    fontWeight = FontWeight.SemiBold
                                                ),
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }


        if (showUnblockUserDialog && targetUser != null) {
            AmityAlertDialog(
                dialogTitle = "Unblock user?",
                dialogText = "They will now be able to see posts and comments that you’ve created. They won’t be notified that you’ve unblocked them.",
                confirmText = "Unblock",
                dismissText = "Cancel",
                confirmTextColor = AmityTheme.colors.alert,
                onConfirmation = {
                    showUnblockUserDialog = false
                    viewModel.unblockUser(
                        userId = targetUser!!.getUserId(),
                        onSuccess = {
                            getPageScope().showSnackbar(
                                message = "User unblocked.",
                                drawableRes = R.drawable.amity_ic_snack_bar_success,
                            )
                        },
                        onError = {
                            getPageScope().showSnackbar(
                                message = "Failed to unblock user. Please try again.",
                                drawableRes = R.drawable.amity_ic_snack_bar_warning,
                            )
                        }
                    )
                },
                onDismissRequest = {
                    showUnblockUserDialog = false
                }
            )
        }
    }
}