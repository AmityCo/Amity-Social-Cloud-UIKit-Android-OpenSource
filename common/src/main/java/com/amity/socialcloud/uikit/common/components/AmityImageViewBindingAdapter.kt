package com.amity.socialcloud.uikit.common.components

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageResource")
fun setImageUri(view: ImageView, imageUri: String?) {
    if (imageUri == null) {
        view.setImageURI(null)
    } else {
        view.setImageURI(Uri.parse(imageUri))
    }
}

@BindingAdapter("imageResource")
fun setImageUri(view: ImageView, imageUri: Uri) {
    view.setImageURI(imageUri)
}

@BindingAdapter("imageResource")
fun setImageDrawable(view: ImageView, drawable: Drawable) {
    view.setImageDrawable(drawable)
}

@BindingAdapter("imageResource")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}