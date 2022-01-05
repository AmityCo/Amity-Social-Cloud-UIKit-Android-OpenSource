package com.amity.socialcloud.uikit.community.setting.viewholder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.community.databinding.AmityItemSettingsHeaderBinding
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem

class AmitySettingsHeaderViewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView),
        AmityBaseRecyclerViewAdapter.IBinder<AmitySettingsItem> {
    private val binding = AmityItemSettingsHeaderBinding.bind(itemView)

    override fun bind(data: AmitySettingsItem?, position: Int) {
        binding.apply {
            when (data) {
                is AmitySettingsItem.Header -> {
                    tvHeader.text = context.getString(data.title)
                }
                else -> {
                }
            }
        }
    }

}