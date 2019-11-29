package com.hr.kurtovic.tomislav.familyboard.main_board;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.common.hash.HashCode;
import com.hr.kurtovic.tomislav.familyboard.R;
import com.hr.kurtovic.tomislav.familyboard.api.MessageHelper;
import com.hr.kurtovic.tomislav.familyboard.models.Message;

import butterknife.OnClick;

public class MainBoardMessageAdapter extends FirestoreRecyclerAdapter<Message, MessageViewHolder> implements ItemClickListener {

    public interface Listener {
        void onDataChanged();
    }

    //FOR DATA
    private final RequestManager glide;
    private final String idCurrentUser;

    //FOR COMMUNICATION
    private Listener callback;

    public MainBoardMessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options, RequestManager glide,
                             Listener callback, String idCurrentUser) {
        super(options);
        this.glide = glide;
        this.callback = callback;
        this.idCurrentUser = idCurrentUser;
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i, @NonNull Message message) {
        messageViewHolder.registerItemClickListener(this);
        messageViewHolder.updateWithMessage(
                message,
                this.idCurrentUser,
                this.glide
        );
        if(message.getID() == null){
            MessageHelper.setMessageId(message, getSnapshots().getSnapshot(i).getId(), "main");
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_board_message_card, parent, false));
    }

    @Override
    public void itemClicked(int position) {
        MessageHelper.setMessageAccepted(getItem(position), "main");
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }
}
