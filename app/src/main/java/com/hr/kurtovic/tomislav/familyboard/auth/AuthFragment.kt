package com.hr.kurtovic.tomislav.familyboard.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.hr.kurtovic.tomislav.familyboard.MainActivity
import com.hr.kurtovic.tomislav.familyboard.R
import kotlinx.android.synthetic.main.authentication_fragment_2.view.*
import org.koin.android.ext.android.inject


class AuthFragment : Fragment() {

    val authManager: AuthManager by inject()

    companion object {
        fun newInstance() = AuthFragment()
        const val SIGN_IN_REQUEST_CODE = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View? = inflater.inflate(R.layout.authentication_fragment_2, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.sign_in.setOnClickListener { startSignIn() }
    }

    private fun startSignIn() {
        val providers = authManager.listOfProviders()

        // Create and launch the sign-in intent.
        // We listen to the response of this activity with the
        // SIGN_IN_REQUEST_CODE.
        startActivityForResult(
            AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.SignIn)
                    .build(),
            SIGN_IN_REQUEST_CODE
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                (activity as? MainActivity)?.showMainBoard()
            } else {
                //TODO handle error
            }
        }
    }

}
