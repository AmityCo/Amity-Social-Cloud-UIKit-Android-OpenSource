package com.amity.socialcloud.uikit.chat.messages.popUp

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmitySelectableMessageViewModel


class AmityPopUp {

    private lateinit var popUpWindow: PopupWindow

    fun showPopUp(
        rootView: View,
        anchor: View,
        viewModel: AmitySelectableMessageViewModel,
        gravity: PopUpGravity
    ) {
        rootView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        rootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        val rootWidth = rootView.measuredWidth
        val rootHeight = rootView.measuredHeight

        popUpWindow = PopupWindow(
            rootView, ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, true
        )

        popUpWindow.setOnDismissListener {
            viewModel.inSelectionMode.set(false)
        }

        val location = IntArray(2)
        anchor.getLocationOnScreen(location)

        val anchorRect =
            Rect(location[0], location[1], location[0] + anchor.width, location[1] + anchor.height)

        if (anchorRect.top > 300) {
            viewModel.inSelectionMode.set(true)
            if (gravity == PopUpGravity.START) {
                popUpWindow.showAsDropDown(anchor, 0, -(anchor.height + rootHeight + 8))
            } else {
                popUpWindow.showAsDropDown(
                    anchor,
                    anchor.width - rootWidth,
                    -(anchor.height + rootHeight + 8)
                )
            }

        }
    }

    fun dismiss() {
        popUpWindow.dismiss()
    }

    enum class PopUpGravity {
        START,
        END
    }

}