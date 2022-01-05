package com.amity.socialcloud.uikit.community.ui.viewHolder

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.common.common.toCircularShape
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.data.AmitySelectMemberItem
import com.amity.socialcloud.uikit.community.ui.clickListener.AmityAddedMemberClickListener

class AmityAddedMemberWithAddButtonViewHolder(
    itemView: View,
    private val mClickListener: AmityAddedMemberClickListener
) :
    RecyclerView.ViewHolder(itemView), AmityBaseRecyclerViewAdapter.IBinder<AmitySelectMemberItem> {

    private val ivAdd: ImageView = itemView.findViewById(R.id.ivAdd)

    init {
        ivAdd.toCircularShape(
            AmityColorPaletteUtil.getColor(
                ContextCompat.getColor(itemView.context, R.color.amityColorBase), AmityColorShade.SHADE4
            )
        )
    }

    override fun bind(data: AmitySelectMemberItem?, position: Int) {
        ivAdd.setOnClickListener {
            mClickListener.onAddButtonClicked()
        }
    }
}