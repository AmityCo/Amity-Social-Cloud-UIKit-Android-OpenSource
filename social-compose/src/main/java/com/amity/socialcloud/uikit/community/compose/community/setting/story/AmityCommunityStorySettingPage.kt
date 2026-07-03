package com.amity.socialcloud.uikit.community.compose.community.setting.story

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityStorySettings
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.community.setting.AmityCommunitySettingPageViewModel
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider

@Composable
fun AmityCommunityStorySettingPage(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
) {
    val context = LocalContext.current

    var isChecked by remember { mutableStateOf(community.getStorySettings().allowComment) }

    val viewModel = remember(community.getCommunityId()) {
        AmityCommunitySettingPageViewModel(community.getCommunityId())
    }

    AmityBasePage(pageId = "community_story_setting_page") {
        Column(modifier.fillMaxSize()) {
            Spacer(modifier.height(24.dp))
            AmityToolBar(
                title = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_title_story_comments"),
                onBackClick = {
                    context.closePageWithResult(Activity.RESULT_CANCELED)
                }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = modifier.weight(1f)
                ) {
                    Text(
                        text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_allow_comments_on_community_stories"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_turn_on_to_receive_comments_on_stories_in_this_communit"),
                        style = AmityTheme.typography.captionLegacy.copy(
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.colors.baseShade1,
                        )
                    )
                }

                Switch(
                    checked = isChecked,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = AmityTheme.colors.highlight,
                        uncheckedBorderColor = AmityTheme.colors.baseShade3,
                        uncheckedThumbColor = AmityTheme.colors.baseInverse,
                        uncheckedTrackColor = AmityTheme.colors.baseShade3,
                    ),
                    onCheckedChange = {
                        isChecked = !isChecked

                        viewModel.updateStorySetting(
                            setting = AmityCommunityStorySettings(allowComment = isChecked),
                            onSuccess = {
                                // PDT-2700: a silent toggle must not surface a toast and must not
                                // navigate away — stay on the page so the user can keep toggling.
                            },
                            onError = {
                                // Revert the optimistic toggle since the update failed.
                                isChecked = !isChecked
                                AmityUIKitSnackbar.publishSnackbarErrorMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_snackbar_community_profile_update_failed"))
                            }
                        )
                    }
                )
            }
        }
    }
}