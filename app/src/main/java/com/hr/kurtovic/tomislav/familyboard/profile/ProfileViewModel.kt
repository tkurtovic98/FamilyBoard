package com.hr.kurtovic.tomislav.familyboard.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.auth.AuthService
import com.hr.kurtovic.tomislav.familyboard.models.Family
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember
import com.hr.kurtovic.tomislav.familyboard.util.SharedPreference
import kotlinx.coroutines.launch


data class State(
    val families: List<Family> = emptyList(),

    val initialized: Boolean = false,

    val currentMember: FamilyMember? = null,

    val spinnerConfigured: Boolean = false,

    val currentFamilyName: String? = null
)

sealed class Event {
    data class FamilyNameChange(val familyName: String) : Event()

    data class Init(val currentMember: FamilyMember?, val families: List<Family>) : Event()

    object SpinnerConfigured : Event()
}


fun reduce(event: Event, state: State): State =
        when (event) {

            is Event.Init -> state.copy(
                families = event.families,
                currentMember = event.currentMember,
                initialized = true
            )

            is Event.FamilyNameChange -> {
                when (state.currentFamilyName) {
                    event.familyName -> state
                    else -> state.copy(currentFamilyName = event.familyName)
                }
            }

            Event.SpinnerConfigured -> state.copy(spinnerConfigured = true)

        }


class ProfileViewModel(
    private val familyMemberService: FamilyMemberService,
    private val authService: AuthService,
    private val memberService: FamilyMemberService,
    private val sharedPreference: SharedPreference
) : ViewModel() {

    private val internalState = MutableLiveData<State>().apply { value = State() }

    val state: LiveData<State> = internalState

    fun onEvent(event: Event) {
        val currentState = internalState.value!!
        val newState = reduce(event, currentState)
        internalState.postValue(newState)

        if (event is Event.FamilyNameChange) {
            saveFamilyName(newState.currentFamilyName)
        }
    }

    private fun saveFamilyName(newFamilyName: String?) {
        sharedPreference.saveNewFamilyName(newFamilyName!!)
    }

    init {
        getAndInitFamilyListAndCurrentMember()
        getAndInitSavedFamilyName()
    }

    private fun getAndInitFamilyListAndCurrentMember() {
        viewModelScope.launch {
            try {
                val currentMember = memberService.currentMember()
                val families = familyMemberService.families()
                onEvent(Event.Init(currentMember = currentMember, families = families))
            } catch (e: FirebaseFirestoreException) {
                //TODO
            }
        }
    }

    private fun getAndInitSavedFamilyName() {
        val savedFamily = sharedPreference.getSavedFamilyName()
        onEvent(Event.FamilyNameChange(savedFamily!!))
    }

    fun logout() {
        authService.logout()
    }
}
