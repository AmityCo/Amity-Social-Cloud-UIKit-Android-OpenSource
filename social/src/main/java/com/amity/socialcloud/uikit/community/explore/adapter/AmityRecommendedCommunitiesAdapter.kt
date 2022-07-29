package com.amity.socialcloud.uikit.community.explore.adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewListAdapter
import com.amity.socialcloud.uikit.common.common.formatCount
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemRecommCommBinding
import com.amity.socialcloud.uikit.community.mycommunity.listener.AmityMyCommunityItemClickListener

class AmityRecommendedCommunitiesAdapter(private val listener: AmityMyCommunityItemClickListener) :
    AmityBaseRecyclerViewListAdapter<AmityCommunity>(diffCallBack) {

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<AmityCommunity>() {

            override fun areItemsTheSame(
                oldItem: AmityCommunity,
                newItem: AmityCommunity
            ): Boolean =
                oldItem.getCommunityId() == newItem.getCommunityId()

            override fun areContentsTheSame(
                oldItem: AmityCommunity,
                newItem: AmityCommunity
            ): Boolean =
                oldItem.getAvatar()?.getUrl() == newItem.getAvatar()?.getUrl()
                        && oldItem.getDisplayName() == newItem.getDisplayName()
                        && oldItem.isOfficial() == newItem.isOfficial()
        }
    }

    override fun getLayoutId(position: Int, obj: AmityCommunity?): Int =
        R.layout.amity_item_recomm_comm

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
        AmityRecommendedCommunityViewHolder(view, listener)

    inner class AmityRecommendedCommunityViewHolder(
        itemView: View,
        private val listener: AmityMyCommunityItemClickListener
    ) : RecyclerView.ViewHolder(itemView), Binder<AmityCommunity> {

        private val binding: AmityItemRecommCommBinding? = DataBindingUtil.bind(itemView)
        private val textviewCommunityName: TextView = itemView.findViewById(R.id.tvCommName)

        override fun bind(data: AmityCommunity?, position: Int) {
            if (data?.isOfficial() == true) {
                textviewCommunityName.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.amity_ic_verified,
                    0
                )
            } else {
                textviewCommunityName.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
            }

            binding?.ekoCommunity = data
            binding?.listener = listener
            binding?.tvMembersCount?.text = itemView.context.getString(
                R.string.amity_members_count,
                "${data?.getMemberCount()?.toDouble()?.formatCount()}"
            )
            binding?.tvCommName?.text =
                data?.getCategories()?.joinToString(separator = " ") { it.getName() }
        }

    }

    override fun getItemCount(): Int {
        return if (super.getItemCount() < 4) {
            super.getItemCount()
        } else {
            4
        }
    }

}