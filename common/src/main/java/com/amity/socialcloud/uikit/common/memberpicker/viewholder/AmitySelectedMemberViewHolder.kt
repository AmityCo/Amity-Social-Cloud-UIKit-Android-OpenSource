package com.amity.socialcloud.uikit.common.memberpicker.viewholder

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.common.common.loadImage
import com.amity.socialcloud.uikit.common.databinding.AmityItemSelectedMemberBinding
import com.amity.socialcloud.uikit.common.memberpicker.listener.AmitySelectedMemberListener
import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem

class AmitySelectedMemberViewHolder(
    itemView: View,
    private val mClickListener: AmitySelectedMemberListener
) :
    RecyclerView.ViewHolder(itemView),
    AmityBaseRecyclerViewAdapter.IBinder<AmitySelectMemberItem> {

    private val binding: AmityItemSelectedMemberBinding? = DataBindingUtil.bind(itemView)

    override fun bind(data: AmitySelectMemberItem?, position: Int) {

        if (data != null) {
            binding?.ivAvatar?.loadImage(data.avatarUrl)
            binding?.tvName?.text = data.name

            binding?.ivCross?.setOnClickListener {
                mClickListener.onMemberRemoved(data)
            }
        }

    }
}