package com.amity.socialcloud.uikit.chat.compose.create

import android.app.Activity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.conversation.AmityChatPageActivity
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyState
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyStateVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.atoms.AmitySearchBar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityAvatarSize
import com.amity.socialcloud.uikit.common.ui.atoms.AmityListItem
import com.amity.socialcloud.uikit.common.ui.atoms.AmityListItemVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityListLeadingContent
import com.amity.socialcloud.uikit.common.ui.atoms.AmityListLeadingType
import com.amity.socialcloud.uikit.common.utils.resolvedAvatarUrl
import com.amity.socialcloud.uikit.chat.compose.common.toChatAvatarInitial
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken

@Composable
fun AmityChannelCreateConversationPage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    val viewModel = viewModel<AmityChannelCreateConversationPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    var keyword by remember { mutableStateOf("") }

    val lazyPagingItems = remember(keyword) {
        viewModel.searchUsers(keyword)
    }.collectAsLazyPagingItems()

    AmityBasePage(pageId = "create_conversation_page", useAmityToast = true) {
        Column(
            modifier = modifier.fillMaxSize(),
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            ) {
                AmityButton(
                    variant = AmityButtonVariant.ICON,
                    onClick = {
                        (context as? Activity)?.finish()
                    },
                    hierarchy = AmityButtonHierarchy.SECONDARY,
                    style = AmityButtonStyle.GHOST,
                    iconSize = AmityIconButtonSize.SIZE32,
                    icon = CommonR.drawable.amity_ic_cross_r,
                    modifier = Modifier.align(Alignment.CenterStart),
                )

                Text(
                    text = amityChatString("chat.create.conversation.title"),
                    style = AmityTheme.typography.titleLegacy,
                    color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTitleDefault),
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )
            }

            AmityDivider(variant = AmityDividerVariant.Content, inset = false)
            Spacer(modifier = Modifier.height(4.dp))

            // Search bar
            AmitySearchBar(
                hint = amityChatString("chat.search.placeholder"),
            ) {
                keyword = it
            }

            Spacer(modifier = Modifier.height(8.dp))

            // No creation overlay by design: picking a user creates the 1-1 conversation and
            // navigates straight into the chat (onSuccess below).
            val loadState = AmityChannelCreateConversationPageViewModel.UserListState.from(
                loadState = lazyPagingItems.loadState.refresh,
                itemCount = lazyPagingItems.itemCount,
                keywordLength = keyword.length,
                minKeywordLength = viewModel.minKeywordLength,
            )

            when (loadState) {
                AmityChannelCreateConversationPageViewModel.UserListState.LOADING -> {
                    UserListSkeleton()
                }
                AmityChannelCreateConversationPageViewModel.UserListState.SHORT_INPUT -> {
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
                AmityChannelCreateConversationPageViewModel.UserListState.EMPTY -> {
                    AmityEmptyState(
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding()
                            .padding(horizontal = 24.dp),
                        variant = AmityEmptyStateVariant.ICON,
                        icon = CommonR.drawable.amity_ic_search_cross_l,
                        title = amityChatString("chat.search.no.results"),
                    )
                }
                AmityChannelCreateConversationPageViewModel.UserListState.SUCCESS -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(
                            count = lazyPagingItems.itemCount,
                            key = { index -> lazyPagingItems[index]?.getUserId() ?: index }
                        ) { index ->
                            val user = lazyPagingItems[index] ?: return@items
                            // List atom row (the legacy AmityUserListItem is no longer used in chat).
                            AmityListItem(
                                variant = AmityListItemVariant.DEFAULT,
                                title = user.getDisplayName() ?: "",
                                leadingType = AmityListLeadingType.AVATAR,
                                leading = AmityListLeadingContent(
                                    type = AmityListLeadingType.AVATAR,
                                    avatarUrl = user.resolvedAvatarUrl()?.ifEmpty { null },
                                    // Chat avatar rule: single first letter.
                                    avatarInitials = user.getDisplayName().toChatAvatarInitial(),
                                    avatarSize = AmityAvatarSize.Size40,
                                ),
                                onPress = {
                                    viewModel.createConversation(
                                        userId = user.getUserId(),
                                        onSuccess = { channelId ->
                                            context.startActivity(
                                                AmityChatPageActivity.newIntent(
                                                    context, channelId
                                                )
                                            )
                                            (context as? Activity)?.finish()
                                        },
                                        onError = {
                                            AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                                context.getString(R.string.amity_chat_load_error)
                                            )
                                        },
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

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
