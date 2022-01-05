package com.amity.socialcloud.uikit.community.mycommunity.adapter

import android.view.View
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.common.loadImage
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemMyCommunityBinding
import com.amity.socialcloud.uikit.community.mycommunity.listener.AmityMyCommunityItemClickListener


class AmityMyCommunityPreviewItemViewHolder(itemView: View, val listener: AmityMyCommunityItemClickListener) : AmityBaseMyCommunityPreviewItemViewHolder(itemView) {

    private val binding: AmityItemMyCommunityBinding? = DataBindingUtil.bind(itemView)

    override fun bind(data: AmityCommunity?, position: Int) {
        binding?.ekoCommunity = data
        binding?.listener = listener
        binding?.ivAvatar?.loadImage(
            data?.getAvatar()?.getUrl(AmityImage.Size.SMALL),
            R.drawable.amity_ic_default_community_avatar_circular
        )
    }

}