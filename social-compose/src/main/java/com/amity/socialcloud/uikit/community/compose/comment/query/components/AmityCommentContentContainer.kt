package com.amity.socialcloud.uikit.community.compose.comment.query.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.ui.elements.AmityExpandableText
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.isCreatorCommunityModerator
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.AmityCommentModeratorBadge
import com.google.gson.JsonObject

@Composable
fun AmityCommentContentContainer(
    modifier: Modifier = Modifier,
    comment: AmityComment,
) {
    val mentionGetter = remember {
        AmityMentionMetadataGetter(comment.getMetadata() ?: JsonObject())
    }
    val commentText = remember(comment.getCommentId(), comment.getEditedAt()) {
        (comment.getData() as? AmityComment.Data.TEXT)?.getText() ?: ""
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .background(
                color = AmityTheme.colors.baseShade4,
                shape = RoundedCornerShape(
                    topEnd = 12.dp,
                    bottomStart = 12.dp,
                    bottomEnd = 12.dp,
                )
            )
            .padding(12.dp)
    ) {
        Text(
            text = comment.getCreator()?.getDisplayName() ?: "",
            style = AmityTheme.typography.caption,
            modifier = modifier.testTag("comment_list/comment_bubble_creator_display_name")
        )

        if (comment.isCreatorCommunityModerator()) {
            AmityCommentModeratorBadge()
        }

        AmityExpandableText(
            text = commentText,
            mentionGetter = mentionGetter,
            mentionees = comment.getMentionees(),
            style = AmityTheme.typography.body,
            modifier = modifier.testTag("comment_list/comment_bubble_comment_text_view")
        )
    }
}