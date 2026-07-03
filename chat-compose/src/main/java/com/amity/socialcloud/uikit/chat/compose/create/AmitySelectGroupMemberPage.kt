package com.amity.socialcloud.uikit.chat.compose.create

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityRoundCheckbox
import com.amity.socialcloud.uikit.common.ui.elements.AmitySearchBarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite

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

    AmityBasePage(pageId = "select_group_member_page") {
        Column(
            modifier = modifier.fillMaxSize(),
        ) {
            // Header
            Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_close),
                        contentDescription = "Close",
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterStart)
                            .clickableWithoutRipple {
                                onBack.invoke()
                            },
                        tint = AmityTheme.colors.base,
                    )

                    Text(
                        text = amityChatString("chat.select.members.title"),
                        style = AmityTheme.typography.titleLegacy,
                        modifier = Modifier
                            .padding(vertical = 17.dp)
                            .align(Alignment.Center),
                    )

                    // Next button in AppBar (right side)
                    Text(
                        text = amityChatString("chat.next"),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (selectedUsers.isNotEmpty()) AmityTheme.colors.primary
                                    else AmityTheme.colors.baseShade3,
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(vertical = 17.dp)
                            .then(
                                if (selectedUsers.isNotEmpty()) Modifier.clickableWithoutRipple { onNext(selectedUsers) }
                                else Modifier
                            ),
                    )
                }

                HorizontalDivider(color = AmityTheme.colors.baseShade4)
                Spacer(modifier = Modifier.height(4.dp))

                AmitySearchBarView(
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
                    HorizontalDivider(color = AmityTheme.colors.baseShade4)
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
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = AmityTheme.colors.primary,
                            )
                        }
                    }
                    AmitySelectGroupMemberPageViewModel.UserListState.SHORT_INPUT -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .imePadding()
                                .padding(32.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.amity_ic_empty_search),
                                contentDescription = "Search",
                                modifier = Modifier.size(60.dp),
                                alpha = 0.5f,
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = amityChatString("chat.search.min.chars"),
                                style = AmityTheme.typography.titleBold.copy(
                                    textAlign = TextAlign.Center,
                                    color = AmityTheme.colors.baseShade3,
                                ),
                            )
                        }
                    }
                    AmitySelectGroupMemberPageViewModel.UserListState.EMPTY -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = amityChatString("chat.no.users.found"),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontSize = 15.sp,
                                    color = AmityTheme.colors.baseShade2,
                                    textAlign = TextAlign.Center,
                                ),
                            )
                        }
                    }
                    AmitySelectGroupMemberPageViewModel.UserListState.SUCCESS -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                        ) {
                            items(
                                count = lazyPagingItems.itemCount,
                                key = { index -> lazyPagingItems[index]?.getUserId() ?: index }
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AmityUserAvatarView(user = user, size = 40.dp)

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = user.getDisplayName() ?: "",
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = AmityTheme.colors.baseInverse,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        if (user.isBrand()) {
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

        AmityRoundCheckbox(
            isChecked = isSelected,
            onValueChange = {
                onToggle.invoke()
            },
            checkedColor = AmityTheme.colors.primary,
        )
    }
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
            AmityUserAvatarView(
                user = user,
                size = 40.dp,
            )
            // Remove badge
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(AmityTheme.colors.baseShade2)
                    .align(Alignment.TopEnd)
                    .clickable(onClick = onRemove),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "✕",
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 10.sp,
                        color = amityColorWhite,
                    ),
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = user.getDisplayName() ?: "",
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 11.sp,
                color = AmityTheme.colors.baseShade1,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
