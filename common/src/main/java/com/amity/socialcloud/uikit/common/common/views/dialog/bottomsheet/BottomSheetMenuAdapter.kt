package com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.databinding.AmityItemBottomSheetMenuBinding

class BottomSheetMenuAdapter(private var items: List<BottomSheetMenuItem>) : RecyclerView.Adapter<BottomSheetMenuAdapter.BottomSheetMenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetMenuViewHolder {
        return BottomSheetMenuViewHolder(AmityItemBottomSheetMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BottomSheetMenuViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class BottomSheetMenuViewHolder(val binding: AmityItemBottomSheetMenuBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BottomSheetMenuItem) {
            with(binding) {
                bottomMenuTitle.text = itemView.resources.getString(item.titleResId)
                if(item.iconResId != null) {
                    bottomMenuIcon.setImageResource(item.iconResId)
                    bottomMenuIcon.visibility = View.VISIBLE
                } else {
                    bottomMenuIcon.visibility = View.GONE
                }
                if (item.colorResId != null) {
                    bottomMenuTitle.setTextColor(itemView.resources.getColor(item.colorResId))
                } else {
                    bottomMenuTitle.setTextColor(itemView.resources.getColor(R.color.amityColorBlack))
                }
                root.setOnClickListener { item.action() }
            }
        }
    }

    fun submitList(items: List<BottomSheetMenuItem>) {
        this.items = items
        notifyDataSetChanged()
    }

}