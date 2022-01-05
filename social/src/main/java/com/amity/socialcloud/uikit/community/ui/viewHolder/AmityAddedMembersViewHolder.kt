package com.amity.socialcloud.uikit.community.ui.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.common.common.loadImage
import com.amity.socialcloud.uikit.common.common.setShape
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.data.AmitySelectMemberItem
import com.amity.socialcloud.uikit.community.ui.clickListener.AmityAddedMemberClickListener
import com.google.android.material.imageview.ShapeableImageView

open class AmityAddedMembersViewHolder(
    itemView: View,
    private val mClickListener: AmityAddedMemberClickListener
) :
    RecyclerView.ViewHolder(itemView),
    AmityBaseRecyclerViewAdapter.IBinder<AmitySelectMemberItem> {

    private val binding: ViewDataBinding? = DataBindingUtil.bind(itemView)
    private val name: TextView = itemView.findViewById(R.id.amName)
    private val avatar: ShapeableImageView = itemView.findViewById(R.id.amAvatar)
    private val cancel: ImageView = itemView.findViewById(R.id.amCross)
    private val layout: ConstraintLayout? = itemView.findViewById(R.id.lAddedMemberItem)

    init {
        val radius = itemView.context.resources.getDimensionPixelSize(R.dimen.amity_twenty_four).toFloat()
        layout?.setShape(
            radius, radius, radius, radius, R.color.amityColorBase,
            R.color.amityColorBase, AmityColorShade.SHADE4
        )

    }

    override fun bind(data: AmitySelectMemberItem?, position: Int) {
        if (data != null) {
            name.text = data.name
            avatar.loadImage(data.avatarUrl)

            cancel.setOnClickListener {
                mClickListener.onMemberRemoved(data)
            }
        }
    }


}