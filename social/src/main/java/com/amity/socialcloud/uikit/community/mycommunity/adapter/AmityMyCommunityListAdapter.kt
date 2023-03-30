package com.amity.socialcloud.uikit.community.mycommunity.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagingDataAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.mycommunity.listener.AmityMyCommunityItemClickListener

class AmityMyCommunityListAdapter(
    private val listener: AmityMyCommunityItemClickListener,
    private val previewMode: Boolean
) :
    AmityBaseRecyclerViewPagingDataAdapter<AmityCommunity>(AmityMyCommunityDiffImpl.diffCallBack) {

    override fun getLayoutId(position: Int, obj: AmityCommunity?): Int {
        return if (previewMode) R.layout.amity_item_my_community else R.layout.amity_item_community
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.amity_item_my_community) {
            AmityMyCommunityListViewHolder(view, listener)
        } else {
            AmityMyCommunitiesViewHolder(view, listener)
        }
    }

    override fun getItemCount(): Int {
        return if (previewMode) {
            super.getItemCount().coerceAtMost(9)
        } else {
            super.getItemCount()
        }
    }

}