package com.amity.socialcloud.uikit.community.compose.community.setting

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.common.utils.isCommentNotificationEnabled
import com.amity.socialcloud.uikit.common.utils.isPostNotificationEnabled
import com.amity.socialcloud.uikit.common.utils.isSocialNotificationEnabled
import com.amity.socialcloud.uikit.common.utils.isStoryNotificationEnabled
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.setting.elements.AmityCommunitySettingItem

@Composable
fun AmityCommunitySettingPage(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val behavior by lazy {
        AmitySocialBehaviorHelper.communitySettingPageBehavior
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            context.closePageWithResult(Activity.RESULT_OK)
        }
    }

    var shouldRefresh by remember { mutableStateOf(false) }

    val viewModel = remember(community.getCommunityId(), community.getUpdatedAt()) {
        AmityCommunitySettingPageViewModel(community.getCommunityId())
    }
    val uiState by viewModel.uiState.collectAsState()

    val communityVM by remember(shouldRefresh) {
        viewModel.getCommunity()
    }.subscribeAsState(null)

    val communityNotificationSettings by remember(shouldRefresh) {
        viewModel.getCommunityNotificationSettings()
    }.subscribeAsState(null)

    val userNotificationSettings by remember(shouldRefresh) {
        viewModel.getUserNotificationSettings()
    }.subscribeAsState(null)

    val hasEditPermission by viewModel.hasEditPermission().subscribeAsState(initial = false)
    val hasDeletePermission by viewModel.hasDeletePermission().subscribeAsState(initial = false)
    val hasReviewPermission by viewModel.hasReviewPermission().subscribeAsState(initial = false)

    var showLeaveCommunityDialog by remember { mutableStateOf(false) }
    var showCloseCommunityDialog by remember { mutableStateOf(false) }
    var showUnableToLeaveCommunityDialog by remember { mutableStateOf(false) }
    var showUnableToCloseCommunityDialog by remember { mutableStateOf(false) }
    var showUserIsLastModeratorDialog by remember { mutableStateOf(false) }
    var showUserIsLastMemberDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (uiState) {
            AmityCommunitySettingUIState.ShowConfirmModeratorLeaveDialog,
            AmityCommunitySettingUIState.ShowConfirmUserLeaveDialog -> {
                showLeaveCommunityDialog = true
            }

            AmityCommunitySettingUIState.ShowUserIsLastModeratorDialog -> {
                showUserIsLastModeratorDialog = true
            }

            AmityCommunitySettingUIState.ShowUserIsLastMemberDialog -> {
                showUserIsLastMemberDialog = true
            }

            AmityCommunitySettingUIState.ShowUserUnableToLeaveDialog -> {
                showUnableToLeaveCommunityDialog = true
            }

            AmityCommunitySettingUIState.CloseCommunitySuccess -> {
                context.closePage()
            }

            is AmityCommunitySettingUIState.CloseCommunityFailed -> {
                val error = (uiState as AmityCommunitySettingUIState.CloseCommunityFailed).error
                AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to close community. Please try again later.")
                showUnableToCloseCommunityDialog = true
            }

            AmityCommunitySettingUIState.LeaveCommunitySuccess -> {
                context.closePage()
            }

            is AmityCommunitySettingUIState.LeaveCommunityFailed -> {
                val error = (uiState as AmityCommunitySettingUIState.LeaveCommunityFailed).error

                when (error) {
                    AmityError.USER_IS_LAST_MODERATOR -> {
                        viewModel.updateUIState(AmityCommunitySettingUIState.ShowUserIsLastModeratorDialog)
                    }

                    AmityError.USER_IS_LAST_MEMBER -> {
                        viewModel.updateUIEvent(AmityCommunitySettingUIEvent.ConfirmLastMemberLeaveCommunity)
                    }

                    else -> {
                        viewModel.updateUIState(AmityCommunitySettingUIState.ShowUserUnableToLeaveDialog)
                    }
                }
            }

            AmityCommunitySettingUIState.Initial -> {}
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            shouldRefresh = when (event) {
                Lifecycle.Event.ON_RESUME -> true
                else -> false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    AmityBasePage(pageId = "community_setting_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.amity_ic_back),
                    contentDescription = "Close",
                    tint = AmityTheme.colors.base,
                    modifier = modifier
                        .size(24.dp)
                        .align(Alignment.CenterStart)
                        .clickableWithoutRipple {
                            context.closePageWithResult(Activity.RESULT_CANCELED)
                        }
                )

                Text(
                    text = communityVM?.getDisplayName() ?: "",
                    style = AmityTheme.typography.titleLegacy,
                    modifier = modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier.height(4.dp))

            Text(
                text = "Basic info",
                style = AmityTheme.typography.titleLegacy,
                modifier = modifier.padding(vertical = 12.dp)
            )

            if (hasEditPermission) {
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "edit_profile"
                ) {
                    AmityCommunitySettingItem(
                        modifier = modifier.testTag(getAccessibilityId()),
                        title = getConfig().getText(),
                        icon = {
                            Box(
                                modifier = modifier
                                    .size(28.dp)
                                    .background(
                                        color = AmityTheme.colors.baseShade4,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.amity_ic_community_edit),
                                    contentDescription = "",
                                    tint = AmityTheme.colors.base,
                                    modifier = modifier.align(Alignment.Center)
                                )
                            }
                        }
                    ) {
                        behavior.goToEditCommunityPage(
                            AmityCommunitySettingPageBehavior.Context(
                                pageContext = context,
                                activityLauncher = launcher,
                                community = communityVM,
                            )
                        )
                    }
                }
            }

            AmityBaseElement(
                pageScope = getPageScope(),
                elementId = "members"
            ) {
                AmityCommunitySettingItem(
                    modifier = modifier.testTag(getAccessibilityId()),
                    title = getConfig().getText(),
                    icon = {
                        Box(
                            modifier = modifier
                                .size(28.dp)
                                .background(
                                    color = AmityTheme.colors.baseShade4,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.amity_ic_community_members),
                                contentDescription = "",
                                tint = AmityTheme.colors.base,
                                modifier = modifier.align(Alignment.Center)
                            )
                        }
                    }
                ) {
                    behavior.goToMembershipPage(
                        AmityCommunitySettingPageBehavior.Context(
                            pageContext = context,
                            activityLauncher = launcher,
                            community = communityVM,
                        )
                    )
                }
            }

            val isNotificationEnabled =
                userNotificationSettings?.isSocialNotificationEnabled() == true
                        && (communityNotificationSettings?.isPostNotificationEnabled() == true ||
                        communityNotificationSettings?.isCommentNotificationEnabled() == true ||
                        communityNotificationSettings?.isStoryNotificationEnabled() == true)

            if (isNotificationEnabled) {
                Box(modifier = modifier.fillMaxWidth()) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "notifications"
                    ) {
                        AmityCommunitySettingItem(
                            modifier = modifier.testTag(getAccessibilityId()),
                            title = getConfig().getText(),
                            icon = {
                                Box(
                                    modifier = modifier
                                        .size(28.dp)
                                        .background(
                                            color = AmityTheme.colors.baseShade4,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.amity_ic_community_notifications),
                                        contentDescription = "",
                                        tint = AmityTheme.colors.base,
                                        modifier = modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        ) {
                            behavior.goToNotificationPage(
                                AmityCommunitySettingPageBehavior.Context(
                                    pageContext = context,
                                    activityLauncher = launcher,
                                    community = communityVM,
                                )
                            )
                        }
                    }

                    Text(
                        text = "On",
                        style = AmityTheme.typography.bodyLegacy.copy(
                            color = AmityTheme.colors.baseShade1,
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .offset((-18).dp, (-4).dp)
                    )
                }
            }

            HorizontalDivider(
                color = AmityTheme.colors.divider,
                modifier = modifier.padding(top = 4.dp, bottom = 8.dp)
            )

            if (hasReviewPermission || hasEditPermission) {
                Text(
                    text = "Community permissions",
                    style = AmityTheme.typography.titleLegacy,
                    modifier = modifier.padding(vertical = 12.dp)
                )
            }

            if (hasReviewPermission) {
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "post_permission"
                ) {
                    AmityCommunitySettingItem(
                        modifier = modifier.testTag(getAccessibilityId()),
                        title = getConfig().getText(),
                        icon = {
                            Box(
                                modifier = modifier
                                    .size(28.dp)
                                    .background(
                                        color = AmityTheme.colors.baseShade4,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.amity_ic_community_post_setting),
                                    contentDescription = "",
                                    tint = AmityTheme.colors.base,
                                    modifier = modifier.align(Alignment.Center)
                                )
                            }
                        }
                    ) {
                        behavior.goToPostPermissionPage(
                            AmityCommunitySettingPageBehavior.Context(
                                pageContext = context,
                                activityLauncher = launcher,
                                community = communityVM,
                            )
                        )
                    }
                }
            }

            if (hasEditPermission) {
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "story_setting"
                ) {
                    AmityCommunitySettingItem(
                        modifier = modifier.testTag(getAccessibilityId()),
                        title = getConfig().getText(),
                        icon = {
                            Box(
                                modifier = modifier
                                    .size(28.dp)
                                    .background(
                                        color = AmityTheme.colors.baseShade4,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.amity_ic_community_story_setting),
                                    contentDescription = "",
                                    tint = AmityTheme.colors.base,
                                    modifier = modifier.align(Alignment.Center)
                                )
                            }
                        }
                    ) {
                        behavior.goToStorySettingPage(
                            AmityCommunitySettingPageBehavior.Context(
                                pageContext = context,
                                activityLauncher = launcher,
                                community = communityVM,
                            )
                        )
                    }
                }
            }

            if(communityVM?.isJoined() == true) {
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "leave_community"
                ) {
                    Text(
                        text = getConfig().getText(),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            color = AmityTheme.colors.alert,
                            fontWeight = FontWeight.SemiBold,
                        ),
                        modifier = modifier
                            .padding(vertical = 12.dp, horizontal = 8.dp)
                            .testTag(getAccessibilityId())
                            .clickableWithoutRipple {
                                viewModel.updateUIEvent(
                                    if (hasDeletePermission) {
                                        AmityCommunitySettingUIEvent.ConfirmModeratorLeaveCommunity
                                    } else {
                                        AmityCommunitySettingUIEvent.ConfirmUserLeaveCommunity
                                    }
                                )
                            }
                    )

                    HorizontalDivider(
                        color = AmityTheme.colors.divider,
                        modifier = modifier.padding(top = 4.dp, bottom = 8.dp)
                    )
                }


            }

            Spacer(modifier = modifier.height(8.dp))

            if (hasDeletePermission) {
                Column(
                    modifier.clickableWithoutRipple {
                        showCloseCommunityDialog = true
                    }
                ) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "close_community"
                    ) {
                        Text(
                            text = getConfig().getText(),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = AmityTheme.colors.alert,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            modifier = modifier
                                .padding(horizontal = 8.dp)
                                .testTag(getAccessibilityId())
                        )
                    }

                    Spacer(modifier = modifier.height(6.dp))

                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "close_community_description"
                    ) {
                        Text(
                            text = getConfig().getText(),
                            style = AmityTheme.typography.captionLegacy.copy(
                                fontWeight = FontWeight.Normal,
                                color = AmityTheme.colors.baseShade1,
                            ),
                            modifier = modifier
                                .padding(horizontal = 8.dp)
                                .testTag(getAccessibilityId())
                        )
                    }

                    Spacer(modifier = modifier.height(12.dp))

                    HorizontalDivider(
                        color = AmityTheme.colors.divider,
                        modifier = modifier.padding(top = 4.dp, bottom = 8.dp)
                    )
                }
            }
        }


        if (showLeaveCommunityDialog) {
            AmityAlertDialog(
                dialogTitle = "Leave community",
                dialogText = "Leave the community. You will no longer be able to post and interact in this community.",
                confirmText = "Leave",
                dismissText = "Cancel",
                confirmTextColor = AmityTheme.colors.alert,
                onConfirmation = {
                    showLeaveCommunityDialog = false
                    viewModel.updateUIEvent(AmityCommunitySettingUIEvent.ProceedLeavingCommunity)
                },
                onDismissRequest = {
                    showLeaveCommunityDialog = false
                }
            )
        }

        if (showCloseCommunityDialog) {
            AmityAlertDialog(
                dialogTitle = "Close community?",
                dialogText = "All members will be removed from the community. All posts, messages, reactions, and media shared in community will be deleted. This cannot be undone.",
                confirmText = "Confirm",
                dismissText = "Cancel",
                confirmTextColor = AmityTheme.colors.alert,
                onConfirmation = {
                    showCloseCommunityDialog = false
                    viewModel.updateUIEvent(AmityCommunitySettingUIEvent.ProceedClosingCommunity)
                },
                onDismissRequest = {
                    showCloseCommunityDialog = false
                }
            )
        }

        if (showUserIsLastModeratorDialog) {
            AmityAlertDialog(
                dialogTitle = "Unable to leave community",
                dialogText = "You're the only moderator in this group. To leave community, nominate other members to moderator role.",
                dismissText = "OK",
                onDismissRequest = {
                    showUserIsLastModeratorDialog = false
                }
            )
        }

        if (showUserIsLastMemberDialog) {
            AmityAlertDialog(
                dialogTitle = "Leave community?",
                dialogText = "As you're the last moderator and member, leaving will also close this community. All posts shared in community will be deleted. This cannot be undone.",
                confirmText = "Close",
                confirmTextColor = AmityTheme.colors.alert,
                dismissText = "Cancel",
                onConfirmation = {
                    showUserIsLastMemberDialog = false
                    viewModel.updateUIEvent(AmityCommunitySettingUIEvent.ProceedClosingCommunity)
                },
                onDismissRequest = {
                    showUserIsLastMemberDialog = false
                }
            )
        }

        if (showUnableToLeaveCommunityDialog) {
            AmityAlertDialog(
                dialogTitle = "Unable to leave community",
                dialogText = "Something went wrong. Please try again later.",
                dismissText = "OK",
                onDismissRequest = {
                    showUnableToLeaveCommunityDialog = false
                }
            )
        }

        if (showUnableToCloseCommunityDialog) {
            AmityAlertDialog(
                dialogTitle = "Unable to close community",
                dialogText = "Something went wrong. Please try again later.",
                dismissText = "OK",
                onDismissRequest = {
                    showUnableToCloseCommunityDialog = false
                }
            )
        }
    }
}