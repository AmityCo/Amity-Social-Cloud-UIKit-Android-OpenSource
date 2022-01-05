package com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.R
import kotlinx.android.synthetic.main.amity_item_bottom_sheet_menu.view.*

class BottomSheetMenuAdapter(private var items: List<BottomSheetMenuItem>) : RecyclerView.Adapter<BottomSheetMenuAdapter.BottomSheetMenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetMenuViewHolder {
        return BottomSheetMenuViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.amity_item_bottom_sheet_menu, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BottomSheetMenuViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class BottomSheetMenuViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: BottomSheetMenuItem) {
            with(view) {
                bottom_menu_title.text = view.resources.getString(item.titleResId)
                if(item.iconResId != null) {
                    bottom_menu_icon.setImageResource(item.iconResId)
                    bottom_menu_icon.visibility = View.VISIBLE
                } else {
                    bottom_menu_icon.visibility = View.GONE
                }
                if (item.colorResId != null) {
                    bottom_menu_title.setTextColor(context.resources.getColor(item.colorResId))
                } else {
                    bottom_menu_title.setTextColor(context.resources.getColor(R.color.amityColorBlack))
                }
                setOnClickListener { item.action() }
            }
        }
    }

    fun submitList(items: List<BottomSheetMenuItem>) {
        this.items = items
        notifyDataSetChanged()
    }

}