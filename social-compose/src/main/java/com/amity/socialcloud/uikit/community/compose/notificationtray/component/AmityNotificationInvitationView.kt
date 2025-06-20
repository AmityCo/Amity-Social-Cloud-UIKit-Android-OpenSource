package com.amity.socialcloud.uikit.community.compose.notificationtray.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.invitation.AmityInvitation
import com.amity.socialcloud.uikit.common.common.readableSocialTimeDiff
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.common.DeclineInvitationDialog
import com.amity.socialcloud.uikit.community.compose.notificationtray.NotificationTrayViewModel

@Composable
fun AmityNotificationInvitationView(
    modifier: Modifier = Modifier,
    isSeen: Boolean = false,
    invitation: AmityInvitation,
    onInvitationAccepted: () -> Unit = {},
    onError: (Throwable) -> Unit = {}
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<NotificationTrayViewModel>(viewModelStoreOwner = viewModelStoreOwner)

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
                onError(error)
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

    Column(modifier = modifier
        .fillMaxWidth()
        .background(
            if (isSeen) AmityTheme.colors.background else AmityTheme.colors.primaryShade3.copy(
                alpha = 0.3f
            )
        )
        .padding(16.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            invitation.getInviterUser()?.let {
                AmityUserAvatarView(
                    user = it,
                )
            }
            Spacer(Modifier.width(12.dp))
            val inviterDisplayName = invitation.getInviterUser()?.getDisplayName() ?: "Unknown User"
            val targetCommunityName =
                (invitation.getTarget() as? AmityInvitation.Target.Community)?.community?.getDisplayName()
                    ?: "Community"

            HighlightText(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.amity_v4_community_invitation_message_template, inviterDisplayName, targetCommunityName),
                templatedText = stringResource(R.string.amity_v4_community_invitation_message_template, "{{$inviterDisplayName}}", "{{$targetCommunityName}}"),
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = invitation.getCreatedAt()?.readableSocialTimeDiff() ?: "",
                style = AmityTheme.typography.caption.copy(fontSize = 13.sp),
                color = AmityTheme.colors.baseShade2
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(Modifier.width(44.dp))
            Box(modifier = Modifier
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
                            AmityUIKitSnackbar.publishSnackbarMessage(acceptSuccessMessage)
                            onInvitationAccepted()
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
                            onError(error)
                        },
                    )
                },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.amity_v4_community_invitation_accept_button),
                    style = AmityTheme.typography.bodyBold,
                    color = Color.White,
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(modifier = Modifier
                .border(
                    width = 1.dp,
                    color = AmityTheme.colors.secondaryShade3,
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 10.dp, horizontal = 16.dp)
                .clickableWithoutRipple {
                    if (requiresJoinApproval){
                        showDeclineDialog.value = true
                    } else {
                        onDecline()
                    }
                },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.amity_v4_community_invitation_reject_button),
                    style = AmityTheme.typography.bodyBold,
                    color = AmityTheme.colors.baseShade1,
                )
            }
        }
    }
}
