package com.amity.socialcloud.uikit.chat.compose.setting

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityMainButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySelection
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySelectionVariant
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken

private enum class MessagingPermission {
    EVERYONE,
    MODERATORS_ONLY,
}

@Composable
fun AmityEditGroupMemberPermissionsPage(
    modifier: Modifier = Modifier,
    channelId: String,
) {
    val viewModel = remember { AmityGroupSettingPageViewModel(channelId) }
    val channel by viewModel.getChannelFlow().collectAsState(initial = null)
    val context = LocalContext.current

    val initialPermission = if (channel?.isMuted() == true)
        MessagingPermission.MODERATORS_ONLY
    else
        MessagingPermission.EVERYONE

    var selectedPermission by remember(initialPermission) { mutableStateOf(initialPermission) }
    val hasChanges = selectedPermission != initialPermission

    AmityBasePage(pageId = "edit_group_member_permission_page", useAmityToast = true) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.token(AmityColorToken.SurfaceListDefaultDefault)),
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            ) {
                AmityButton(
                    variant = AmityButtonVariant.ICON,
                    style = AmityButtonStyle.GHOST,
                    hierarchy = AmityButtonHierarchy.SECONDARY,
                    iconSize = AmityIconButtonSize.SIZE32,
                    icon = CommonR.drawable.amity_ic_chevron_left,
                    onClick = { (context as? Activity)?.finish() },
                    modifier = Modifier.align(Alignment.CenterStart),
                )

                Text(
                    text = amityChatString("chat.group.member.permissions.navbar.title"),
                    style = AmityTheme.typography.titleLegacy,
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )

                AmityButton(
                    variant = AmityButtonVariant.MAIN,
                    style = AmityButtonStyle.GHOST,
                    hierarchy = AmityButtonHierarchy.PRIMARY,
                    mainSize = AmityMainButtonSize.SM,
                    label = amityChatString("chat.group.edit.permission.save"),
                    enabled = hasChanges,
                    onClick = {
                        if (selectedPermission == MessagingPermission.MODERATORS_ONLY) {
                            viewModel.muteChannel(
                                onSuccess = {
                                    AmityUIKitSnackbar.publishSnackbarMessage(
                                        DefaultAmityChatStringProvider.getInstance().getString("chat.edit.group.perm.toast.success")
                                    )
                                    (context as? Activity)?.finish()
                                },
                                onError = {
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                        DefaultAmityChatStringProvider.getInstance().getString("chat.edit.group.perm.toast.failed")
                                    )
                                }
                            )
                        } else {
                            viewModel.unmuteChannel(
                                onSuccess = {
                                    (context as? Activity)?.finish()
                                    AmityUIKitSnackbar.publishSnackbarMessage(
                                        DefaultAmityChatStringProvider.getInstance().getString("chat.edit.group.perm.toast.success"))
                                },
                                onError = {
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                        DefaultAmityChatStringProvider.getInstance().getString("chat.edit.group.perm.toast.failed")
                                    )
                                }
                            )
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = amityChatString("chat.group.edit.permissions.messaging.title"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
                ),
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            PermissionOption(
                title = amityChatString("chat.group.edit.permissions.everyone.title"),
                description = amityChatString("chat.group.edit.permissions.everyone.description"),
                selected = selectedPermission == MessagingPermission.EVERYONE,
                onClick = { selectedPermission = MessagingPermission.EVERYONE },
            )

            Spacer(modifier = Modifier.height(8.dp))

            PermissionOption(
                title = amityChatString("chat.group.edit.permissions.moderators.only.title"),
                description = amityChatString("chat.group.edit.permissions.moderators.only.description"),
                selected = selectedPermission == MessagingPermission.MODERATORS_ONLY,
                onClick = { selectedPermission = MessagingPermission.MODERATORS_ONLY },
            )
        }
    }
}

@Composable
private fun PermissionOption(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
                ),
            )
            Text(
                text = description,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 13.sp,
                    color = AmityTheme.token(AmityColorToken.TextListTextDescriptionDefaultDefault),
                ),
            )
        }
        AmitySelection(
            variant = AmitySelectionVariant.RADIO,
            isSelected = selected,
            onChange = { _, _ -> onClick() },
        )
    }
}
