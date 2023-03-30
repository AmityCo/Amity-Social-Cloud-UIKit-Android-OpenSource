package com.amity.socialcloud.uikit.community.newsfeed.adapter

import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.databinding.AmityItemGalleryPostVideoBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.PostGalleryClickEvent
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.subjects.PublishSubject

class AmityGalleryVideoPostViewHolder constructor(
    private val binding: AmityItemGalleryPostVideoBinding,
    private val postClickPublisher: PublishSubject<PostGalleryClickEvent>,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: AmityPost?) {
        binding.itemGalleryPostVideoContainer.setOnClickListener { _ ->
            post?.let { postClickPublisher.onNext(PostGalleryClickEvent.Video(it)) }
        }
        binding.itemGalleryPostVideoImageview.setImageDrawable(null)
        if (post?.getData() is AmityPost.Data.VIDEO
            && (post.getData() as AmityPost.Data.VIDEO).getThumbnailImage() != null
        ) {
            val video = (post.getData() as AmityPost.Data.VIDEO)
            val thumbnail = video.getThumbnailImage()
            Glide.with(itemView)
                .load(thumbnail?.getUrl(AmityImage.Size.MEDIUM))
                .into(binding.itemGalleryPostVideoImageview)

            //TODO get video length (BE unsupported)
        }
    }
}
