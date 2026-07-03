package com.amity.socialcloud.uikit.chat.compose.notification

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannelNotificationMode
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityGroupNotificationPreferencePage(
    modifier: Modifier = Modifier,
    channelId: String,
) {
    val context = LocalContext.current
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    val viewModel: AmityGroupNotificationPreferencePageViewModel = viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AmityGroupNotificationPreferencePageViewModel(channelId) as T
            }
        }
    )

    val channel by viewModel.getChannelFlow().collectAsState(initial = null)
    val notificationsEnabledOnServer by viewModel.notificationsEnabled.collectAsState()

    // Fetch notification settings on first composition
    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.refreshNotificationState()
    }

    val isSilent = channel?.getNotificationMode() == AmityChannelNotificationMode.SILENT.apiKey
    var notificationsEnabled by remember(notificationsEnabledOnServer) { mutableStateOf(notificationsEnabledOnServer) }
    val errorUpdateNotification = amityChatString("chat.error.update.notification")

    AmityBasePage(pageId = "group_notification_preference_page") {
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
                    text = amityChatString("chat.group.notif.pref.navbar.title"),
                    style = AmityTheme.typography.titleLegacy,
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )
            }

            // Silent mode banner
            if (isSilent) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AmityTheme.colors.backgroundShade1)
                        .padding(16.dp),
                ) {
                    Text(
                        text = amityChatString("chat.group.notifications.disabled"),
                        style = AmityTheme.typography.captionLegacy.copy(
                            color = AmityTheme.colors.baseShade3,
                        ),
                    )
                }
            }

            // Personal notification toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = amityChatString("chat.group.notification.preference.title"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSilent) AmityTheme.colors.baseShade3 else AmityTheme.colors.base,
                        ),
                    )
                    Text(
                        text = amityChatString("chat.group.notification.preference.description"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 13.sp,
                            color = if (isSilent) AmityTheme.colors.baseShade3 else AmityTheme.colors.baseShade1,
                        ),
                    )
                }
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = {
                        if (isSilent) {
                            null
                        } else {
                            notificationsEnabled = it
                            if (notificationsEnabled) {
                                viewModel.enableNotifications(
                                    onSuccess = {  },
                                    onError = { err ->
                                        AmityUIKitSnackbar.publishSnackbarErrorMessage(errorUpdateNotification)
                                         }
                                )
                            } else {
                                viewModel.disableNotifications(
                                    onSuccess = { },
                                    onError = { err ->
                                        AmityUIKitSnackbar.publishSnackbarErrorMessage(errorUpdateNotification)
                                    }
                                )
                            }
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = AmityTheme.colors.background,
                        checkedTrackColor = if (isSilent) AmityTheme.colors.primaryShade3 else AmityTheme.colors.primary,
                        uncheckedTrackColor = if (isSilent) AmityTheme.colors.baseShade4 else AmityTheme.colors.baseShade3,
                    ),
                )
            }
        }
    }
}
