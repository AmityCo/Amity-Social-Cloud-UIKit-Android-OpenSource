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
import kotlinx.android.synthetic.main.amity_toolbar.view.*

class AmityToolBar : MaterialToolbar {

    private lateinit var mBinding: AmityToolbarBinding

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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.amity_toolbar, this, true)
        mBinding.rightStringActive = false
        toggleRightTextColor(false)
        setContentInsetsRelative(0, 0)
        setUpImageViewLeft()
        setUpImageViewRight()
    }

    private fun setUpImageViewLeft() {
        ivLeft.expandViewHitArea()
    }

    private fun setUpImageViewRight() {
        tv_right.expandViewHitArea()
    }

    fun setLeftString(value: String) {
        mBinding.leftString = value
    }

    fun setLeftDrawable(value: Drawable?, color: Int? = null) {
        mBinding.leftDrawable = value
        if (color != null && mBinding.leftDrawable != null) {
            mBinding.leftDrawable!!.colorFilter =
                PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }

    }

    fun setRightString(value: String) {
        mBinding.rightString = value
    }

    fun setRightStringActive(value: Boolean) {
        mBinding.rightStringActive = value
        toggleRightTextColor(value)
    }

    private fun toggleRightTextColor(value: Boolean) {
        if (value) {
            tv_right.setTextColor(
                AmityColorPaletteUtil.getColor(
                    ContextCompat.getColor(context, R.color.amityColorHighlight),
                    AmityColorShade.DEFAULT
                )
            )
        } else {
            tv_right.setTextColor(
                AmityColorPaletteUtil.getColor(
                    ContextCompat.getColor(context, R.color.amityColorHighlight), AmityColorShade.SHADE2
                )
            )
        }
    }

    fun setRightDrawable(value: Drawable?, color: Int? = null) {
        mBinding.rightDrawable = value
        if (color != null && mBinding.rightDrawable != null) {
            mBinding.rightDrawable!!.colorFilter =
                PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    fun setClickListener(listener: AmityToolBarClickListener) {
        mBinding.clickListener = listener
    }
}