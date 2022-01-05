package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostMediaClickListener
import com.amity.socialcloud.uikit.community.newsfeed.model.PostMedia
import com.google.android.material.imageview.ShapeableImageView

class AmityOnePostImageChildrenViewHolder(
    view: View,
    images: List<AmityImage>,
    parentPostId: String,
    itemClickListener: AmityPostMediaClickListener?,
    mediaType: PostMedia.Type
) : AmityBasePostImageChildrenViewHolder(view, images, parentPostId, mediaType, itemClickListener) {

    private val imageOne: ShapeableImageView = itemView.findViewById(R.id.imageViewPreviewImageOne)
    private val playIconOne: ShapeableImageView = itemView.findViewById(R.id.ivPlayImageOne)

    override fun bind(data: AmityPostImageChildrenItem?, position: Int) {
        setupView()
        setData(data?.images ?: listOf())
    }

    private fun setupView() {
        setCornerRadius(imageOne, true, true, true, true)
        setBackgroundColor(imageOne)
        playIconOne.visibility = if (mediaType == PostMedia.Type.VIDEO) View.VISIBLE else View.GONE
    }

    private fun setData(images: List<AmityImage>) {
        val imageOneUrl = images.firstOrNull()?.getUrl(AmityImage.Size.MEDIUM) ?: ""
        setImage(imageOne, imageOneUrl, 0)
    }

}
