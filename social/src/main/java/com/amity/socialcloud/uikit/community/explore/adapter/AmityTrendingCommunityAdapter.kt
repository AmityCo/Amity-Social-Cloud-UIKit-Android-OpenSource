package com.amity.socialcloud.uikit.community.explore.adapter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagedAdapter
import com.amity.socialcloud.uikit.common.common.formatCount
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemTrendingCommunityListBinding
import com.amity.socialcloud.uikit.community.mycommunity.listener.AmityMyCommunityItemClickListener

class AmityTrendingCommunityAdapter(private val listener: AmityMyCommunityItemClickListener) :
    AmityBaseRecyclerViewPagedAdapter<AmityCommunity>(diffCallBack) {

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<AmityCommunity>() {

            override fun areItemsTheSame(oldItem: AmityCommunity, newItem: AmityCommunity): Boolean =
                oldItem.getCommunityId() == newItem.getCommunityId()

            override fun areContentsTheSame(oldItem: AmityCommunity, newItem: AmityCommunity): Boolean =
                oldItem.getAvatar()?.getUrl() == newItem.getAvatar()?.getUrl()
                        && oldItem.getDisplayName() == newItem.getDisplayName()
                        && oldItem.isOfficial() == newItem.isOfficial()
                        && oldItem.getCommunityId() == newItem.getCommunityId()
                        && oldItem.getMemberCount() == newItem.getMemberCount()
                        && oldItem.getCategories() == newItem.getCategories()
        }
    }

    override fun getLayoutId(position: Int, obj: AmityCommunity?): Int =
        R.layout.amity_item_trending_community_list

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
        AmityTrendingCommunityViewHolder(view, listener)

    override fun getItemCount(): Int {
        return if (super.getItemCount() < 5) {
            super.getItemCount()
        } else {
            5
        }
    }

    class AmityTrendingCommunityViewHolder(
        itemView: View,
        private val listener: AmityMyCommunityItemClickListener
    ) :
        RecyclerView.ViewHolder(itemView), Binder<AmityCommunity> {

        private val binding: AmityItemTrendingCommunityListBinding? =
            DataBindingUtil.bind(itemView)

        override fun bind(data: AmityCommunity?, position: Int) {
            binding?.tvCount?.text = "${position.plus(1)}"
            binding?.avatarUrl = data?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM)
            binding?.community = data
            binding?.listener = listener
            binding?.tvMembersCount?.text = itemView.context.getString(
                R.string.amity_members_count,
                "${data?.getMemberCount()?.toDouble()?.formatCount()}"
            )
            binding?.tvCategory?.text =
                data?.getCategories()?.joinToString(separator = " ") { it.getName() }

            if (data?.getCategories().isNullOrEmpty()) {
                binding?.tvCategory?.visibility = View.GONE
            } else {
                binding?.tvCategory?.visibility = View.VISIBLE
            }
        }
    }

}