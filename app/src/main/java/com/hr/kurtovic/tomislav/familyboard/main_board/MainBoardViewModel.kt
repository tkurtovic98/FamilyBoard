package com.hr.kurtovic.tomislav.familyboard.main_board

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMessageService
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember
import com.hr.kurtovic.tomislav.familyboard.util.Box
import kotlinx.coroutines.launch

data class State(
    val actionInProgress: Boolean = false,
    val isEmpty: Boolean = false,
    val currentMember: FamilyMember? = null,
    val messageQuery: Query? = null,
    val recycleViewConfigured: Boolean = false,
    val errorMessage: Box<Int> = Box()
)


sealed class Event {
    object RecyclerViewConfigured : Event()
    object ActionFinished : Event()
    data class BoardDataChange(val isEmpty: Boolean) : Event()
    data class FamilyMemberLoaded(val familyMember: FamilyMember) : Event()
    data class SetMessageAccepted(val messageId: String) : Event()
    data class DeleteMessage(val messageId: String) : Event()

    data class ErrorEvent(val error: Error) : Event()
}

sealed class Error {

    data class MessageAcceptedError(val exception: Exception) : Error() {
        @StringRes
        fun errorMessageId(): Int =
                when ((exception as FirebaseFirestoreException).code) {
                    FirebaseFirestoreException.Code.ABORTED -> R.string.action_aborted_error
                    else -> R.string.family_add_generic
                }
    }

    data class MessageDeleteError(val exception: Exception) : Error() {
        @StringRes
        fun errorMessageId(): Int =
                when ((exception as FirebaseFirestoreException).code) {
                    FirebaseFirestoreException.Code.ABORTED -> R.string.action_aborted_error
                    else -> R.string.family_member_add_generic
                }
    }

    data class MemberLoadError(val exception: Exception) : Error() {
        @StringRes
        fun errorMessageId(): Int =
                when ((exception as FirebaseFirestoreException).code) {
                    FirebaseFirestoreException.Code.ABORTED -> R.string.action_aborted_error
                    else -> R.string.family_member_add_generic
                }
    }


}

fun reduce(event: Event, state: State): State =
        when (event) {
            Event.RecyclerViewConfigured -> state.copy(recycleViewConfigured = true)
            Event.ActionFinished -> state.copy(actionInProgress = false)

            is Event.FamilyMemberLoaded -> state.copy(
                currentMember = event.familyMember
            )
            is Event.BoardDataChange -> state.copy(isEmpty = event.isEmpty)
            is Event.SetMessageAccepted -> state.copy(
                actionInProgress = true
            )
            is Event.DeleteMessage -> state.copy(
                actionInProgress = true
            )


            is Event.ErrorEvent -> {
                val messageId = when (event.error) {
                    is Error.MessageAcceptedError -> event.error.errorMessageId()
                    is Error.MessageDeleteError -> event.error.errorMessageId()
                    is Error.MemberLoadError -> event.error.errorMessageId()
                }
                state.copy(
                    errorMessage = Box(messageId),
                    actionInProgress = false
                )
            }
        }


class MainBoardViewModel(
    private val memberService: FamilyMemberService,
    private val messageService: FamilyMessageService
) : ViewModel() {

    private val internalState = MutableLiveData<State>().apply {
        value = State(messageQuery = messageService.messages())
    }

    val state: LiveData<State> = internalState

    init {
        viewModelScope.launch {
            try {
                memberService.currentMember()?.apply {
                    onEvent(Event.FamilyMemberLoaded(this))
                }
            } catch (e: FirebaseFirestoreException) {
                onEvent(Event.ErrorEvent(Error.MemberLoadError(e)))
            }
        }

    }

    fun onEvent(event: Event) {
        val currentBoard = internalState.value!!
        val newState = reduce(event, currentBoard)
        internalState.postValue(newState)

        if (newState.actionInProgress) {
            startAction(event)
        }
    }

    private fun startAction(event: Event) {
        when (event) {
            is Event.DeleteMessage -> deleteMessage(event.messageId)
            is Event.SetMessageAccepted -> setMessageAccepted(event.messageId)
        }
    }

    private fun setMessageAccepted(messageId: String) {
        viewModelScope.launch {
            try {
                messageService.setMessageAccepted(
                    messageId,
                    internalState.value?.currentMember?.uid!!
                )
                onEvent(Event.ActionFinished)
            } catch (e: FirebaseFirestoreException) {
                onEvent(Event.ErrorEvent(Error.MessageAcceptedError(e)))
            }
        }
    }

    private fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            try {
                messageService.deleteMessage(messageId)
                onEvent(Event.ActionFinished)
            } catch (e: FirebaseFirestoreException) {
                onEvent(Event.ErrorEvent(Error.MessageDeleteError(e)))
            }
        }
    }


}