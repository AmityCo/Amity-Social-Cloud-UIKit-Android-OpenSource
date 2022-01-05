package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.amity.socialcloud.uikit.common.base.AmityRecyclerViewAdapter
import java.util.*

class AmityPollDraftAnswerAdapter(private val removeCallback: (answer: DraftAnswer) -> Unit) :
    AmityRecyclerViewAdapter<AmityPollDraftAnswerAdapter.DraftAnswer, AmityPollDraftAnswerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmityPollDraftAnswerViewHolder {
        return AmityPollDraftAnswerViewHolder(parent.context, removeCallback)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.hashCode().toLong()
    }

    fun removeAnswer(answer: DraftAnswer) {
        val newAnswers = getItems().toMutableList()
        newAnswers.remove(answer)
        submitList(newAnswers, DiffCallback(getItems(), newAnswers))
    }

    fun addAnswer() {
        val newAnswers = getItems().toMutableList()
        newAnswers.add(DraftAnswer())
        submitList(newAnswers, DiffCallback(getItems(), newAnswers))
    }

    fun invalidateAnswers() {
        val newAnswers = getItems().toMutableList()
            .map { DraftAnswer(id = it.id, data = it.data, allowEmpty = false, requestFocusOnce = false) }

        submitList(newAnswers, DiffCallback(getItems(), newAnswers))
    }

    class DiffCallback(private val oldList: List<DraftAnswer>, private val newList: List<DraftAnswer>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    data class DraftAnswer(
        val id: String = UUID.randomUUID().toString(),
        var data: String = "",
        var allowEmpty: Boolean = true,
        var requestFocusOnce: Boolean = true
    )
}