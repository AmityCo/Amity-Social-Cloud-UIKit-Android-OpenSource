package com.amity.socialcloud.uikit.community.views.communitycategory

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemCategoryListBinding
import com.bumptech.glide.Glide

class AmityCommunityCategoryView : ConstraintLayout {
    private lateinit var binding: AmityItemCategoryListBinding


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
            .into(binding.categoryAvatar)
    }

    fun setCategory(category: AmityCommunityCategory) {
        binding.tvCategoryName.text = category.getName()
        binding.avatarUrl = category.getAvatar()?.getUrl(AmityImage.Size.SMALL)
    }


    private fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding =
            DataBindingUtil.inflate(inflater, R.layout.amity_item_category_list, this, true)

    }

}