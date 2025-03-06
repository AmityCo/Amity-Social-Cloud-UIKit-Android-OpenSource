package com.amity.socialcloud.uikit.common.memberpicker.viewholder

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagingDataAdapter
import com.amity.socialcloud.uikit.common.databinding.AmityItemHeaderSelectMemberBinding
import com.amity.socialcloud.uikit.common.memberpicker.adapter.DisplayNameGrouper
import com.amity.socialcloud.uikit.common.memberpicker.listener.AmitySelectMemberListener


class AmityMemberListHeaderViewHolder(
    itemView: View,
    private val mClickListener: AmitySelectMemberListener,
    private val membersSet: HashSet<String>
) :
    RecyclerView.ViewHolder(itemView),
    AmityBaseRecyclerViewPagingDataAdapter.Binder<AmityUser> {

    private val binding: AmityItemHeaderSelectMemberBinding? = DataBindingUtil.bind(itemView)

    override fun bind(data: AmityUser?, position: Int) {
        if (data != null) {
            val groupingKey = DisplayNameGrouper.getGroupingKey(data.getDisplayName())
            binding?.tvHeader?.text = groupingKey
            if (data.getDisplayName().isNullOrEmpty()) {
                binding?.layoutMember?.smTitle?.text =
                    itemView.context.getString(R.string.amity_anonymous)
            } else {
                binding?.layoutMember?.smTitle?.text = data.getDisplayName()
            }
            binding?.layoutMember?.avatarUrl = data.getAvatar()?.getUrl(AmityImage.Size.MEDIUM)
            binding?.layoutMember?.smSubTitle?.text = ""
            binding?.layoutMember?.ivStatus?.isChecked = membersSet.contains(data.getUserId())
            binding?.layoutMember?.ivStatus?.setOnClickListener {
                mClickListener.onMemberClicked(data, position)
            }
        }
    }
}