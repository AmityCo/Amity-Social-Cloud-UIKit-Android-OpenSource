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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

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

    AmityBasePage(pageId = "edit_group_member_permission_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background),
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterStart)
                        .clickableWithoutRipple {
                            (context as? Activity)?.finish()
                        },
                    tint = AmityTheme.colors.base,
                )

                Text(
                    text = amityChatString("chat.group.member.permissions.navbar.title"),
                    style = AmityTheme.typography.titleLegacy,
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )

                TextButton(
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
                    enabled = hasChanges,
                    modifier = Modifier.align(Alignment.CenterEnd),
                ) {
                    Text(
                        text = amityChatString("chat.group.edit.permission.save"),
                        color = if (hasChanges) AmityTheme.colors.primary
                        else AmityTheme.colors.primary.copy(alpha = 0.4f),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = amityChatString("chat.group.edit.permissions.messaging.title"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
            Text(
                text = description,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 14.sp,
                    color = AmityTheme.colors.baseShade1,
                ),
            )
        }
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = AmityTheme.colors.primary,
            ),
        )
    }
}
