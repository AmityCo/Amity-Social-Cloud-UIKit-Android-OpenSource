package com.amity.socialcloud.uikit.community.compose.community.setting.notifications

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.isCommentNotificationEnabled
import com.amity.socialcloud.uikit.common.utils.isPostNotificationEnabled
import com.amity.socialcloud.uikit.common.utils.isSocialNotificationEnabled
import com.amity.socialcloud.uikit.common.utils.isStoryNotificationEnabled
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.setting.elements.AmityCommunitySettingItem

@Composable
fun AmityCommunityNotificationSettingPage(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
) {
    val context = LocalContext.current

    val behavior by lazy {
        AmitySocialBehaviorHelper.communityNotificationSettingPageBehavior
    }

    val viewModel = remember(community.getCommunityId()) {
        AmityCommunityNotificationSettingPageViewModel(community.getCommunityId())
    }

    val communityNotificationSettings by remember {
        viewModel.getCommunityNotificationSettings()
    }.subscribeAsState(null)

    val userNotificationSettings by remember {
        viewModel.getUserNotificationSettings()
    }.subscribeAsState(null)

    val isNotificationEnabled by remember(userNotificationSettings) {
        derivedStateOf {
            userNotificationSettings?.isSocialNotificationEnabled() ?: false
        }
    }

    var isChecked by remember(communityNotificationSettings?.isEnabled()) {
        mutableStateOf(
            communityNotificationSettings?.isEnabled() ?: false
        )
    }

    AmityBasePage(pageId = "community_notification_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            AmityToolBar(
                title = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_notification_title_notifications"),
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
                        text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_notification_allow_notifications"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_notification_turn_on_to_receive_push_notifications_from_this_communi"),
                        style = AmityTheme.typography.captionLegacy.copy(
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.colors.baseShade1,
                        )
                    )
                }

                Switch(
                    checked = isChecked,
                    enabled = isNotificationEnabled,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = AmityTheme.colors.highlight,
                        uncheckedBorderColor = AmityTheme.colors.baseShade3,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = AmityTheme.colors.baseShade3,
                    ),
                    onCheckedChange = {
                        isChecked = !isChecked

                        viewModel.updateCommunityNotificationSetting(
                            enable = isChecked,
                            onSuccess = {
                                AmityUIKitSnackbar.publishSnackbarMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_snackbar_community_profile_updated"))
                            },
                            onError = {
                                AmityUIKitSnackbar.publishSnackbarErrorMessage(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_snackbar_community_profile_update_failed"))
                            }
                        )
                    }
                )
            }

            if (!isNotificationEnabled) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_community_notification_disabled),
                        contentDescription = null,
                        tint = AmityTheme.colors.baseShade2,
                        modifier = modifier.size(20.dp),
                    )
                    Text(
                        text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_notification_moderator_has_disabled_notification_for_this_community"),
                        style = AmityTheme.typography.captionLegacy.copy(
                            color = AmityTheme.colors.baseShade2,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                }
            }

            if (isChecked) {
                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 4.dp, bottom = 8.dp)
                )

                if (communityNotificationSettings?.isPostNotificationEnabled() == true) {
                    AmityCommunitySettingItem(
                        title = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_tab_tab_posts"),
                        titleStyle = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = modifier.padding(start = 16.dp, end = 8.dp),
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.amity_ic_community_notification_post),
                                contentDescription = "",
                                tint = AmityTheme.colors.base,
                                modifier = modifier.size(24.dp)
                            )
                        }
                    ) {
                        behavior.goToPostsNotificationSettingPage(
                            AmityCommunityNotificationSettingPageBehavior.Context(
                                pageContext = context,
                                community = community,
                            )
                        )
                    }
                }

                if (communityNotificationSettings?.isCommentNotificationEnabled() == true) {
                    AmityCommunitySettingItem(
                        title = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_title_comments"),
                        titleStyle = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = modifier.padding(start = 16.dp, end = 8.dp),
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.amity_ic_community_notification_comment),
                                contentDescription = "",
                                tint = AmityTheme.colors.base,
                                modifier = modifier.size(24.dp)
                            )
                        }
                    ) {
                        behavior.goToCommentsNotificationSettingPage(
                            AmityCommunityNotificationSettingPageBehavior.Context(
                                pageContext = context,
                                community = community,
                            )
                        )
                    }
                }

                if (communityNotificationSettings?.isStoryNotificationEnabled() == true) {
                    AmityCommunitySettingItem(
                        title = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_title_stories"),
                        titleStyle = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = modifier.padding(start = 16.dp, end = 8.dp),
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.amity_ic_community_story_setting),
                                contentDescription = "",
                                tint = AmityTheme.colors.base,
                                modifier = modifier.size(24.dp)
                            )
                        }
                    ) {
                        behavior.goToStoriesNotificationSettingPage(
                            AmityCommunityNotificationSettingPageBehavior.Context(
                                pageContext = context,
                                community = community,
                            )
                        )
                    }
                }
            }
        }
    }
}