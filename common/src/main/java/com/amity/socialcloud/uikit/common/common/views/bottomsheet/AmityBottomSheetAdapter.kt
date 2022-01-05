package com.amity.socialcloud.uikit.common.common.views.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.databinding.AmityItemBottomSheetBinding
import com.amity.socialcloud.uikit.common.model.AmityMenuItem

class AmityBottomSheetAdapter(private val list: List<AmityMenuItem>,
                              private val listener: AmityMenuItemClickListener?) : RecyclerView.Adapter<AmityBottomSheetAdapter.BottomSheetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: AmityItemBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.amity_item_bottom_sheet, parent, false)
        return BottomSheetViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class BottomSheetViewHolder(
            private val binding: AmityItemBottomSheetBinding,
            private val listener: AmityMenuItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AmityMenuItem?) {
            if (item != null) {
                binding.menuItem = item
                binding.listener = listener
            }
        }
    }
}