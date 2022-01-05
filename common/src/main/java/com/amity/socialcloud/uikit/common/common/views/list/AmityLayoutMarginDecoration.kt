package com.amity.socialcloud.uikit.common.common.views.list

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class AmityLayoutMarginDecoration(
    val left: Int,
    val right: Int,
    val top: Int,
    val bottom: Int
) : RecyclerView.ItemDecoration() {

    constructor(margin: Int) : this(margin, margin, margin, margin)


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.adapter != null) {
            outRect.left = left
            outRect.right = right
            outRect.top = top
            outRect.bottom = bottom
        }
    }

}