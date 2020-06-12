package com.hr.kurtovic.tomislav.familyboard.family_list

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestoreException
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyService
import com.hr.kurtovic.tomislav.familyboard.models.Family
import com.hr.kurtovic.tomislav.familyboard.util.Box

data class State(
    val familyName: String = "",
    val submitError: Box<Int> = Box(),
    val isEmpty: Boolean = true,
    val familyAddInProgress: Boolean = false,
    val memberToFamilyAddInProgress: Boolean = false,
    val successMessage: String = "",
    val loading: Boolean = true
)


sealed class Event {
    data class FamilyNameChange(val familyName: String) : Event()
    object FamilyAdd : Event()
    object FamilyAdded : Event()

    data class FamilyMemberAdd(val familyName: String) : Event()
    object FamilyMemberAdded : Event()

    data class ListDataChanged(val isEmpty: Boolean) : Event()
    data class ErrorEvent(val error: Error) : Event()
    object RecyclerViewConfigured : Event()
}

sealed class Error {

    data class FamilyAddError(val exception: Exception) : Error() {
        @StringRes
        fun errorMessageId(): Int =
                when ((exception as FirebaseFirestoreException).code) {
                    FirebaseFirestoreException.Code.ABORTED -> R.string.family_add_aborted
                    FirebaseFirestoreException.Code.ALREADY_EXISTS -> R.string.family_add_already_exists
                    FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED -> R.string.family_add_unable
                    else -> R.string.family_add_generic
                }
    }

    data class FamilyMemberAddError(val exception: Exception) : Error() {
        @StringRes
        fun errorMessageId(): Int =
                when ((exception as FirebaseFirestoreException).code) {
                    FirebaseFirestoreException.Code.ABORTED -> R.string.family_member_add_aborted
                    FirebaseFirestoreException.Code.ALREADY_EXISTS -> R.string.family_add_already_exists
                    FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED -> R.string.family_member_add_unable

                    else -> R.string.family_member_add_generic
                }
    }


}

fun reduce(currentState: State, event: Event): State =
        when (event) {
            Event.FamilyAdd -> currentState.copy(familyAddInProgress = true)
            Event.RecyclerViewConfigured -> currentState.copy(loading = false)
            is Event.FamilyAdded -> currentState.copy(
                familyName = "",
                familyAddInProgress = false
            )
            is Event.FamilyNameChange -> currentState.copy(familyName = event.familyName)
            is Event.ListDataChanged -> currentState.copy(isEmpty = event.isEmpty)
            is Event.FamilyMemberAdd -> currentState.copy(
                familyName = event.familyName,
                memberToFamilyAddInProgress = true
            )
            is Event.FamilyMemberAdded -> currentState.copy(
                memberToFamilyAddInProgress = false
            )

            is Event.ErrorEvent -> {
                val messageId = when (event.error) {
                    is Error.FamilyAddError -> event.error.errorMessageId()
                    is Error.FamilyMemberAddError -> event.error.errorMessageId()
                }

                currentState.copy(
                    submitError = Box(messageId),
                    familyAddInProgress = false,
                    memberToFamilyAddInProgress = false
                )
            }


        }

class FamilyListViewModel(
    private val familyService: FamilyService,
    private val familyMemberService: FamilyMemberService
) : ViewModel() {

    private val internalState = MutableLiveData<State>().apply {
        value = State()
    }

    val state: LiveData<State> = internalState

    fun onEvent(event: Event) {
        val currentState = internalState.value!!
        val newState = reduce(currentState, event)
        internalState.postValue(newState)

        if (newState.familyAddInProgress) {
            addFamily(newState)
        }

        if (newState.memberToFamilyAddInProgress) {
            addFamilyMember(newState.familyName)
        }
    }

    private fun addFamily(state: State) {
        //TODO(check for errors in input)
        familyService.addFamily(Family(name = state.familyName)).addOnSuccessListener {
            onEvent(Event.FamilyAdded)
        }.addOnFailureListener {
            onEvent(Event.ErrorEvent(Error.FamilyAddError(it)))
        }
    }

    private fun addFamilyMember(familyName: String) {
        //TODO(add family to member)
        val memberId = familyMemberService.currentMemberId
        familyService.addFamilyMember(familyName, memberId).addOnSuccessListener {
            familyMemberService.addFamily(memberId, familyName).addOnSuccessListener {
                onEvent(Event.FamilyMemberAdded)
            }.addOnFailureListener { e ->
                onEvent(Event.ErrorEvent(Error.FamilyMemberAddError(e)))
            }
        }.addOnFailureListener { e ->
            onEvent(Event.ErrorEvent(Error.FamilyMemberAddError(e)))
        }
    }

}