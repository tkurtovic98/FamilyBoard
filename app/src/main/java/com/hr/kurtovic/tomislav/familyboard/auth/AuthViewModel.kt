package com.hr.kurtovic.tomislav.familyboard.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.disposables.CompositeDisposable

data class Response(
    val success: Boolean = false,
    val error: String? = null
)


sealed class Event {
    data class LoginSubmit(val email: String, val password: String) : Event()
    object LogoutSubmit : Event()
}

class AuthViewModel(private val authService: AuthService) : ViewModel() {

    private val webClient = "225090068697-1uaoqec2b1sedrf2fcron5sm30f799ug.apps.googleusercontent.com"

    val authInstance = FirebaseAuth.getInstance()

    val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClient)
            .requestEmail()
            .build()

    private val compositeDisposable = CompositeDisposable()

    private var internalResponseObserver = MutableLiveData<Response>().apply { value = Response() }

    val responseObserver: LiveData<Response> = internalResponseObserver

    fun onEvent(event: Event) {
        when (event) {
            is Event.LoginSubmit -> authService.login(
                event.email,
                event.password
            ).addOnCompleteListener {
                internalResponseObserver.postValue(Response(it.isSuccessful, it.exception?.message))
            }
            is Event.LogoutSubmit -> {
                authService.logout()
                internalResponseObserver.postValue(Response(true))
            }
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}