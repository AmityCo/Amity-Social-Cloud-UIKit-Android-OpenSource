package com.amity.socialcloud.uikit.community.setting.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.community.databinding.AmityItemSeparateContentBinding
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem

class AmitySeparateContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        AmityBaseRecyclerViewAdapter.IBinder<AmitySettingsItem> {
    private val binding = AmityItemSeparateContentBinding.bind(itemView)

    override fun bind(data: AmitySettingsItem?, position: Int) = Unit

}