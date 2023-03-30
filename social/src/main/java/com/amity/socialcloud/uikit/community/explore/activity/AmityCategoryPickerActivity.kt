package com.amity.socialcloud.uikit.community.explore.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.data.AmitySelectCategoryItem
import com.amity.socialcloud.uikit.community.explore.fragments.AmityCategoryPickerFragment
import com.amity.socialcloud.uikit.community.explore.listener.AmityCategoryItemClickListener

const val EXTRA_DEFAULT_CATEGORY_SELECTION = "default_category_selection"

class AmityCategoryPickerActivity :
    AmityBaseToolbarFragmentContainerActivity(), AmityCategoryItemClickListener {

    private lateinit var defaultSelection: AmitySelectCategoryItem


    override fun initToolbar() {
        showToolbarDivider()
        getToolBar()?.setLeftDrawable(
            ContextCompat.getDrawable(this, R.drawable.amity_ic_cross)
        )
        getToolBar()?.setLeftString(getString(R.string.amity_select_category))
    }

    override fun getContentFragment(): Fragment {
        defaultSelection =
            intent.getParcelableExtra(EXTRA_DEFAULT_CATEGORY_SELECTION) ?: AmitySelectCategoryItem()
        val fragment = AmityCategoryPickerFragment.newInstance()
            .selectedCategory(defaultSelection.name)
            .build(this)
        fragment.setCategoryItemClickListener(this)
        return fragment
    }

    override fun onCategorySelected(category: AmityCommunityCategory) {
        defaultSelection = AmitySelectCategoryItem(category.getCategoryId(), category.getName())
    }

    class AmityCategorySelectionActivityContract :
        ActivityResultContract<AmitySelectCategoryItem, AmitySelectCategoryItem?>() {
        override fun createIntent(
            context: Context,
            defaultSelection: AmitySelectCategoryItem?
        ): Intent {
            return Intent(context, AmityCategoryPickerActivity::class.java).apply {
                putExtra(EXTRA_DEFAULT_CATEGORY_SELECTION, defaultSelection)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): AmitySelectCategoryItem? {
            val data =
                intent?.getParcelableExtra<AmitySelectCategoryItem>(EXTRA_DEFAULT_CATEGORY_SELECTION)
            return if (resultCode == Activity.RESULT_OK && data != null) data
            else null
        }
    }

}