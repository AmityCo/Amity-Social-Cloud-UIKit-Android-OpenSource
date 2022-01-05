package com.amity.socialcloud.uikit.common.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import com.bumptech.glide.request.target.ImageViewTarget


class AmityScaleErrorImageViewTarget(private val imageView: ImageView) :
    ImageViewTarget<Drawable>(imageView) {

    private var scaleType = ScaleType.CENTER_INSIDE

    fun error(): AmityScaleErrorImageViewTarget {
        return AmityScaleErrorImageViewTarget(imageView)
    }

    fun error(scaleType: ScaleType): AmityScaleErrorImageViewTarget {
        this.scaleType = scaleType
        return AmityScaleErrorImageViewTarget(imageView)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        getView().scaleType = scaleType
        super.onLoadFailed(errorDrawable)
    }

    override fun setResource(resource: Drawable?) {
        getView().scaleType = ScaleType.CENTER_INSIDE
        getView().setImageDrawable(resource)
    }
}