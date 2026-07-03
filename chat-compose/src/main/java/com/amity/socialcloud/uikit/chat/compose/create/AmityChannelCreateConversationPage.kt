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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
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
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.conversation.AmityChatPageActivity
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmitySearchBarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserListItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityChannelCreateConversationPage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    val viewModel = viewModel<AmityChannelCreateConversationPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    val creationState by viewModel.creationState.collectAsState()

    var keyword by remember { mutableStateOf("") }

    val lazyPagingItems = remember(keyword) {
        viewModel.searchUsers(keyword)
    }.collectAsLazyPagingItems()

    AmityBasePage(pageId = "create_conversation_page") {
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
                    painter = painterResource(id = R.drawable.amity_ic_close3),
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
                    text = amityChatString("chat.create.conversation.title"),
                    style = AmityTheme.typography.titleLegacy,
                    modifier = Modifier
                        .padding(vertical = 17.dp)
                        .align(Alignment.Center),
                )
            }

            HorizontalDivider(color = AmityTheme.colors.baseShade4)
            Spacer(modifier = Modifier.height(4.dp))

            // Search bar
            AmitySearchBarView(
                hint = amityChatString("chat.search.placeholder"),
            ) {
                keyword = it
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Loading overlay when creating conversation
            if (creationState is AmityChannelCreateConversationPageViewModel.CreationState.Loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = AmityTheme.colors.primary,
                    )
                }
            } else {
                // User list
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
                    AmityChannelCreateConversationPageViewModel.UserListState.EMPTY -> {
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
                    AmityChannelCreateConversationPageViewModel.UserListState.SUCCESS -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(
                                count = lazyPagingItems.itemCount,
                                key = { index -> lazyPagingItems[index]?.getUserId() ?: index }
                            ) { index ->
                                val user = lazyPagingItems[index] ?: return@items
                                AmityUserListItem(
                                    user = user,
                                    showRightMenu = false,
                                    onUserClick = { selectedUser ->
                                        viewModel.createConversation(
                                            userId = selectedUser.getUserId(),
                                            onSuccess = { channelId ->
                                                context.startActivity(
                                                    AmityChatPageActivity.newIntent(
                                                        context, channelId
                                                    )
                                                )
                                                (context as? Activity)?.finish()
                                            },
                                            onError = {
                                                Toast.makeText(context, R.string.amity_chat_load_error, Toast.LENGTH_SHORT).show()
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
            AmityTheme.colors.baseShade4,
            AmityTheme.colors.baseShade4.copy(alpha = 0.4f),
            AmityTheme.colors.baseShade4,
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
