package com.amity.socialcloud.uikit.common.common.views.avatar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.common.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.amity_component_avatar.view.*

class AmityAvatarView : ConstraintLayout {
    lateinit var style: AmityAvatarViewStyle

    init {
        LayoutInflater.from(context).inflate(R.layout.amity_component_avatar, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        parseStyle(attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        parseStyle(attrs)
    }

    private fun parseStyle(attrs: AttributeSet) {
        style = AmityAvatarViewStyle(context, attrs)
        applyStyle()
    }

    fun setViewStyle(style: AmityAvatarViewStyle) {
        this.style = style
        applyStyle()
    }

    fun setImage(url: String) {
        style.avatarUrl = url
        applyStyle()
    }

    fun setImage(@DrawableRes drawable: Int) {
        style.avatarDrawable = drawable
        applyStyle()
    }

    private fun applyStyle() {
        background = ContextCompat.getDrawable(context, R.drawable.amity_ic_default_ring)
        image_avatar.layoutParams.height = style.avatarHeight
        image_avatar.layoutParams.width = style.avatarWidth
        if (style.avatarDrawable != -1) {
            Glide.with(context).load(style.avatarDrawable)
                .into(image_avatar)
        } else if (style.avatarUrl != null) {
            Glide.with(context)
                .load(style.avatarUrl)
                .centerCrop()
                .into(image_avatar)
        } else {
            Glide.with(context).load(R.drawable.amity_ic_avatar_placeholder).into(image_avatar)
        }


    }

}