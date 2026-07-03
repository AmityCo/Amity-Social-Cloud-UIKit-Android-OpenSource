package com.amity.socialcloud.uikit.chat.compose.setting

import android.app.Activity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.member.AmityChannelMember
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.common.AmityChatConfirmDialog
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageAvatarView
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amity.socialcloud.uikit.common.localization.amityCommonString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityGroupMemberListPage(
    modifier: Modifier = Modifier,
    channelId: String,
) {
    val viewModel = remember { AmityGroupMemberListPageViewModel(channelId) }
    val isModerator by viewModel.isModerator().collectAsState(initial = false)
    val searchKeyword by viewModel.searchKeyword.collectAsState()
    val context = LocalContext.current

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        amityChatString("chat.group.member.list.tab.title"),
        amityChatString("chat.member.tab.moderators"),
    )

    val membersItems = viewModel.searchMembers(searchKeyword).collectAsLazyPagingItems()
    val moderatorsItems = viewModel.searchModerators(searchKeyword).collectAsLazyPagingItems()

    var selectedMember by remember { mutableStateOf<AmityChannelMember?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBanConfirmDialog by remember { mutableStateOf(false) }
    var pendingBanUserId by remember { mutableStateOf<String?>(null) }
    var showMuteConfirmDialog by remember { mutableStateOf(false) }
    var pendingMuteUserId by remember { mutableStateOf<String?>(null) }
    var pendingMuteIsMuted by remember { mutableStateOf(false) }
    var showPromoteConfirmDialog by remember { mutableStateOf(false) }
    var pendingPromoteUserId by remember { mutableStateOf<String?>(null) }
    var showRemoveConfirmDialog by remember { mutableStateOf(false) }
    var pendingRemoveUserId by remember { mutableStateOf<String?>(null) }
    var showDemoteConfirmDialog by remember { mutableStateOf(false) }
    var pendingDemoteUserId by remember { mutableStateOf<String?>(null) }
    var searchText by remember { mutableStateOf("") }

    // Snackbar messages
    val successMemberDemoted = amityChatString("chat.group.member.list.toast.demoted")
    val errorDemoteMember = amityChatString("chat.action.demote.member.failed")
    val successMemberPromoted = amityChatString("chat.group.member.list.toast.promoted")
    val errorPromoteMember = amityChatString("chat.action.promote.member.failed")
    val successMemberUnmuted = amityChatString("chat.action.unmute.user")
    val errorUnmuteMember = amityChatString("chat.action.unmute.user.failed")
    val successMemberMuted = amityChatString("chat.action.mute.user")
    val errorMuteMember = amityChatString("chat.action.mute.user.failed")
    val successUserUnreported = amityChatString("chat.action.unreport.user.success")
    val errorUnreportUser = amityChatString("chat.action.unreport.user.failed")
    val successUserReported = amityChatString("chat.action.report.user.success")
    val errorReportUser = amityChatString("chat.action.report.user.failed")
    val successMemberRemoved = amityChatString("chat.action.remove.member")
    val errorRemoveMember = amityChatString("chat.action.remove.member.failed")
    val successMemberBanned = amityChatString("chat.group.member.list.toast.banned")
    val errorBanMember = amityChatString("chat.action.ban.member.failed")

    AmityBasePage(pageId = "group_member_list_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background),
        ) {
            // Header with add button
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
                    text = amityChatString("chat.member.list.title"),
                    style = AmityTheme.typography.titleLegacy,
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )

                if (isModerator) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_add,
                        ),
                        contentDescription = "Add member",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterEnd)
                            .clickableWithoutRipple {
                                context.startActivity(
                                    AmityAddGroupMemberPageActivity.newIntent(context, channelId)
                                )
                            },
                        tint = AmityTheme.colors.base,
                    )
                }
            }

            // Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = AmityTheme.colors.background,
                contentColor = AmityTheme.colors.primary,
                edgePadding = 16.dp,
                divider = {
                    HorizontalDivider(
                        color = AmityTheme.colors.baseShade4,
                        thickness = 1.dp,
                    )
                },
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        Box(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                .height(2.dp)
                                .background(AmityTheme.colors.primary),
                        )
                    }
                },
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        selectedContentColor = AmityTheme.colors.primary,
                        unselectedContentColor = AmityTheme.colors.baseShade2,
                        text = {
                            Text(
                                text = title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 17.sp,
                            )
                        },
                    )
                }
            }

            // Member list
            val currentUserId = AmityCoreClient.getUserId()

            // Always fetch current user's membership independently so it can be pinned
            // regardless of whether it appears in the search/paging results.
            val currentUserMember by remember {
                viewModel.getMyMembership()
            }.collectAsState(initial = null)

            // Search bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(AmityTheme.colors.baseShade4, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_search,
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = AmityTheme.colors.baseShade3,
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        viewModel.onSearchKeywordChanged(it)
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 15.sp,
                        color = AmityTheme.colors.base,
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (searchText.isEmpty()) {
                                Text(
                                    text = amityChatString("chat.search.placeholder"),
                                    style = AmityTheme.typography.bodyLegacy.copy(
                                        fontSize = 15.sp,
                                        color = AmityTheme.colors.baseShade3,
                                    ),
                                )
                            }
                            innerTextField()
                        }
                    },
                )
            }

            if (selectedTabIndex == 0) {
                val isLoading = membersItems.loadState.refresh is LoadState.Loading
                if (isLoading && membersItems.itemCount == 0) {
                    MemberListSkeleton()
                } else if (!isLoading && membersItems.itemCount == 0) {
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
                                color = AmityTheme.colors.baseShade2,
                            ),
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        // Always pin current user at the top
                        if (membersItems.itemSnapshotList.items.isNotEmpty()) {
                            currentUserMember?.let { self ->
                                item(key = "self_${self.getUserId()}") {
                                    MemberListItem(
                                        member = self,
                                        isCurrentUser = true,
                                        showMoreAction = false,
                                        showMuteIcon = isModerator,
                                        onMoreClick = {},
                                    )
                                }
                            }
                        }
                        items(membersItems.itemCount) { index ->
                            val member = membersItems[index] ?: return@items
                            // Skip current user — already pinned at top
                            if (member.getUserId() == currentUserId) return@items
                            MemberListItem(
                                member = member,
                                isCurrentUser = false,
                                showMoreAction = member.getUserId() != currentUserId,
                                showMuteIcon = isModerator,
                                onMoreClick = {
                                    selectedMember = member
                                    scope.launch { sheetState.show() }
                                },
                            )
                        }
                    }
                }
            } else {
                val isModLoading = moderatorsItems.loadState.refresh is LoadState.Loading
                if (isModLoading && moderatorsItems.itemCount == 0) {
                    MemberListSkeleton()
                } else if (!isModLoading && moderatorsItems.itemCount == 0) {
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
                                color = AmityTheme.colors.baseShade2,
                            ),
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        // Pin current user at the top if present in results
                        if (moderatorsItems.itemSnapshotList.items.isNotEmpty()) {
                            val selfMod = moderatorsItems.itemSnapshotList.items.find { it.getUserId() == currentUserId }
                            selfMod?.let { self ->
                                item(key = "mod_self_${self.getUserId()}") {
                                    MemberListItem(
                                        member = self,
                                        isCurrentUser = true,
                                        showMoreAction = false,
                                        showMuteIcon = isModerator,
                                        onMoreClick = {},
                                    )
                                }
                            }
                        }
                        items(moderatorsItems.itemCount) { index ->
                            val member = moderatorsItems[index] ?: return@items
                            if (member.getUserId() == currentUserId) return@items
                            MemberListItem(
                                member = member,
                                isCurrentUser = false,
                                showMoreAction = member.getUserId() != currentUserId,
                                showMuteIcon = isModerator,
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

        // Action bottom sheet
        if (selectedMember != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    selectedMember = null
                },
                sheetState = sheetState,
                containerColor = AmityTheme.colors.background,
            ) {
                val member = selectedMember ?: return@ModalBottomSheet
                val memberRoles = member.getRoles()
                val isMemberModerator = memberRoles.contains(AmityConstants.CHANNEL_MODERATOR_ROLE)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                ) {
                    if (isModerator) {
                        // Promote / Demote
                        if (isMemberModerator) {
                            MemberActionItem(
                                text = amityChatString("chat.member.action.demote"),
                                iconResId = R.drawable.amity_ic_promote_moderator,
                                onClick = {
                                    pendingDemoteUserId = member.getUserId()
                                    selectedMember = null
                                    showDemoteConfirmDialog = true
                                },
                            )
                        } else {
                            MemberActionItem(
                                text = amityChatString("chat.member.action.promote"),
                                iconResId = R.drawable.amity_ic_promote_moderator,
                                onClick = {
                                    pendingPromoteUserId = member.getUserId()
                                    selectedMember = null
                                    showPromoteConfirmDialog = true
                                },
                            )
                        }

                        // Mute / Unmute (only for non-moderator members)
                        if (!isMemberModerator) {
                            val isMuted = member.isMuted()
                            MemberActionItem(
                                text = amityChatString(
                                    if (isMuted) "chat.group.member.action.unmute"
                                    else "chat.group.member.action.mute"
                                ),
                                iconResId = if (isMuted) com.amity.socialcloud.uikit.common.R.drawable.amity_ic_unmute_user
                                else com.amity.socialcloud.uikit.common.R.drawable.amity_ic_mute_user,
                                onClick = {
                                    pendingMuteUserId = member.getUserId()
                                    pendingMuteIsMuted = isMuted
                                    selectedMember = null
                                    showMuteConfirmDialog = true
                                },
                            )
                        }
                    }

                    // Report / Unreport
                    val isFlagged = member.getUser()?.isFlaggedByMe() == true
                    MemberActionItem(
                        text = amityChatString(
                            if (isFlagged) "chat.action.unreport.user"
                            else "chat.action.report.user"
                        ),
                        iconResId = if (isFlagged) R.drawable.amity_ic_flag_message
                        else R.drawable.amity_ic_unreport,
                        onClick = {
                            if (isFlagged) {
                                viewModel.unreportUser(
                                    member.getUserId(),
                                    onSuccess = {
                                        AmityUIKitSnackbar.publishSnackbarMessage(successUserUnreported)
                                        selectedMember = null
                                    },
                                    onError = {
                                        AmityUIKitSnackbar.publishSnackbarErrorMessage(errorUnreportUser)
                                        selectedMember = null
                                    }
                                )
                            } else {
                                viewModel.reportUser(
                                    member.getUserId(),
                                    onSuccess = {
                                        AmityUIKitSnackbar.publishSnackbarMessage(successUserReported)
                                        selectedMember = null
                                    },
                                    onError = {
                                        AmityUIKitSnackbar.publishSnackbarErrorMessage(errorReportUser)
                                        selectedMember = null
                                    }
                                )
                            }
                        },
                    )

                    if (isModerator) {

                        // Ban
                        MemberActionItem(
                            text = amityChatString("chat.user.action.ban"),
                            iconResId = R.drawable.amity_ic_ban_member,
                            onClick = {
                                pendingBanUserId = member.getUserId()
                                selectedMember = null
                                showBanConfirmDialog = true
                            },
                        )

                        // Remove (alert color)
                        MemberActionItem(
                            text = amityChatString("chat.member.action.remove"),
                            iconResId = R.drawable.amity_ic_delete_message,
                            textColor = AmityTheme.colors.alert,
                            onClick = {
                                pendingRemoveUserId = member.getUserId()
                                selectedMember = null
                                showRemoveConfirmDialog = true
                            },
                        )
                    }
                }
            }
        }

        // Ban confirmation dialog
        if (showBanConfirmDialog) {
            AmityChatConfirmDialog(
                title = amityChatString("chat.ban.confirm.title"),
                message = amityChatString("chat.ban.confirm.message"),
                confirmLabel = amityChatString("chat.ban.confirm.label"),
                onConfirm = {
                    showBanConfirmDialog = false
                    pendingBanUserId?.let { userId ->
                        viewModel.banMember(
                            userId,
                            onSuccess = {
                                AmityUIKitSnackbar.publishSnackbarMessage(successMemberBanned)
                            },
                            onError = {
                                AmityUIKitSnackbar.publishSnackbarErrorMessage(errorBanMember)
                            }
                        )
                    }
                    pendingBanUserId = null
                },
                onDismiss = {
                    showBanConfirmDialog = false
                    pendingBanUserId = null
                },
            )
        }

        // Mute / Unmute confirmation dialog
        if (showMuteConfirmDialog) {
            AmityChatConfirmDialog(
                title = if (pendingMuteIsMuted) amityChatString("chat.unmute.confirm.title")
                else amityChatString("chat.mute.confirm.title"),
                message = if (pendingMuteIsMuted) amityChatString("chat.unmute.confirm.message")
                else amityChatString("chat.mute.confirm.message"),
                confirmLabel = if (pendingMuteIsMuted) amityChatString("chat.unmute.confirm.label")
                else amityChatString("chat.mute.confirm.label"),
                onConfirm = {
                    showMuteConfirmDialog = false
                    pendingMuteUserId?.let { userId ->
                        if (pendingMuteIsMuted) {
                            viewModel.unmuteMember(
                                userId,
                                onSuccess = {
                                    AmityUIKitSnackbar.publishSnackbarMessage(successMemberUnmuted)
                                },
                                onError = {
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage(errorUnmuteMember)
                                }
                            )
                        } else {
                            viewModel.muteMember(
                                userId,
                                onSuccess = {
                                    AmityUIKitSnackbar.publishSnackbarMessage(successMemberMuted)
                                },
                                onError = {
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage(errorMuteMember)
                                }
                            )
                        }
                    }
                    pendingMuteUserId = null
                },
                onDismiss = {
                    showMuteConfirmDialog = false
                    pendingMuteUserId = null
                },
            )
        }

            // Promote to Moderator confirmation dialog
            if (showPromoteConfirmDialog) {
                AmityChatConfirmDialog(
                    title = amityChatString("chat.group.member.list.promote.title"),
                    message = amityChatString("chat.group.member.list.promote.message"),
                    confirmLabel = amityChatString("chat.group.member.list.promote.confirm"),
                    confirmColor = AmityTheme.colors.primary,
                    onConfirm = {
                        showPromoteConfirmDialog = false
                        pendingPromoteUserId?.let { userId ->
                            viewModel.promoteModerator(
                                userId,
                                onSuccess = {
                                    AmityUIKitSnackbar.publishSnackbarMessage(successMemberPromoted)
                                },
                                onError = {
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage(errorPromoteMember)
                                }
                            )
                        }
                        pendingPromoteUserId = null
                    },
                    onDismiss = {
                        showPromoteConfirmDialog = false
                        pendingPromoteUserId = null
                    },
                )
            }

            // Remove from Group confirmation dialog
            if (showRemoveConfirmDialog) {
                AmityChatConfirmDialog(
                    title = amityChatString("chat.group.member.list.remove.title"),
                    message = amityChatString("chat.group.member.list.remove.message"),
                    confirmLabel = amityChatString("chat.group.member.list.remove.confirm"),
                    onConfirm = {
                        showRemoveConfirmDialog = false
                        pendingRemoveUserId?.let { userId ->
                            viewModel.removeMember(
                                userId,
                                onSuccess = {
                                    AmityUIKitSnackbar.publishSnackbarMessage(successMemberRemoved)
                                },
                                onError = {
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage(errorRemoveMember)
                                }
                            )
                        }
                        pendingRemoveUserId = null
                    },
                    onDismiss = {
                        showRemoveConfirmDialog = false
                        pendingRemoveUserId = null
                    },
                )
            }

        if (showDemoteConfirmDialog) {
            AmityChatConfirmDialog(
                title = amityChatString("chat.group.member.list.demote.title"),
                message = amityChatString("chat.group.member.list.demote.message"),
                confirmLabel = amityChatString("chat.group.member.list.demote.confirm"),
                onConfirm = {
                    showDemoteConfirmDialog = false
                    pendingDemoteUserId?.let { userId ->
                        viewModel.demoteModerator(
                            userId,
                            onSuccess = {
                                AmityUIKitSnackbar.publishSnackbarMessage(successMemberDemoted)
                            },
                            onError = {
                                AmityUIKitSnackbar.publishSnackbarErrorMessage(errorDemoteMember)
                            }
                        )
                    }
                    pendingDemoteUserId = null
                },
                onDismiss = {
                    showDemoteConfirmDialog = false
                    pendingDemoteUserId = null
                },
            )
        }
        }
}

@Composable
private fun MemberListItem(
    member: AmityChannelMember,
    isCurrentUser: Boolean,
    showMoreAction: Boolean,
    showMuteIcon: Boolean = false,
    onMoreClick: () -> Unit,
) {
    val user = member.getUser()
    val isModerator = member.getRoles().contains(AmityConstants.CHANNEL_MODERATOR_ROLE)
    val isMuted = member.isMuted()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Avatar with moderator badge
        Box(modifier = Modifier.size(40.dp)) {
            AmityMessageAvatarView(
                avatarUrl = user?.getAvatar()?.getUrl(AmityImage.Size.SMALL) ?: "",
                displayName = user?.getDisplayName(),
                size = 40.dp,
            )
            if (isModerator) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(
                            R.drawable.amity_ic_chat_group_moderator,
                        ),
                        contentDescription = "Moderator",
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = buildString {
                        append(user?.getDisplayName() ?: member.getUserId())
                        if (isCurrentUser) {
                            append(" ")
                            append(amityChatString("chat.member.you.suffix"))
                        }
                    },
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                if(user?.isBrand() == true) {
                    val badge = R.drawable.amity_ic_brand_badge
                    Image(
                        painter = painterResource(id = badge),
                        contentDescription = "",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(start = 4.dp)
                            .testTag("user_view/brand_user_icon")
                    )
                }
                if (showMuteIcon && isMuted) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_mute_user,
                        ),
                        contentDescription = "Muted",
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(16.dp),
                        tint = AmityTheme.colors.baseShade2,
                    )
                }
            }
        }
        if (showMoreAction) {
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
}

@Composable
internal fun MemberActionItem(
    text: String,
    iconResId: Int? = null,
    textColor: androidx.compose.ui.graphics.Color = AmityTheme.colors.base,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (iconResId != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = textColor,
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = text,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
            ),
        )
    }
}

@Composable
private fun MemberListSkeleton() {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer",
    )

    val baseColor = AmityTheme.colors.baseShade4
    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            baseColor,
            baseColor.copy(alpha = 0.4f),
            baseColor,
        ),
        start = Offset(translateAnim - 200f, 0f),
        end = Offset(translateAnim, 0f),
    )

    Column(modifier = Modifier.fillMaxSize()) {
        repeat(8) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(shimmerBrush),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .height(14.dp)
                        .fillMaxWidth(0.4f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmerBrush),
                )
            }
        }
    }
}
