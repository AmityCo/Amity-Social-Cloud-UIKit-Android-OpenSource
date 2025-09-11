package com.amity.socialcloud.uikit.community.compose.community.profile.element

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageViewModel
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipModalSheetUIState
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipPageBehavior
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponent
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityModalSheetUIState
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageBehavior
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfileViewModel
import com.amity.socialcloud.uikit.community.compose.utils.sharePost
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.text.replace


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityCommunityModalBottomSheet(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
    behavior: AmityCommunityProfilePageBehavior,
    viewModel: AmityCommunityProfileViewModel,
) {
    val context = LocalContext.current

    val clipboardManager = LocalClipboardManager.current

    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetUIState by viewModel.sheetUIState.collectAsState()

    val showSheet by remember(viewModel) {
        derivedStateOf {
            sheetUIState != AmityCommunityModalSheetUIState.CloseSheet
        }
    }

    val domain = AmityUIKitConfigController.shareableLinkPattern?.getDomain()
    val pattern = AmityUIKitConfigController.shareableLinkPattern?.getPatterns()?.get("communities")
    val communityLink = if (domain != null && pattern != null) {
        val finalPattern = pattern.replace("{communityId}", community.getCommunityId())
        "$domain$finalPattern"
    } else { "" }

    val isModerator by AmityCoreClient.hasPermission(AmityPermission.EDIT_COMMUNITY)
        .atCommunity(community.getCommunityId())
        .check()
        .asFlow()
        .collectAsState(initial = false)

    LaunchedEffect(showSheet) {
        delay(100)

        if (!showSheet) {
            scope.launch { sheetState.hide() }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.updateSheetUIState(AmityCommunityModalSheetUIState.CloseSheet)
            },
            sheetState = sheetState,
            containerColor = AmityTheme.colors.background,
            contentWindowInsets = { WindowInsets.waterfall },
            modifier = modifier.statusBarsPadding()
        ) {
            when (sheetUIState) {
                is AmityCommunityModalSheetUIState.OpenCommunityMenuSheet -> {
                    val data =
                        sheetUIState as AmityCommunityModalSheetUIState.OpenCommunityMenuSheet
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 64.dp)
                    ) {
                        if (isModerator) {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_v4_setting_icon,
                                text = "Community settings",
                                modifier = modifier,
                            ) {
                                viewModel.updateSheetUIState(AmityCommunityModalSheetUIState.CloseSheet)
                                behavior.goToCommunitySettingPage(
                                    AmityCommunityProfilePageBehavior.Context(
                                        pageContext = context,
                                        community = data.community,
                                    )
                                )
                            }
                        } else if (community.isJoined()) {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_v4_community_info,
                                text = "Community information",
                                modifier = modifier,
                            ) {
                                viewModel.updateSheetUIState(AmityCommunityModalSheetUIState.CloseSheet)
                                behavior.goToCommunitySettingPage(
                                    AmityCommunityProfilePageBehavior.Context(
                                        pageContext = context,
                                        community = data.community,
                                    )
                                )
                            }
                        }

                        val isPrivateAndHidden = !community.isPublic() && !community.isDiscoverable()
                        val communityLink = AmityUIKitConfigController.getCommunityLink(community)
                        val isSharable = communityLink.isNotEmptyOrBlank() && (community.isPublic() || (!community.isPublic() && community.isDiscoverable()) || (isPrivateAndHidden && isModerator))

                        if (isSharable) {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_v4_link_icon,
                                text = "Copy profile link",
                                modifier = modifier.testTag("bottom_sheet_copy_link_button"),
                            ) {
                                viewModel.updateSheetUIState(AmityCommunityModalSheetUIState.CloseSheet)
                                // Generate the post link URL (adjust the URL format according to your app's deep linking structure)
                                // Copy to clipboard
                                clipboardManager.setText(AnnotatedString(communityLink))
                                AmityUIKitSnackbar.publishSnackbarMessage("Link copied")
                            }

                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_v4_share_icon,
                                text = "Share to",
                                modifier = modifier.testTag("bottom_sheet_share_to_button"),
                            ) {
                                viewModel.updateSheetUIState(AmityCommunityModalSheetUIState.CloseSheet)
                                // Open native Android share sheet
                                sharePost(context, communityLink)
                            }
                        }
                    }
                }

                AmityCommunityModalSheetUIState.CloseSheet -> {}
            }
        }
    }
}