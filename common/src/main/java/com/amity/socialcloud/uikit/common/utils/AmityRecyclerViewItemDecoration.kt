package com.amity.socialcloud.uikit.common.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class AmityRecyclerViewItemDecoration(
    private var mTop: Int = 0, private var mLeft: Int = 0,
    private var mBottom: Int = 0, private var mRight: Int = 0
) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            top = mTop
            left = mLeft
            bottom = mBottom
            right = mRight
        }
    }
}