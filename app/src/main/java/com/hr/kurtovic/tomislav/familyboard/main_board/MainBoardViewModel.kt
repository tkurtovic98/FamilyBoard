package com.hr.kurtovic.tomislav.familyboard.main_board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class State(
    val familyNameIsChanging: Boolean = true,
    val isEmpty: Boolean = true
)


sealed class Event {
    object FamilyNameChange : Event()
    data class BoardDataChange(val isEmpty: Boolean) : Event()
    object NewFamilyBoardLoaded : Event()
}

fun reduce(event: Event, state: State): State =
        when (event) {
            Event.NewFamilyBoardLoaded -> state.copy(familyNameIsChanging = false)
            Event.FamilyNameChange -> state.copy(familyNameIsChanging = true)
            is Event.BoardDataChange -> state.copy(isEmpty = event.isEmpty)
        }

class MainBoardViewModel() : ViewModel() {

    private val internalState = MutableLiveData<State>().apply {
        value = State()
    }

    val state: LiveData<State> = internalState

    fun onEvent(event: Event) {
        val currentBoard = internalState.value!!
        val newBoard = reduce(event, currentBoard)
        internalState.postValue(newBoard)
    }

}