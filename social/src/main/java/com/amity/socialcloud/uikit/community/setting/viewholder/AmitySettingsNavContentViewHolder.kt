package com.amity.socialcloud.uikit.community.setting.viewholder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.common.components.setBold
import com.amity.socialcloud.uikit.common.components.setImageResource
import com.amity.socialcloud.uikit.common.components.setVisibility
import com.amity.socialcloud.uikit.community.databinding.AmityItemSettingsNavContentBinding
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem

class AmitySettingsNavContentViewHolder(val context: Context, itemView: View) :
        RecyclerView.ViewHolder(itemView),
        AmityBaseRecyclerViewAdapter.IBinder<AmitySettingsItem> {
    private val binding = AmityItemSettingsNavContentBinding.bind(itemView)

    override fun bind(data: AmitySettingsItem?, position: Int) {
        binding.apply {
            when (data) {
                is AmitySettingsItem.NavigationContent -> {
                    data.icon?.let { setImageResource(mainSettingsContent.ivIcon, it) }
                    setVisibility(mainSettingsContent.ivIcon, data.icon != null)

                    mainSettingsContent.tvTitle.text = context.getString(data.title)
                    setBold(mainSettingsContent.tvTitle, data.isTitleBold)

                    mainSettingsContent.tvDescription.text = data.description?.let(context::getString)
                    setVisibility(mainSettingsContent.tvDescription, data.description != null)

                    tvValue.text = data.value?.let(context::getString)
                    setVisibility(tvValue, data.value != null)

                    data.iconNavigation?.let { setImageResource(ivNavigation, it) }
                    setVisibility(ivNavigation, data.iconNavigation != null)

                    rootView.setOnClickListener { data.callback() }
                }
                else -> {
                }
            }
        }
    }
}