package com.amity.socialcloud.uikit.community.explore.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.social.community.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagedAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.explore.listener.AmityCategoryItemClickListener
import com.amity.socialcloud.uikit.community.views.communitycategory.AmityCommunityCategoryView

open class AmityCategoryItemViewHolder(
    itemView: View,
    val itemClickListener: AmityCategoryItemClickListener?
) : RecyclerView.ViewHolder(itemView),
    AmityBaseRecyclerViewPagedAdapter.Binder<AmityCommunityCategory> {
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