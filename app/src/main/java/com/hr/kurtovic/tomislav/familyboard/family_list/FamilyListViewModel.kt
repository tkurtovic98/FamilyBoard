package com.hr.kurtovic.tomislav.familyboard.family_list

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyService
import com.hr.kurtovic.tomislav.familyboard.models.Family
import com.hr.kurtovic.tomislav.familyboard.util.Box
import kotlinx.coroutines.launch

data class State(
    val family: Family? = null,
    val submitError: Box<Int> = Box(),
    val isEmpty: Boolean = false,
    val familyAddInProgress: Boolean = false,
    val memberToFamilyAddInProgress: Boolean = false,
    val successMessage: String = "",
    val recyclerViewConfigured: Boolean = false,
    val familyListQuery: Query? = null
)


sealed class Event {
    object FamilyAdd : Event()
    object FamilyAdded : Event()
    object RecyclerViewConfigured : Event()
    object FamilyMemberAdded : Event()


    data class FamilyMemberAdd(val familyName: String) : Event()
    data class FamilyNameChange(val familyName: String) : Event()


    data class ListDataChanged(val isEmpty: Boolean) : Event()
    data class ErrorEvent(val error: Error) : Event()
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
            Event.RecyclerViewConfigured -> currentState.copy(recyclerViewConfigured = true)
            is Event.FamilyAdded -> currentState.copy(
                family = null,
                familyAddInProgress = false
            )
            is Event.FamilyNameChange -> currentState.copy(family = Family(name = event.familyName))
            is Event.ListDataChanged -> currentState.copy(isEmpty = event.isEmpty)
            is Event.FamilyMemberAdd -> currentState.copy(
                family = Family(name = event.familyName),
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
        value = State(familyListQuery = familyService.showFamilies())
    }

    val state: LiveData<State> = internalState

    fun onEvent(event: Event) {
        val currentState = internalState.value!!
        val newState = reduce(currentState, event)
        internalState.postValue(newState)

        checkIfAddInProgressAndRun(newState)
    }

    private fun checkIfAddInProgressAndRun(state: State) {
        if (state.familyAddInProgress) {
            addFamily(state.family)
        }

        if (state.memberToFamilyAddInProgress) {
            connectFamilyAndMember(state.family)
        }
    }

    private fun addFamily(family: Family?) {
        //TODO(check for errors in input)
        viewModelScope.launch {
            try {
                familyService.addFamily(family!!)
            } catch (e: FirebaseFirestoreException) {
                onEvent(Event.ErrorEvent(Error.FamilyAddError(e)))
            }
        }
    }

    private fun connectFamilyAndMember(family: Family?) {
        //TODO(add family to member)
        val memberId = familyMemberService.currentMemberId
        viewModelScope.launch {
            try {
                familyService.addFamilyMember(family!!, memberId)
                familyMemberService.addFamily(memberId, family)
                onEvent(Event.FamilyMemberAdded)
            } catch (e: FirebaseFirestoreException) {
                onEvent(Event.ErrorEvent(Error.FamilyMemberAddError(e)))
            }
        }
    }

}