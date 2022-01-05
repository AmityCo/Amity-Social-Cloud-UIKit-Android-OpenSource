package com.amity.socialcloud.uikit.common.components

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.utils.AmityScaleErrorImageViewTarget
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import java.io.File


@BindingAdapter(
        value = ["textColorShade", "textColorHintShade"],
        requireAll = false)
fun setTextColor(view: TextView, colorShade: AmityColorShade?, textColorHintShade: AmityColorShade?) {
    colorShade?.let {
        view.setTextColor(AmityColorPaletteUtil.getColor(view.currentTextColor, colorShade))
    }
    textColorHintShade?.let {
        view.setHintTextColor(AmityColorPaletteUtil.getColor(view.currentHintTextColor, textColorHintShade))
    }
}

@BindingAdapter(
        value = [
            "buttonEnabledTextColor",
            "buttonEnabledTextColorShade",
            "buttonDisabledTextColor",
            "buttonDisabledTextColorShade"],
        requireAll = false)
fun setButtonTextColor(view: Button, buttonEnabledTextColor: Int?,
                       buttonEnabledTextColorShade: AmityColorShade?, buttonDisabledTextColor: Int?,
                       buttonDisabledTextColorShade: AmityColorShade?) {

    val states = arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled))
    val colorEnabled = AmityColorPaletteUtil.getColor(buttonEnabledTextColor!!, buttonEnabledTextColorShade
            ?: AmityColorShade.DEFAULT)
    val colorDefault = AmityColorPaletteUtil.getColor(buttonDisabledTextColor!!, buttonDisabledTextColorShade
            ?: AmityColorShade.DEFAULT)
    val colors = intArrayOf(colorEnabled, colorDefault)

    val colorStateList = ColorStateList(states, colors)
    view.setTextColor(colorStateList)
}

@BindingAdapter(value = ["amityBackgroundColorAlpha"], requireAll = true)
fun setBackgroundAlpha(view: ShapeableImageView, amityBackgroundColorAlpha: Int) {
    view.background.alpha = amityBackgroundColorAlpha
}

@BindingAdapter(value = ["amityButtonStrokeShade"], requireAll = true)
fun setBackgroundAlpha(view: MaterialButton, shade: AmityColorShade) {
    val strokeColor = view.strokeColor.defaultColor

    val colorDefault = AmityColorPaletteUtil.getColor(strokeColor, AmityColorShade.SHADE3)
    val states = arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled))
    val colors = intArrayOf(colorDefault, colorDefault)

    val colorStateList = ColorStateList(states, colors)
    view.strokeColor = colorStateList
}

@BindingAdapter(value = ["drawableTintColor", "drawableTintShade"], requireAll = false)
fun setDrawableTint(view: TextView, drawableTintColor: Int?, drawableTintShade: AmityColorShade?) {
    if (drawableTintColor != null) {
        val tintColor: Int = AmityColorPaletteUtil.getColor(drawableTintColor, drawableTintShade
                ?: AmityColorShade.DEFAULT)
        if (drawableTintShade != null) {
            for (drawable in view.compoundDrawables) {
                drawable?.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
            }
        }
    }
}

@BindingAdapter(value = ["amityTintColor", "amityTintShade"], requireAll = false)
fun setImageViewTint(imageView: ImageView, tintColor: Int, tintShade: AmityColorShade?) {
    val shade = tintShade ?: AmityColorShade.SHADE2
    ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(AmityColorPaletteUtil.getColor(tintColor, shade)))
}

@BindingAdapter(
        value = [
            "checkBoxDefaultColor",
            "checkBoxDefaultShade",
            "checkBoxCheckedColor",
            "checkBoxCheckedShade"],
        requireAll = false)
fun setCheckboxSelectorColor(view: MaterialCheckBox, checkBoxDefaultColor: Int?, checkBoxDefaultShade: AmityColorShade?,
                             checkBoxCheckedColor: Int?, checkBoxCheckedShade: AmityColorShade?) {

    val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf(-android.R.attr.state_checked))
    val tintColorChecked = AmityColorPaletteUtil.getColor(checkBoxCheckedColor!!, checkBoxCheckedShade
            ?: AmityColorShade.DEFAULT)
    val tintColorDefault = AmityColorPaletteUtil.getColor(checkBoxDefaultColor!!, checkBoxDefaultShade
            ?: AmityColorShade.DEFAULT)
    val colors = intArrayOf(tintColorChecked, tintColorDefault)

    val colorStateList = ColorStateList(states, colors)
    view.buttonTintList = colorStateList
    view.setTextColor(colorStateList)
}

@BindingAdapter(value = ["message", "isSender"], requireAll = true)
fun setText(view: AmityReadMoreTextView, message: String?, isSender: Boolean) {
    view.isSender(isSender)
    view.setText(message)
}

@BindingAdapter(value = ["longPress", "maxLines"], requireAll = false)
fun setListener(view: AmityReadMoreTextView, listener: AmityLongPressListener?, maxLines: Int?) {
    if (listener != null) {
        view.setReadMoreListener(listener)
    }
    if (maxLines != null) {
        view.setMaxLines(maxLines)
    }
}

@BindingAdapter(value = ["amityBackgroundColor", "backgroundColorShade"], requireAll = false)
fun setViewBackgroundColor(view: View, color: Int?, colorShade: AmityColorShade?) {
    val shade = colorShade ?: AmityColorShade.DEFAULT
    val bgColor = color ?: ContextCompat.getColor(view.context, R.color.amityColorPrimary)
    view.setBackgroundColor(AmityColorPaletteUtil.getColor(bgColor, shade))
}

@BindingAdapter(
        value = [
            "avatarViewImage",
            "avatarViewPlaceHolder",
            "avatarViewSignature",
            "isCircular"],
        requireAll = false)
fun setAvatarViewImage(view: AmityAvatarView, imagePath: String?, placeholder: Drawable?,
                       signature: String? = "", isCircular: Boolean = true) {
    view.setIsCircular(isCircular)
    view.setAvatarUrl(imagePath, placeholder, signature ?: "")
}

@BindingAdapter("showCameraIcon")
fun setShowCameraIcon(view: AmityAvatarView, showCameraIcon: Boolean = false) {
    view.showCameraIcon(showCameraIcon)
}

@BindingAdapter("required")
fun setRequiredInLabel(view: TextView, required: Boolean = false) {
    if (required) {
        val required: Spannable = SpannableString("*")

        required.setSpan(ForegroundColorSpan(Color.RED), 0, required.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.append(required)
    }
}

@BindingAdapter(
        value = [
            "roundedCorner",
            "cornerRadius",
            "topLeftRadius",
            "bottomLeftRadius",
            "topRightRadius",
            "bottomRightRadius",
            "fillColor",
            "StrokeColor",
            "colorShade"],
        requireAll = false)
fun setRoundedCorner(view: View,
                     roundedCorner: Boolean,
                     @DimenRes cornerRadius: Int?,
                     topLeft: Float?,
                     bottomLeft: Float?,
                     topRight: Float?,
                     bottomRight: Float?,
                     @ColorRes fillColor: Int?,
                     @ColorRes strokeColor: Int?,
                     colorShade: AmityColorShade?) {
    if (roundedCorner) {
        val radius = view.context.resources.getDimension(cornerRadius ?: R.dimen.amity_six)
        val modal = ShapeAppearanceModel().toBuilder()
        if (topLeft == null) {
            modal.setTopLeftCorner(CornerFamily.ROUNDED, radius)
        } else {
            modal.setTopLeftCorner(CornerFamily.ROUNDED, topLeft)
        }

        if (bottomLeft == null) {
            modal.setBottomLeftCorner(CornerFamily.ROUNDED, radius)
        } else {
            modal.setBottomLeftCorner(CornerFamily.ROUNDED, bottomLeft)
        }

        if (topRight == null) {
            modal.setTopRightCorner(CornerFamily.ROUNDED, radius)
        } else {
            modal.setTopRightCorner(CornerFamily.ROUNDED, topRight)
        }

        if (bottomRight == null) {
            modal.setBottomRightCorner(CornerFamily.ROUNDED, radius)
        } else {
            modal.setBottomRightCorner(CornerFamily.ROUNDED, bottomRight)
        }

        val shapeDrawable = MaterialShapeDrawable(modal.build())
        if (fillColor == null) {
            shapeDrawable.fillColor = ContextCompat.getColorStateList(view.context, R.color.amityColorWhite)
        } else {
            if (colorShade == null)
                shapeDrawable.fillColor = ContextCompat.getColorStateList(view.context, fillColor)
            else
                shapeDrawable.setTint(AmityColorPaletteUtil.getColor(ContextCompat.getColor(view.context, fillColor), colorShade))
        }

        if (strokeColor == null) {
            if (fillColor == null) {
                shapeDrawable.setStroke(2F, ContextCompat.getColor(view.context, R.color.amityColorWhite))
            } else {
                if (colorShade == null)
                    shapeDrawable.setStroke(2F, ContextCompat.getColor(view.context, fillColor))
                else
                    shapeDrawable.setStroke(2F, AmityColorPaletteUtil.getColor(ContextCompat.getColor(view.context, fillColor), colorShade))
            }
        } else {
            shapeDrawable.setStroke(2F, ContextCompat.getColor(view.context, strokeColor))
        }

        ViewCompat.setBackground(view, shapeDrawable)
    }
}

@BindingAdapter("imageUrl", "placeHolder", requireAll = false)
fun setImageUrl(view: ImageView, imageUrl: String?, placeholder: Drawable?) {
    var glideImageUrl = imageUrl
    var mPlaceholder = placeholder
    var imageUri: Uri = Uri.EMPTY
    if (imageUrl == null) {
        glideImageUrl = ""
    }
    if (placeholder == null) {
        mPlaceholder = ContextCompat.getDrawable(view.context, R.drawable.amity_ic_user)
    }
    val imageSynced = when {
        glideImageUrl!!.startsWith("https") -> {
            true
        }
        glideImageUrl.isNotEmptyOrBlank() -> {
            imageUri = Uri.fromFile(File(glideImageUrl))
            false
        }
        else -> false
    }
    Glide.with(view.context)
            .load(if (imageSynced) glideImageUrl else imageUri)
            .centerCrop()
            .placeholder(mPlaceholder)
            .error(mPlaceholder)
            .dontAnimate()
            .into(AmityScaleErrorImageViewTarget(view).error())
}

@BindingAdapter(value = ["onScrolled", "onScrollStateChanged"], requireAll = false)
fun setOnRVScroll(rv: RecyclerView, onScroll: OnScroll?, onScrollStateChanged: OnScrollStateChanged?) {
    rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            onScroll?.onScrolled(recyclerView, dx, dy)
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            onScrollStateChanged?.onScrollStateChanged(recyclerView, newState)
        }
    })
}

interface OnScroll {

    fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
}

interface OnScrollStateChanged {

    fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int)
}

@BindingAdapter(value = ["amityFillColor", "amityFillShade"])
fun setRoundedImageView(imageView: ImageView, fillColor: Int, shade: AmityColorShade) {
    val modal = ShapeAppearanceModel()
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, imageView.context.resources.getDimensionPixelSize(R.dimen.amity_thirty_two).toFloat())
    val shapeDrawable = MaterialShapeDrawable(modal.build())
    shapeDrawable.setTint(AmityColorPaletteUtil.getColor(fillColor, shade))

    ViewCompat.setBackground(imageView, shapeDrawable)
}

@BindingAdapter("safeText")
fun setText(textView: TextView, input: CharSequence?) {
    if (input != null && input.isNotEmpty()) {
        textView.text = input
    } else {
        textView.text = textView.context.getString(R.string.amity_anonymous)
    }
}

@BindingAdapter("isBold")
fun setBold(view: TextView, isBold: Boolean) {
    if (isBold) {
        view.setTypeface(null, Typeface.BOLD)
    } else {
        view.setTypeface(null, Typeface.NORMAL)
    }
}

@BindingAdapter("amityVisibility")
fun setVisibility(view: View, value: Boolean) {
    view.visibility = if (value) View.VISIBLE else View.GONE
}