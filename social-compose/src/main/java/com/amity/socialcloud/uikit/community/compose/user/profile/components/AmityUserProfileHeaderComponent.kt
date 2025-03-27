package com.amity.socialcloud.uikit.community.compose.user.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.follow.AmityFollowStatus
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityExpandableText
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityNumberUtil.getNumberAbbreveation
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageViewModel
import com.amity.socialcloud.uikit.community.compose.user.profile.elements.AmityUserFollowRelationshipButton
import com.amity.socialcloud.uikit.community.compose.user.profile.elements.AmityUserPendingRequestButton
import com.amity.socialcloud.uikit.community.compose.user.profile.elements.AmityUserUnfollowBottomSheet
import com.amity.socialcloud.uikit.community.compose.user.relationship.AmityUserRelationshipPageTab

@Composable
fun AmityUserProfileHeaderComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    user: AmityUser,
    onAvatarClick: () -> Unit = {},
) {
    val context = LocalContext.current
    var showUnfollowPopupDialog by remember { mutableStateOf(false) }
    var showUnfollowSheet by remember { mutableStateOf(false) }
    var showUnblockUserDialog by remember { mutableStateOf(false) }
    var showUserIsBlockedDialog by remember { mutableStateOf(false) }
    var showFollowErrorDialog by remember { mutableStateOf(false) }
    var showCancelErrorDialog by remember { mutableStateOf(false) }
    var showUnFollowErrorDialog by remember { mutableStateOf(false) }

    val behavior by lazy {
        AmitySocialBehaviorHelper.userProfileHeaderComponentBehavior
    }

    val viewModel =
        viewModel<AmityUserProfilePageViewModel>(
            factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AmityUserProfilePageViewModel(user.getUserId()) as T
                }
            }
        )

    val state by remember { viewModel.userProfileState }.collectAsState()
    val myFollowInfo by remember { derivedStateOf { state.myFollowInfo } }
    val userFollowInfo by remember { derivedStateOf { state.userFollowInfo } }

    val followingCount by remember(state) {
        derivedStateOf {
            if (state.isMyUserProfile()) {
                myFollowInfo?.getFollowingCount() ?: 0
            } else {
                userFollowInfo?.getFollowingCount() ?: 0
            }
        }
    }
    val followerCount by remember(state) {
        derivedStateOf {
            if (state.isMyUserProfile()) {
                myFollowInfo?.getFollowerCount() ?: 0
            } else {
                userFollowInfo?.getFollowerCount() ?: 0
            }
        }
    }

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "user_profile_header"
    ) {
        Column(modifier.padding(horizontal = 16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ) {
                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = getComponentScope(),
                    elementId = "user_avatar"
                ) {
                    AmityUserAvatarView(
                        user = user,
                        size = 56.dp,
                        modifier = modifier.clickableWithoutRipple { onAvatarClick() }
                    )
                }

                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = getComponentScope(),
                    elementId = "user_name"
                ) {
                    Row(modifier = modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = modifier.width(IntrinsicSize.Max)
                        ) {
                            Text(
                                text = user.getDisplayName() ?: "",
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    lineHeight = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmityTheme.colors.base,
                                ),
                                modifier = modifier.weight(1f)
                            )

                            if (user.isBrand()) {
                                Image(
                                    painter = painterResource(id = R.drawable.amity_ic_brand_badge),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .testTag("user_view/brand_user_icon")
                                )
                            }
                        }
                    }
                }
            }

            if (user.getDescription().isNotEmptyOrBlank()) {
                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = getComponentScope(),
                    elementId = "user_description"
                ) {
                    AmityExpandableText(
                        text = user.getDescription(),
                        previewLines = 4,
                        modifier = modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                Spacer(modifier.height(12.dp))
            }

            Row(
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                if (!user.isBrand()) {
                    AmityBaseElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "user_following"
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier.clickableWithoutRipple {
                                if (userFollowInfo?.getStatus() == AmityFollowStatus.ACCEPTED || state.isMyUserProfile()) {
                                    behavior.goToUserRelationshipPage(
                                        context = context,
                                        userId = user.getUserId(),
                                        selectedTab = AmityUserRelationshipPageTab.FOLLOWING,
                                    )
                                }
                            }
                        ) {
                            Text(
                                text = getNumberAbbreveation(followingCount),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontWeight = FontWeight.SemiBold,
                                ),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = getConfig().getText(),
                                style = AmityTheme.typography.captionLegacy.copy(
                                    fontWeight = FontWeight.Normal,
                                    color = AmityTheme.colors.baseShade2,
                                ),
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .width(1.dp)
                            .height(20.dp)
                            .background(color = AmityTheme.colors.baseShade4)
                    )
                }

                AmityBaseElement(
                    pageScope = pageScope,
                    componentScope = getComponentScope(),
                    elementId = "user_follower"
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.clickableWithoutRipple {
                            if (userFollowInfo?.getStatus() == AmityFollowStatus.ACCEPTED || state.isMyUserProfile()) {
                                behavior.goToUserRelationshipPage(
                                    context = context,
                                    userId = user.getUserId(),
                                    selectedTab = AmityUserRelationshipPageTab.FOLLOWER,
                                )
                            }
                        }
                    ) {
                        Text(
                            text = getNumberAbbreveation(followerCount),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = getConfig().getText(),
                            style = AmityTheme.typography.captionLegacy.copy(
                                fontWeight = FontWeight.Normal,
                                color = AmityTheme.colors.baseShade2,
                            ),
                        )
                    }
                }
            }
            Spacer(modifier.height(12.dp))

            if (!state.isMyUserProfile() && userFollowInfo?.getStatus() != null) {
                AmityUserFollowRelationshipButton(
                    modifier = modifier,
                    pageScope = pageScope,
                    componentScope = getComponentScope(),
                    followStatus = userFollowInfo?.getStatus()!!,
                    onClick = { followStatus ->
                        when (followStatus) {
                            AmityFollowStatus.ACCEPTED -> showUnfollowSheet = true
                            AmityFollowStatus.BLOCKED -> showUnblockUserDialog = true

                            AmityFollowStatus.PENDING -> {
                                viewModel.unfollowUser(
                                    targetUserId = user.getUserId(),
                                    onError = {
                                        showCancelErrorDialog = true
                                    }
                                )
                            }

                            AmityFollowStatus.NONE -> {
                                viewModel.followUser(
                                    targetUserId = user.getUserId(),
                                    onError = {
                                        if (it == AmityError.PERMISSION_DENIED) {
                                            showUserIsBlockedDialog = true
                                        }
                                        else {
                                            showFollowErrorDialog = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                )
                Spacer(modifier.height(12.dp))
            }

            if (state.isMyUserProfile()) {
                myFollowInfo?.getPendingRequestCount()?.takeIf { it > 0 }?.let {
                    AmityUserPendingRequestButton(
                        modifier = modifier,
                        pendingCount = myFollowInfo?.getPendingRequestCount() ?: 0
                    ) {
                        behavior.goToPendingFollowRequestPage(context)
                    }
                }
            }
        }
    }

    if (showUnfollowSheet) {
        AmityUserUnfollowBottomSheet(
            user = user,
            onDismiss = {
                showUnfollowSheet = false
            },
            onUnfollow = {
                showUnfollowPopupDialog = true
            }
        )
    }

    if (showUnfollowPopupDialog) {
        AmityAlertDialog(
            dialogTitle = "Unfollow this user?",
            dialogText = "If you change your mind, you'll have to request to follow them again.",
            confirmText = "Unfollow",
            dismissText = "Cancel",
            confirmTextColor = AmityTheme.colors.alert,
            onConfirmation = {
                showUnfollowPopupDialog = false
                viewModel.unfollowUser(
                    targetUserId = user.getUserId(),
                    onError = {
                        showUnFollowErrorDialog = true
                    }
                )
            },
            onDismissRequest = {
                showUnfollowPopupDialog = false
            }
        )
    }

    if (showUnblockUserDialog) {
        AmityAlertDialog(
            dialogTitle = "Unblock user?",
            dialogText = buildAnnotatedString {
                val displayName = user.getDisplayName() ?: ""
                append(displayName)
                addStyle(
                    style = SpanStyle(AmityTheme.colors.base),
                    start = 0,
                    end = displayName.length,
                )
                append(" will now be able to see posts and comments that you've created. They won't be notified that you've unblocked them.")
            },
            confirmText = "Unblock",
            dismissText = "Cancel",
            confirmTextColor = AmityTheme.colors.alert,
            onConfirmation = {
                showUnblockUserDialog = false

                viewModel.unblockUser(
                    targetUserId = user.getUserId(),
                    onSuccess = {
                        pageScope?.showSnackbar(
                            message = "User unblocked.",
                            drawableRes = R.drawable.amity_ic_snack_bar_success,
                        )
                    },
                    onError = {
                        pageScope?.showSnackbar(
                            message = "Failed to unblock user. Please try again.",
                            drawableRes = R.drawable.amity_ic_snack_bar_warning
                        )
                    }
                )
            },
            onDismissRequest = {
                showUnblockUserDialog = false
            }
        )
    }


    if (showUserIsBlockedDialog) {
        AmityAlertDialog(
            dialogTitle = "Unable to follow this user",
            dialogText = "Oops! something went wrong. Please try again later.",
            dismissText = "OK",
            onDismissRequest = {
                showUserIsBlockedDialog = false
            }
        )
    }

    if (showFollowErrorDialog) {
        AmityAlertDialog(
            dialogTitle = "Unable to follow this user",
            dialogText = "Oops! something went wrong. Please try again later.",
            dismissText = "OK",
            onDismissRequest = {
                showFollowErrorDialog = false
            }
        )
    }

    if (showCancelErrorDialog) {
        AmityAlertDialog(
            dialogTitle = "Unable to cancel the follow request",
            dialogText = "Oops! something went wrong. Please try again later.",
            dismissText = "OK",
            onDismissRequest = {
                showCancelErrorDialog = false
            }
        )
    }

    if (showUnFollowErrorDialog) {
        AmityAlertDialog(
            dialogTitle = "Unable to unfollow this user",
            dialogText = "Oops! something went wrong. Please try again later.",
            dismissText = "OK",
            onDismissRequest = {
                showUnFollowErrorDialog = false
            }
        )
    }
}