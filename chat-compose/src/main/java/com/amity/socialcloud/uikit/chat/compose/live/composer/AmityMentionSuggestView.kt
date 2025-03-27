package com.amity.socialcloud.uikit.chat.compose.live.composer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.live.AmityLiveChatPageViewModel
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityAvatarType
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageAvatarView
import com.amity.socialcloud.uikit.chat.compose.live.mention.AmityMentionSuggestion
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityMentionSuggestionView(
    modifier: Modifier = Modifier,
    viewModel: AmityLiveChatPageViewModel,
    keyword: String,
    onClick: (AmityMentionSuggestion) -> Unit
) {

    val suggestions = remember(keyword) {
        if(keyword.isEmpty()) {
            viewModel.getChannelMembers()
        } else {
            viewModel.searchChannelMembers(keyword)
        }
    }.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier
            .background(color = AmityTheme.colors.baseShade4)
            .fillMaxWidth()
            .requiredHeightIn(0.dp, 156.dp)

    ) {
        items(
            count = suggestions.itemCount,
            key = { index -> index }
        ) {
            val suggestion = suggestions[it] ?: return@items
            val text = if(suggestion is AmityMentionSuggestion.USER) suggestion.user.getDisplayName() ?: "" else "All"
            val avatarUrl = if(suggestion is AmityMentionSuggestion.USER) suggestion.user.getAvatar()?.getUrl(AmityImage.Size.SMALL) else null
            val isBrandUser = suggestion is AmityMentionSuggestion.USER && suggestion.user.isBrand()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .padding(horizontal = 16.dp, vertical = 0.dp)
                    .fillMaxWidth()
                    .height(52.dp)
                    .clickableWithoutRipple {
                        onClick(suggestion)
                    },
            ) {
                AmityMessageAvatarView(
                    avatarUrl = avatarUrl,
                    avatarType = if(suggestion is AmityMentionSuggestion.USER) AmityAvatarType.USER else AmityAvatarType.MENTION_ALL,
                    size = 32.dp,
                )
                Text(
                    text = text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = AmityTheme.colors.baseShade1,
                    ),
                    modifier = Modifier.padding(start = 16.dp)
                )

                if(isBrandUser) {
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

                Spacer(modifier = Modifier.weight(1f))

                if(suggestion is AmityMentionSuggestion.CHANNEL){
                    Text(
                        text = "Notify everyone",
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight(400),
                        color = AmityTheme.colors.baseShade2,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityMentionSuggestionViewPreview() {
    AmityMentionSuggestionView(
        keyword = "keyword",
        viewModel = AmityLiveChatPageViewModel(""),
    ) {}
}