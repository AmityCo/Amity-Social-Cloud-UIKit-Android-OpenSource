package com.amity.socialcloud.uikit.community.newsfeed.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.amity.socialcloud.sdk.core.user.AmityUser

class UserDiffUtil : DiffUtil.ItemCallback<AmityUser>() {

    override fun areItemsTheSame(oldItem: AmityUser, newItem: AmityUser): Boolean {
        return oldItem.getUserId() == newItem.getUserId()
    }

    override fun areContentsTheSame(oldItem: AmityUser, newItem: AmityUser): Boolean {
        return oldItem.getDisplayName() == newItem.getDisplayName()
                && oldItem.getAvatar()?.getUrl() == newItem.getAvatar()?.getUrl()
                && oldItem.getAvatarCustomUrl() == newItem.getAvatarCustomUrl()
                && oldItem.isGlobalBan() == newItem.isGlobalBan()
    }
}
