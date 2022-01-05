package com.amity.socialcloud.uikit.community.mycommunity.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagedAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.mycommunity.listener.AmityMyCommunityItemClickListener

class AmityMyCommunitiesAdapter(private val listener: AmityMyCommunityItemClickListener) :
    AmityBaseRecyclerViewPagedAdapter<AmityCommunity>(AmityMyCommunityDiffImpl.diffCallBack) {

    override fun getLayoutId(position: Int, obj: AmityCommunity?): Int =
        R.layout.amity_item_community

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
        AmityMyCommunitiesViewHolder(view, listener)


}