package com.amity.socialcloud.uikit.community.compose.community.setting.notifications.posts

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.notification.AmityCommunityNotificationEvent
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.amityStringResource
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.getEnabledPostNotificationSettings
import com.amity.socialcloud.uikit.common.utils.shade
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.AmityCommunityNotificationSettingDataType
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.AmityCommunityNotificationSettingPageViewModel
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.elements.AmityCommunityNotificationSettingMenuItem
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.getSettingDataType
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider

@Composable
fun AmityCommunityPostsNotificationSettingPage(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
) {
    val context = LocalContext.current

    val viewModel = remember(community.getCommunityId()) {
        AmityCommunityNotificationSettingPageViewModel(community.getCommunityId())
    }

    val communityNotificationSettings by remember {
        viewModel.getCommunityNotificationSettings()
    }.subscribeAsState(null)

    val settings by remember(communityNotificationSettings?.getEnabledPostNotificationSettings()?.size) {
        derivedStateOf {
            communityNotificationSettings?.getEnabledPostNotificationSettings()
        }
    }

    var newPostDefaultSetting by remember {
        mutableStateOf(AmityCommunityNotificationSettingDataType.OFF)
    }
    var newPostSetting by remember(newPostDefaultSetting) {
        mutableStateOf(newPostDefaultSetting)
    }

    var reactPostDefaultSetting by remember {
        mutableStateOf(AmityCommunityNotificationSettingDataType.OFF)
    }
    var reactPostSetting by remember(reactPostDefaultSetting) {
        mutableStateOf(reactPostDefaultSetting)
    }

    var showLeaveConfirmDialog by remember { mutableStateOf(false) }

    val shouldAllowToSave by remember(newPostSetting, reactPostSetting) {
        derivedStateOf {
            newPostSetting != newPostDefaultSetting || reactPostSetting != reactPostDefaultSetting
        }
    }

    BackHandler {
        showLeaveConfirmDialog = true
    }

    AmityBasePage(pageId = "community_posts_notification_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            AmityToolBar(
                title = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_title_posts"),
                onBackClick = {
                    showLeaveConfirmDialog = true
                }
            ) {
                Text(
                    text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_edit_user_save_button"),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        color = if (shouldAllowToSave) AmityTheme.colors.highlight
                        else AmityTheme.colors.highlight.shade(AmityColorShade.SHADE2),
                    ),
                    modifier = modifier.clickableWithoutRipple(shouldAllowToSave) {
                        viewModel.updatePostNotificationSettings(
                            newPostSetting = newPostSetting,
                            newPostDefaultSetting = newPostDefaultSetting,
                            reactPostSetting = reactPostSetting,
                            reactPostDefaultSetting = reactPostDefaultSetting,
                            onSuccess = {
                                AmityUIKitSnackbar.publishSnackbarMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_snackbar_community_profile_updated"))
                                context.closePage()
                            },
                            onError = {
                                AmityUIKitSnackbar.publishSnackbarErrorMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_snackbar_community_profile_update_failed"))
                            }
                        )
                    }
                )
            }

            LazyColumn {
                item {
                    val setting =
                        settings?.filterIsInstance<AmityCommunityNotificationEvent.POST_REACTED>()
                            ?.firstOrNull() ?: return@item

                    reactPostDefaultSetting = setting.getSettingDataType()

                    AmityCommunityNotificationSettingMenuItem(
                        title = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_title_react_posts"),
                        description = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_notif_post_reaction"),
                        selectedSetting = reactPostSetting
                    ) {
                        reactPostSetting = it
                    }
                }

                item {
                    val count = settings?.size ?: 0
                    if (count > 1) {
                        HorizontalDivider(
                            color = AmityTheme.colors.divider,
                            modifier = modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp)
                        )
                    }
                }

                item {
                    val setting =
                        settings?.filterIsInstance<AmityCommunityNotificationEvent.POST_CREATED>()
                            ?.firstOrNull() ?: return@item

                    newPostDefaultSetting = setting.getSettingDataType()

                    AmityCommunityNotificationSettingMenuItem(
                        title = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_title_new_posts"),
                        description = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_notification_notif_new_post"),
                        selectedSetting = newPostSetting
                    ) {
                        newPostSetting = it
                    }
                }
            }
        }

        if (showLeaveConfirmDialog) {
            if (shouldAllowToSave) {
                AmityAlertDialog(
                    dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_community_dialog_leave_title"),
                    dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_community_dialog_leave_description"),
                    confirmText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_leave_button"),
                    dismissText = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_modal_dialog_cancel_button"),
                    confirmTextColor = AmityTheme.colors.alert,
                    onConfirmation = {
                        context.closePage()
                    },
                    onDismissRequest = {
                        showLeaveConfirmDialog = false
                    }
                )
            } else {
                context.closePage()
            }
        }
    }
}