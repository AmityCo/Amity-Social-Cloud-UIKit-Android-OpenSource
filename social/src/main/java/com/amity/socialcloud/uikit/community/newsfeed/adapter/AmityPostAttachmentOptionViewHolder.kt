package com.amity.socialcloud.uikit.community.newsfeed.adapter

import com.amity.socialcloud.uikit.common.base.AmityViewHolder
import com.amity.socialcloud.uikit.community.databinding.AmityItemPostAttachmentOptionBinding
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityPostAttachmentOptionItem
import com.bumptech.glide.Glide
import io.reactivex.subjects.PublishSubject

class AmityPostAttachmentOptionViewHolder(private val binding: AmityItemPostAttachmentOptionBinding,
                                          private val postAttachmentOptionClickEventPublisher: PublishSubject<AmityPostAttachmentOptionItem>
) :
    AmityViewHolder<AmityPostAttachmentOptionItem>(binding.root) {

    override fun bind(item: AmityPostAttachmentOptionItem) {
        val image: Int
        if (item.isEnable) {
            image = item.activeIcon
        } else {
            image = item.inactiveIcon
        }
        Glide.with(binding.root.context)
            .load(image)
            .centerInside()
            .into(binding.imageViewIcon)

        binding.layoutIcon.setOnClickListener {
            if(item.isEnable) {
                postAttachmentOptionClickEventPublisher.onNext(item)
            }
        }
    }

}