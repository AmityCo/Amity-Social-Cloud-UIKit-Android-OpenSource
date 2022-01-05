package com.amity.socialcloud.uikit.common.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

@Deprecated("Use AmityRecyclerViewAdapter instead")
abstract class AmityBaseRecyclerViewAdapter<T> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val list: ArrayList<T> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        getViewHolder(
            LayoutInflater.from(parent.context).inflate(viewType, parent, false),
            viewType
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as IBinder<T>).bind(list[position], position)

    override fun getItemViewType(position: Int): Int = getLayoutId(position, list[position])

    override fun getItemCount(): Int = list.size

    protected abstract fun getLayoutId(position: Int, obj: T?): Int

    abstract fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder

    fun setItems(listItems: List<T>, diffCallBack: DiffUtil.Callback) {
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        list.clear()
        list.addAll(listItems)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setItems(listItems: List<T>) {
        list.clear()
        list.addAll(listItems)
        notifyDataSetChanged()
    }

    fun getItems(): List<T> {
        return list
    }

    fun getItem(position: Int) :T {
        return list[position]
    }

    interface IBinder<T> {
        fun bind(data: T?, position: Int)
    }


}