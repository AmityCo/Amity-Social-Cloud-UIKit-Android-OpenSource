package com.amity.socialcloud.uikit.community.setting.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemSettingsRadioContentBinding
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem

class AmitySettingsRadioContentViewHolder(val context: Context, itemView: View) :
    RecyclerView.ViewHolder(itemView),
    AmityBaseRecyclerViewAdapter.IBinder<AmitySettingsItem> {

    private val binding: AmityItemSettingsRadioContentBinding? = DataBindingUtil.bind(itemView)

    override fun bind(data: AmitySettingsItem?, position: Int) {
        binding?.apply {
            when (data) {
                is AmitySettingsItem.RadioContent -> {
                    var checkedIndex = -1
                    radioGroup.removeAllViews()
                    for (i in data.choices.indices) {
                        val item = data.choices[i]
                        val radioButton: RadioButton = LayoutInflater.from(context)
                            .inflate(
                                R.layout.amity_view_radio_button,
                                radioGroup,
                                false
                            ) as RadioButton
                        radioButton.text = context.getString(item.first)
                        if (item.second) {
                            checkedIndex = i
                        }
                        radioGroup.addView(radioButton)
                        radioButton.setOnClickListener {
                            data.callback(item.first)
                        }
                    }
                    if (checkedIndex != -1) {
                        (radioGroup.getChildAt(checkedIndex) as RadioButton).isChecked = true
                    }

                }
                else -> {
                }
            }
        }
    }
}