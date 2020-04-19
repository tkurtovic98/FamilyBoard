package com.hr.kurtovic.tomislav.familyboard.family_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.models.Family


class FamilyListAdapter(
    options: FirestoreRecyclerOptions<Family>
) : FirestoreRecyclerAdapter<Family, FamilyListViewHolder>(options) {

    override fun onBindViewHolder(
        familyListViewHolder: FamilyListViewHolder,
        i: Int,
        family: Family
    ) {
        familyListViewHolder.updateWithFamily(
            family
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            FamilyListViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.family_list_item, parent, false)
            )
}
