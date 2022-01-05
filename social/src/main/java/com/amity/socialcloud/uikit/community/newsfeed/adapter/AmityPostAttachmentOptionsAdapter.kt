package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.amity.socialcloud.uikit.common.base.AmityRecyclerViewAdapter
import com.amity.socialcloud.uikit.community.databinding.AmityItemPostAttachmentOptionBinding
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityPostAttachmentOptionItem
import io.reactivex.subjects.PublishSubject

class AmityPostAttachmentOptionsAdapter(private val postAttachmentOptionClickEventPublisher: PublishSubject<AmityPostAttachmentOptionItem>)
    : AmityRecyclerViewAdapter<AmityPostAttachmentOptionItem, AmityPostAttachmentOptionViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AmityPostAttachmentOptionViewHolder {
        val itemBinding = AmityItemPostAttachmentOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AmityPostAttachmentOptionViewHolder(itemBinding, postAttachmentOptionClickEventPublisher)
    }

}