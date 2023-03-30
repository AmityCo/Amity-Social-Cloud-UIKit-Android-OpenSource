package com.amity.socialcloud.uikit.community.newsfeed.adapter

import com.amity.socialcloud.uikit.community.databinding.AmityItemPostFooterPostReviewBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.PostReviewClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostFooterItem
import io.reactivex.rxjava3.subjects.PublishSubject

class AmityPostFooterPostReviewViewHolder (
    private val binding: AmityItemPostFooterPostReviewBinding,
    private val postReviewClickPublisher: PublishSubject<PostReviewClickEvent>,
) : AmityPostFooterViewHolder(binding.root) {

    override fun bind(data: AmityBasePostFooterItem, position: Int) {
        binding.buttonAccept.setOnClickListener {
            binding.buttonAccept.isEnabled = false
            binding.buttonDecline.isEnabled = false
            postReviewClickPublisher.onNext(PostReviewClickEvent.ACCEPT(data.post))
        }
        binding.buttonDecline.setOnClickListener {
            binding.buttonAccept.isEnabled = false
            binding.buttonDecline.isEnabled = false
            postReviewClickPublisher.onNext(PostReviewClickEvent.DECLINE(data.post))
        }
    }

}