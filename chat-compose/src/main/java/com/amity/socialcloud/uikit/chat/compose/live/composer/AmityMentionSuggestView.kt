package com.amity.socialcloud.uikit.chat.compose.live.composer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.file.AmityImage
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
            .background(color = Color(0xFF292B32))
            .fillMaxWidth()
            .requiredHeightIn(0.dp, 156.dp)

    ) {
        items(
            count = suggestions.itemCount,
            key = { index -> index }
        ) {
            val suggestion = suggestions[it] ?: return@items
            val text = if(suggestion is AmityMentionSuggestion.USER) suggestion.user?.getDisplayName() ?: "" else "All"
            val avatarUrl = if(suggestion is AmityMentionSuggestion.USER) suggestion.user?.getAvatar()?.getUrl(AmityImage.Size.SMALL) else null
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 0.dp)
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
                    overflow = TextOverflow.Ellipsis,
                    style = AmityTheme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFFFFFF)//AmityTheme.colors.baseShade1,
                    )
                )
                if(suggestion is AmityMentionSuggestion.CHANNEL){
                    Spacer(modifier = Modifier.weight(1f))
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