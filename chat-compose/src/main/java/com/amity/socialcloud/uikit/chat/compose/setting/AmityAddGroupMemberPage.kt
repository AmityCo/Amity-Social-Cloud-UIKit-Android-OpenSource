package com.amity.socialcloud.uikit.chat.compose.setting

import android.app.Activity
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageAvatarView
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityBoxedInputStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityInput
import com.amity.socialcloud.uikit.common.ui.atoms.AmityInputSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityInputVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityMainButtonSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySelection
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySelectionVariant
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
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

    AmityBasePage(pageId = "add_group_member_page", useAmityToast = true) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.token(AmityColorToken.SurfacePageBackgroundDefault))
                .imePadding(),
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
                    icon = CommonR.drawable.amity_ic_cross_r,
                    onClick = {
                        (context as? Activity)?.finish()
                    },
                    modifier = Modifier.align(Alignment.CenterStart),
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                AmityInput(
                    variant = AmityInputVariant.BOXED,
                    modifier = Modifier.fillMaxWidth(),
                    value = searchKeyword,
                    placeholder = amityChatString("chat.search.placeholder"),
                    leadingIcon = CommonR.drawable.amity_ic_search_r,
                    trailingIcon = if (searchKeyword.isNotEmpty()) CommonR.drawable.amity_ic_clear_r else null,
                    size = AmityInputSize.M,
                    boxedStyle = AmityBoxedInputStyle.SQUARE,
                    onChangeText = { searchKeyword = it },
                )

                if (searchKeyword.isNotEmpty()) {
                    // Transparent tap target over the atom's own trailing clear glyph — AmityInput
                    // renders the icon but exposes no click callback for it. Height matches the
                    // AmityInputSize.M Boxed Input row height (48.dp) selected above.
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .height(48.dp)
                            .width(32.dp)
                            .clickableWithoutRipple {
                                searchKeyword = ""
                            },
                    )
                }
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
                                    borderWidth = 2,
                                )
                                AmityButton(
                                    variant = AmityButtonVariant.ICON,
                                    style = AmityButtonStyle.TRANSPARENT,
                                    hierarchy = AmityButtonHierarchy.PRIMARY,
                                    iconSize = AmityIconButtonSize.SIZE16,
                                    icon = CommonR.drawable.amity_ic_cross_r,
                                    contentDescription = "Remove",
                                    onClick = { viewModel.removeUser(user.getUserId()) },
                                    modifier = Modifier.align(Alignment.TopEnd),
                                )
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
                AmityDivider(variant = AmityDividerVariant.Post)
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
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AmityMessageAvatarView(
                            avatarUrl = user.getAvatar()?.getUrl(AmityImage.Size.SMALL) ?: "",
                            displayName = user.getDisplayName(),
                            size = 40.dp,
                            borderWidth = 2,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            Text(
                                text = user.getDisplayName() ?: user.getUserId(),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault),
                                ),
                                modifier = Modifier.weight(1f, fill = false),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            if (user.isBrand()) {
                                Image(
                                    painter = painterResource(id = CommonR.drawable.amity_ic_brand_badge),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .testTag("user_view/brand_user_icon"),
                                )
                            }
                        }
                        // Circular checkbox (CheckboxAtomic) — row click owns the toggle
                        AmitySelection(
                            variant = AmitySelectionVariant.CHECKBOX,
                            isSelected = isSelected,
                            icon = CommonR.drawable.amity_ic_scale_2_s,
                        )
                    }
                }
            }

            // Add member button
            AmityButton(
                variant = AmityButtonVariant.MAIN,
                style = AmityButtonStyle.FILLED,
                hierarchy = AmityButtonHierarchy.PRIMARY,
                mainSize = AmityMainButtonSize.LG,
                label = amityChatString("chat.add.member.button"),
                enabled = selectedUsers.isNotEmpty(),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            )
        }
    }
}
