package com.hr.kurtovic.tomislav.familyboard.family_list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hr.kurtovic.tomislav.familyboard.models.Family
import kotlinx.android.synthetic.main.family_list_item.view.*

class FamilyListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun updateWithFamily(family: Family) {
        itemView.family_list_family_name.text = family.name
    }
}
