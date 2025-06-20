package com.amity.socialcloud.uikit.community.compose.community.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.sdk.model.core.invitation.AmityInvitation
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfileViewModel
import com.amity.socialcloud.uikit.community.compose.common.DeclineInvitationDialog

@Composable
fun AmityCommunityInvitationBanner(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    invitation: AmityInvitation,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommunityProfileViewModel>(viewModelStoreOwner = viewModelStoreOwner, factory =
            object : ViewModelProvider.Factory {
                override fun <VM : androidx.lifecycle.ViewModel> create(
                    modelClass: Class<VM>
                ): VM {
                    return AmityCommunityProfileViewModel(invitation.getTargetId()) as VM
                }
            }
        )
    val communityName =
        (invitation.getTarget() as? AmityInvitation.Target.Community)?.community?.getDisplayName()
            ?: "the community"
    val acceptSuccessMessage = stringResource(R.string.amity_v4_community_invitation_accept_success, communityName)
    val rejectSuccessMessage = stringResource(R.string.amity_v4_community_invitation_reject_success)
    val errorAcceptMessage = stringResource(R.string.amity_v4_community_invitation_fail_to_accept)
    val errorRejectMessage = stringResource(R.string.amity_v4_community_invitation_fail_to_reject)
    val errorUnavailableMessage = stringResource(R.string.amity_v4_community_invitation_unavailable_error)

    val requiresJoinApproval = (invitation.getTarget() as? AmityInvitation.Target.Community)?.community?.requiresJoinApproval() ?: false
    val showDeclineDialog = remember { mutableStateOf(false) }
    val onDecline = {
        viewModel.rejectCommunityInvitation(
            invitation = invitation,
            onSuccess = {
                viewModel.refresh()
                AmityUIKitSnackbar.publishSnackbarMessage(rejectSuccessMessage)
            },
            onError = { error ->
                if (AmityError.from(error) == AmityError.ITEM_NOT_FOUND) {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                        message = errorUnavailableMessage
                    )
                } else {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                        message = errorRejectMessage
                    )
                }
            },
        )
    }

    DeclineInvitationDialog(
        showDialog = showDeclineDialog.value,
        onDismiss = { showDeclineDialog.value = false },
        onConfirm = {
            showDeclineDialog.value = false
            onDecline()
        }
    )

    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = "community_invitation_banner",
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(color = AmityTheme.colors.background)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = AmityTheme.colors.backgroundShade1,
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                ) {
                    invitation.getInviterUser()?.let {
                        AmityUserAvatarView(
                            user = it,
                        )
                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )
                        Text(
                            text = "${
                                invitation.getInviterUser()?.getDisplayName() ?: "Unknown User"
                            } invited you.",
                            style = AmityTheme.typography.bodyBold,
                            fontSize = 15.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            color = AmityTheme.colors.base,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }

                }
                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    Box(
                        modifier = Modifier
                            .background(
                                color = AmityTheme.colors.primary,
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 10.dp, horizontal = 16.dp)
                            .clickableWithoutRipple {
                                viewModel.acceptCommunityInvitation(
                                    invitation = invitation,
                                    onSuccess = {
                                        viewModel.refresh()
                                        AmityUIKitSnackbar.publishSnackbarMessage(acceptSuccessMessage)
                                    },
                                    onError = { error ->
                                        if (AmityError.from(error) == AmityError.ITEM_NOT_FOUND) {
                                            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                                message = errorUnavailableMessage
                                            )
                                        } else {
                                            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                                message = errorAcceptMessage
                                            )
                                        }
                                    },
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Join",
                            style = AmityTheme.typography.bodyBold,
                            color = Color.White,
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = AmityTheme.colors.secondaryShade3,
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 10.dp, horizontal = 16.dp)
                            .clickableWithoutRipple {
                                if (requiresJoinApproval) {
                                    showDeclineDialog.value = true
                                } else {
                                    onDecline()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Decline",
                            style = AmityTheme.typography.bodyBold,
                            color = AmityTheme.colors.baseShade1,
                        )
                    }
                }
            }
        }
    }
}
