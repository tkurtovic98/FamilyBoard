package com.hr.kurtovic.tomislav.familyboard.main_board

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.api.MessageHelper
import com.hr.kurtovic.tomislav.familyboard.models.Message


class MainBoardMessageAdapter(options: FirestoreRecyclerOptions<Message>,
                              private val glide: RequestManager,
                              private val callback: Listener, private val idCurrentUser: String)
    : FirestoreRecyclerAdapter<Message, MessageViewHolder>(options), ItemClickListener {

    interface Listener {
        fun onDataChanged()
    }

    override fun onBindViewHolder(messageViewHolder: MessageViewHolder, i: Int, message: Message) {
        messageViewHolder.registerItemClickListener(this)
        messageViewHolder.updateWithMessage(
                message,
                this.idCurrentUser,
                this.glide
        )
        if (message.id == null) {
            MessageHelper.setMessageId(message, snapshots.getSnapshot(i).id, "main")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.main_board_message_card, parent, false))
    }

    override fun itemClicked(position: Int) {
        MessageHelper.setMessageAccepted(getItem(position), "main")
    }

    override fun onDataChanged() {
        super.onDataChanged()
        this.callback.onDataChanged()
    }
}
