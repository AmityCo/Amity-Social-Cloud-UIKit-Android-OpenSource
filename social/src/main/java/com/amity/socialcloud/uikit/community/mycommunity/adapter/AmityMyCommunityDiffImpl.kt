package com.amity.socialcloud.uikit.community.mycommunity.adapter

import androidx.recyclerview.widget.DiffUtil
import com.amity.socialcloud.sdk.social.community.AmityCommunity

object AmityMyCommunityDiffImpl {
    val diffCallBack = object : DiffUtil.ItemCallback<AmityCommunity>() {

        override fun areItemsTheSame(oldItem: AmityCommunity, newItem: AmityCommunity): Boolean =
            oldItem.getCommunityId() == newItem.getCommunityId()

        override fun areContentsTheSame(oldItem: AmityCommunity, newItem: AmityCommunity): Boolean {
            return oldItem.getCommunityId() == newItem.getCommunityId()
                    && oldItem.getDisplayName() == newItem.getDisplayName()
                    && oldItem.isOfficial() == newItem.isOfficial()
                    && oldItem.isPublic() == newItem.isPublic()
                    && oldItem.getAvatar()?.getUrl() == newItem.getAvatar()?.getUrl()
        }
    }

}