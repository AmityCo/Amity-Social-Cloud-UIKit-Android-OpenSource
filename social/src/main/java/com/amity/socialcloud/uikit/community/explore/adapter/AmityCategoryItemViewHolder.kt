package com.amity.socialcloud.uikit.community.explore.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagingDataAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.explore.listener.AmityCategoryItemClickListener
import com.amity.socialcloud.uikit.community.views.communitycategory.AmityCommunityCategoryView

open class AmityCategoryItemViewHolder(
    itemView: View,
    val itemClickListener: AmityCategoryItemClickListener?
) : RecyclerView.ViewHolder(itemView),
    AmityBaseRecyclerViewPagingDataAdapter.Binder<AmityCommunityCategory> {

    private val categoryView: AmityCommunityCategoryView = itemView.findViewById(R.id.categoryView)

    override fun bind(data: AmityCommunityCategory?, position: Int) {
        data?.let {
            categoryView.setCategory(it)
            itemView.setOnClickListener {
                itemClickListener?.onCategorySelected(data)
            }
        }

    }
}