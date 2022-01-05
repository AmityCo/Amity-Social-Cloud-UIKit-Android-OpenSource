package com.amity.socialcloud.uikit.community.newsfeed.adapter

import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.community.databinding.AmityItemGalleryPostImageBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.PostGalleryClickEvent
import com.bumptech.glide.Glide
import io.reactivex.subjects.PublishSubject

class AmityGalleryImagePostViewHolder constructor(
    private val binding: AmityItemGalleryPostImageBinding,
    private val postClickPublisher: PublishSubject<PostGalleryClickEvent>,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: AmityPost?) {
        binding.itemGalleryPostImageContainer.setOnClickListener { _ ->
            post?.let { postClickPublisher.onNext(PostGalleryClickEvent.Image(it)) }
        }
        binding.itemGalleryPostImageImageview.setImageDrawable(null)
        if (post?.getData() is AmityPost.Data.IMAGE
            && (post.getData() as AmityPost.Data.IMAGE).getImage() != null
        ) {
            val image = (post.getData() as AmityPost.Data.IMAGE).getImage()
            Glide.with(itemView)
                .load(image?.getUrl(AmityImage.Size.MEDIUM))
                .into(binding.itemGalleryPostImageImageview)
        }
    }
}
