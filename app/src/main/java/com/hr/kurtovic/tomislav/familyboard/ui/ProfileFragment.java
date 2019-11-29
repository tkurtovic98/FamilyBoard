package com.hr.kurtovic.tomislav.familyboard.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.hr.kurtovic.tomislav.familyboard.R;
import com.hr.kurtovic.tomislav.familyboard.api.UserHelper;
import com.hr.kurtovic.tomislav.familyboard.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.profile_image)
    AppCompatImageView profileImage;

    @BindView(R.id.user_name)
    MaterialTextView userNameInput;

    @BindView(R.id.user_role)
    TextInputEditText userRoleInput;

    User currentUser;

    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getCurrentUserFromFirestore();
    }

    @OnClick(R.id.profile_image)
    public void changeProfile(){
        Snackbar.make(getView(), "Profile image change", Snackbar.LENGTH_LONG).show();
    }

    private void getCurrentUserFromFirestore() {
        UserHelper.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addOnSuccessListener(documentSnapshot -> {
                    currentUser = documentSnapshot.toObject(User.class);
                    loadProfileImage();
                    setUserInfo();
                });
    }

    private void loadProfileImage() {
        if (currentUser.getUrlPicture() != null) {
            Glide.with(getView())
                    .load(currentUser.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(profileImage);
        }
    }

    private void setUserInfo() {
        userNameInput.setText(currentUser.getName());

        if (currentUser.getRole() != null) {
            userRoleInput.setText(currentUser.getRole());
        }
    }

}
