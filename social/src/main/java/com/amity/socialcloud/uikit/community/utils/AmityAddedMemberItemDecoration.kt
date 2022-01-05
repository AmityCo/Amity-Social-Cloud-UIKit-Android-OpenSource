package com.amity.socialcloud.uikit.community.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class AmityAddedMemberItemDecoration(private val topMargin: Int, private val leftMargin: Int = 0) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) > 1) {
                top = topMargin
            }
            if (parent.getChildAdapterPosition(view) % 2 != 0) {
                left = leftMargin
            }
        }
    }
}