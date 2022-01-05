package com.amity.socialcloud.uikit.community.views.communitycategory

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.social.community.AmityCommunityCategory
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemCategoryListBinding
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.amity_item_category_list.view.*

class AmityCommunityCategoryView : ConstraintLayout {
    private lateinit var mBinding: AmityItemCategoryListBinding


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
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.amity_ic_default_category_avatar)
            .centerCrop()
            .into(categoryAvatar)
    }

    fun setCategory(category: AmityCommunityCategory) {
        tvCategoryName.text = category.getName()
        mBinding.avatarUrl = category.getAvatar()?.getUrl(AmityImage.Size.SMALL)
    }


    private fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.amity_item_category_list, this, true)

    }

}