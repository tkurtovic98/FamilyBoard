package com.hr.kurtovic.tomislav.familyboard.ui;


import android.Manifest;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.hr.kurtovic.tomislav.familyboard.BoardActivity;
import com.hr.kurtovic.tomislav.familyboard.CurrentBoardKeyHolder;
import com.hr.kurtovic.tomislav.familyboard.R;
import com.hr.kurtovic.tomislav.familyboard.api.MessageHelper;
import com.hr.kurtovic.tomislav.familyboard.api.UserHelper;
import com.hr.kurtovic.tomislav.familyboard.main_board.MainBoardMessageAdapter;
import com.hr.kurtovic.tomislav.familyboard.models.Message;
import com.hr.kurtovic.tomislav.familyboard.models.User;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomChatFragment extends Fragment implements MainBoardMessageAdapter.Listener {

    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;

    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;

    // FOR DESIGN
    // 1 - Getting all views needed
    @BindView(R.id.main_board_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.main_board_fragment_empty_message_tv)
    MaterialTextView textViewRecyclerViewEmpty;
    @BindView(R.id.main_board_fragment_input_add_text)
    TextInputEditText editTextMessage;

    // FOR DATA
    // 2 - Declaring Adapter and data
    private MainBoardMessageAdapter mainBoardMessageAdapter;
    @Nullable
    private User modelCurrentUser;

    private Uri uriImageSelected;

    public RoomChatFragment() {

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_board, container, false);
        ButterKnife.bind(this, view);
        textViewRecyclerViewEmpty.setVisibility(View.GONE);
        this.getCurrentUserFromFirestore();
        this.configureRecyclerView();
        return view;
    }

    @Override
    public void onDataChanged() {
        textViewRecyclerViewEmpty.setVisibility(this.mainBoardMessageAdapter.getItemCount() == 0 ?
                View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.main_board_fragment_input_send)
    public void send() {
        String input = editTextMessage.getText().toString();

        if(TextUtils.isEmpty(input)){
            Snackbar.make(getView(), "Message can not be empty", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .show();
            return;
        }

        MessageHelper.createMessageForChat(
                input , CurrentBoardKeyHolder.getInstance().getCurrentKey(), modelCurrentUser)
                .addOnFailureListener(e -> Snackbar.make(RoomChatFragment.this.getView(),"Err", Snackbar.LENGTH_LONG));
        mainBoardMessageAdapter.notifyDataSetChanged();
        this.editTextMessage.setText("");
    }

    private void getCurrentUserFromFirestore() {
        UserHelper.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addOnSuccessListener(documentSnapshot -> modelCurrentUser = documentSnapshot.toObject(User.class));
    }

    private void configureRecyclerView() {
        //Configure Adapter & RecyclerView
        this.mainBoardMessageAdapter =
                new MainBoardMessageAdapter(
                        generateOptionsForAdapter((MessageHelper.getAllMessageForChat(CurrentBoardKeyHolder.getInstance().getCurrentKey()))),
                        Glide.with(this),
                        this,
                        FirebaseAuth.getInstance().getCurrentUser().getUid()
                );
        mainBoardMessageAdapter.registerAdapterDataObserver(
                new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        recyclerView.smoothScrollToPosition(mainBoardMessageAdapter.getItemCount());
                    }
                }
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(this.mainBoardMessageAdapter);
    }

    private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query) {
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();
    }





    /*private void uploadPhotoInFirebaseAndSendMessage(final String message) {
        String uuid = UUID.randomUUID().toString();

        StorageReference mImageRef =
                FirebaseStorage.getInstance().getReference(uuid);
        mImageRef.putFile(this.uriImageSelected)
                .addOnSuccessListener(this,
                        taskSnapshot -> {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri ->
                                    MessageHelper.createMessageWithImageForChat(uri.toString(), message, currentChatName, modelCurrentUser)
                                    .addOnFailureListener(onFailureListener()));
                        }).addOnFailureListener(this.onFailureListener());
    }*/

    /*@OnClick(R.id.)
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void onClickAddFile() {
        this.chooseImageFromPhone();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void chooseImageFromPhone() {
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this,
                    getString(R.string.popup_title_permission_files_access),
                    RC_IMAGE_PERMS,
                    PERMS);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RC_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                this.uriImageSelected = data.getData();
                Glide.with(this)
                        .load(this.uriImageSelected)
                        .apply(RequestOptions.circleCropTransform())
                        .into(this.imageViewPreview);
            } else {
                Toast.makeText(this, getString(R.string.toast_title_no_image_chosen), Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}
