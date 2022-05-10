package com.amity.socialcloud.uikit.common.memberpicker.viewholder

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagedAdapter
import com.amity.socialcloud.uikit.common.memberpicker.listener.AmitySelectMemberListener
import com.amity.socialcloud.uikit.common.databinding.AmityItemHeaderSelectMemberBinding


class AmityMemberListHeaderViewHolder(
    itemView: View,
    private val mClickListener: AmitySelectMemberListener,
    private val membersSet: HashSet<String>
) :
    RecyclerView.ViewHolder(itemView),
    AmityBaseRecyclerViewPagedAdapter.Binder<AmityUser> {

    private val binding: AmityItemHeaderSelectMemberBinding? = DataBindingUtil.bind(itemView)

    override fun bind(data: AmityUser?, position: Int) {
        if (data != null) {
            if (data.getDisplayName().isNullOrEmpty()) {
                binding?.tvHeader?.text = "#"
                binding?.layoutMember?.smTitle?.text =
                    itemView.context.getString(R.string.amity_anonymous)
            } else {
                binding?.tvHeader?.text = data.getDisplayName()!![0].toString()
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