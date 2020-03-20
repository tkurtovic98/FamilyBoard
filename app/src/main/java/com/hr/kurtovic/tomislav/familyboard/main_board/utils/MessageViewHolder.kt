package com.hr.kurtovic.tomislav.familyboard.main_board.utils

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hr.kurtovic.tomislav.familyboard.models.Message
import kotlinx.android.synthetic.main.main_board_message_card.view.*
import java.text.SimpleDateFormat
import java.util.*

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var listener: ItemClickListener? = null

    init {
        itemView.main_board_message_accept_button.setOnClickListener { accept() }
    }

    fun accept() {
        this.listener!!.itemClicked(adapterPosition)
    }

    fun registerItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    fun removeItemClickListener() {
        this.listener = null
    }

    fun updateWithMessage(message: Message, currentUserId: String) {

        //Check if current user is the sender
        val isCurrentUser = message.userSender!!.uid == currentUserId

        //TODO update
//        //Update message TextView
//        itemView.main_board_message_text.text = message.message
//
//        //Update date TextView
//        if (message.dateCreated != null) {
//            itemView.main_board_message_date.text = this.convertDateToHour(message.dateCreated)
//        }

        //Update profile picture ImageView
        if (message.userSender!!.urlPicture != null) {
            Glide.with(itemView.main_board_message_image.context)
                    .load(message.userSender!!.urlPicture)
                    .apply(RequestOptions.circleCropTransform())
                    .into(itemView.main_board_message_image)
        }
    }


    private fun convertDateToHour(date: Date?): String {
        @SuppressLint("SimpleDateFormat")
        val dfTime = SimpleDateFormat("HH:mm")
        return dfTime.format(date!!)
    }
}
