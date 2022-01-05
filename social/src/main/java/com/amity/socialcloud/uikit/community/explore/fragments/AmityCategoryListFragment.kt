package com.amity.socialcloud.uikit.community.explore.fragments

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.uikit.community.explore.adapter.AmityCategoryListAdapter
import com.amity.socialcloud.uikit.community.explore.listener.AmityCategoryItemClickListener
import com.amity.socialcloud.uikit.community.explore.viewmodel.AmityCategoryListViewModel

class AmityCategoryListFragment : AmityBaseCategoryListFragment() {

    override fun getCategoryListAdapter(): AmityCategoryListAdapter {
        return AmityCategoryListAdapter(
            AmityCategoryListAdapter.AmityCategoryListDiffUtil(),
            this,
            false,
            null
        )
    }

    class Builder internal constructor(){
        private var categoryItemClickListener: AmityCategoryItemClickListener? = null

        fun build(activity: AppCompatActivity): AmityCategoryListFragment {
            val fragment = AmityCategoryListFragment()
            fragment.viewModel =
                ViewModelProvider(activity).get(AmityCategoryListViewModel::class.java)
            fragment.viewModel.categoryItemClickListener = categoryItemClickListener
            return fragment
        }

        private fun categoryItemClickListener(listener: AmityCategoryItemClickListener): Builder {
            this.categoryItemClickListener = listener
            return this
        }
    }

    companion object {

        fun newInstance(): Builder {
            return Builder()
        }
    }

}