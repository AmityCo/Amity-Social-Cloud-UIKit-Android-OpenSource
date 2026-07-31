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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannelNotificationMode
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

    AmityBasePage(pageId = "edit_group_notification_page", useAmityToast = true) {
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AmityTheme.token(AmityColorToken.SurfaceSheetsBackgroundGeneral)),
            ) {
                // Header with Save button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                ) {
                    AmityButton(
                        variant = AmityButtonVariant.ICON,
                        modifier = Modifier.align(Alignment.CenterStart),
                        onClick = { (context as? Activity)?.finish() },
                        hierarchy = AmityButtonHierarchy.SECONDARY,
                        style = AmityButtonStyle.GHOST,
                        iconSize = AmityIconButtonSize.SIZE32,
                        icon = CommonR.drawable.amity_ic_chevron_left,
                    )

                    Text(
                        text = amityChatString("chat.group.notifications"),
                        style = AmityTheme.typography.titleLegacy.copy(
                            color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTitleDefault),
                        ),
                        modifier = Modifier
                            .padding(vertical = 17.dp)
                            .align(Alignment.Center),
                    )

                    AmityButton(
                        variant = AmityButtonVariant.MAIN,
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = {
                            viewModel.saveNotificationMode(
                                mode = selectedMode,
                                onSuccess = {
                                    AmityUIKitSnackbar.publishSnackbarMessage(message = saveSuccessMessage)
                                    // Pop back to group settings — the toast shows on the resumed page.
                                    (context as? Activity)?.finish()
                                },
                                onError = {
                                    AmityUIKitSnackbar.publishSnackbarMessage(message = saveErrorMessage)
                                },
                            )
                        },
                        hierarchy = AmityButtonHierarchy.PRIMARY,
                        style = AmityButtonStyle.GHOST,
                        mainSize = AmityMainButtonSize.SM,
                        label = amityChatString("chat.group.edit.notification.save"),
                        enabled = hasChanges,
                    )
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
                    description = amityChatString("chat.group.notification.silent.desc"),
                    isSelected = selectedMode == AmityChannelNotificationMode.SILENT,
                    onSelect = { selectedMode = AmityChannelNotificationMode.SILENT },
                )

                // Subscribe Mode
                GroupNotificationModeOption(
                    title = amityChatString("chat.group.notification.subscribe.title"),
                    description = amityChatString("chat.group.notification.subscribe.desc"),
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
            isSelected = isSelected,
            onChange = { _, _ -> onSelect() },
        )
    }
}

