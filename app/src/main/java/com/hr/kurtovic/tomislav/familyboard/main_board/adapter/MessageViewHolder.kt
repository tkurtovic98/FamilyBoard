package com.hr.kurtovic.tomislav.familyboard.main_board.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.ktx.toObject
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember
import com.hr.kurtovic.tomislav.familyboard.models.Message
import kotlinx.android.synthetic.main.main_board_message_card.view.*
import java.text.SimpleDateFormat
import java.util.*

enum class PopupMenuItem {
    ACCEPTED,
    SHOW,
    DELETE
}


class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val categories = mapOf<String, Drawable>(
        "pets" to itemView.resources.getDrawable(R.drawable.ic_pets_black, null),
        "event" to itemView.resources.getDrawable(R.drawable.ic_event, null),
        "store" to itemView.resources.getDrawable(R.drawable.ic_store_black, null)
    )

    fun bind(
        message: Message,
        currentUserId: String,
        position: Int,
        menuItemClickListener: MenuItemClickListener
    ) {

        var canDelete = false

        message.memberSenderRef?.get()?.addOnSuccessListener {
            val memberSender = it.toObject<FamilyMember>()

            //Update profile picture ImageView
            memberSender?.urlPicture.let { url ->
                Glide.with(itemView.main_board_message_profile_image.context)
                        .load(url)
                        .apply(RequestOptions.circleCropTransform())
                        .into(itemView.main_board_message_profile_image)
            }

            canDelete = memberSender?.uid == currentUserId
        }

        Glide.with(itemView.main_board_message_category.context)
                .load(categories[message.category])
                .apply(RequestOptions.circleCropTransform())
                .into(itemView.main_board_message_category)

        if (message.accepted) {
            Glide.with(itemView.main_board_message_accepted.context)
                    .load(itemView.resources.getDrawable(R.drawable.ic_check_24dp, null))
                    .apply(RequestOptions.circleCropTransform())
                    .into(itemView.main_board_message_accepted)
        }

        itemView.main_board_message_menu_button.setOnClickListener {
            popupMenu(
                menuItemClickListener,
                message,
                canDelete
            )
        }
    }

    private fun popupMenu(
        menuItemClickListener: MenuItemClickListener,
        message: Message,
        canDelete: Boolean
    ) {
        val popup = PopupMenu(itemView.context, itemView.main_board_message_menu_button)
        //inflating menu from xml resource
        popup.inflate(R.menu.message_card_menu)

        popup.menu.findItem(R.id.message_card_accept).isVisible = !message.accepted
        popup.menu.findItem(R.id.message_card_delete).isVisible = canDelete

        //adding click listener
        popup.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.message_card_more_info -> {
                    menuItemClickListener.onMenuItemClick(message, PopupMenuItem.SHOW)
                    true
                }
                R.id.message_card_accept -> {
                    menuItemClickListener.onMenuItemClick(message, PopupMenuItem.ACCEPTED)
                    true
                }
                R.id.message_card_delete -> {
                    menuItemClickListener.onMenuItemClick(message, PopupMenuItem.DELETE)
                    true
                }
                else -> false
            }
        }
        //displaying the popup
        popup.show()
    }

    private fun convertDateToHour(date: Date?): String {
        @SuppressLint("SimpleDateFormat")
        val dfTime = SimpleDateFormat("HH:mm")
        return dfTime.format(date!!)
    }
}
