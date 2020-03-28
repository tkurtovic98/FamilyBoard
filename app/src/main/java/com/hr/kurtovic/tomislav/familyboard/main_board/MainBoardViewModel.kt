package com.hr.kurtovic.tomislav.familyboard.main_board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Board(
    val currentFamilyName: String = "",
    val familyNameIsChanging: Boolean = true,
    val isEmpty: Boolean = true
)


sealed class Event {
    data class FamilyNameChange(val familyName: String) : Event()
    data class BoardDataChange(val isEmpty: Boolean) : Event()
    object NewFamilyBoardLoaded : Event()
}

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
    }

}