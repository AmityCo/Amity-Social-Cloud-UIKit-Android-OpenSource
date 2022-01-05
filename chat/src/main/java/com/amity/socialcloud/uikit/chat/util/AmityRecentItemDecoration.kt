package com.amity.socialcloud.uikit.chat.util

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.amity.socialcloud.uikit.chat.R

class AmityRecentItemDecoration(private val context: Context, private val margin: Int) :
    ItemDecoration() {

    private var mDivider: Drawable? = null

    private val ATTRS = intArrayOf(
        android.R.attr.listDivider
    )

    init {
        init()
    }

    private fun init() {
        val a: TypedArray = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawDivider(c, parent)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            top = margin
        }
    }

    private fun drawDivider(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount - 1
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top =
                child.bottom + params.bottomMargin + context.resources.getDimensionPixelSize(R.dimen.amity_ten)
            val dividerHeight = mDivider?.intrinsicHeight ?: 0
            val bottom = top + dividerHeight

            mDivider?.setBounds(
                left + context.resources.getDimensionPixelSize(R.dimen.amity_sixty_eight),
                top, right - context.resources.getDimensionPixelSize(R.dimen.amity_padding_m1), bottom
            )
            mDivider?.draw(c)
        }
    }
}