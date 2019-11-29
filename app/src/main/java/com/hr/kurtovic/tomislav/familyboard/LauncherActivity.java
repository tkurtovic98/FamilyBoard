package com.hr.kurtovic.tomislav.familyboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.ContentLoadingProgressBar;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hr.kurtovic.tomislav.familyboard.api.UserHelper;
import com.hr.kurtovic.tomislav.familyboard.models.User;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class  LauncherActivity extends AppCompatActivity {

    //FOR DATA
    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth  authInstance;

    @BindView(R.id.main_activity_coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initAuth();
        checkUserStatus();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, requestCode, data);
    }


    @OnClick(R.id.log_out)
    public void logOut() {
        if (authInstance.getCurrentUser() != null) {
            authInstance.signOut();
            Snackbar.make(coordinatorLayout, "Logged out successfully", Snackbar.LENGTH_LONG)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                    .show();
            progressBar.hide();
        }
    }

    private void initAuth() {
        authInstance = FirebaseAuth.getInstance();
    }

    private void checkUserStatus() {
        if (authInstance.getCurrentUser() != null) {
            loginUser();
        } else {
            startSignInActivity();
        }
    }

    private void loginUser() {
        showSnackBar("Login successful");
        startActivity(new Intent(this, BoardActivity.class));
    }

    // --------------------
    // NAVIGATION
    // --------------------

    private void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(initProviders())
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private List<AuthUI.IdpConfig> initProviders() {
        return Arrays.asList
                (
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()
                        //                new AuthUI.IdpConfig.FacebookBuilder().build()
                );
    }


    private void showSnackBar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    // --------------------
    // UTILS
    // --------------------

    @SuppressLint("RestrictedApi")
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (response == null) {
                return;
            }

            if (response.isSuccessful()) { // SUCCESS
                createUserInFirestore();
                loginUser();
            } else { // ERRORS
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                }
            }
        }
    }

    private void createUserInFirestore(){
        FirebaseUser currentUser = authInstance.getCurrentUser();
        if (currentUser != null){
            String urlPicture = (currentUser.getPhotoUrl() != null) ? currentUser.getPhotoUrl().toString() : null;
            String username = currentUser.getDisplayName();
            String uid = currentUser.getUid();

            UserHelper.createUser(uid, username, urlPicture).addOnFailureListener(e -> {
                showSnackBar("Unable to create new user! ");
            });
        }
    }

}
