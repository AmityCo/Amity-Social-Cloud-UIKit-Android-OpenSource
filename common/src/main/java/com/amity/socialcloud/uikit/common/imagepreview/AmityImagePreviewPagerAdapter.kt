package com.amity.socialcloud.uikit.common.imagepreview


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView

class AmityImagePreviewPagerAdapter : AmityBaseRecyclerViewAdapter<AmityPreviewImage>() {

    override fun getLayoutId(position: Int, obj: AmityPreviewImage?): Int {
        return R.layout.amity_item_image_preview
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return ImagePreviewItemViewHolder(view)
    }


    class ImagePreviewItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        IBinder<AmityPreviewImage> {
        var image: PhotoView = itemView.findViewById(R.id.ivPreviewImage)
        override fun bind(data: AmityPreviewImage?, position: Int) {
            image.maximumScale = 10F
            data?.let {
                Glide.with(itemView).load(it.url).into(image)
            }
        }
    }

}