package com.amity.socialcloud.uikit.community.compose.user.profile.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityModalSheetUIState
import com.amity.socialcloud.uikit.community.compose.utils.sharePost
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.text.replace

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityUserMenuBottomSheet(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    user: AmityUser,
    isBlockedByMe: Boolean,
    isFollowedByMe: Boolean,
    onCloseSheet: () -> Unit,
    onBlockUser: () -> Unit,
    onUnblockUser: () -> Unit,
    onUnfollow: (user: AmityUser) -> Unit,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.userProfilePageBehavior
    }
    val clipboardManager = LocalClipboardManager.current

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
                .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                .navigationBarsPadding()
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
                if (isFollowedByMe) {
                    AmityBottomSheetActionItem(
                        icon = R.drawable.amity_ic_user_unfollow,
                        text = "Unfollow",
                        modifier = modifier,
                    ) {
                        onCloseSheet()
                        onUnfollow(user)
                    }
                }

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
                        if (AmityCoreClient.isVisitor()) {
                            behavior.handleVisitorUserAction()
                        } else {
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
                        }

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
                        if (AmityCoreClient.isVisitor()) {
                            behavior.handleVisitorUserAction()
                        } else {
                            onBlockUser()
                        }
                    }
                }
            }

            val userLink = AmityUIKitConfigController.getUserLink(user)
            if (userLink.isNotEmptyOrBlank()) {
                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_v4_link_icon,
                    text = "Copy profile link",
                    modifier = modifier.testTag("bottom_sheet_copy_link_button"),
                ) {
                    onCloseSheet()
                    // Generate the post link URL (adjust the URL format according to your app's deep linking structure)
                    clipboardManager.setText(AnnotatedString(userLink))
                    AmityUIKitSnackbar.publishSnackbarMessage("Link copied")
                }

                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_v4_share_icon,
                    text = "Share to",
                    modifier = modifier.testTag("bottom_sheet_share_to_button"),
                ) {
                    onCloseSheet()
                    // Open native Android share sheet
                    sharePost(context, userLink)
                }
            }
        }
    }
}

