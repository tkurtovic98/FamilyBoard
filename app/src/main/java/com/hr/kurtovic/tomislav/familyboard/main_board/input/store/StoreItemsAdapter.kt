package com.hr.kurtovic.tomislav.familyboard.main_board.input.store

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.hr.kurtovic.tomislav.familyboard.R
import kotlinx.android.synthetic.main.store_item.view.*

interface ItemCountChangeListener {
    fun countChange(item: String, count: Int)
}

class StoreItemsAdapter(
    private val storeItems: List<String>,
    private val itemCountChangeListener: ItemCountChangeListener
) :
    RecyclerView.Adapter<StoreItemsAdapter.ItemViewHolder>() {

    override fun getItemCount(): Int =
            storeItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)
            )


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(storeItems[position], itemCountChangeListener = itemCountChangeListener)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(storeItem: String, itemCountChangeListener: ItemCountChangeListener) {
            itemView.store_item_name.text = storeItem

            val storeItemCount = itemView.store_item_count

            storeItemCount.doAfterTextChanged {
                itemCountChangeListener.countChange(
                    item = itemView.store_item_name.text.toString(),
                    count = storeItemCount.text.toString().toInt()
                )
            }

            itemView.store_item_add.setOnClickListener {
                val currentCount = storeItemCount.text.toString().toInt()
                val newCount = currentCount.plus(1)
                storeItemCount.setText(newCount.toString())
            }

            itemView.store_item_remove.setOnClickListener {
                val currentCount = storeItemCount.text.toString().toInt()
                if (currentCount > 0) {
                    val newCount = currentCount.minus(1)
                    storeItemCount.setText(newCount.toString())
                }
            }
        }
    }


}