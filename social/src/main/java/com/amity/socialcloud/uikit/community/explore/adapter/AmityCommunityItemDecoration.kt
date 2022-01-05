package com.amity.socialcloud.uikit.community.explore.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class AmityCommunityItemDecoration(
    private var mTop: Int = 0, private var mLeft: Int = 0,
    private var mBottom: Int = 0, private var mRight: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                left = 2 * mLeft
            }
            right = if (parent.getChildAdapterPosition(view) == 3) {
                2 * mRight
            } else {
                mRight
            }
            bottom = mBottom
            top = mTop

        }
    }
}