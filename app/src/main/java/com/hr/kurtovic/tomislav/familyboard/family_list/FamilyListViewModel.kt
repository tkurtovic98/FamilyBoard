package com.hr.kurtovic.tomislav.familyboard.family_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyService
import com.hr.kurtovic.tomislav.familyboard.models.Family

data class State(
    val familyList: List<String> = emptyList<String>(),
    val familyName: String = "",
    val submitInProgress: Boolean = false,
    val submitError: Boolean = false,
    val isEmpty: Boolean = true
)

sealed class Event {
    data class FamilyNameChange(val familyName: String) : Event()
    object FamilyAdd : Event()
    data class FamilySubmitted(val submitError: Boolean) : Event()
    data class ListDataChanged(val isEmpty: Boolean) : Event()
}

fun reduce(currentState: State, event: Event): State =
        when (event) {
            Event.FamilyAdd -> currentState.copy(submitInProgress = true)
            is Event.FamilySubmitted -> currentState.copy(
                submitInProgress = false,
                submitError = event.submitError,
                familyName = ""
            )
            is Event.FamilyNameChange -> currentState.copy(familyName = event.familyName)
            is Event.ListDataChanged -> currentState.copy(isEmpty = event.isEmpty)
        }

class FamilyListViewModel(
    private val familyService: FamilyService,
    private val familyMemberService: FamilyMemberService
) : ViewModel() {

    var internalState = MutableLiveData<State>().apply {
        familyService.showFamilies().addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                value = State()
                return@addSnapshotListener
            }

            val list = querySnapshot?.toObjects(Family::class.java)!!

            value = State(familyList = list.map { family: Family? -> family?.name!! })
        }
    }

    val state: LiveData<State> = internalState

    fun onEvent(event: Event) {
        val currentState = internalState.value!!
        val newState = reduce(currentState, event)
        internalState.postValue(newState)

        if (newState.submitInProgress) {
            if (newState.familyName in newState.familyList) {
                onEvent(Event.FamilySubmitted(submitError = true))
                return
            }
            addFamily(newState)
//            familyService.addFamily(Family(name = newState.familyName))
//            onEvent(Event.FamilySubmitted(submitError = false))
        }
    }

    private fun addFamily(state: State) {
        familyService.addFamily(Family(name = "Kurtović")).addOnSuccessListener {
//            familyMemberService.getMember(familyMemberService.currentMemberId)
//                    .addOnSuccessListener { memberSnapshot ->
//                        val familyMember = memberSnapshot?.toObject(FamilyMember::class.java)!!
//                        familyService.addFamilyMember("Kurtović", familyMember)
//                                .addOnSuccessListener {
//                                }.addOnFailureListener {
//                                }
//
//                    }
            onEvent(Event.FamilySubmitted(submitError = false))
        }.addOnFailureListener {
            onEvent(Event.FamilySubmitted(submitError = true))
        }

    }

}