package com.amity.socialcloud.uikit.common.memberpicker.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.common.memberpicker.AmitySelectMemberItemDiffCallBack
import com.amity.socialcloud.uikit.common.memberpicker.listener.AmitySelectedMemberListener
import com.amity.socialcloud.uikit.common.memberpicker.viewholder.AmitySelectedMemberViewHolder
import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem

class AmitySelectedMemberAdapter(private val listener: AmitySelectedMemberListener) :
    AmityBaseRecyclerViewAdapter<AmitySelectMemberItem>() {

    override fun getLayoutId(position: Int, obj: AmitySelectMemberItem?): Int =
        R.layout.amity_item_selected_member

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
        AmitySelectedMemberViewHolder(view, listener)

    fun submitList(newList: List<AmitySelectMemberItem>) {
        setItems(newList, AmitySelectMemberItemDiffCallBack(list, newList))
    }
}