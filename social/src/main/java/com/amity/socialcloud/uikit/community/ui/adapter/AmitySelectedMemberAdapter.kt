package com.amity.socialcloud.uikit.community.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.data.AmitySelectMemberItem
import com.amity.socialcloud.uikit.community.ui.clickListener.AmitySelectedMemberListener
import com.amity.socialcloud.uikit.community.ui.viewHolder.AmitySelectedMemberViewHolder
import com.amity.socialcloud.uikit.community.utils.AmitySelectMemberItemDiffCallBack

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