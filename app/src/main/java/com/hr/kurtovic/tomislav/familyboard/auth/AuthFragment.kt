package com.hr.kurtovic.tomislav.familyboard.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.hr.kurtovic.tomislav.familyboard.MainActivity
import com.hr.kurtovic.tomislav.familyboard.R
import kotlinx.android.synthetic.main.authentication_fragment.*
import kotlinx.android.synthetic.main.email_registration_form.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AuthFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModel()

    companion object {
        fun newInstance() = AuthFragment()
        const val SIGN_IN_REQUEST_CODE = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View? = inflater.inflate(R.layout.authentication_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        google_sign_in.setOnClickListener { startGoogleSignIn() }
        sign_in_button.setOnClickListener { startEmailSignIn() }

        authViewModel.responseObserver.observe(viewLifecycleOwner, Observer { handleResponse(it) })
    }

    private fun handleResponse(response: Response?) {
        //TODO handle error
        if (response == null) return

        if (response.success) {
            (activity as? MainActivity)?.showMainBoard()
        }
    }

    private fun startEmailSignIn() {
        val email = email_input.text.toString()
        val password = password_input.text.toString()

        authViewModel.onEvent(Event.LoginSubmit(email, password))
    }

    private fun startGoogleSignIn() {
        val googleSignInClient = activity?.let { GoogleSignIn.getClient(it, authViewModel.gso) }
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, SIGN_IN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            // code for custom ui
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }

        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        authViewModel.authInstance.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        (activity as MainActivity).showMainBoard()
                    } else {

                    }

                }

    }

}
