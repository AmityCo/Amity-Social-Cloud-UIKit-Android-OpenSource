package com.amity.socialcloud.uikit.community.setting.viewholder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.community.databinding.AmityItemSettingsMarginBinding
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem

class AmitySettingsMarginViewHolder(private val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView),
    AmityBaseRecyclerViewAdapter.IBinder<AmitySettingsItem> {
    private val binding = AmityItemSettingsMarginBinding.bind(itemView)

    override fun bind(data: AmitySettingsItem?, position: Int) {
        binding.apply {
            when(data) {
                is AmitySettingsItem.Margin -> {
                    val margin = context.resources.getDimensionPixelSize(data.margin)
                    vMargin.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, margin)
                }
                else -> {}
            }
        }
    }

}