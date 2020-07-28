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
import com.hr.kurtovic.tomislav.familyboard.util.Box
import kotlinx.coroutines.launch


data class State(
    val families: List<Family> = emptyList(),
    val currentMember: FamilyMember? = null,
    val spinnerConfigure: Boolean = false,
    val loading: Boolean = true,
    val firstTimeLoading: Boolean = true,
    val currentFamilyName: Box<String> = Box()
)

sealed class Event {
    data class FamilyListChange(val families: List<Family>) : Event()
    data class CurrentMemberChange(val currentMember: FamilyMember?) : Event()
    data class FamilyNameChange(val familyName: String) : Event()
    data class Init(val currentMember: FamilyMember?, val families: List<Family>) : Event()
    object SpinnerConfigured : Event()
    object FirstTimeLoaded : Event()
}


fun reduce(event: Event, state: State): State =
        when (event) {
            is Event.Init -> state.copy(
                families = event.families,
                spinnerConfigure = true,
                currentMember = event.currentMember,
                loading = false
            )
            is Event.FamilyListChange -> state.copy(
                families = event.families,
                spinnerConfigure = true
            )
            is Event.CurrentMemberChange -> state.copy(
                currentMember = event.currentMember,
                loading = false
            )
            is Event.FamilyNameChange -> state.copy(currentFamilyName = Box(event.familyName))
            Event.FirstTimeLoaded -> state.copy(firstTimeLoading = false)
            Event.SpinnerConfigured -> state.copy(spinnerConfigure = false)
        }


class ProfileViewModel(
    private val familyMemberService: FamilyMemberService,
    private val authService: AuthService,
    private val memberService: FamilyMemberService
) : ViewModel() {

    private val internalState = MutableLiveData<State>().apply { value = State() }

    val state: LiveData<State> = internalState

    init {
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

    fun onEvent(event: Event) {
        val currentState = internalState.value!!
        val newState = reduce(event, currentState)
        internalState.postValue(newState)
    }

    fun logout() {
        authService.logout()
    }
}
