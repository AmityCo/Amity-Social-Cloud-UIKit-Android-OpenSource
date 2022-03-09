package com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.uikit.common.databinding.AmityBottomSheetMenuBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class AmityBottomSheetDialog(context: Context, items: List<BottomSheetMenuItem>? = listOf()) {

    private var _binding: AmityBottomSheetMenuBinding? = null
    private val binding get() = _binding!!
    private val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
    private val adapter = BottomSheetMenuAdapter(items ?: listOf())

    init {
        _binding = AmityBottomSheetMenuBinding.inflate(LayoutInflater.from(context), null, false)
        bottomSheetDialog.setContentView(binding.root)
        with(binding.root) {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
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