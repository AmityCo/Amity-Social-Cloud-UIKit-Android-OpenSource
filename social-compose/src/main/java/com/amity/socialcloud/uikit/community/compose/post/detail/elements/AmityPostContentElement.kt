package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtag
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtagMetadataGetter
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityExpandableText
import com.amity.socialcloud.uikit.common.ui.elements.HashtagMetadataGetter
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.google.gson.Gson
import com.google.gson.JsonObject

@Composable
fun AmityPostContentElement(
    modifier: Modifier = Modifier,
    post: AmityPost,
    style: AmityPostContentComponentStyle,
    boldedText: String? = null,
    onClick: () -> Unit,
    onMentionedUserClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
) {
    val mentionGetter = AmityMentionMetadataGetter(post.getMetadata() ?: JsonObject())
    val hashtagGetter = AmityHashtagMetadataGetter(post.getMetadata() ?: JsonObject())
    val text = remember(post.getPostId(), post.getEditedAt(), post.getUpdatedAt()) {
        (post.getData() as? AmityPost.Data.TEXT)?.getText() ?: ""
    }

    val title = remember(post.getPostId(), post.getEditedAt(), post.getUpdatedAt()) {
        (post.getData() as? AmityPost.Data.TEXT)?.getTitle() ?: ""
    }

    if (text.isEmpty() && title.isEmpty()) return

    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Display title if available
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = AmityTheme.typography.titleBold.copy(
                    fontSize = 17.sp,
                    color = AmityTheme.colors.base,
                    textAlign = TextAlign.Start
                )
            )

            if (text.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Display body text if available
        if (text.isNotEmpty()) {
            AmityExpandableText(
                modifier = modifier,
                text = text,
                mentionGetter = mentionGetter,
                mentionees = post.getMentionees(),
                hashtagGetter = hashtagGetter,
                style = AmityTheme.typography.body,
                intialExpand = style == AmityPostContentComponentStyle.DETAIL,
                boldWhenMatches = boldedText?.let { listOf(it) } ?: emptyList(),
                onClick = onClick,
                onMentionedUserClick = onMentionedUserClick,
                onHashtagClick = onHashtagClick,
            )
        }
    }
}