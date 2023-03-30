package com.amity.socialcloud.uikit.community.newsfeed.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMember

class CommunityMemberDiffUtil : DiffUtil.ItemCallback<AmityCommunityMember>() {

    override fun areItemsTheSame(oldItem: AmityCommunityMember, newItem: AmityCommunityMember): Boolean {
        return oldItem.getUserId() == newItem.getUserId()
    }

    override fun areContentsTheSame(oldItem: AmityCommunityMember, newItem: AmityCommunityMember): Boolean {
        return oldItem.getUser()?.getDisplayName() == newItem.getUser()?.getDisplayName()
                && oldItem.getUser()?.getAvatar()?.getUrl() == newItem.getUser()?.getAvatar()?.getUrl()
                && oldItem.getUser()?.getAvatarCustomUrl() == newItem.getUser()?.getAvatarCustomUrl()
                && oldItem.getUser()?.isGlobalBan() == newItem.getUser()?.isGlobalBan()
    }
}
