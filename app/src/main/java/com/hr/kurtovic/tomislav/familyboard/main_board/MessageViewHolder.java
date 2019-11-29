package com.hr.kurtovic.tomislav.familyboard.main_board;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.hr.kurtovic.tomislav.familyboard.R;
import com.hr.kurtovic.tomislav.familyboard.api.MessageHelper;
import com.hr.kurtovic.tomislav.familyboard.models.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hr.kurtovic.tomislav.familyboard.R.drawable.main_board_recycler_view_item_background_accepted;
import static com.hr.kurtovic.tomislav.familyboard.R.drawable.main_board_recycler_view_item_background_default;


public class MessageViewHolder extends RecyclerView.ViewHolder {

    //ROOT VIEW
    /*@BindView(R.id.main_board_root_view)
    LinearLayout rootView;*/

    //Buttons CONTAINER
    @BindView(R.id.main_board_button_container)
    LinearLayout buttonContainer;

    @BindView(R.id.main_board_message_image)
    ImageView imageViewProfile;

    @BindView(R.id.main_board_message_text)
    MaterialTextView textViewMessage;
    //DATE TEXT
    @BindView(R.id.main_board_message_date)
    MaterialTextView textViewDate;

    @BindView(R.id.main_board_message_accept_button)
    MaterialButton acceptButton;

    ItemClickListener listener;

    public MessageViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.main_board_message_accept_button)
    public void accept(){
        this.listener.itemClicked(getAdapterPosition());
    }

    public void registerItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    public void removeItemClickListener(){
        this.listener = null;
    }

    public void updateWithMessage(Message message, String currentUserId, RequestManager glide){

        //Check if current user is the sender
        boolean isCurrentUser = message.getUserSender().getUid().equals(currentUserId);

       /* if (message.isAccepted()) {
            setAccepted();
        } else {
            setDefault();
        }*/

        //Update message TextView
        this.textViewMessage.setText(message.getMessage());

        //Update date TextView
        if(message.getDateCreated() != null){
            this.textViewDate.setText(this.convertDateToHour(message.getDateCreated()));
        }

        //Update profile picture ImageView
        if(message.getUserSender().getUrlPicture() != null){
            glide.load(message.getUserSender().getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(this.imageViewProfile);
        }
    }

  /*  private void setDefault() {
        rootView.setBackgroundResource(main_board_recycler_view_item_background_default);
        acceptButton.setEnabled(true);
    }

    private void setAccepted() {
        rootView.setBackgroundResource(main_board_recycler_view_item_background_accepted);
        acceptButton.setEnabled(false);
    }*/

    private String convertDateToHour(Date date){
        @SuppressLint("SimpleDateFormat")
        DateFormat dfTime = new SimpleDateFormat("HH:mm");
        return dfTime.format(date);
    }
}
