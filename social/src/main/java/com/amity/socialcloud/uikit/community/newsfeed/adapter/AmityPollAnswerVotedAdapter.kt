package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.ViewGroup
import com.amity.socialcloud.sdk.model.social.poll.AmityPollAnswer
import com.amity.socialcloud.uikit.common.base.AmityRecyclerViewAdapter

private const val MAX_ITEM_COUNT = 2

class AmityPollAnswerVotedAdapter(
    private val totalVoteCount: Int,
    private val showFullContent: Boolean
) : AmityRecyclerViewAdapter<AmityPollAnswer, AmityPollAnswerVotedViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AmityPollAnswerVotedViewHolder {
        return AmityPollAnswerVotedViewHolder(parent.context, totalVoteCount)
    }

    override fun getItemCount(): Int {
        return when (showFullContent) {
            true -> super.getItemCount()
            false -> MAX_ITEM_COUNT
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.hashCode().toLong()
    }
}