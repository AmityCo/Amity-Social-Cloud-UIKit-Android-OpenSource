package com.amity.socialcloud.uikit.chat.compose.create

import android.app.Activity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.res.painterResource
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarVariant
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
import com.amity.socialcloud.uikit.common.ui.atoms.AmityMainButtonSize
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySearchBar
import com.amity.socialcloud.uikit.chat.compose.common.toChatAvatarInitial
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.utils.resolvedAvatarUrl

@Composable
fun AmitySelectGroupMemberPage(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onNext: (List<AmityUser>) -> Unit,
) {
    val context = LocalContext.current
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    val viewModel = viewModel<AmitySelectGroupMemberPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val selectedUsers by viewModel.selectedUsers.collectAsState()
    var keyword by remember { mutableStateOf("") }

    val lazyPagingItems = remember(keyword) {
        viewModel.searchUsers(keyword)
    }.collectAsLazyPagingItems()

    AmityBasePage(pageId = "select_group_member_page", useAmityToast = true) {
        Column(
            modifier = modifier.fillMaxSize(),
        ) {
            // Header
            Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                ) {
                    // Ghost close/back icon-button (Button atom, IconButton Ghost Secondary)
                    AmityButton(
                        variant = AmityButtonVariant.ICON,
                        style = AmityButtonStyle.GHOST,
                        hierarchy = AmityButtonHierarchy.SECONDARY,
                        iconSize = AmityIconButtonSize.SIZE32,
                        icon = CommonR.drawable.amity_ic_cross_r,
                        onClick = { onBack.invoke() },
                        modifier = Modifier.align(Alignment.CenterStart),
                    )

                    Text(
                        text = amityChatString("chat.select.members.title"),
                        style = AmityTheme.typography.titleLegacy.copy(
                            color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTitleDefault),
                        ),
                        modifier = Modifier
                            .padding(vertical = 17.dp)
                            .align(Alignment.Center),
                    )

                    // Next progression action (Button atom, MainButton Sm Ghost Primary)
                    AmityButton(
                        variant = AmityButtonVariant.MAIN,
                        style = AmityButtonStyle.GHOST,
                        hierarchy = AmityButtonHierarchy.PRIMARY,
                        mainSize = AmityMainButtonSize.SM,
                        label = amityChatString("chat.next"),
                        enabled = selectedUsers.isNotEmpty(),
                        onClick = { onNext(selectedUsers) },
                        modifier = Modifier.align(Alignment.CenterEnd),
                    )
                }

                AmityDivider(variant = AmityDividerVariant.Post)
                Spacer(modifier = Modifier.height(4.dp))

                AmitySearchBar(
                    hint = amityChatString("chat.search.placeholder"),
                ) {
                    keyword = it
                }

                // Selected users chips
                if (selectedUsers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(selectedUsers, key = { it.getUserId() }) { user ->
                            SelectedUserChip(
                                user = user,
                                onRemove = { viewModel.removeUser(user.getUserId()) },
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    AmityDivider(variant = AmityDividerVariant.Post)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // User list
                val loadState = AmitySelectGroupMemberPageViewModel.UserListState.from(
                    loadState = lazyPagingItems.loadState.refresh,
                    itemCount = lazyPagingItems.itemCount,
                    keywordLength = keyword.length,
                    minKeywordLength = 3
                )

                when (loadState) {
                    AmitySelectGroupMemberPageViewModel.UserListState.LOADING -> {
                        // Skeleton (not a centered spinner) while the member list loads — matches the
                        // 1-1 create flow (AmityChannelCreateConversationPage).2.md.
                        UserListSkeleton()
                    }
                    AmitySelectGroupMemberPageViewModel.UserListState.SHORT_INPUT -> {
                        AmityEmptyState(
                            modifier = Modifier
                                .fillMaxSize()
                                .imePadding()
                                .padding(32.dp),
                            variant = AmityEmptyStateVariant.ICON,
                            icon = CommonR.drawable.amity_ic_search_l,
                            title = amityChatString("chat.search.min.chars"),
                        )
                    }
                    AmitySelectGroupMemberPageViewModel.UserListState.EMPTY -> {
                        // No-results empty state, matching AmityChannelCreateConversationPage.
                        AmityEmptyState(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            variant = AmityEmptyStateVariant.ICON,
                            icon = CommonR.drawable.amity_ic_search_cross_l,
                            title = amityChatString("chat.no.users.found"),
                        )
                    }
                    AmitySelectGroupMemberPageViewModel.UserListState.SUCCESS -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                        ) {
                            items(
                                count = lazyPagingItems.itemCount,
                                key = lazyPagingItems.itemKey { it.getUserId() }
                            ) { index ->
                                val user = lazyPagingItems[index] ?: return@items
                                val isChecked = selectedUsers.any { it.getUserId() == user.getUserId() }
                                SelectableUserItem(
                                    user = user,
                                    isSelected = isChecked,
                                    onToggle = { viewModel.toggleUserSelection(user) },
                                )
                            }
                            item { Spacer(modifier = Modifier.height(16.dp)) }
                        }
                    }
                }
            }
        }
    }


@Composable
private fun SelectableUserItem(
    user: AmityUser,
    isSelected: Boolean,
    onToggle: () -> Unit,
) {
    AmityListItem(
        variant = AmityListItemVariant.DEFAULT,
        title = user.getDisplayName() ?: "",
        titleAccessory = if (user.isBrand()) {
            {
                Image(
                    painter = painterResource(id = R.drawable.amity_ic_brand_badge),
                    contentDescription = "",
                    modifier = Modifier
                        .size(20.dp)
                        .testTag("user_view/brand_user_icon"),
                )
            }
        } else null,
        leadingType = AmityListLeadingType.AVATAR,
        leading = AmityListLeadingContent(
            type = AmityListLeadingType.AVATAR,
            avatarUrl = user.avatarImageUrl(),
            avatarInitials = user.avatarInitials(),
            icon = CommonR.drawable.amity_ic_user_r,
            avatarSize = AmityAvatarSize.Size40,
            avatarBorderWidth = 2,
        ),
        trailing = listOf(
            AmityListTrailingContent(
                type = AmityListTrailingType.CHECKBOX,
                checked = isSelected,
                icon = CommonR.drawable.amity_ic_scale_2_s,
            )
        ),
        onPress = onToggle,
        onTrailingPress = { onToggle() },
    )
}

@Composable
private fun SelectedUserChip(
    user: AmityUser,
    onRemove: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
    ) {
        Box {
            AmityAvatar(
                variant = if (user.avatarImageUrl() != null) AmityAvatarVariant.Image else AmityAvatarVariant.Text,
                imageUrl = user.avatarImageUrl(),
                initials = user.avatarInitials(),
                icon = CommonR.drawable.amity_ic_user_r,
                size = AmityAvatarSize.Size40,
                borderWidth = 2,
            )
            // Remove badge
            AmityButton(
                variant = AmityButtonVariant.ICON,
                style = AmityButtonStyle.TRANSPARENT,
                hierarchy = AmityButtonHierarchy.PRIMARY,
                iconSize = AmityIconButtonSize.SIZE16,
                icon = CommonR.drawable.amity_ic_cross_r,
                contentDescription = "Remove",
                onClick = onRemove,
                modifier = Modifier.align(Alignment.TopEnd),
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = user.getDisplayName() ?: "",
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 11.sp,
                color = AmityTheme.token(AmityColorToken.TextAvatarLabelDefault),
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

// AmityAvatar (atom) call-site helpers — derive the atom's Image/Text inputs from AmityUser.
private fun AmityUser.avatarImageUrl(): String? = resolvedAvatarUrl()?.ifEmpty { null }

private fun AmityUser.avatarInitials(): String? =
    getDisplayName().toChatAvatarInitial()

/**
 * Shimmering member-list placeholder shown while the paged user list refreshes. Mirrors the 1-1
 * create flow's skeleton (AmityChannelCreateConversationPage) so both create surfaces present an
 * identical loading state instead of a bare spinner.
 */
@Composable
private fun UserListSkeleton() {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer",
    )
    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            AmityTheme.token(AmityColorToken.SurfaceListDefaultHover),
            AmityTheme.token(AmityColorToken.SurfaceListDefaultHover).copy(alpha = 0.4f),
            AmityTheme.token(AmityColorToken.SurfaceListDefaultHover),
        ),
        start = Offset(translateAnim - 200f, 0f),
        end = Offset(translateAnim, 0f),
    )
    Column(modifier = Modifier.fillMaxSize()) {
        repeat(10) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
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
                        .fillMaxWidth(0.45f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmerBrush),
                )
            }
        }
    }
}
