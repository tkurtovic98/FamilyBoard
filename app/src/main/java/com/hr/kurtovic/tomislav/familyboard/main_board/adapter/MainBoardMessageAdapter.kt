package com.hr.kurtovic.tomislav.familyboard.main_board.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.models.Message

interface MenuItemClickListener {
    fun onMenuItemClick(message: Message, menuItem: PopupMenuItem)
}


class MainBoardMessageAdapter(
    options: FirestoreRecyclerOptions<Message>,
    private val idCurrentUser: String,
    private val menuItemClickListener: MenuItemClickListener
) : FirestoreRecyclerAdapter<Message, MessageViewHolder>(options) {

    override fun onBindViewHolder(messageViewHolder: MessageViewHolder, i: Int, message: Message) {
        messageViewHolder.bind(
            message,
            this.idCurrentUser,
            i,
            menuItemClickListener
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MessageViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.main_board_message_card, parent, false)
            )
}
