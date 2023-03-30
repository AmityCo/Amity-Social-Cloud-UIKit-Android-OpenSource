package com.amity.socialcloud.uikit.community.mycommunity.adapter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagingDataAdapter
import com.amity.socialcloud.uikit.common.common.loadImage
import com.amity.socialcloud.uikit.common.common.setBackgroundColor
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemMyCommunityBinding
import com.amity.socialcloud.uikit.community.mycommunity.listener.AmityMyCommunityItemClickListener


class AmityMyCommunityListViewHolder(
    itemView: View,
    private val listener: AmityMyCommunityItemClickListener
) : RecyclerView.ViewHolder(itemView),
    AmityBaseRecyclerViewPagingDataAdapter.Binder<AmityCommunity> {

    private val binding: AmityItemMyCommunityBinding? = DataBindingUtil.bind(itemView)

    override fun bind(data: AmityCommunity?, position: Int) {
        if (position == 8) {
            binding?.listener = listener
            binding?.executePendingBindings()
            binding?.tvName?.text = itemView.context.getString(R.string.amity_see_all)
            binding?.ivAvatar?.setBackgroundColor(null, AmityColorShade.SHADE4)
            binding?.ivAvatar?.setImageResource(R.drawable.amity_ic_arrow_back)
            binding?.ivAvatar?.rotation = 180F
        } else {
            binding?.ekoCommunity = data
            binding?.listener = listener
            binding?.ivAvatar?.loadImage(
                data?.getAvatar()?.getUrl(AmityImage.Size.SMALL),
                R.drawable.amity_ic_default_community_avatar_circular
            )
        }
    }

}
