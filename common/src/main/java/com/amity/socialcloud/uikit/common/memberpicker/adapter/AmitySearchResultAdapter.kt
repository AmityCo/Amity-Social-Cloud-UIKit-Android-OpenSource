package com.amity.socialcloud.uikit.common.memberpicker.adapter

import android.view.View
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagedAdapter
import com.amity.socialcloud.uikit.common.memberpicker.listener.AmitySelectMemberListener
import com.amity.socialcloud.uikit.common.memberpicker.viewholder.AmityMemberListItemViewHolder



class AmitySearchResultAdapter(private val listener: AmitySelectMemberListener) :
    AmityBaseRecyclerViewPagedAdapter<AmityUser>(diffCallback) {

    private val selectedMemberSet = HashSet<String>()

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<AmityUser>() {
            override fun areItemsTheSame(oldItem: AmityUser, newItem: AmityUser): Boolean =
                oldItem.getUserId() == newItem.getUserId()

            override fun areContentsTheSame(oldItem: AmityUser, newItem: AmityUser): Boolean =
                oldItem.getDisplayName() == newItem.getDisplayName() &&
                        oldItem.getAvatar()?.getUrl() == newItem.getAvatar()?.getUrl()
        }
    }

    override fun getLayoutId(position: Int, obj: AmityUser?): Int =
        R.layout.amity_item_select_member

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
        AmityMemberListItemViewHolder(view, listener, selectedMemberSet)

    fun submitPagedList(pagedList: PagedList<AmityUser>?, memberSet: HashSet<String>) {
        selectedMemberSet.clear()
        selectedMemberSet.addAll(memberSet)
        super.submitList(pagedList)
    }

    fun notifyChange(position: Int, memberSet: HashSet<String>) {
        selectedMemberSet.clear()
        selectedMemberSet.addAll(memberSet)
        super.notifyItemChanged(position)
    }

}