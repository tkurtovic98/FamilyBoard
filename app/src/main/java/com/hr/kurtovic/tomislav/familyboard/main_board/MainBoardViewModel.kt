package com.hr.kurtovic.tomislav.familyboard.main_board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

<<<<<<< HEAD
data class Board(
    val currentFamilyName: String = "",
=======
data class State(
>>>>>>> develop
    val familyNameIsChanging: Boolean = true,
    val isEmpty: Boolean = true
)


sealed class Event {
<<<<<<< HEAD
    data class FamilyNameChange(val familyName: String) : Event()
=======
    object FamilyNameChange : Event()
>>>>>>> develop
    data class BoardDataChange(val isEmpty: Boolean) : Event()
    object NewFamilyBoardLoaded : Event()
}

<<<<<<< HEAD
fun changeBoard(event: Event, board: Board): Board =
        when (event) {
            is Event.NewFamilyBoardLoaded -> board.copy(familyNameIsChanging = false)
            is Event.FamilyNameChange -> board.copy(
                currentFamilyName = event.familyName,
                familyNameIsChanging = true
            )
            is Event.BoardDataChange -> board.copy(isEmpty = event.isEmpty)
        }

class MainBoardViewModel : ViewModel() {

    private var internalBoard = MutableLiveData<Board>().apply { value = Board() }

    val board: LiveData<Board> = internalBoard

    fun onEvent(event: Event) {
        val currentBoard = internalBoard.value!!
        val newBoard = changeBoard(event, currentBoard)
        internalBoard.postValue(newBoard)
=======
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
>>>>>>> develop
    }

}