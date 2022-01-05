package com.amity.socialcloud.uikit.community.setting.viewholder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.common.components.setBold
import com.amity.socialcloud.uikit.common.components.setImageResource
import com.amity.socialcloud.uikit.common.components.setVisibility
import com.amity.socialcloud.uikit.community.databinding.AmityItemSettingsToggleContentBinding
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AmitySettingsToggleContentViewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView),
        AmityBaseRecyclerViewAdapter.IBinder<AmitySettingsItem> {
    private val binding = AmityItemSettingsToggleContentBinding.bind(itemView)

    override fun bind(data: AmitySettingsItem?, position: Int) {
        binding.apply {
            when (data) {
                is AmitySettingsItem.ToggleContent -> {
                    data.icon?.let { setImageResource(mainSettingsContent.ivIcon, it) }
                    setVisibility(mainSettingsContent.ivIcon, data.icon != null)

                    mainSettingsContent.tvTitle.text = context.getString(data.title)
                    setBold(mainSettingsContent.tvTitle, data.isTitleBold)

                    mainSettingsContent.tvDescription.text = data.description?.let(context::getString)
                    setVisibility(mainSettingsContent.tvDescription, data.description != null)

                    data.isToggled
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext { svButton.isChecked = it }
                            .untilLifecycleEnd(view = itemView)
                            .subscribe()

                    svButton.setOnClickListener { data.callback(svButton.isChecked) }
                }
                else -> {
                }
            }
        }
    }
}