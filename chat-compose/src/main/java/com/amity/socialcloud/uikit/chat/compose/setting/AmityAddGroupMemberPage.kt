package com.amity.socialcloud.uikit.chat.compose.setting

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageAvatarView
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityAddGroupMemberPage(
    modifier: Modifier = Modifier,
    channelId: String,
) {
    val viewModel = remember { AmityAddGroupMemberPageViewModel() }
    val memberListViewModel = remember { AmityGroupMemberListPageViewModel(channelId) }
    val selectedUserIds by viewModel.selectedUserIds.collectAsState()
    val selectedUsers by viewModel.selectedUsers.collectAsState()
    val context = LocalContext.current
    var searchKeyword by remember { mutableStateOf("") }
    val currentUserId = AmityCoreClient.getUserId()

    val searchResults = viewModel.searchUsers(searchKeyword).collectAsLazyPagingItems()

    // Snackbar messages
    val successMemberAdded = amityChatString("chat.success.member.added")
    val errorAddMember = amityChatString("chat.add.group.member.toast.failed")

    AmityBasePage(pageId = "add_group_member_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background)
                .imePadding(),
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_close,
                    ),
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.CenterStart)
                        .clickableWithoutRipple {
                            (context as? Activity)?.finish()
                        },
                    tint = AmityTheme.colors.base,
                )

                Text(
                    text = amityChatString("chat.add.member.title"),
                    style = AmityTheme.typography.titleLegacy,
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )
            }

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
                    value = searchKeyword,
                    onValueChange = { searchKeyword = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 15.sp,
                        color = AmityTheme.colors.base,
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (searchKeyword.isEmpty()) {
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

            // Selected users horizontal list
            if (selectedUsers.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    items(selectedUsers) { user ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(56.dp),
                        ) {
                            Box(modifier = Modifier.size(40.dp)) {
                                AmityMessageAvatarView(
                                    avatarUrl = user.getAvatar()?.getUrl(AmityImage.Size.SMALL) ?: "",
                                    displayName = user.getDisplayName(),
                                    size = 40.dp,
                                )
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .align(Alignment.TopEnd)
                                        .clip(CircleShape)
                                        .background(AmityTheme.colors.baseShade2)
                                        .clickableWithoutRipple {
                                            viewModel.removeUser(user.getUserId())
                                        },
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(
                                            id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_close,
                                        ),
                                        contentDescription = "Remove",
                                        modifier = Modifier.size(10.dp),
                                        tint = AmityTheme.colors.background,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = user.getDisplayName() ?: "",
                                style = AmityTheme.typography.captionLegacy.copy(
                                    fontSize = 11.sp,
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
                HorizontalDivider(color = AmityTheme.colors.baseShade4)
            }

            // User list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                items(searchResults.itemCount) { index ->
                    val user = searchResults[index] ?: return@items
                    if (user.getUserId() == currentUserId) return@items

                    val isSelected = selectedUserIds.contains(user.getUserId())

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleUser(user) }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AmityMessageAvatarView(
                            avatarUrl = user.getAvatar()?.getUrl(AmityImage.Size.SMALL) ?: "",
                            displayName = user.getDisplayName(),
                            size = 40.dp,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = user.getDisplayName() ?: user.getUserId(),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        // Circular checkbox
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .then(
                                    if (isSelected) {
                                        Modifier.background(AmityTheme.colors.primary)
                                    } else {
                                        Modifier.border(
                                            width = 2.dp,
                                            color = AmityTheme.colors.baseShade3,
                                            shape = CircleShape,
                                        )
                                    }
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(
                                        id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_check,
                                    ),
                                    contentDescription = "Selected",
                                    modifier = Modifier.size(14.dp),
                                    tint = AmityTheme.colors.background,
                                )
                            }
                        }
                    }
                }
            }

            // Add member button
            Button(
                onClick = {
                    memberListViewModel.addMembers(
                        userIds = selectedUserIds.toList(),
                        onSuccess = {
                            AmityUIKitSnackbar.publishSnackbarMessage(successMemberAdded)
                            (context as? Activity)?.finish()
                        },
                        onError = {
                            AmityUIKitSnackbar.publishSnackbarErrorMessage(errorAddMember)
                        }
                    )
                },
                enabled = selectedUsers.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmityTheme.colors.primary,
                    disabledContainerColor = AmityTheme.colors.primaryShade2,
                ),
            ) {
                Text(
                    text = amityChatString("chat.add.member.button"),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AmityTheme.colors.background,
                    ),
                )
            }
        }
    }
}
