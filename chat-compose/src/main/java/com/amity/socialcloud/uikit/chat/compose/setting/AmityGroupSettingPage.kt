package com.amity.socialcloud.uikit.chat.compose.setting

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import coil3.compose.AsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannelNotificationMode
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.common.AmityChatConfirmDialog
import com.amity.socialcloud.uikit.chat.compose.home.AmityChatHomePageActivity
import com.amity.socialcloud.uikit.chat.compose.home.element.AmityUserAvatarView
import com.amity.socialcloud.uikit.chat.compose.notification.AmityGroupNotificationPreferencePageActivity
import com.amity.socialcloud.uikit.chat.compose.notification.AmityEditGroupNotificationPageActivity
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

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

    AmityBasePage(pageId = "group_setting_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background),
        ) {
            // Header
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
                    text = channel?.getDisplayName() ?: "",
                    style = AmityTheme.typography.titleLegacy,
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
                            avatarUrl = otherMembers.firstOrNull()?.getUser()?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM),
                            displayName = otherMembers.firstOrNull()?.getUser()?.getDisplayName(),
                            isDeleted = otherMembers.firstOrNull()?.getUser()?.isDeleted() == true,
                            size = 120,
                        )
                    } else {
                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest
                                .Builder(LocalContext.current)
                                .data(channel?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM))
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .build()
                        )
                        val painterState by painter.state.collectAsState()
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(24.dp))
                        ) {
                            Image(
                                painter = painter,
                                contentDescription = "Channel Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(24.dp))
                            )
                            if (painterState !is AsyncImagePainter.State.Success) {
                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(AmityTheme.colors.primaryShade3),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.amity_ic_group_chat_avatar_placeholder),
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                SectionHeader(text = amityChatString("chat.group.settings.section"))

                // Moderator section (moderator only)
                if (isModerator) {
                    Spacer(modifier = Modifier.height(8.dp))

                    SettingItem(
                        text = amityChatString("chat.edit.group.profile.navbar.title"),
                        iconResId = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_edit_profile,
                        onClick = {
                            context.startActivity(
                                AmityEditGroupProfilePageActivity.newIntent(context, channelId)
                            )
                        },
                    )

                    SettingItem(
                        text = amityChatString("chat.group.notifications"),
                        iconResId = R.drawable.amity_ic_chat_notification,
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
                        iconResId = R.drawable.amity_ic_member_permissions,
                        onClick = {
                            context.startActivity(
                                AmityEditGroupMemberPermissionsPageActivity.newIntent(context, channelId)
                            )
                        },
                    )

                    SettingItem(
                        text = amityChatString("chat.group.members.label"),
                        iconResId = R.drawable.amity_ic_members_list,
                        onClick = {
                            context.startActivity(
                                AmityGroupMemberListPageActivity.newIntent(context, channelId)
                            )
                        },
                    )

                    SettingItem(
                        text = amityChatString("chat.group.banned.members"),
                        iconResId = R.drawable.amity_ic_ban_member,
                        onClick = {
                            context.startActivity(
                                AmityBannedGroupMemberListPageActivity.newIntent(context, channelId)
                            )
                        },
                    )

                    HorizontalDivider(
                        color = AmityTheme.colors.baseShade4,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }

                // All users section
                Spacer(modifier = Modifier.height(8.dp))

                if (!isModerator) {
                    SettingItem(
                        text = amityChatString("chat.group.members.label"),
                        iconResId = R.drawable.amity_ic_members_list,
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
                    iconResId = R.drawable.amity_ic_chat_notification,
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
                    textColor = AmityTheme.colors.alert,
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
                confirmColor = AmityTheme.colors.primary,
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
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = AmityTheme.colors.base,
        ),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun SettingItem(
    text: String,
    iconResId: Int? = null,
    textColor: androidx.compose.ui.graphics.Color = AmityTheme.colors.base,
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
                    .size(28.dp)
                    .background(
                        color = AmityTheme.colors.baseShade4,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = AmityTheme.colors.base,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = text,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
            ),
            modifier = Modifier.weight(1f),
        )
        if (trailingText != null) {
            Text(
                text = trailingText,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 13.sp,
                    color = AmityTheme.colors.baseShade2,
                ),
                modifier = Modifier.padding(end = 4.dp),
            )
        }
        if (showArrow && iconResId != null) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_chevron_right,
                ),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = AmityTheme.colors.baseShade2,
            )
        }
    }
}
