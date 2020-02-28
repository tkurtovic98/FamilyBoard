package com.hr.kurtovic.tomislav.familyboard.auth

import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


sealed class Event {
    data class LoginSubmit(val email: String, val password: String) : Event()
    object LogoutSubmit : Event()
    data class ProfileChange(val email: String, val role: String) : Event()
}

class AuthViewModel(private val authService: AuthService) : ViewModel() {

    val webClient = "225090068697-1uaoqec2b1sedrf2fcron5sm30f799ug.apps.googleusercontent.com"

    val authInstance = FirebaseAuth.getInstance()

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClient)
            .requestEmail()
            .build()


    fun onEvent(event: Event) {

        when (event) {
            is Event.LoginSubmit -> authService.login(event.email, event.password)
            is Event.LogoutSubmit -> authService.logout()
            is Event.ProfileChange -> authService.changeProfile(event.email, event.role)
        }

    }

}