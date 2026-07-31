package com.amity.socialcloud.uikit.chat.compose.setting

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.common.localization.amityCommonString
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.common.AmityChatConfirmDialog
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyState
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyStateVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityListItem
import com.amity.socialcloud.uikit.common.ui.atoms.AmityListItemVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityListLeadingContent
import com.amity.socialcloud.uikit.common.ui.atoms.AmityListLeadingType
import com.amity.socialcloud.uikit.common.ui.atoms.AmityListTrailingContent
import com.amity.socialcloud.uikit.common.ui.atoms.AmityListTrailingType
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoader
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoaderSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoaderVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySheet
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySearchBar
import com.amity.socialcloud.uikit.chat.compose.common.toChatAvatarInitial
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
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
    val bannedMembers = remember(searchKeyword) {
        viewModel.searchBannedMembers(searchKeyword)
    }.collectAsLazyPagingItems()

    var selectedMember by remember { mutableStateOf<AmityChannelMember?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showUnbanConfirmDialog by remember { mutableStateOf(false) }
    var pendingUnbanUserId by remember { mutableStateOf<String?>(null) }

    // Snackbar messages
    val successMemberUnbanned = amityChatString("chat.action.unban.user")
    val errorUnbanMember = amityChatString("chat.action.unban.user.failed")

    AmityBasePage(pageId = "banned_group_member_list_page", useAmityToast = true) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.token(AmityColorToken.SurfacePageBackgroundDefault)),
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
                    text = amityChatString("chat.banned.member.list.navbar.title"),
                    style = AmityTheme.typography.titleLegacy.copy(
                        color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTitleDefault),
                    ),
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )
            }

            AmityDivider(variant = AmityDividerVariant.Content, inset = false)

            AmitySearchBar(
                hint = amityChatString("chat.search.placeholder"),
            ) {
                searchKeyword = it
            }

            val isLoading = bannedMembers.loadState.refresh is LoadState.Loading
            when {
                isLoading && bannedMembers.itemCount == 0 -> {
                    // Loading
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        AmityLoader(
                            variant = AmityLoaderVariant.Spinner,
                            size = AmityLoaderSize.Sm,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
                bannedMembers.itemCount == 0 -> {
                    if (searchKeyword.isBlank()) {
                        // No banned members at all
                        AmityEmptyState(
                            modifier = Modifier.fillMaxSize(),
                            variant = AmityEmptyStateVariant.ICON,
                            icon = CommonR.drawable.amity_ic_list_radio_l,
                            title = amityChatString("chat.banned.members.empty"),
                        )
                    } else {
                        // Search returned no matches
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .imePadding()
                                .padding(32.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = amityChatString("chat.search.no.results"),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    color = AmityTheme.token(AmityColorToken.TextEmptyStateDescriptionDefault),
                                ),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(bannedMembers.itemCount) { index ->
                            val member = bannedMembers[index] ?: return@items
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
        }

        // Unban action bottom sheet
        if (selectedMember != null) {
            AmitySheet(
                onDismissRequest = { selectedMember = null },
                sheetState = sheetState,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                ) {
                    MemberActionItem(
                        text = amityChatString("chat.member.action.unban"),
                        iconResId = CommonR.drawable.amity_ic_ban_r,
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

    AmityListItem(
        variant = AmityListItemVariant.DEFAULT,
        title = user?.getDisplayName() ?: member.getUserId(),
        leadingType = AmityListLeadingType.AVATAR,
        leading = AmityListLeadingContent(
            type = AmityListLeadingType.AVATAR,
            avatarUrl = user?.getAvatar()?.getUrl(AmityImage.Size.SMALL),
            avatarInitials = user?.getDisplayName().toChatAvatarInitial(),
            icon = CommonR.drawable.amity_ic_user_r,
            avatarSize = AmityAvatarSize.Size40,
            avatarBorderWidth = 2,
        ),
        trailing = listOf(
            AmityListTrailingContent(
                type = AmityListTrailingType.ICON,
                icon = CommonR.drawable.amity_ic_ellipsis_r,
            )
        ),
        onTrailingPress = { onMoreClick() },
    )
}
