package com.amity.socialcloud.uikit.common.memberpicker.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.common.memberpicker.AmitySelectMemberItemDiffCallBack
import com.amity.socialcloud.uikit.common.memberpicker.listener.AmityAddedMemberClickListener
import com.amity.socialcloud.uikit.common.memberpicker.viewholder.AmityAddedMemberWithAddButtonViewHolder
import com.amity.socialcloud.uikit.common.memberpicker.viewholder.AmityAddedMembersCountViewHolder
import com.amity.socialcloud.uikit.common.memberpicker.viewholder.AmityAddedMembersViewHolder
import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem


class AmityAddedMembersAdapter(private val listener: AmityAddedMemberClickListener) :
    AmityBaseRecyclerViewAdapter<AmitySelectMemberItem>() {
    override fun getLayoutId(position: Int, obj: AmitySelectMemberItem?): Int {
        return when (obj?.name) {
            "ADD" -> R.layout.amity_view_added_member_with_add_icon
            else -> {
                if (position == 7) {
                    R.layout.amity_view_added_member_with_count
                } else {
                    R.layout.amity_item_added_member
                }
            }
        }
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.amity_view_added_member_with_count -> AmityAddedMembersCountViewHolder(
                view,
                listener
            )
            R.layout.amity_view_added_member_with_add_icon -> AmityAddedMemberWithAddButtonViewHolder(
                view,
                listener
            )
            else -> AmityAddedMembersViewHolder(view, listener)
        }
    }

    override fun getItemCount(): Int {
        return if (list.size < 8) {
            super.getItemCount()
        } else {
            8
        }

    }

    fun submitList(newList: List<AmitySelectMemberItem>) {
        setItems(newList, AmitySelectMemberItemDiffCallBack(list, newList))
    }
}