package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityExpandableText
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.google.gson.JsonObject

@Composable
fun AmityPostContentElement(
    modifier: Modifier = Modifier,
    post: AmityPost,
    style: AmityPostContentComponentStyle,
    boldedText: String? = null,
    onClick: () -> Unit,
    onMentionedUserClick: (String) -> Unit = {},
) {
    val mentionGetter = AmityMentionMetadataGetter(post.getMetadata() ?: JsonObject())
    val text = remember(post.getPostId(), post.getEditedAt(), post.getUpdatedAt()) {
        (post.getData() as? AmityPost.Data.TEXT)?.getText() ?: ""
    }

    if (text.isEmpty()) return

    Box(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        AmityExpandableText(
            modifier = modifier,
            text = text,
            mentionGetter = mentionGetter,
            mentionees = post.getMentionees(),
            style = AmityTheme.typography.bodyLegacy,
            intialExpand = style == AmityPostContentComponentStyle.DETAIL,
            boldWhenMatches = boldedText?.let { listOf(it) } ?: emptyList(),
            onClick = onClick,
            onMentionedUserClick = onMentionedUserClick,
        )
    }
}