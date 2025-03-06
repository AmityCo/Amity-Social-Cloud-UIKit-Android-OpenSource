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
                                    text = "Demote to member",
                                    modifier = modifier.testTag(""),
                                ) {
                                    viewModel.updateSheetUIState(
                                        AmityCommunityMembershipSheetUIState.CloseSheet
                                    )

                                    viewModel.demoteModerator(
                                        userId = member.getUserId(),
                                        onSuccess = {
                                            pageScope?.showSnackbar(
                                                message = "Successfully demoted to member!",
                                                drawableRes = R.drawable.amity_ic_snack_bar_success,
                                            )
                                        },
                                        onError = {
                                            pageScope?.showSnackbar(
                                                message = "Failed to demote member. Please try again.",
                                                drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                            )
                                        }
                                    )
                                }
                            } else {
                                AmityBottomSheetActionItem(
                                    icon = R.drawable.amity_ic_promote_moderator,
                                    text = "Promote to moderator",
                                    modifier = modifier.testTag(""),
                                ) {
                                    viewModel.updateSheetUIState(
                                        AmityCommunityMembershipSheetUIState.CloseSheet
                                    )

                                    viewModel.promoteModerator(
                                        userId = member.getUserId(),
                                        onSuccess = {
                                            pageScope?.showSnackbar(
                                                message = "Successfully promoted to moderator!",
                                                drawableRes = R.drawable.amity_ic_snack_bar_success,
                                            )
                                        },
                                        onError = {
                                            pageScope?.showSnackbar(
                                                message = "Failed to promote member. Please try again.",
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
                                text = "Unreport member",
                                modifier = modifier.testTag(""),
                            ) {
                                viewModel.updateSheetUIState(AmityCommunityMembershipSheetUIState.CloseSheet)
                                viewModel.unflagUser(
                                    userId = member.getUserId(),
                                    onSuccess = {
                                        pageScope?.showSnackbar(
                                            message = "Member unreported.",
                                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                                        )
                                    },
                                    onError = {
                                        pageScope?.showSnackbar(
                                            message = "Failed to unreport member. Please try again.",
                                            drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                        )
                                    }
                                )
                            }
                        } else {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_ic_report1,
                                text = "Report member",
                                modifier = modifier.testTag(""),
                            ) {
                                viewModel.updateSheetUIState(AmityCommunityMembershipSheetUIState.CloseSheet)
                                viewModel.flagUser(
                                    userId = member.getUserId(),
                                    onSuccess = {
                                        pageScope?.showSnackbar(
                                            message = "Member reported",
                                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                                        )
                                    },
                                    onError = {
                                        pageScope?.showSnackbar(
                                            message = "Failed to report member. Please try again.",
                                            drawableRes = R.drawable.amity_ic_snack_bar_warning,
                                        )
                                    }
                                )
                            }
                        }

                        if (hasRemovePermission) {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_ic_delete1,
                                text = "Remove from community",
                                color = AmityTheme.colors.alert,
                                modifier = modifier.testTag(""),
                            ) {
                                viewModel.updateSheetUIState(AmityCommunityMembershipSheetUIState.CloseSheet)
                                viewModel.removeMember(
                                    userId = member.getUserId(),
                                    onSuccess = {
                                        pageScope?.showSnackbar(
                                            message = "Member removed from this community.",
                                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                                        )
                                    },
                                    onError = {
                                        pageScope?.showSnackbar(
                                            message = "Failed to remove member. Please try again.",
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