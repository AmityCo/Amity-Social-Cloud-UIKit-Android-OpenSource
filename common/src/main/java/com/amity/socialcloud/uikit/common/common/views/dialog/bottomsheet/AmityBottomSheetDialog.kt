package com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class AmityBottomSheetDialog(context: Context, items: List<BottomSheetMenuItem>? = listOf()) {

    private val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
    private val adapter = BottomSheetMenuAdapter(items ?: listOf())

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.amity_bottom_sheet_menu, null, false)
        bottomSheetDialog.setContentView(view)
        (view.findViewById<RecyclerView>(R.id.bottom_sheet_recyclerview))?.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }
    }

    fun show() {
        bottomSheetDialog.show()
    }

    fun show(items: List<BottomSheetMenuItem>) {
        adapter.submitList(items)
        bottomSheetDialog.show()
    }

    fun dismiss() {
        bottomSheetDialog.dismiss()
    }

}