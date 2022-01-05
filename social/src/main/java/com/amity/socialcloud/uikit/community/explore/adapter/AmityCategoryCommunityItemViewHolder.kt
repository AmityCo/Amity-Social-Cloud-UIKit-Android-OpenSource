package com.amity.socialcloud.uikit.community.explore.adapter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagedAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemCategoryCommunityListBinding
import com.amity.socialcloud.uikit.community.explore.listener.AmityCommunityItemClickListener
import com.bumptech.glide.Glide

open class AmityCategoryCommunityItemViewHolder(
    itemView: View,
    private val itemClickListener: AmityCommunityItemClickListener?
) : RecyclerView.ViewHolder(itemView),
    AmityBaseRecyclerViewPagedAdapter.Binder<AmityCommunity> {
    private val binding: AmityItemCategoryCommunityListBinding? = DataBindingUtil.bind(itemView)

    override fun bind(data: AmityCommunity?, position: Int) {
        binding?.ekoCommunity = data
        if (data?.isOfficial() == true) {
            binding?.tvCommunityName?.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.amity_ic_verified,
                0
            )
        } else {
            binding?.tvCommunityName?.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                null,
                null
            )
        }

        data?.let {
            loadAvatar(data)
            setupCommunityDetails(data)
            itemView.setOnClickListener {
                itemClickListener?.onClick(data, position)
            }
        }
    }

    private fun setupCommunityDetails(data: AmityCommunity) {
        binding?.tvCommunityName?.text = data.getDisplayName()
        //TODO uncomment after finalizing
        //binding?.tvMembersCount?.text = data.getMemberCount().readableNumber()
    }

    private fun loadAvatar(data: AmityCommunity) {
        data.getAvatar()?.getUrl()?.let {
            Glide.with(itemView)
                .load(it)
                .centerCrop()
                .into(binding?.communityAvatar!!)
        }
    }
}