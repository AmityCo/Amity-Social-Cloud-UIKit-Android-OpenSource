package com.amity.socialcloud.uikit.community.explore.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagingDataAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.explore.listener.AmityCommunityItemClickListener

class AmityCategoryCommunityListAdapter(
    diffUtil: AmityCommunityDiffUtil,
    private val itemClickListener: AmityCommunityItemClickListener
) :
    AmityBaseRecyclerViewPagingDataAdapter<AmityCommunity>(diffUtil) {


    override fun getLayoutId(position: Int, obj: AmityCommunity?): Int {
        return R.layout.amity_item_category_community_list
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return AmityCategoryCommunityItemViewHolder(view, itemClickListener)
    }

    class AmityCommunityDiffUtil : DiffUtil.ItemCallback<AmityCommunity>() {
        override fun areItemsTheSame(
            oldItem: AmityCommunity,
            newItem: AmityCommunity
        ): Boolean {
            return oldItem.getCommunityId() == newItem.getCommunityId()
        }

        override fun areContentsTheSame(
            oldItem: AmityCommunity,
            newItem: AmityCommunity
        ): Boolean {
            return oldItem.getCommunityId() == newItem.getCommunityId()
                    && oldItem.getDisplayName() == newItem.getDisplayName()
                    && oldItem.getMemberCount() == newItem.getMemberCount()
        }

    }

}
