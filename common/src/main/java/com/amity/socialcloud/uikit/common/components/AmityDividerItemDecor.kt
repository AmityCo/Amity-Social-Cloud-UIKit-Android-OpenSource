package com.amity.socialcloud.uikit.common.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView


class AmityDividerItemDecor : RecyclerView.ItemDecoration {

    private val ATTRS = intArrayOf(android.R.attr.listDivider)
    private var mDivider: Drawable? = null

    constructor(divider: Drawable?) {
        mDivider = divider
    }

    constructor(context: Context) {
        val styledAttributes = context.obtainStyledAttributes(ATTRS)
        mDivider = styledAttributes.getDrawable(0)
        styledAttributes.recycle()
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0..childCount - 2) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + mDivider!!.intrinsicHeight
            mDivider!!.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mDivider!!.draw(c)
        }
    }


}