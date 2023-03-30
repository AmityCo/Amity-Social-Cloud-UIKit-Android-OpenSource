package com.amity.socialcloud.uikit.community.explore.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagingDataAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.explore.listener.AmityCategoryItemClickListener

class AmityCategoryListAdapter(
    diffUtil: AmityCategoryListDiffUtil,
    private val itemClickListener: AmityCategoryItemClickListener?
) : AmityBaseRecyclerViewPagingDataAdapter<AmityCommunityCategory>(diffUtil),
    AmityCategorySelectionListener {

    constructor(
        diffUtil: AmityCategoryListDiffUtil,
        itemClickListener: AmityCategoryItemClickListener?,
        modeSelection: Boolean,
        preSelectedCategory: String?
    ) : this(diffUtil, itemClickListener) {
        selectedCategory = preSelectedCategory
        this.modeSelection = modeSelection
    }

    private var selectedCategory: String? = null
    private var modeSelection = false


    override fun getLayoutId(position: Int, obj: AmityCommunityCategory?): Int {
        if (modeSelection)
            return R.layout.amity_item_type_selector_community_category_list
        return R.layout.amity_item_community_category_list
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.amity_item_community_category_list)
            AmityCategoryItemViewHolder(view, itemClickListener)
        else
            AmityCategoryItemTypeSelectorViewHolder(view, itemClickListener, this)
    }

    override fun setSelection(selectedCategory: String?) {
        this.selectedCategory = selectedCategory
        notifyDataSetChanged()
    }

    override fun getSelection(): String? = selectedCategory

    class AmityCategoryListDiffUtil : DiffUtil.ItemCallback<AmityCommunityCategory>() {
        override fun areItemsTheSame(
            oldItem: AmityCommunityCategory,
            newItem: AmityCommunityCategory
        ): Boolean {
            return oldItem.getCategoryId() == newItem.getCategoryId()
        }

        override fun areContentsTheSame(
            oldItem: AmityCommunityCategory,
            newItem: AmityCommunityCategory
        ): Boolean {
            return oldItem.getCategoryId() == newItem.getCategoryId() && oldItem.getName() == newItem.getName()
        }

    }

}

interface AmityCategorySelectionListener {
    fun setSelection(selectedCategory: String?)
    fun getSelection(): String?
}
