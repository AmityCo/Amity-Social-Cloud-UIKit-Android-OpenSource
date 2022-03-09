package com.amity.socialcloud.uikit.common.components

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.common.expandViewHitArea
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.databinding.AmityToolbarBinding
import com.google.android.material.appbar.MaterialToolbar

class AmityToolBar : MaterialToolbar {

    private lateinit var binding: AmityToolbarBinding

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.amity_toolbar, this, true)
        binding.rightStringActive = false
        toggleRightTextColor(false)
        setContentInsetsRelative(0, 0)
        setUpImageViewLeft()
        setUpImageViewRight()
    }

    private fun setUpImageViewLeft() {
        binding.ivLeft.expandViewHitArea()
    }

    private fun setUpImageViewRight() {
        binding.tvRight.expandViewHitArea()
    }

    fun setLeftString(value: String) {
        binding.leftString = value
    }

    fun setLeftDrawable(value: Drawable?, color: Int? = null) {
        binding.leftDrawable = value
        if (color != null && binding.leftDrawable != null) {
            binding.leftDrawable!!.colorFilter =
                PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }

    }

    fun setRightString(value: String) {
        binding.rightString = value
    }

    fun setRightStringActive(value: Boolean) {
        binding.rightStringActive = value
        toggleRightTextColor(value)
    }

    private fun toggleRightTextColor(value: Boolean) {
        if (value) {
            binding.tvRight.setTextColor(
                AmityColorPaletteUtil.getColor(
                    ContextCompat.getColor(context, R.color.amityColorHighlight),
                    AmityColorShade.DEFAULT
                )
            )
        } else {
            binding.tvRight.setTextColor(
                AmityColorPaletteUtil.getColor(
                    ContextCompat.getColor(context, R.color.amityColorHighlight),
                    AmityColorShade.SHADE2
                )
            )
        }
    }

    fun setRightDrawable(value: Drawable?, color: Int? = null) {
        binding.rightDrawable = value
        if (color != null && binding.rightDrawable != null) {
            binding.rightDrawable!!.colorFilter =
                PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    fun setClickListener(listener: AmityToolBarClickListener) {
        binding.clickListener = listener
    }
}