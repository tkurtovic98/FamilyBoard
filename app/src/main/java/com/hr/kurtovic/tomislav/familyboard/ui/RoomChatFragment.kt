package com.hr.kurtovic.tomislav.familyboard.ui


import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.hr.kurtovic.tomislav.familyboard.CurrentBoardKeyHolder
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.api.MessageHelper
import com.hr.kurtovic.tomislav.familyboard.api.UserHelper
import com.hr.kurtovic.tomislav.familyboard.main_board.MainBoardMessageAdapter
import com.hr.kurtovic.tomislav.familyboard.models.Message
import com.hr.kurtovic.tomislav.familyboard.models.User
import kotlinx.android.synthetic.main.fragment_main_board.*

/**
 * A simple [Fragment] subclass.
 */
class RoomChatFragment : Fragment(), MainBoardMessageAdapter.Listener {


    companion object {
        fun newInstance() = RoomChatFragment()

        private val PERMS = Manifest.permission.READ_EXTERNAL_STORAGE

        private val RC_IMAGE_PERMS = 100
        private val RC_CHOOSE_PHOTO = 200
    }

    // FOR DATA

    // 2 - Declaring Adapter and data
    private lateinit var mainBoardMessageAdapter: MainBoardMessageAdapter
    private lateinit var modelCurrentUser: User

    private lateinit var uriImageSelected: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?
        , savedInstanceState: Bundle?
    )
            : View? = inflater.inflate(R.layout.fragment_main_board, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        main_board_fragment_empty_message_tv.visibility = View.GONE
        this.getCurrentUserFromFirestore()
        this.configureRecyclerView()

        main_board_fragment_input_send.setOnClickListener { send() }
    }

    override fun onDataChanged() {
        main_board_fragment_empty_message_tv.visibility = if (this.mainBoardMessageAdapter.itemCount == 0)
            View.VISIBLE
        else
            View.GONE
    }

    private fun send() {
        val input = main_board_fragment_input_add_text.text.toString()
        if (TextUtils.isEmpty(input)) {
            Snackbar.make(view!!, "Message can not be empty", Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .show()
            return
        }

        MessageHelper.createMessageForChat(
            input, CurrentBoardKeyHolder.getInstance()!!.currentKey, modelCurrentUser
        )
                .addOnFailureListener {
                    Snackbar.make(
                        this@RoomChatFragment.view!!,
                        "Err",
                        Snackbar.LENGTH_LONG
                    )
                }
        mainBoardMessageAdapter.notifyDataSetChanged()
        main_board_fragment_input_add_text.setText("")
    }

    private fun getCurrentUserFromFirestore() {
        UserHelper.getUser(FirebaseAuth.getInstance().currentUser!!.uid)
                .addOnSuccessListener { documentSnapshot ->
                    modelCurrentUser = documentSnapshot.toObject(
                        User::class.java
                    )!!
                }
    }

    private fun configureRecyclerView() {
        //Configure Adapter & RecyclerView
        this.mainBoardMessageAdapter = MainBoardMessageAdapter(
            generateOptionsForAdapter(MessageHelper.getAllMessageForChat(CurrentBoardKeyHolder.getInstance()!!.currentKey)),
            Glide.with(this),
            this,
            FirebaseAuth.getInstance().currentUser!!.uid
        )
        mainBoardMessageAdapter.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    main_board_recyclerview.smoothScrollToPosition(mainBoardMessageAdapter.itemCount)
                }
            }
        )

        main_board_recyclerview.layoutManager = LinearLayoutManager(context)
        main_board_recyclerview.adapter = this.mainBoardMessageAdapter
    }

    private fun generateOptionsForAdapter(query: Query): FirestoreRecyclerOptions<Message> {
        return FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message::class.java)
                .setLifecycleOwner(this)
                .build()
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
