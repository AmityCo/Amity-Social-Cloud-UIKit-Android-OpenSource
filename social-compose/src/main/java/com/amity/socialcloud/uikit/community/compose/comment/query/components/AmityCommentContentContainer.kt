package com.amity.socialcloud.uikit.community.compose.comment.query.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.ui.elements.AmityExpandableText
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.isCreatorCommunityModerator
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.AmityCommentModeratorBadge
import com.google.gson.JsonObject

@Composable
fun AmityCommentContentContainer(
    modifier: Modifier = Modifier,
    comment: AmityComment,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.commentTrayComponentBehavior
    }

    val mentionGetter = remember(comment.getCommentId(), comment.getEditedAt()) {
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = comment.getCreator()?.getDisplayName()?.trim() ?: "",
                style = AmityTheme.typography.captionLegacy,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier
                    .weight(1f, fill = false)
                    .testTag("comment_list/comment_bubble_creator_display_name")
                    .clickableWithoutRipple {
                        behavior.goToUserProfilePage(context, comment.getCreatorId())
                    }
            )

            Spacer(modifier = Modifier.width(4.dp))

            val isBrandCreator = comment.getCreator()?.isBrand() == true
            if (isBrandCreator) {
                val badge = R.drawable.amity_ic_brand_badge
                Image(
                    painter = painterResource(id = badge),
                    contentDescription = "",
                    modifier = Modifier
                        .size(18.dp)
                        .testTag("comment_list/brand_user_icon")
                )
            }
        }

        if (comment.isCreatorCommunityModerator()) {
            AmityCommentModeratorBadge()
        }

        AmityExpandableText(
            text = commentText,
            mentionGetter = mentionGetter,
            mentionees = comment.getMentionees(),
            style = AmityTheme.typography.bodyLegacy,
            modifier = modifier.testTag("comment_list/comment_bubble_comment_text_view"),
            onMentionedUserClick = {
                behavior.goToUserProfilePage(context, it)
            }
        )
    }
}