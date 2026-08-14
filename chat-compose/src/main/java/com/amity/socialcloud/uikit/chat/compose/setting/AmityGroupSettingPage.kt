package com.amity.socialcloud.uikit.chat.compose.setting

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannelNotificationMode
import com.amity.socialcloud.uikit.chat.compose.common.AmityChatConfirmDialog
import com.amity.socialcloud.uikit.chat.compose.home.AmityChatHomePageActivity
import com.amity.socialcloud.uikit.chat.compose.home.element.AmityUserAvatarView
import com.amity.socialcloud.uikit.chat.compose.notification.AmityGroupNotificationPreferencePageActivity
import com.amity.socialcloud.uikit.chat.compose.notification.AmityEditGroupNotificationPageActivity
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBanner
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBannerHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.common.utils.resolvedAvatarUrl

@Composable
fun AmityGroupSettingPage(
    modifier: Modifier = Modifier,
    channelId: String,
) {
    val viewModel = remember { AmityGroupSettingPageViewModel(channelId) }
    val channel by viewModel.getChannelFlow().collectAsState(initial = null)
    val members by viewModel.getMembers().collectAsState(initial = emptyList())
    val memberRoles by viewModel.memberRoles.collectAsState(initial = emptyMap())
    val isModerator = memberRoles[AmityCoreClient.getUserId()]?.any { it.contains(AmityConstants.CHANNEL_MODERATOR_ROLE) } == true
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val context = LocalContext.current

    // Re-fetch notification state each time screen resumes
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshNotificationState()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    var showLeaveDialog by remember { mutableStateOf(false) }
    var showLastModeratorDialog by remember { mutableStateOf(false) }

    // Snackbar messages
    val errorLeaveGroup = amityChatString("chat.action.leave.group.failed")
    val leftGroupChat = amityChatString("chat.group.leave.toast")

    AmityBasePage(pageId = "group_setting_page", useAmityToast = true) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.token(AmityColorToken.SurfaceSheetsBackgroundGeneral)),
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
                    text = channel?.getDisplayName() ?: "",
                    style = AmityTheme.typography.titleLegacy,
                    color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTitleDefault),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .padding(horizontal = 40.dp)
                        .align(Alignment.Center),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                // Group info header
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Avatar — use circular user avatar for conversation channels
                    if (channel?.getChannelType() == AmityChannel.Type.CONVERSATION) {
                        val otherMembers = members.filter { it.getUserId() != AmityCoreClient.getUserId() }
                        AmityUserAvatarView(
                            avatarUrl = otherMembers.firstOrNull()?.getUser()?.resolvedAvatarUrl(AmityImage.Size.MEDIUM),
                            displayName = otherMembers.firstOrNull()?.getUser()?.getDisplayName(),
                            isDeleted = otherMembers.firstOrNull()?.getUser()?.isDeleted() == true,
                            size = 120,
                        )
                    } else {
                        AmityAvatar(
                            variant = AmityAvatarVariant.Image,
                            imageUrl = channel?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM),
                            icon = CommonR.drawable.amity_ic_comments_alt_s,
                            style = AmityAvatarStyle.Squared,
                            size = AmityAvatarSize.Size120,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                AmityBanner(
                    hierarchy = AmityBannerHierarchy.DEFAULT,
                    header = amityChatString("chat.group.settings.section"),
                )

                // Moderator section (moderator only)
                if (isModerator) {
                    Spacer(modifier = Modifier.height(8.dp))

                    SettingItem(
                        text = amityChatString("chat.edit.group.profile.navbar.title"),
                        iconResId = CommonR.drawable.amity_ic_pen_s,
                        onClick = {
                            context.startActivity(
                                AmityEditGroupProfilePageActivity.newIntent(context, channelId)
                            )
                        },
                    )

                    SettingItem(
                        text = amityChatString("chat.group.notifications"),
                        iconResId = CommonR.drawable.amity_ic_bell_s,
                        trailingText = when (channel?.getNotificationMode()) {
                            "silent" -> amityChatString("chat.group.notification.silent.label")
                            "subscribe" -> amityChatString("chat.group.notification.subscribe.label")
                            else -> amityChatString("chat.group.notification.default.label")
                        },
                        onClick = {
                            context.startActivity(
                                AmityEditGroupNotificationPageActivity.newIntent(context, channelId)
                            )
                        },
                    )

                    SettingItem(
                        text = amityChatString("chat.group.member.permissions"),
                        iconResId = CommonR.drawable.amity_ic_user_lock_s,
                        onClick = {
                            context.startActivity(
                                AmityEditGroupMemberPermissionsPageActivity.newIntent(context, channelId)
                            )
                        },
                    )

                    SettingItem(
                        text = amityChatString("chat.group.members.label"),
                        iconResId = CommonR.drawable.amity_ic_user_group_s,
                        onClick = {
                            context.startActivity(
                                AmityGroupMemberListPageActivity.newIntent(context, channelId)
                            )
                        },
                    )

                    SettingItem(
                        text = amityChatString("chat.group.banned.members"),
                        iconResId = CommonR.drawable.amity_ic_ban_s,
                        onClick = {
                            context.startActivity(
                                AmityBannedGroupMemberListPageActivity.newIntent(context, channelId)
                            )
                        },
                    )

                    AmityDivider(
                        variant = AmityDividerVariant.Post,
                        inset = true,
                    )
                }

                // All users section
                Spacer(modifier = Modifier.height(8.dp))

                if (!isModerator) {
                    SettingItem(
                        text = amityChatString("chat.group.members.label"),
                        iconResId = CommonR.drawable.amity_ic_user_group_s,
                        onClick = {
                            context.startActivity(
                                AmityGroupMemberListPageActivity.newIntent(context, channelId)
                            )
                        },
                    )
                }

                // Your Preferences section (visible to all members)
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeader(text = amityChatString("chat.your.preferences.section"))

                SettingItem(
                    text = amityChatString("chat.notifications.title"),
                    iconResId = CommonR.drawable.amity_ic_bell_s,
                    trailingText = if (notificationsEnabled) amityChatString("chat.notifications.on")
                                   else amityChatString("chat.notifications.off"),
                    onClick = {
                        context.startActivity(
                            AmityGroupNotificationPreferencePageActivity.newIntent(context, channelId)
                        )
                    },
                )

                // Leave group
                Spacer(modifier = Modifier.height(16.dp))

                SettingItem(
                    text = amityChatString("chat.group.leave"),
                    textColor = AmityTheme.token(AmityColorToken.TextListHeaderDestructiveDefault),
                    showArrow = false,
                    onClick = {
                        if (isModerator && (channel?.getModeratorMemberCount() ?: 0) <= 1) {
                            showLastModeratorDialog = true
                        } else {
                            showLeaveDialog = true
                        }
                    },
                )
            }
        }

        // Leave confirmation dialog
        if (showLeaveDialog) {
            AmityChatConfirmDialog(
                title = amityChatString("chat.group.leave.confirm.title"),
                message = amityChatString("chat.group.leave.confirm.message"),
                confirmLabel = amityChatString("chat.group.leave.confirm.label"),
                onConfirm = {
                    showLeaveDialog = false
                    viewModel.leaveChannel(
                        onSuccess = {
                            // Publish before navigating: the snackbar flow is a shared broadcast, so
                            // the chat list — already in the back stack — receives it and shows the
                            // toast on the page the user lands on.
                            AmityUIKitSnackbar.publishSnackbarMessage(message = leftGroupChat)
                            context.startActivity(
                                Intent(context, AmityChatHomePageActivity::class.java).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                }
                            )
                        },
                        onError = {
                            AmityUIKitSnackbar.publishSnackbarErrorMessage(errorLeaveGroup)
                        },
                    )
                },
                onDismiss = { showLeaveDialog = false },
            )
        }
        // Last-moderator leave guard dialog
        if (showLastModeratorDialog) {
            AmityChatConfirmDialog(
                title = amityChatString("chat.group.leave.last.mod.title"),
                message = amityChatString("chat.group.leave.last.mod.message"),
                confirmLabel = amityChatString("chat.group.promote.member"),
                confirmColor = AmityTheme.token(AmityColorToken.TextBaseHighlight),
                onConfirm = {
                    showLastModeratorDialog = false
                    context.startActivity(
                        AmityGroupMemberListPageActivity.newIntent(context, channelId)
                    )
                },
                onDismiss = { showLastModeratorDialog = false },
            )
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = AmityTheme.typography.bodyLegacy.copy(
            fontSize = 17.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
        ),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun SettingItem(
    text: String,
    iconResId: Int? = null,
    textColor: androidx.compose.ui.graphics.Color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
    showArrow: Boolean = true,
    trailingText: String? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (iconResId != null) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = AmityTheme.token(AmityColorToken.SurfaceFeaturedIconTinted),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = AmityTheme.token(AmityColorToken.IconFeaturedIconTinted),
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 15.sp,
                lineHeight = 20.sp,
                fontWeight = if (iconResId != null) FontWeight.Normal else FontWeight.SemiBold,
                color = textColor,
            ),
            modifier = Modifier.weight(1f),
        )
        if (trailingText != null) {
            Text(
                text = trailingText,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 15.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = AmityTheme.token(AmityColorToken.TextListTrailingTextGeneral),
                ),
                modifier = Modifier.padding(end = 4.dp),
            )
        }
        if (showArrow && iconResId != null) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = CommonR.drawable.amity_ic_chevron_right,
                ),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = AmityTheme.token(AmityColorToken.IconListLeadingDefaultDefault),
            )
        }
    }
}
