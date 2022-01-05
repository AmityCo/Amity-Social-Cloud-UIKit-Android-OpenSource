package com.amity.socialcloud.uikit.community.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.community.ui.viewHolder.AmityMemberListHeaderViewHolder

class AmitySelectMembersItemDecoration(
    private val headerMargin: Int,
    private val itemMargin: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            when (parent.getChildViewHolder(view)) {
                is AmityMemberListHeaderViewHolder -> {
                    if (parent.getChildAdapterPosition(view) > 0) {
                        top = headerMargin
                    }
                }

                else -> top = itemMargin
            }
        }
    }
}