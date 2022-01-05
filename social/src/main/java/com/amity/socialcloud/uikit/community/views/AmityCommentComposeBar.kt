package com.amity.socialcloud.uikit.community.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityCommentComposeBarBinding
import com.amity.socialcloud.uikit.community.views.comment.AmityCommentComposeView
import kotlinx.android.synthetic.main.amity_comment_compose_bar.view.*

class AmityCommentComposeBar : ConstraintLayout {
    private lateinit var mBinding: AmityCommentComposeBarBinding
    private var commentExpandClickListener: OnClickListener? = null


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

    fun setImageUrl(url: String) {
        mBinding.avatarUrl = url
    }

    fun setCommentExpandClickListener(onClickListener: OnClickListener) {
        commentExpandClickListener = onClickListener
    }

    fun getCommentEditText(): AmityCommentComposeView {
        return mBinding.etPostComment
    }

    fun getTextCompose(): String {
        return mBinding.etPostComment.text.toString()
    }

    fun getPostButton(): Button {
        return mBinding.btnPost
    }

    private fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.amity_comment_compose_bar, this, true)
        avProfile.setBackgroundColor(
            AmityColorPaletteUtil.getColor(
                ContextCompat.getColor(context, R.color.amityColorPrimary), AmityColorShade.SHADE3
            )
        )
        btnPost.isEnabled = false

        etPostComment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnPost.isEnabled = s.toString().trim().isNotEmpty()
            }

        })
        ivExpand.setOnClickListener {
            commentExpandClickListener?.onClick(it)
        }


    }
}