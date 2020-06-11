package com.hr.kurtovic.tomislav.familyboard.main_board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMessageService
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember
import kotlinx.coroutines.launch

data class State(
    internal val loading: Boolean = true,
    val isEmpty: Boolean = true,
    val currentMember: FamilyMember? = null,
    val messageQuery: Query? = null,
    val recycleViewConfigured: Boolean = false
)


sealed class Event {
    object RecyclerViewConfigured : Event()
    data class BoardDataChange(val isEmpty: Boolean) : Event()
    data class FamilyMemberLoaded(val familyMember: FamilyMember) : Event()
}

fun reduce(event: Event, state: State): State =
        when (event) {
            is Event.FamilyMemberLoaded -> state.copy(
                currentMember = event.familyMember,
                loading = false
            )
            is Event.BoardDataChange -> state.copy(isEmpty = event.isEmpty)
            Event.RecyclerViewConfigured -> state.copy(recycleViewConfigured = true)
        }

class MainBoardViewModel(
    private val memberService: FamilyMemberService,
    private val messageService: FamilyMessageService
) : ViewModel() {

    private val internalState = MutableLiveData<State>().apply {
        value = State(messageQuery = messageService.messages())
    }

    init {
        viewModelScope.launch {
            try {
                memberService.currentMember()?.let {
                    onEvent(Event.FamilyMemberLoaded(it))
                }
            } catch (e: FirebaseFirestoreException) {
                //TODO
            }
        }

    }

    val state: LiveData<State> = internalState

    fun onEvent(event: Event) {
        val currentBoard = internalState.value!!
        val newBoard = reduce(event, currentBoard)
        internalState.postValue(newBoard)
    }

}