package com.amity.socialcloud.uikit.community.compose.community.setting.post

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityPostSettings
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.amityStringResource
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.shade
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.setting.AmityCommunitySettingPageViewModel
import com.amity.socialcloud.uikit.community.compose.community.setting.elements.AmityCommunitySettingRadioDataItem
import com.amity.socialcloud.uikit.community.compose.community.setting.elements.AmityCommunitySettingRadioGroup
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider

@Composable
fun AmityCommunityPostPermissionPage(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
) {
    val context = LocalContext.current

    val viewModel = remember(community.getCommunityId()) {
        AmityCommunitySettingPageViewModel(community.getCommunityId())
    }

    val postPermissionEveryoneStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_permission_post_permission_everyone")
    val approvePostsStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_title_approve_member_posts")
    val postPermissionAdminStr = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_only_admin")

    val settingItems = remember(postPermissionEveryoneStr, approvePostsStr, postPermissionAdminStr) {
        listOf(
            AmityCommunitySettingRadioDataItem(
                index = 0,
                setting = AmityCommunityPostSettings.ANYONE_CAN_POST,
                title = postPermissionEveryoneStr,
            ),
            AmityCommunitySettingRadioDataItem(
                index = 1,
                setting = AmityCommunityPostSettings.ADMIN_REVIEW_POST_REQUIRED,
                title = approvePostsStr,
            ),
            AmityCommunitySettingRadioDataItem(
                index = 2,
                setting = AmityCommunityPostSettings.ADMIN_CAN_POST_ONLY,
                title = postPermissionAdminStr,
            )
        )
    }

    val initialSelection by remember(community.getCommunityId()) {
        mutableStateOf(settingItems.find { it.setting == community.getPostSettings() })
    }

    var selectedSetting by remember(initialSelection) {
        mutableStateOf(initialSelection)
    }

    val shouldAllowToSave by remember {
        derivedStateOf {
            initialSelection != selectedSetting
        }
    }

    var showLeaveConfirmDialog by remember { mutableStateOf(false) }

    BackHandler {
        showLeaveConfirmDialog = true
    }

    AmityBasePage(pageId = "community_post_permission_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            AmityToolBar(
                title = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_permission_community_setting_post_permission"),
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
                        selectedSetting?.setting?.let {
                            viewModel.updatePostSetting(
                                setting = it as AmityCommunityPostSettings,
                                onSuccess = {
                                    context.closePageWithResult(Activity.RESULT_OK)
                                    AmityUIKitSnackbar.publishSnackbarMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_snackbar_community_profile_updated"))
                                },
                                onError = {
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                        DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_snackbar_community_profile_update_failed")
                                    )
                                }
                            )
                        }
                    }
                )
            }

            Spacer(modifier.height(8.dp))
            Text(
                text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_who_can_post_on_this_community"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier.height(4.dp))
            Text(
                text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_you_can_control_who_can_create_posts_in_your_community"),
                style = AmityTheme.typography.captionLegacy.copy(
                    fontWeight = FontWeight.Normal,
                    color = AmityTheme.colors.baseShade1,
                ),
                modifier = modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier.height(8.dp))
            AmityCommunitySettingRadioGroup(
                items = settingItems,
                selectedKey = selectedSetting ?: settingItems.first(),
                setSelectedKey = {
                    selectedSetting = it
                }
            )
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