package com.hr.kurtovic.tomislav.familyboard.family_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.models.Family

interface FamilyListItemListener {
    fun onItemClick(familyName: String)
}

class FamilyListAdapter(
    options: FirestoreRecyclerOptions<Family>,
    private val listener: FamilyListItemListener
) : FirestoreRecyclerAdapter<Family, FamilyListViewHolder>(options) {


    override fun onBindViewHolder(
        familyListViewHolder: FamilyListViewHolder,
        i: Int,
        family: Family
    ) {
        familyListViewHolder.bind(family, listener)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            FamilyListViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.family_list_item, parent, false)
            )
}
