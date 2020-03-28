package com.hr.kurtovic.tomislav.familyboard.main_board.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember
import com.hr.kurtovic.tomislav.familyboard.models.Message
import kotlinx.android.synthetic.main.main_board_message_card.view.*
import java.text.SimpleDateFormat
import java.util.*

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun updateWithMessage(message: Message, currentUserId: String) {

        message.userRef?.get()?.addOnSuccessListener {
            val memberSender = it.toObject(FamilyMember::class.java)

            //Check if current user is the sender
            val isCurrentUser = memberSender?.uid == currentUserId

            //TODO update
//        //Update message TextView
//        itemView.main_board_message_text.text = message.message
//
//        //Update date TextView
//        if (message.dateCreated != null) {
//            itemView.main_board_message_date.text = this.convertDateToHour(message.dateCreated)
//        }

            //Update profile picture ImageView
            memberSender?.urlPicture.let { url ->
                Glide.with(itemView.main_board_message_image.context)
                        .load(url)
                        .apply(RequestOptions.circleCropTransform())
                        .into(itemView.main_board_message_image)
            }
        }
    }

    private fun convertDateToHour(date: Date?): String {
        @SuppressLint("SimpleDateFormat")
        val dfTime = SimpleDateFormat("HH:mm")
        return dfTime.format(date!!)
    }
}
