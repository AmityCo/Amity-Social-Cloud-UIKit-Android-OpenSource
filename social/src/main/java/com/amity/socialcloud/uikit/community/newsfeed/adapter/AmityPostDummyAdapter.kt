package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.community.databinding.AmityItemPostDummyBinding
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityPostDummyItem


class AmityPostDummyAdapter : RecyclerView.Adapter<AmityPostDummyViewHolder>() {

    val list: ArrayList<AmityPostDummyItem> = arrayListOf()

    fun setItems(listItems: List<AmityPostDummyItem>) {
        list.clear()
        list.addAll(listItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AmityPostDummyViewHolder {
        val itemBinding = AmityItemPostDummyBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        )
        return AmityPostDummyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AmityPostDummyViewHolder, position: Int) {
        // do nothing
    }

    override fun getItemCount(): Int {
        return list.size
    }

}