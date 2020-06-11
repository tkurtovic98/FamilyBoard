package com.hr.kurtovic.tomislav.familyboard.main_board.input.pets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMessageService
import com.hr.kurtovic.tomislav.familyboard.models.Message

data class State(
    val whatInput: String = "",
    val whoInput: String = "",
    val untilWhenInput: String = "",
    val postingInProgress: Boolean = false
)

sealed class Event {
    data class WhatInputChange(val whatInput: String) : Event()
    data class WhoInputChange(val whoInput: String) : Event()
    data class UntilWhenInputChange(val untilWhenInput: String) : Event()
    object Submit : Event()
    object Submitted : Event()
}

fun reduce(state: State, event: Event): State =
        when (event) {
            Event.Submitted -> state.copy(
                postingInProgress = false,
                whatInput = "",
                whoInput = "",
                untilWhenInput = ""
            )
            Event.Submit -> state.copy(
                postingInProgress = true
            )
            is Event.WhatInputChange -> state.copy(whatInput = event.whatInput)
            is Event.WhoInputChange -> state.copy(whoInput = event.whoInput)
            is Event.UntilWhenInputChange -> state.copy(untilWhenInput = event.untilWhenInput)
        }


class PetsViewModel(
    private val familyMessageService: FamilyMessageService,
    private val familyMemberService: FamilyMemberService
) :
    ViewModel() {

    private val internalState = MutableLiveData<State>().apply { value = State() }

    val state: LiveData<State> = internalState

    fun onEvent(event: Event) {
        val currentState = internalState.value!!
        val newState = reduce(currentState, event)
        internalState.postValue(newState)

        if (newState.postingInProgress) {
            postMessage(newState)
        }

    }

    private fun postMessage(state: State) {
        //todo check if who exists in room
        //todo check if when is valid time
        val message = Message(
            content = mapOf(
                "who" to state.whoInput,
                "what" to state.whatInput,
                "until" to state.untilWhenInput
            ),
            memberSenderRef = familyMemberService.currentMemberRef(),
            category = "pets"
        )
        familyMessageService.postMessage(message)
        onEvent(Event.Submitted)
    }
}