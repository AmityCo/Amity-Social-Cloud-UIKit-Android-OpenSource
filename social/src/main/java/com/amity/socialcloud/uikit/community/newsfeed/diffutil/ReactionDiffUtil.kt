package com.amity.socialcloud.uikit.community.newsfeed.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.amity.socialcloud.sdk.model.core.reaction.AmityReaction

class ReactionDiffUtil : DiffUtil.ItemCallback<AmityReaction>() {

    override fun areItemsTheSame(oldItem: AmityReaction, newItem: AmityReaction): Boolean {
        return oldItem.getReactionId() == newItem.getReactionId()
    }

    override fun areContentsTheSame(oldItem: AmityReaction, newItem: AmityReaction): Boolean {
        return oldItem.getCreatorId() == newItem.getCreatorId()
                && oldItem.getCreatorDisplayName() == newItem.getCreatorDisplayName()
                && oldItem.getReactionName() == newItem.getReactionName()
    }

}