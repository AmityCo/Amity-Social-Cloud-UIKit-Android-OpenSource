package com.amity.socialcloud.uikit.common.reaction.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.reaction.AmityReaction
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityReactionListItem(
    modifier: Modifier = Modifier,
    reaction: AmityReaction,
    onRemoveReaction: (AmityReaction) -> Unit = {},
    onUserClick: (String) -> Unit = {},
) {
    val displayName = reaction.getCreator()?.getDisplayName() ?: ""
    val reactionName = reaction.getReactionName()
    val isMyReaction = reaction.getCreatorId() == AmityCoreClient.getUserId()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickableWithoutRipple {
                if (isMyReaction) {
                    onRemoveReaction.invoke(reaction)
                }
            }
    ) {
        AmityUserAvatarView(
            size = 40.dp,
            user = reaction.getCreator(),
            modifier = modifier.clickableWithoutRipple {
                onUserClick(reaction.getCreatorId())
            }
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .padding(start = 12.dp)
                .fillMaxHeight()
        ) {
            Text(
                text = displayName,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = AmityTheme.colors.base
                ),
                modifier = modifier.clickableWithoutRipple {
                    onUserClick(reaction.getCreatorId())
                }
            )

            if (isMyReaction) {
                Text(
                    text = "Tap to remove reaction",
                    style = AmityTheme.typography.captionLegacy.copy(
                        color = AmityTheme.colors.baseShade1
                    ),
                )
            }
        }


        Spacer(modifier = Modifier.weight(1f))

        val iconId = AmityMessageReactions.getList()
            .find { reaction ->
                reaction.name == reactionName
            }?.icon ?: R.drawable.amity_ic_message_reaction_missing

        Image(
            imageVector = ImageVector.vectorResource(id = iconId),
            contentDescription = "",
            modifier = Modifier
                .size(24.dp)
        )

    }
}

@Preview(showBackground = true)
@Composable
fun AmityReactionListItemPreview() {
    AmityReactionListItem(
        reaction = AmityReaction::class.java.newInstance()
    )
}