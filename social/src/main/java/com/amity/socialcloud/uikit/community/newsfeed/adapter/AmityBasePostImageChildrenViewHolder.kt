package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostImageClickListener
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostMediaClickListener
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostVideoClickListener
import com.amity.socialcloud.uikit.community.newsfeed.model.PostMedia
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel

abstract class AmityBasePostImageChildrenViewHolder(
    val view: View,
    val images: List<AmityImage>,
    val parentPostId: String,
    val mediaType: PostMedia.Type,
    val itemClickListener: AmityPostMediaClickListener?
) : RecyclerView.ViewHolder(view), AmityBaseRecyclerViewAdapter.IBinder<AmityPostImageChildrenItem> {

    internal fun setCornerRadius(
        imageView: ShapeableImageView,
        topLeft: Boolean,
        topRight: Boolean,
        bottomLeft: Boolean,
        bottomRight: Boolean
    ) {
        val shape = ShapeAppearanceModel.Builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, getImageCornerRadius(topLeft))
            .setTopRightCorner(CornerFamily.ROUNDED, getImageCornerRadius(topRight))
            .setBottomLeftCorner(CornerFamily.ROUNDED, getImageCornerRadius(bottomLeft))
            .setBottomRightCorner(CornerFamily.ROUNDED, getImageCornerRadius(bottomRight))
            .build()
        imageView.shapeAppearanceModel = shape
    }

    internal fun setBackgroundColor(imageView: ShapeableImageView) {
        val backgroundColor = AmityColorPaletteUtil.getColor(
            ContextCompat.getColor(
                itemView.context,
                R.color.amityColorBase
            ), AmityColorShade.SHADE4
        )
        imageView.setBackgroundColor(backgroundColor)
    }

    internal fun setImage(imageView: ShapeableImageView, imageUrl: String, position: Int) {
        if (imageUrl.isNotEmpty()) {
            Glide.with(itemView)
                .load(imageUrl)
                .placeholder(R.drawable.amity_view_image_post_placeholder)
                .into(imageView)

            imageView.setOnClickListener {
                if (mediaType == PostMedia.Type.VIDEO
                    && itemClickListener is AmityPostVideoClickListener) {
                    itemClickListener.onClickVideoThumbnail(parentPostId, images, position)
                } else if (mediaType == PostMedia.Type.IMAGE
                    && itemClickListener is AmityPostImageClickListener) {
                    itemClickListener.onClickImage(images, position)
                }
            }
        } else {
            Glide.with(itemView)
                .clear(imageView)
            imageView.setOnClickListener(null)
        }
    }

    private fun getImageCornerRadius(isRounded: Boolean) : Float {
        val imageCornerRadius: Float = itemView.context.resources.getDimension(R.dimen.amity_post_image_preview_radius)
        return if(isRounded) imageCornerRadius else 0f
    }

}
