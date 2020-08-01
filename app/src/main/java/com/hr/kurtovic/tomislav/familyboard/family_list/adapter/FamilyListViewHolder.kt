package com.hr.kurtovic.tomislav.familyboard.family_list.adapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hr.kurtovic.tomislav.familyboard.models.Family
import kotlinx.android.synthetic.main.family_list_item.view.*

class FamilyListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private fun updateWithFamily(family: Family) {
        itemView.family_list_family_name.text = family.name
    }

    private fun addListener(listener: FamilyListItemListener) {
        itemView.family_list_add_member_to_family.setOnClickListener {
            Log.d("Clicked Family", itemView.family_list_family_name.text.toString())
            listener.onItemClick(itemView.family_list_family_name.text.toString())
        }
    }

    fun bind(family: Family, listener: FamilyListItemListener) {
        updateWithFamily(family)
        addListener(listener)
    }
}
