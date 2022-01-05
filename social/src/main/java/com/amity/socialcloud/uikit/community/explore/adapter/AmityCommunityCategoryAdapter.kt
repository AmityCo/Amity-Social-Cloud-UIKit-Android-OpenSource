package com.amity.socialcloud.uikit.community.explore.adapter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.social.community.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagedAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemCommunityCategoryBinding
import com.amity.socialcloud.uikit.community.explore.listener.AmityCategoryItemClickListener

class AmityCommunityCategoryAdapter(private val listener: AmityCategoryItemClickListener) :
    AmityBaseRecyclerViewPagedAdapter<AmityCommunityCategory>(diffCallBack) {

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<AmityCommunityCategory>() {

            override fun areItemsTheSame(
                oldItem: AmityCommunityCategory,
                newItem: AmityCommunityCategory
            ): Boolean =
                oldItem.getCategoryId() == newItem.getCategoryId()

            override fun areContentsTheSame(
                oldItem: AmityCommunityCategory,
                newItem: AmityCommunityCategory
            ): Boolean =
                oldItem.getAvatar()?.getUrl() == newItem.getAvatar()?.getUrl()
                        && oldItem.getName() == newItem.getName()
        }
    }

    override fun getLayoutId(position: Int, obj: AmityCommunityCategory?): Int =
        R.layout.amity_item_community_category

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
        AmityCommunityCategoryViewHolder(view, listener)


    inner class AmityCommunityCategoryViewHolder(
        itemView: View,
        private val listener: AmityCategoryItemClickListener
    ) : RecyclerView.ViewHolder(itemView), Binder<AmityCommunityCategory> {

        private val binding: AmityItemCommunityCategoryBinding? = DataBindingUtil.bind(itemView)

        override fun bind(data: AmityCommunityCategory?, position: Int) {
            binding?.communityCategory = data
            binding?.listener = listener
            binding?.avatarUrl = data?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM)
        }

    }

    override fun getItemCount(): Int {
        return if (super.getItemCount() < 8) {
            super.getItemCount()
        } else {
            8
        }
    }

}