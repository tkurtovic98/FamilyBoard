package com.hr.kurtovic.tomislav.familyboard.main_board.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.models.Message


class MainBoardMessageAdapter(
    options: FirestoreRecyclerOptions<Message>,
    private val idCurrentUser: String
) : FirestoreRecyclerAdapter<Message, MessageViewHolder>(options) {

    override fun onBindViewHolder(messageViewHolder: MessageViewHolder, i: Int, message: Message) {
        messageViewHolder.updateWithMessage(
            message,
            this.idCurrentUser
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MessageViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.main_board_message_card, parent, false)
            )
}
