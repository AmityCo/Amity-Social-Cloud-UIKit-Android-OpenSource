package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.databinding.AmityItemGalleryPostImageBinding
import com.amity.socialcloud.uikit.community.databinding.AmityItemGalleryPostUnknownBinding
import com.amity.socialcloud.uikit.community.databinding.AmityItemGalleryPostVideoBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.PostGalleryClickEvent
import io.reactivex.rxjava3.subjects.PublishSubject

class AmityPostGalleryAdapter(private val postClickPublisher: PublishSubject<PostGalleryClickEvent>) :
    PagingDataAdapter<AmityPost, RecyclerView.ViewHolder>(POST_GALLERY_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GALLERY_IMAGE_VIEW_TYPE -> createGalleryImagePostHolder(parent)
            GALLERY_VIDEO_VIEW_TYPE -> createGalleryVideoPostHolder(parent)
            else -> createGalleryUnknownPostHolder(parent)
        }
    }

    private fun createGalleryImagePostHolder(parent: ViewGroup): AmityGalleryImagePostViewHolder {
        val itemBinding = AmityItemGalleryPostImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AmityGalleryImagePostViewHolder(itemBinding, postClickPublisher)
    }

    private fun createGalleryVideoPostHolder(parent: ViewGroup): AmityGalleryVideoPostViewHolder {
        val itemBinding = AmityItemGalleryPostVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AmityGalleryVideoPostViewHolder(itemBinding, postClickPublisher)
    }

    private fun createGalleryUnknownPostHolder(parent: ViewGroup): AmityGalleryUnknownPostViewHolder {
        val itemBinding = AmityItemGalleryPostUnknownBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AmityGalleryUnknownPostViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = getItem(position)
        when (holder) {
            is AmityGalleryImagePostViewHolder -> {
                holder.bind(post)
            }
            is AmityGalleryVideoPostViewHolder -> {
                holder.bind(post)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position)?.getData() is AmityPost.Data.IMAGE) {
            return GALLERY_IMAGE_VIEW_TYPE
        } else if (getItem(position)?.getData() is AmityPost.Data.VIDEO) {
            return GALLERY_VIDEO_VIEW_TYPE
        }
        return GALLERY_UNKNOWN_VIEW_TYPE
    }

    companion object {
        val POST_GALLERY_COMPARATOR = object : DiffUtil.ItemCallback<AmityPost>() {
            override fun areItemsTheSame(oldItem: AmityPost, newItem: AmityPost): Boolean =
                oldItem.getPostId() == newItem.getPostId()

            override fun areContentsTheSame(oldItem: AmityPost, newItem: AmityPost): Boolean {
                return oldItem.getPostId() == newItem.getPostId()
                        && oldItem.getEditedAt() == newItem.getEditedAt()
                        && oldItem.isDeleted() == newItem.isDeleted()
            }
        }
    }

}


private const val GALLERY_UNKNOWN_VIEW_TYPE = 1
private const val GALLERY_IMAGE_VIEW_TYPE = 2
private const val GALLERY_VIDEO_VIEW_TYPE = 3
