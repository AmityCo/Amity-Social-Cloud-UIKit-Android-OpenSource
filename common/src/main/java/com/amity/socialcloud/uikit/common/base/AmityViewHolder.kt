package com.amity.socialcloud.uikit.common.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AmityViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(data: T)
}