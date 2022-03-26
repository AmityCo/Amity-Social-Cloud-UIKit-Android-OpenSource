package com.amity.socialcloud.uikit.common.common.views.avatar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.databinding.AmityComponentAvatarBinding
import com.bumptech.glide.Glide

class AmityAvatarView : ConstraintLayout {
    lateinit var style: AmityAvatarViewStyle

    private val binding: AmityComponentAvatarBinding = AmityComponentAvatarBinding.inflate(LayoutInflater.from(context), this, true)

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
        binding.imageAvatar.layoutParams.height = style.avatarHeight
        binding.imageAvatar.layoutParams.width = style.avatarWidth
        if (style.avatarDrawable != -1) {
            Glide.with(context).load(style.avatarDrawable)
                .into(binding.imageAvatar)
        } else if (style.avatarUrl != null) {
            Glide.with(context)
                .load(style.avatarUrl)
                .centerCrop()
                .into(binding.imageAvatar)
        } else {
            Glide.with(context).load(R.drawable.amity_ic_avatar_placeholder).into(binding.imageAvatar)
        }
    }

}