package com.amity.socialcloud.uikit.community.newsfeed.adapter

import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.reaction.AmityReaction
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemReactionBinding
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.subjects.PublishSubject

class AmityReactionViewHolder constructor(
    private val binding: AmityItemReactionBinding,
    private val userClickPublisher: PublishSubject<AmityUser>
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: AmityReaction?) {
        data?.getCreator()?.let { creator ->
            setUserClickListener(creator)

            binding.tvDisplayName.text = creator.getDisplayName()

            Glide.with(itemView.context)
                .load(creator.getAvatar()?.getUrl(AmityImage.Size.MEDIUM))
                .placeholder(R.drawable.amity_ic_default_profile_large)
                .centerCrop()
                .into(binding.ivAvatar)
        }
    }

    private fun setUserClickListener(creator: AmityUser) {
        binding.ivAvatar.setOnClickListener {
            userClickPublisher.onNext(creator)
        }

        binding.tvDisplayName.setOnClickListener {
            userClickPublisher.onNext(creator)
        }
    }
}