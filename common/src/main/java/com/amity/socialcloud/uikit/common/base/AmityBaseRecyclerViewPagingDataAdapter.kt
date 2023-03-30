package com.amity.socialcloud.uikit.common.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class AmityBaseRecyclerViewPagingDataAdapter<T : Any>(diffCallBack: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, RecyclerView.ViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        getViewHolder(
            LayoutInflater.from(parent.context).inflate(
                viewType, parent, false
            ), viewType
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as Binder<T>).bind(getItem(position), position)

    override fun getItemViewType(position: Int): Int = getLayoutId(position, getItem(position))

    protected abstract fun getLayoutId(position: Int, obj: T?): Int

    abstract fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder

    interface Binder<T> {
        fun bind(data: T?, position: Int)
    }
}