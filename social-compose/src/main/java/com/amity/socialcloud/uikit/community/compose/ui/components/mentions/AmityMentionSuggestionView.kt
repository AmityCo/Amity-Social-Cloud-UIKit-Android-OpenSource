package com.amity.socialcloud.uikit.community.compose.ui.components.mentions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityMentionSuggestionView(
    modifier: Modifier = Modifier,
    community: AmityCommunity? = null,
    keyword: String = "",
    heightIn: Dp = 200.dp,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    onUserSelected: (AmityUser) -> Unit = {}
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityMentionViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val suggestions = remember(community?.getCommunityId(), keyword) {
        if (community == null || community.isPublic()) {
            viewModel.searchUsersMention(keyword)
        } else {
            viewModel.searchCommunityUsersMention(community.getCommunityId(), keyword)
        }
    }.collectAsLazyPagingItems()

    val isLoading = suggestions.loadState.refresh is LoadState.Loading
    val isNoResultsState = suggestions.loadState.refresh is LoadState.NotLoading &&
            suggestions.itemCount == 0

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = heightIn)
            .clip(shape)
            .background(AmityTheme.colors.background)
    ) {
        when {
            isLoading -> {
                // Loading state - show shimmer items
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    items(3) {
                        UserMentionShimmerItem()
                    }
                }
            }
            isNoResultsState -> {
                // No results state: API returned empty
                UserMentionPlaceholder(
                    message = "No results found",
                    modifier = Modifier.height(heightIn)
                )
            }
            else -> {
                // Has results: show user list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    items(
                        count = suggestions.itemCount,
                        key = { index -> suggestions.itemSnapshotList[index]?.getUserId() ?: index }
                    ) {
                        val user = suggestions[it] ?: return@items
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .clickableWithoutRipple {
                                    onUserSelected(user)
                                },
                        ) {
                            AmityUserAvatarView(
                                user = user,
                                size = 40.dp,
                                modifier = Modifier,
                            )

                            Row(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    maxLines = 1,
                                    text = user.getDisplayName() ?: "",
                                    overflow = TextOverflow.Ellipsis,
                                    style = AmityTheme.typography.bodyLegacy,
                                )

                                val isBrandUser = user.isBrand()
                                if (isBrandUser) {
                                    val badge = R.drawable.amity_ic_brand_badge
                                    Image(
                                        painter = painterResource(id = badge),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(20.dp)
                                            .padding(start = 4.dp)
                                            .testTag("mention_suggestion_view/brand_user_icon")
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserMentionPlaceholder(
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.amity_ic_search_placeholder),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
        )
        Text(
            text = message,
            style = AmityTheme.typography.body.copy(fontSize = 15.sp),
            color = AmityTheme.colors.baseShade3,
            modifier = Modifier.padding(top = 12.dp),
        )
    }
}

@Composable
private fun UserMentionShimmerItem(
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        // Shimmer avatar (40x40 circle)
        Box(
            modifier = Modifier
                .size(40.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = CircleShape
                )
        )

        // Shimmer text line
        Box(
            modifier = Modifier
                .width(123.dp)
                .height(12.dp)
                .shimmerBackground(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(6.dp)
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AmityMentionSuggestionViewPreview() {
    AmityMentionSuggestionView(
        community = null,
        keyword = "keyword",
    ) {}
}