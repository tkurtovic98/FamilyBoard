package com.hr.kurtovic.tomislav.familyboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.hr.kurtovic.tomislav.familyboard.api.UserHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class LauncherActivity : AppCompatActivity() {

    private var authInstance: FirebaseAuth? = null

    companion object {
        private val RC_SIGN_IN = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        initAuth()
        checkUserStatus()

        log_out.setOnClickListener { logOut() }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this.handleResponseAfterSignIn(requestCode, requestCode, data)
    }


    private fun logOut() {
        if (authInstance!!.currentUser != null) {
            authInstance!!.signOut()
            Snackbar.make(main_activity_coordinator_layout, "Logged out successfully", Snackbar.LENGTH_LONG)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                    .show()
            progress_bar.hide()
        }
    }

    private fun initAuth() {
        authInstance = FirebaseAuth.getInstance()
    }

    private fun checkUserStatus() {
        if (authInstance!!.currentUser != null) {
            loginUser()
        } else {
            startSignInActivity()
        }
    }

    private fun loginUser() {
        showSnackBar("Login successful")
        startActivity(Intent(this, BoardActivity::class.java))
    }

    // --------------------
    // NAVIGATION
    // --------------------

    private fun startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(initProviders())
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN)
    }

    private fun initProviders(): List<AuthUI.IdpConfig> {
        return Arrays.asList(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
                //                new AuthUI.IdpConfig.FacebookBuilder().build()
        )
    }


    private fun showSnackBar(message: String) {
        Snackbar.make(main_activity_coordinator_layout, message, Snackbar.LENGTH_SHORT).show()
    }

    // --------------------
    // UTILS
    // --------------------

    private fun handleResponseAfterSignIn(requestCode: Int, resultCode: Int, data: Intent?) {
        val response = IdpResponse.fromResultIntent(data)

        when (requestCode) {
            RC_SIGN_IN -> checkRespone(response)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun checkRespone(response: IdpResponse?) {
        if (response == null) {
            return
        }

        if (response.isSuccessful) { // SUCCESS
            createUserInFirestore()
            loginUser()
        } else { // ERRORS
            if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
            } else if (response.error!!.errorCode == ErrorCodes.UNKNOWN_ERROR) {
            }
        }
    }

    private fun createUserInFirestore() {
        val currentUser = authInstance!!.currentUser
        if (currentUser != null) {
            val urlPicture = if (currentUser.photoUrl != null) currentUser.photoUrl!!.toString() else null
            val username = currentUser.displayName
            val uid = currentUser.uid

            UserHelper.createUser(uid, username!!, urlPicture!!).addOnFailureListener { showSnackBar("Unable to create new user! ") }
        }
    }


}
