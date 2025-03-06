package com.amity.socialcloud.uikit.community.compose.user.profile.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import io.reactivex.rxjava3.schedulers.Schedulers

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityUserMenuBottomSheet(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    user: AmityUser,
    isBlockedByMe: Boolean,
    onCloseSheet: () -> Unit,
    onBlockUser: () -> Unit,
    onUnblockUser: () -> Unit,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.userProfilePageBehavior
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onCloseSheet,
        sheetState = sheetState,
        containerColor = AmityTheme.colors.background,
        contentWindowInsets = { WindowInsets.waterfall },
        modifier = modifier
            .semantics {
                testTagsAsResourceId = true
            },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 64.dp)
        ) {
            if (user.getUserId() == AmityCoreClient.getUserId()) {
                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_edit_profile,
                    text = "Edit profile",
                    modifier = modifier.testTag("bottom_sheet_edit_profile"),
                ) {
                    onCloseSheet()
                    behavior.goToEditUserPage(
                        context = context,
                    )
                }

                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_blocked_user,
                    text = "Manage blocked users",
                    modifier = modifier.testTag("bottom_sheet_manage_blocked_users"),
                ) {
                    onCloseSheet()
                    behavior.goToBlockedUsersPage(
                        context = context,
                    )
                }
            } else {
                if (user.isFlaggedByMe()) {
                    AmityBottomSheetActionItem(
                        icon = R.drawable.amity_ic_unreport,
                        text = "Unreport user",
                        modifier = modifier.testTag("bottom_sheet_unreport_user"),
                    ) {
                        AmityCoreClient.newUserRepository()
                            .unflagUser(user.getUserId())
                            .subscribeOn(Schedulers.io())
                            .doOnComplete {
                                pageScope?.showSnackbar(
                                    message = "User unreported.",
                                    drawableRes = R.drawable.amity_ic_snack_bar_success
                                )
                            }
                            .doOnError {
                                pageScope?.showSnackbar(
                                    message = "Failed to unreport user. Please try again.",
                                    drawableRes = R.drawable.amity_ic_snack_bar_warning
                                )
                            }.subscribe()

                        onCloseSheet()
                    }
                } else {
                    AmityBottomSheetActionItem(
                        icon = R.drawable.amity_ic_report1,
                        text = "Report user",
                        modifier = modifier.testTag("bottom_sheet_report_user"),
                    ) {
                        AmityCoreClient.newUserRepository()
                            .flagUser(user.getUserId())
                            .subscribeOn(Schedulers.io())
                            .doOnComplete {
                                pageScope?.showSnackbar(
                                    message = "User reported.",
                                    drawableRes = R.drawable.amity_ic_snack_bar_success
                                )
                            }
                            .doOnError {
                                pageScope?.showSnackbar(
                                    message = "Failed to report user. Please try again.",
                                    drawableRes = R.drawable.amity_ic_snack_bar_warning
                                )
                            }
                            .subscribe()

                        onCloseSheet()
                    }
                }

                if (isBlockedByMe) {
                    AmityBottomSheetActionItem(
                        icon = R.drawable.amity_ic_blocked_user,
                        text = "Unblock user",
                        modifier = modifier.testTag("bottom_sheet_unblock_user"),
                    ) {
                        onCloseSheet()
                        onUnblockUser()
                    }
                } else {
                    AmityBottomSheetActionItem(
                        icon = R.drawable.amity_ic_blocked_user,
                        text = "Block user",
                        modifier = modifier.testTag("bottom_sheet_block_user"),
                    ) {
                        onCloseSheet()
                        onBlockUser()
                    }
                }
            }
        }
    }
}

