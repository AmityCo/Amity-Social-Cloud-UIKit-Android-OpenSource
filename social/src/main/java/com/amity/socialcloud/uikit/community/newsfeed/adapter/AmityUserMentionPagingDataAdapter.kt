package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.amity.socialcloud.sdk.social.community.AmityCommunityMember
import com.amity.socialcloud.uikit.community.databinding.AmityItemUserMentionBinding
import com.amity.socialcloud.uikit.community.newsfeed.diffutil.CommunityMemberDiffUtil
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityUserMention

class AmityUserMentionPagingDataAdapter :
    PagingDataAdapter<AmityCommunityMember, AmityUserMentionPagingDataViewHolder>(
        CommunityMemberDiffUtil()
    ),
    AmityUserMentionViewHolder.AmityUserMentionListener {

    private var listener: AmityUserMentionViewHolder.AmityUserMentionListener? = null

    override fun onClickUserMention(userMention: AmityUserMention) {
        listener?.onClickUserMention(userMention)
    }

    override fun onBindViewHolder(holder: AmityUserMentionPagingDataViewHolder, position: Int) {
        holder.bind(getItem(position)?.getUser())
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AmityUserMentionPagingDataViewHolder {
        val itemBinding = AmityItemUserMentionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AmityUserMentionPagingDataViewHolder(itemBinding, this)
    }

    fun setListener(listener: AmityUserMentionViewHolder.AmityUserMentionListener) {
        this.listener = listener
    }
}