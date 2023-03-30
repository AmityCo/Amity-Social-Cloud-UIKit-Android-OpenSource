package com.amity.socialcloud.uikit.community.explore.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.data.AmitySelectCategoryItem
import com.amity.socialcloud.uikit.community.explore.activity.EXTRA_DEFAULT_CATEGORY_SELECTION
import com.amity.socialcloud.uikit.community.explore.adapter.AmityCategoryListAdapter
import com.amity.socialcloud.uikit.community.explore.listener.AmityCategoryItemClickListener
import com.amity.socialcloud.uikit.community.explore.viewmodel.AmityCategoryListViewModel

class AmityCategoryPickerFragment : AmityBaseCategoryListFragment() {

    private val ID_MENU_ITEM_SAVE_PROFILE: Int = 122
    private var menuItemDone: MenuItem? = null
    private var selectedCategoryAmity: AmitySelectCategoryItem? = null

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menuItemDone =
            menu.add(
                Menu.NONE,
                ID_MENU_ITEM_SAVE_PROFILE,
                Menu.NONE,
                getString(R.string.amity_done)
            )
        menuItemDone?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == ID_MENU_ITEM_SAVE_PROFILE) {
            var resultIntent = Intent()
            resultIntent.putExtra(EXTRA_DEFAULT_CATEGORY_SELECTION, selectedCategoryAmity)
            activity?.setResult(Activity.RESULT_OK, resultIntent)
            activity?.finish()
            return false
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCategorySelected(category: AmityCommunityCategory) {
        super.onCategorySelected(category)
        selectedCategoryAmity =
            AmitySelectCategoryItem(category.getCategoryId(), category.getName())
    }

    override fun getCategoryListAdapter(): AmityCategoryListAdapter {
        val preSelectedCategory = arguments?.getString(ARG_DEFAULT_SELECTION)
        return AmityCategoryListAdapter(
            AmityCategoryListAdapter.AmityCategoryListDiffUtil(),
            this,
            true,
            preSelectedCategory
        )
    }

    class Builder internal constructor() {
        private var defaultSelection: String? = null
        private var categoryItemClickListener: AmityCategoryItemClickListener? = null

        fun build(activity: AppCompatActivity): AmityCategoryPickerFragment {
            val fragment = AmityCategoryPickerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DEFAULT_SELECTION, defaultSelection)
                }
            }
            fragment.viewModel = ViewModelProvider(activity).get(AmityCategoryListViewModel::class.java)
            categoryItemClickListener?.let {
                fragment.viewModel.categoryItemClickListener = categoryItemClickListener
            }
            return fragment
        }

        fun selectedCategory(category: String): Builder {
            defaultSelection = category
            return this
        }

        internal fun categoryItemClickListener(listener: AmityCategoryItemClickListener): Builder {
            this.categoryItemClickListener = listener
            return this
        }
    }
}