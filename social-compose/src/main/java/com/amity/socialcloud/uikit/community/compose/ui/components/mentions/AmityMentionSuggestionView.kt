package com.amity.socialcloud.uikit.community.compose.ui.components.mentions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityMentionSuggestionView(
    modifier: Modifier = Modifier,
    community: AmityCommunity? = null,
    keyword: String = "",
    heightIn: Dp = 200.dp, // Add default max height but allow it to be overridden
    shape: RoundedCornerShape = RoundedCornerShape(0.dp), // Default to no rounded corners
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

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = heightIn) // Always apply heightIn constraint
            .clip(shape) // Apply shape for rounded corners
            .background(AmityTheme.colors.background)
            .padding(vertical = 4.dp) // Remove horizontal padding here, only vertical spacing
    ) {
        items(
            count = suggestions.itemCount,
            key = { index -> suggestions.itemSnapshotList[index]?.getUserId() ?: index }
        ) {
            val user = suggestions[it] ?: return@items
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier  // Use Modifier here, not the passed-in modifier which may have padding
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp) // Apply consistent padding to each row
                    .clickableWithoutRipple {
                        onUserSelected(user)
                    },
            ) {
                AmityUserAvatarView(
                    user = user,
                    size = 40.dp,
                    modifier = Modifier, // Remove the padding here
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

@Preview(showBackground = true)
@Composable
fun AmityMentionSuggestionViewPreview() {
    AmityMentionSuggestionView(
        community = null,
        keyword = "keyword",
    ) {}
}