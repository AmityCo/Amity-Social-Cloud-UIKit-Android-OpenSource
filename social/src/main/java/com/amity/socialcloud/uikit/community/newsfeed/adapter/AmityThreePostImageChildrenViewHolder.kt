package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostMediaClickListener
import com.amity.socialcloud.uikit.community.newsfeed.model.PostMedia
import com.google.android.material.imageview.ShapeableImageView

class AmityThreePostImageChildrenViewHolder(
    view: View,
    images: List<AmityImage>,
    parentPostId: String,
    itemClickListener: AmityPostMediaClickListener?,
    mediaType: PostMedia.Type
) : AmityBasePostImageChildrenViewHolder(view, images, parentPostId, mediaType, itemClickListener) {

    private val imageOne: ShapeableImageView = itemView.findViewById(R.id.imageViewPreviewImageOne)
    private val imageTwo: ShapeableImageView = itemView.findViewById(R.id.imageViewPreviewImageTwo)
    private val imageThree: ShapeableImageView = itemView.findViewById(R.id.imageViewPreviewImageThree)
    private val playIconOne: ShapeableImageView = itemView.findViewById(R.id.ivPlayImageOne)
    private val playIconTwo: ShapeableImageView = itemView.findViewById(R.id.ivPlayImageTwo)
    private val playIconThree: ShapeableImageView = itemView.findViewById(R.id.ivPlayImageThree)

    override fun bind(data: AmityPostImageChildrenItem?, position: Int) {
        setupView()
        setData(data?.images ?: listOf())
    }

    private fun setupView() {
        setCornerRadius(imageOne, true, true, false, false)
        setCornerRadius(imageTwo,  false, false, true, false)
        setCornerRadius(imageThree, false, false, false, true)
        setBackgroundColor(imageOne)
        setBackgroundColor(imageTwo)
        setBackgroundColor(imageThree)
        playIconOne.visibility = if (mediaType == PostMedia.Type.VIDEO) View.VISIBLE else View.GONE
        playIconTwo.visibility = if (mediaType == PostMedia.Type.VIDEO) View.VISIBLE else View.GONE
        playIconThree.visibility = if (mediaType == PostMedia.Type.VIDEO) View.VISIBLE else View.GONE
    }

    private fun setData(images: List<AmityImage>) {
        val imageOneUrl = images.firstOrNull()?.getUrl(AmityImage.Size.MEDIUM) ?: ""
        setImage(imageOne, imageOneUrl, 0)

        val imageTwoUrl = images.getOrNull(1)?.getUrl(AmityImage.Size.MEDIUM) ?: ""
        setImage(imageTwo, imageTwoUrl, 1)

        val imageThreeUrl = images.getOrNull(2)?.getUrl(AmityImage.Size.MEDIUM) ?: ""
        setImage(imageThree, imageThreeUrl, 2)
    }

}
