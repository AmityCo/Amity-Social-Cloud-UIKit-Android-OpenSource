package com.amity.socialcloud.uikit.community.ui.viewHolder

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.common.common.loadImage
import com.amity.socialcloud.uikit.community.data.AmitySelectMemberItem
import com.amity.socialcloud.uikit.community.databinding.AmityItemSelectedMemberBinding
import com.amity.socialcloud.uikit.community.ui.clickListener.AmitySelectedMemberListener

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