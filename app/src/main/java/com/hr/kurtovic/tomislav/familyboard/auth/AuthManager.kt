package com.hr.kurtovic.tomislav.familyboard.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hr.kurtovic.tomislav.familyboard.api.UserHelper


class AuthManager {

    companion object {
        val authInstance = FirebaseAuth.getInstance()
        private const val RC_SIGN_IN = 123
    }

    fun isUserLogged() = authInstance.currentUser != null

    fun currentUser() = authInstance.currentUser

    fun listOfProviders(): List<AuthUI.IdpConfig> =
            listOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
//                new AuthUI.IdpConfig.FacebookBuilder().build()
            )

    fun configureGoogleSignIn(context: FragmentActivity): GoogleSignInClient? {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        return GoogleSignIn.getClient(context, gso)
    }

    private fun createUserInFirestore() {
        val currentUser = authInstance.currentUser
        if (currentUser != null) {
            val urlPicture = if (currentUser.photoUrl != null) currentUser.photoUrl!!.toString() else null
            val username = currentUser.displayName
            val uid = currentUser.uid

            UserHelper.createUser(uid, username!!, urlPicture!!).addOnFailureListener { }
        }
    }

    fun getAccount(data: Intent?) =
            GoogleSignIn.getSignedInAccountFromIntent(data)!!

    fun authUserWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        authInstance.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                    } else {
                        //TODO handle error
                    }

                }
    }


}