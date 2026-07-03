package com.amity.socialcloud.uikit.chat.compose.notification

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannelNotificationMode
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityEditGroupNotificationPage(
    modifier: Modifier = Modifier,
    channelId: String,
) {
    val context = LocalContext.current
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    val viewModel: AmityEditGroupNotificationPageViewModel = viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AmityEditGroupNotificationPageViewModel(channelId) as T
            }
        }
    )

    val channel by viewModel.getChannelFlow().collectAsState(initial = null)
    val initialMode = remember(channel) { viewModel.getInitialMode(channel) }

    var selectedMode by remember(initialMode) { mutableStateOf(initialMode) }
    val hasChanges = selectedMode != initialMode

    val saveSuccessMessage = amityChatString("chat.group.notification.save.success")
    val saveErrorMessage = amityChatString("group.notification.save.error")

    AmityBasePage(pageId = "edit_group_notification_page") {
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AmityTheme.colors.background),
            ) {
                // Header with Save button
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
                        text = amityChatString("chat.group.notifications"),
                        style = AmityTheme.typography.titleLegacy,
                        modifier = Modifier
                            .padding(vertical = 17.dp)
                            .align(Alignment.Center),
                    )

                    TextButton(
                        onClick = {
                            viewModel.saveNotificationMode(
                                mode = selectedMode,
                                onSuccess = {
                                    AmityUIKitSnackbar.publishSnackbarMessage(message = saveSuccessMessage)
                                },
                                onError = {
                                    AmityUIKitSnackbar.publishSnackbarMessage(message = saveErrorMessage)
                                },
                            )
                        },
                        enabled = hasChanges,
                        modifier = Modifier.align(Alignment.CenterEnd),
                    ) {
                        Text(
                            text = amityChatString("chat.group.edit.notification.save"),
                            color = if (hasChanges) AmityTheme.colors.primary
                            else AmityTheme.colors.primary.copy(alpha = 0.4f),
                        )
                    }
                }

                // Default Mode
                GroupNotificationModeOption(
                    title = amityChatString("chat.group.notification.default.title"),
                    description = amityChatString("chat.group.notification.default.desc"),
                    isSelected = selectedMode == AmityChannelNotificationMode.DEFAULT,
                    onSelect = { selectedMode = AmityChannelNotificationMode.DEFAULT },
                )

                // Silent Mode
                GroupNotificationModeOption(
                    title = amityChatString("chat.group.notification.silent.title"),
                    description = amityChatString("chat.group.notification.silent.title"),
                    isSelected = selectedMode == AmityChannelNotificationMode.SILENT,
                    onSelect = { selectedMode = AmityChannelNotificationMode.SILENT },
                )

                // Subscribe Mode
                GroupNotificationModeOption(
                    title = amityChatString("chat.group.notification.subscribe.title"),
                    description = amityChatString("chat.group.notification.default.desc"),
                    isSelected = selectedMode == AmityChannelNotificationMode.SUBSCRIBE,
                    onSelect = { selectedMode = AmityChannelNotificationMode.SUBSCRIBE },
                )
            }
        }
    }
}

@Composable
private fun GroupNotificationModeOption(
    title: String,
    description: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
            Text(
                text = description,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 13.sp,
                    color = AmityTheme.colors.baseShade1,
                ),
            )
        }
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = AmityTheme.colors.primary,
                unselectedColor = AmityTheme.colors.baseShade2,
            ),
        )
    }
}

