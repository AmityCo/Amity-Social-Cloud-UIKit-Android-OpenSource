package com.amity.socialcloud.uikit.common.base

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class LinearLayoutPagerManager(
    context: Context?,
    orientation: Int,
    reverseLayout: Boolean,
    private val itemsPerPage: Int
) :
    LinearLayoutManager(context, orientation, reverseLayout) {

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
        return super.checkLayoutParams(lp) && lp.width == getItemSize()
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return setProperItemSize(super.generateDefaultLayoutParams())
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): RecyclerView.LayoutParams {
        return setProperItemSize(super.generateLayoutParams(lp))
    }

    private fun setProperItemSize(lp: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
        val itemSize = getItemSize()
        if (orientation == HORIZONTAL) {
            lp.width = itemSize
        } else {
            lp.height = itemSize
        }
        return lp
    }

    private fun getItemSize() : Int {
        val pageSize = if (orientation == HORIZONTAL) width else height
        return Math.round(pageSize.toFloat() / itemsPerPage)
    }

}