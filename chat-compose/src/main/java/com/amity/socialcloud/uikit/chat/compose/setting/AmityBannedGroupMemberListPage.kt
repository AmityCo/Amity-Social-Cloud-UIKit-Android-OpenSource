package com.amity.socialcloud.uikit.chat.compose.setting

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.localization.amityCommonString
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.common.AmityChatConfirmDialog
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageAvatarView
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmitySearchBarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityBannedGroupMemberListPage(
    modifier: Modifier = Modifier,
    channelId: String,
) {
    val viewModel = remember { AmityGroupMemberListPageViewModel(channelId) }
    val context = LocalContext.current

    var searchKeyword by remember { mutableStateOf("") }
    val bannedMembers by viewModel.getBannedMembers(searchKeyword).collectAsState(initial = emptyList())

    var selectedMember by remember { mutableStateOf<AmityChannelMember?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showUnbanConfirmDialog by remember { mutableStateOf(false) }
    var pendingUnbanUserId by remember { mutableStateOf<String?>(null) }

    // Snackbar messages
    val successMemberUnbanned = amityChatString("chat.action.unban.user")
    val errorUnbanMember = amityChatString("chat.action.unban.user.failed")

    AmityBasePage(pageId = "banned_group_member_list_page") {
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
                    text = amityChatString("chat.banned.member.list.navbar.title"),
                    style = AmityTheme.typography.titleLegacy,
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )
            }

            HorizontalDivider(color = AmityTheme.colors.baseShade4)

            AmitySearchBarView(
                hint = amityChatString("chat.search.placeholder"),
            ) {
                searchKeyword = it
            }

            if (bannedMembers.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_banned_empty),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = AmityTheme.colors.secondaryShade4,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = amityChatString("chat.banned.members.empty"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp,
                            color = AmityTheme.colors.baseShade3,
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(bannedMembers.size) { index ->
                        val member = bannedMembers[index]
                        BannedMemberItem(
                            member = member,
                            onMoreClick = {
                                selectedMember = member
                                scope.launch { sheetState.show() }
                            },
                        )
                    }
                }
            }
        }

        // Unban action bottom sheet
        if (selectedMember != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedMember = null },
                sheetState = sheetState,
                containerColor = AmityTheme.colors.background,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                ) {
                    MemberActionItem(
                        text = amityChatString("chat.member.action.unban"),
                        iconResId = R.drawable.amity_ic_ban_member,
                        onClick = {
                            pendingUnbanUserId = selectedMember?.getUserId()
                            selectedMember = null
                            showUnbanConfirmDialog = true
                        },
                    )
                }
            }
        }

        // Unban confirmation dialog
        if (showUnbanConfirmDialog) {
            AmityChatConfirmDialog(
                title = amityChatString("chat.unban.confirm.title"),
                message = amityChatString("chat.unban.confirm.message"),
                confirmLabel = amityChatString("chat.unban.confirm.label"),
                onConfirm = {
                    showUnbanConfirmDialog = false
                    pendingUnbanUserId?.let { userId ->
                        viewModel.unbanMember(
                            userId,
                            onSuccess = {
                                AmityUIKitSnackbar.publishSnackbarMessage(successMemberUnbanned)
                            },
                            onError = {
                                AmityUIKitSnackbar.publishSnackbarErrorMessage(errorUnbanMember)
                            }
                        )
                    }
                    pendingUnbanUserId = null
                },
                onDismiss = {
                    showUnbanConfirmDialog = false
                    pendingUnbanUserId = null
                },
            )
        }
    }
}

@Composable
private fun BannedMemberItem(
    member: AmityChannelMember,
    onMoreClick: () -> Unit,
) {
    val user = member.getUser()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AmityMessageAvatarView(
            avatarUrl = user?.getAvatar()?.getUrl(AmityImage.Size.SMALL) ?: "",
            displayName = user?.getDisplayName(),
            size = 40.dp,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = user?.getDisplayName() ?: member.getUserId(),
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_more_horiz),
            contentDescription = "More",
            modifier = Modifier
                .size(24.dp)
                .clickableWithoutRipple { onMoreClick() },
            tint = AmityTheme.colors.base,
        )
    }
}
