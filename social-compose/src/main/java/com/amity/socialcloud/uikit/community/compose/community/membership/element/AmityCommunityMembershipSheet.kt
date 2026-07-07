package com.amity.socialcloud.uikit.community.compose.community.membership.element

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.isModerator
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipPageViewModel
import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipSheetUIState
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityCommunityMembershipSheet(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityCommunityMembershipPageViewModel,
) {
    val context = LocalContext.current

    val hasEditPermission by viewModel.hasEditPermission().subscribeAsState(initial = false)
    val hasRemovePermission by viewModel.hasRemovePermission().subscribeAsState(initial = false)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetUIState by viewModel.sheetUIState.collectAsState()

    val demoteInProgressStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_member_demote_in_progress")
    val demoteSuccessStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_member_demote_success_toast")
    val demoteFailedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_user_demoted_failed")
    val promoteInProgressStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_member_promote_in_progress")
    val promoteSuccessStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_member_promoted")
    val promoteFailedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_member_promote_failed")
    val unreportedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_member_unreported")
    val unreportFailedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_member_unreport_failed")
    val reportedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_member_reported")
    val reportFailedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_member_report_failed")
    val removingInProgressStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_member_remove_in_progress")
    val removedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_member_removed_toast")
    val removeFailedStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_member_remove_failed")


    val showSheet by remember(viewModel) {
        derivedStateOf {
            sheetUIState != AmityCommunityMembershipSheetUIState.CloseSheet
        }
    }


    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.updateSheetUIState(AmityCommunityMembershipSheetUIState.CloseSheet)
            },
            sheetState = sheetState,
            containerColor = AmityTheme.colors.background,
            contentWindowInsets = { WindowInsets.waterfall },
        ) {
            when (sheetUIState) {
                is AmityCommunityMembershipSheetUIState.OpenSheet -> {
                    val data = sheetUIState as AmityCommunityMembershipSheetUIState.OpenSheet
                    val member = data.member

                    val isFlaggedByMe = member.getUser()?.isFlaggedByMe() ?: false

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = modifier.padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                    ) {
                        if (hasEditPermission) {
                            if (member.isModerator()) {
                                AmityBottomSheetActionItem(
                                    icon = R.drawable.amity_ic_demote_moderator,
                                    text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_demote_to_member"),
                                    modifier = modifier.testTag(""),
                                ) {
                                    viewModel.updateSheetUIState(
                                        AmityCommunityMembershipSheetUIState.CloseSheet
                                    )

                                    viewModel.demoteModerator(
                                        userId = member.getUserId(),
                                        onDemotingStarted = {
                                            // Surfaces the "Demoting…" progress snackbar while we
                                            // wait for the backend to reindex the role change.
                                            pageScope?.showProgressSnackbar(demoteInProgressStr)
                                        },
                                        onSuccess = {
                                            pageScope?.dismissSnackbar()
                                            pageScope?.showSnackbar(
                                                message = demoteSuccessStr,
                                                drawableRes = R.drawable.amity_ic_snack_bar_success,
                                            )
                                        },
                                        onError = {
                                            pageScope?.dismissSnackbar()
                                            pageScope?.showSnackbar(
                                                message = demoteFailedStr,
                                                drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                            )
                                        }
                                    )
                                }
                            } else {
                                AmityBottomSheetActionItem(
                                    icon = R.drawable.amity_ic_promote_moderator,
                                    text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_promote_to_moderator"),
                                    modifier = modifier.testTag(""),
                                ) {
                                    viewModel.updateSheetUIState(
                                        AmityCommunityMembershipSheetUIState.CloseSheet
                                    )

                                    viewModel.promoteModerator(
                                        userId = member.getUserId(),
                                        onPromotingStarted = {
                                            // Surfaces the "Promoting…" progress snackbar while we
                                            // wait for the backend to reindex the role change.
                                            pageScope?.showProgressSnackbar(promoteInProgressStr)
                                        },
                                        onSuccess = {
                                            pageScope?.dismissSnackbar()
                                            pageScope?.showSnackbar(
                                                message = promoteSuccessStr,
                                                drawableRes = R.drawable.amity_ic_snack_bar_success,
                                            )
                                        },
                                        onError = {
                                            pageScope?.dismissSnackbar()
                                            pageScope?.showSnackbar(
                                                message = promoteFailedStr,
                                                drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        if (isFlaggedByMe) {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_ic_unreport,
                                text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_unreport_user"),
                                modifier = modifier.testTag(""),
                            ) {
                                viewModel.updateSheetUIState(AmityCommunityMembershipSheetUIState.CloseSheet)
                                viewModel.unflagUser(
                                    userId = member.getUserId(),
                                    onSuccess = {
                                        pageScope?.showSnackbar(
                                            message = unreportedStr,
                                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                                        )
                                    },
                                    onError = {
                                        pageScope?.showSnackbar(
                                            message = unreportFailedStr,
                                            drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                        )
                                    }
                                )
                            }
                        } else {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_ic_report1,
                                text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_report_user"),
                                modifier = modifier.testTag(""),
                            ) {
                                viewModel.updateSheetUIState(AmityCommunityMembershipSheetUIState.CloseSheet)
                                viewModel.flagUser(
                                    userId = member.getUserId(),
                                    onSuccess = {
                                        pageScope?.showSnackbar(
                                            message = reportedStr,
                                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                                        )
                                    },
                                    onError = {
                                        pageScope?.showSnackbar(
                                            message = reportFailedStr,
                                            drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                        )
                                    }
                                )
                            }
                        }

                        if (hasRemovePermission) {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_ic_delete1,
                                text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_remove_from_community"),
                                color = AmityTheme.colors.alert,
                                modifier = modifier.testTag(""),
                            ) {
                                viewModel.updateSheetUIState(AmityCommunityMembershipSheetUIState.CloseSheet)
                                viewModel.removeMember(
                                    userId = member.getUserId(),
                                    onRemovingStarted = {
                                        // Surfaces the "Removing member…" progress snackbar while we
                                        // wait for the backend to deindex the member.
                                        pageScope?.showProgressSnackbar(removingInProgressStr)
                                    },
                                    onSuccess = {
                                        pageScope?.dismissSnackbar()
                                        pageScope?.showSnackbar(
                                            message = removedStr,
                                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                                        )
                                    },
                                    onError = {
                                        pageScope?.dismissSnackbar()
                                        pageScope?.showSnackbar(
                                            message = removeFailedStr,
                                            drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                AmityCommunityMembershipSheetUIState.CloseSheet -> {}
            }
        }
    }
}