package com.amity.socialcloud.uikit.chat.compose.notification

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannelNotificationMode
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBanner
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBannerHierarchy
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityToggle
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken

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

    AmityBasePage(pageId = "group_notification_preference_page", useAmityToast = true) {
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
                    text = amityChatString("chat.group.notif.pref.navbar.title"),
                    style = AmityTheme.typography.titleLegacy.copy(
                        color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTitleDefault),
                    ),
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )
            }

            // Silent mode banner — group notifications disabled by a moderator. Uses the Banner atom
            // (SUBDUE, centered, bell-slash) to match the chat-list "push notifications disabled"
            // banner (AmityChatListComponent.NotificationsDisabledBanner) instead of a plain text row.
            if (isSilent) {
                AmityBanner(
                    hierarchy = AmityBannerHierarchy.SUBDUE,
                    centered = true,
                    description = amityChatString("chat.group.notifications.disabled"),
                    descriptionIcon = CommonR.drawable.amity_ic_bell_slash_r,
                )
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
                            color = if (isSilent) AmityTheme.token(AmityColorToken.TextListHeaderDefaultDisabled) else AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
                        ),
                    )
                    Text(
                        text = amityChatString("chat.group.notification.preference.description"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontSize = 13.sp,
                            color = if (isSilent) AmityTheme.token(AmityColorToken.TextListTextDescriptionDefaultDefault) else AmityTheme.token(AmityColorToken.TextListTextDescriptionDefaultDefault),
                        ),
                    )
                }
                AmityToggle(
                    isOn = notificationsEnabled,
                    isDisabled = isSilent,
                    onChange = { checked ->
                        notificationsEnabled = checked
                        if (notificationsEnabled) {
                            viewModel.enableNotifications(
                                onSuccess = { },
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
                    },
                )
            }
        }
    }
}
