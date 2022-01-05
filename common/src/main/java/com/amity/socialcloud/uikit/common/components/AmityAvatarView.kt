package com.amity.socialcloud.uikit.common.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.common.R
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel

class AmityAvatarView : FrameLayout {

    // to track user avatar url
    private var mUrl: String = ""

    // to track user avatar url
    private lateinit var mPlaceholderDrawable: Drawable

    // to track avatar image
    private lateinit var mAvatarImageView: ShapeableImageView

    private lateinit var mCameraImageView: ShapeableImageView

    private var isCircular = true

    private var showCameraIcon = false

    private var mUri: Uri? = null

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
        val rootView = View.inflate(context, R.layout.amity_view_avatar, null)
        mAvatarImageView = rootView.findViewById(R.id.iv_avatar)
        mCameraImageView = rootView.findViewById(R.id.iv_camera)
        mPlaceholderDrawable = ContextCompat.getDrawable(context, R.drawable.amity_ic_tick_green)!!
        loadView()
        addView(rootView)
    }

    private fun loadView(signatureString: String = "") {

        val shapeBuilder = ShapeAppearanceModel.builder()
        if (isCircular) {
            shapeBuilder.setAllCornerSizes(ShapeAppearanceModel.PILL)
        }
        mAvatarImageView.shapeAppearanceModel = shapeBuilder.build()

        if (showCameraIcon) {
            mCameraImageView.visibility = View.VISIBLE
        } else {
            mCameraImageView.visibility = View.GONE
        }


        Glide.with(this)
            .load(mUri ?: mUrl)
            .dontAnimate()
            .signature(ObjectKey(signatureString))
            .into(mAvatarImageView)
    }

    /**
     * setter for AvatarView properties
     * @param [url]
     * @param [placeholder]
     * @param [signatureString]
     * @author sumitlakra
     * @date 06/01/2020
     */
    fun setAvatarUrl(url: String?, placeholder: Drawable? = null, signatureString: String = "") {
        mUrl = url ?: ""
        if (placeholder != null) {
            mPlaceholderDrawable = placeholder
        }
        loadView(signatureString)
    }

    /**
     * Function to load local storage image
     * @author sumitlakra
     * @date 06/17/2020
     */
    fun setUri(uri: Uri?) {
        mUri = uri
        loadView("")
    }

    /**
     * setter for AvatarView Shape
     * @param [value]
     * @author sumitlakra
     * @date 06/01/2020
     */
    fun setIsCircular(value: Boolean) {
        isCircular = value
        loadView("")
    }

    /**
     * setter to control camera icon visibility
     * @param [value]
     * @author sumitlakra
     * @date 06/15/2020
     */
    fun showCameraIcon(value: Boolean) {
        showCameraIcon = value
        loadView("")
    }

}