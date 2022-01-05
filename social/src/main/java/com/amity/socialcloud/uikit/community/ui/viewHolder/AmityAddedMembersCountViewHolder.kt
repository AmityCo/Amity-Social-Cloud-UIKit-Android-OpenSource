package com.amity.socialcloud.uikit.community.ui.viewHolder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.uikit.common.common.setShape
import com.amity.socialcloud.uikit.common.common.toCircularShape
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.data.AmitySelectMemberItem
import com.amity.socialcloud.uikit.community.databinding.AmityViewAddedMemberWithCountBinding
import com.amity.socialcloud.uikit.community.ui.clickListener.AmityAddedMemberClickListener

class AmityAddedMembersCountViewHolder(
    itemView: View,
    private val mClickListener: AmityAddedMemberClickListener
) :
    AmityAddedMembersViewHolder(itemView, mClickListener) {

    private val layoutMember: ConstraintLayout = itemView.findViewById(R.id.layoutAddedMember)
    private val binding: AmityViewAddedMemberWithCountBinding? = DataBindingUtil.bind(itemView)

    init {
        val radius = itemView.context.resources.getDimensionPixelSize(R.dimen.amity_twenty_four).toFloat()
        layoutMember.setShape(
            radius, radius, radius, radius, R.color.amityColorBase,
            R.color.amityColorBase, AmityColorShade.SHADE4
        )
        binding?.ivAdd?.toCircularShape(
            AmityColorPaletteUtil.getColor(
                ContextCompat.getColor(itemView.context, R.color.amityColorBase), AmityColorShade.SHADE4
            )
        )
        binding?.tvCount?.toCircularShape(
            AmityColorPaletteUtil.getColor(
                ContextCompat.getColor(itemView.context, R.color.amityColorBase), AmityColorShade.SHADE4
            )
        )
    }

    override fun bind(data: AmitySelectMemberItem?, position: Int) {
        super.bind(data, position)
        if (data != null) {
            binding?.tvCount?.text = "+${data.subTitle}"
            binding?.executePendingBindings()
            if (data.subTitle == "0") {
                binding?.tvCount?.visibility = View.GONE
                binding?.ivAdd?.visibility = View.VISIBLE
            } else {
                binding?.tvCount?.visibility = View.VISIBLE
                binding?.ivAdd?.visibility = View.GONE
            }
            binding?.ivAdd?.setOnClickListener {
                mClickListener.onAddButtonClicked()
            }

            binding?.tvCount?.setOnClickListener {
                mClickListener.onMemberCountClicked()
            }
        }

    }
}