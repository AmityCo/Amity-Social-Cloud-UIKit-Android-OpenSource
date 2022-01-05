package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.ViewGroup
import android.widget.CompoundButton
import com.amity.socialcloud.sdk.social.feed.AmityPoll
import com.amity.socialcloud.sdk.social.feed.AmityPollAnswer
import com.amity.socialcloud.uikit.common.base.AmityRecyclerViewAdapter
import com.google.android.material.card.MaterialCardView

private const val MAX_ITEM_COUNT = 2
private const val SINGLE_ANSWER_VIEW_TYPE = 1
private const val MULTIPLE_ANSWERS_VIEW_TYPE = 2

class AmityPollAnswerAdapter(
    private val answerType: AmityPoll.AnswerType,
    private val showFullContent: Boolean,
    private val isEnabled: Boolean,
    private val voteCallback: (answerId: String, isSelected: Boolean, holder: MaterialCardView?) -> Unit
) : AmityRecyclerViewAdapter<AmityPollAnswer, AmityPollAnswerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmityPollAnswerViewHolder {
        return when (viewType) {
            SINGLE_ANSWER_VIEW_TYPE -> AmityPollSingleAnswerViewHolder(parent.context, isEnabled, voteCallback)
            else -> AmityPollMultipleAnswersViewHolder(parent.context, isEnabled, voteCallback)
        }
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

    override fun getItemViewType(position: Int): Int {
        return when (answerType) {
            AmityPoll.AnswerType.SINGLE -> SINGLE_ANSWER_VIEW_TYPE
            else -> MULTIPLE_ANSWERS_VIEW_TYPE
        }
    }
}