package com.hr.kurtovic.tomislav.familyboard.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.auth.AuthService
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember


data class State(
    val familyList: List<String> = emptyList(),
    val currentMember: FamilyMember? = null,
    val spinnerConfigure: Boolean = false,
    val profileImageLoad: Boolean = false
)

sealed class Event {
    data class FamilyListChange(val familyList: List<String>) : Event()
    data class CurrentMemberChange(val currentMember: FamilyMember?) : Event()
    object SpinnerConfigured : Event()
    object FirstTimeLoaded : Event()
}


fun reduce(event: Event, state: State): State =
        when (event) {
            is Event.FamilyListChange -> state.copy(
                familyList = event.familyList,
                spinnerConfigure = true
            )
            is Event.CurrentMemberChange -> state.copy(
                currentMember = event.currentMember,
                profileImageLoad = true
            )
            Event.SpinnerConfigured -> state.copy(spinnerConfigure = false)
            Event.FirstTimeLoaded -> state.copy(profileImageLoad = false)
        }


class ProfileViewModel(
    private val familyMemberService: FamilyMemberService,
    private val authService: AuthService,
    private val memberService: FamilyMemberService

class ProfileViewModel(
    familyMemberService: FamilyMemberService,
    private val authService: AuthService
) : ViewModel() {

    private val internalState = MutableLiveData<State>().apply { value = State() }

    val state: LiveData<State> = internalState

    private val familyChangeListener = familyMemberService.families()
            .addSnapshotListener { querySnapshot, _ ->
                val families = arrayListOf<String>()
                for (documentSnapshot in querySnapshot!!.documents) {
                    val familyName = documentSnapshot.get("familyName").toString()
                    families.add(familyName)
                }

        //TODO(fix interfering events on data load)
//        memberService.currentMemberRef().addSnapshotListener { documentSnapshot, _ ->
//            val familyMember = documentSnapshot?.toObject(FamilyMember::class.java)
//            onEvent(Event.CurrentMemberChange(currentMember = familyMember))
//        }

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
