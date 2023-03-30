package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostMediaClickListener
import com.amity.socialcloud.uikit.community.newsfeed.model.PostMedia


class AmityPostImageChildrenAdapter(
    private val itemClickListener: AmityPostMediaClickListener?
) : AmityBaseRecyclerViewAdapter<AmityPostImageChildrenItem>() {

    private var parentPostId: String = ""
    private var mediaType = PostMedia.Type.IMAGE

    fun setParentPostId(parentPostId: String) {
        this.parentPostId = parentPostId
    }

    fun setMediaType(mediaType: PostMedia.Type) {
        this.mediaType = mediaType
    }

    override fun getLayoutId(position: Int, obj: AmityPostImageChildrenItem?): Int {
        val count = Math.min(obj?.images?.count() ?: 1, 4)
        return when (count) {
            0 -> {
                R.layout.amity_item_post_image_children_one
            }
            1 -> {
                R.layout.amity_item_post_image_children_one
            }
            2 -> {
                R.layout.amity_item_post_image_children_two
            }
            3 -> {
                R.layout.amity_item_post_image_children_three
            }
            else -> {
                R.layout.amity_item_post_image_children_four
            }
        }
    }

    override fun getViewHolder(view: View, viewType: Int): AmityBasePostImageChildrenViewHolder {
        val images = list.first().images
        return when (viewType) {
            R.layout.amity_item_post_image_children_one -> {
                AmityOnePostImageChildrenViewHolder(
                    view,
                    images,
                    parentPostId,
                    itemClickListener,
                    mediaType
                )
            }
            R.layout.amity_item_post_image_children_two -> {
                AmityTwoPostImageChildrenViewHolder(
                    view,
                    images,
                    parentPostId,
                    itemClickListener,
                    mediaType
                )
            }
            R.layout.amity_item_post_image_children_three -> {
                AmityThreePostImageChildrenViewHolder(
                    view,
                    images,
                    parentPostId,
                    itemClickListener,
                    mediaType
                )
            }
            else -> {
                AmityFourPostImageChildrenViewHolder(
                    view,
                    images,
                    parentPostId,
                    itemClickListener,
                    mediaType
                )
            }
        }
    }

    fun submitList(images: List<AmityImage>) {
        val item = AmityPostImageChildrenItem(images)
        setItems(listOf(item), DiffCallback(list, listOf(item)))
    }

    class DiffCallback(
        private val oldList: List<AmityPostImageChildrenItem>,
        private val newList: List<AmityPostImageChildrenItem>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return true
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areImagesTheSame(
                oldList[oldItemPosition].images,
                newList[newItemPosition].images
            )
        }

        private fun areImagesTheSame(
            oldImages: List<AmityImage>,
            newImages: List<AmityImage>
        ): Boolean {
            if (oldImages.size != newImages.size) {
                return false
            } else {
                for (index in oldImages.indices) {
                    val oldImage = oldImages[index]
                    val newImage = newImages[index]
                    if (oldImage.getUrl() != newImage.getUrl()) {
                        return false
                    }
                }
                return true
            }
        }
    }

}