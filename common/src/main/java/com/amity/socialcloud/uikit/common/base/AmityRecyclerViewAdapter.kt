package com.amity.socialcloud.uikit.common.base

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class AmityRecyclerViewAdapter<T, H : AmityViewHolder<T>>
    : RecyclerView.Adapter<H>() {

    private val list: ArrayList<T> = arrayListOf()

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: H, position: Int) {
        holder.bind(getItem(position))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(listItems: List<T>, diffCallBack: DiffUtil.Callback? = null) {
        val diffResult = diffCallBack?.let { DiffUtil.calculateDiff(diffCallBack) }
        list.clear()
        list.addAll(listItems)
        if (diffResult != null) {
            diffResult.dispatchUpdatesTo(this)
        } else {
            notifyDataSetChanged()
        }
    }

    fun getItems(): List<T> {
        return list
    }

    fun getItem(position: Int): T {
        return list[position]
    }
}