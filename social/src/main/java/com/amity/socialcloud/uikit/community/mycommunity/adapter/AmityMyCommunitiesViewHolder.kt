package com.amity.socialcloud.uikit.community.mycommunity.adapter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagingDataAdapter
import com.amity.socialcloud.uikit.community.databinding.AmityItemCommunityBinding
import com.amity.socialcloud.uikit.community.mycommunity.listener.AmityMyCommunityItemClickListener

class AmityMyCommunitiesViewHolder(
    itemView: View,
    private val listener: AmityMyCommunityItemClickListener
) : RecyclerView.ViewHolder(itemView), AmityBaseRecyclerViewPagingDataAdapter.Binder<AmityCommunity> {
    private val binding: AmityItemCommunityBinding? = DataBindingUtil.bind(itemView)

    override fun bind(data: AmityCommunity?, position: Int) {
        binding?.ekoCommunity = data
        binding?.listener = listener
        binding?.avatarUrl = data?.getAvatar()?.getUrl(AmityImage.Size.SMALL)
    }
}