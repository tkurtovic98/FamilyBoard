package com.hr.kurtovic.tomislav.familyboard.main_board.input.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMessageService
import com.hr.kurtovic.tomislav.familyboard.models.Message

data class State(
    val postingInProgress: Boolean = false,
    val whatInput: String = "",
    val whenInput: String = ""
)


sealed class Event {
    object Submit : Event()
    object Submitted : Event()
    data class WhatInputChange(val whatInput: String) : Event()
    data class WhenInputChange(val whenInput: String) : Event()
}

fun reduce(state: State, event: Event): State =
        when (event) {
            is Event.WhatInputChange -> state.copy(whatInput = event.whatInput)
            is Event.WhenInputChange -> state.copy(whenInput = event.whenInput)
            Event.Submit -> state.copy(postingInProgress = true)
            Event.Submitted -> state.copy(postingInProgress = false)
        }


class EventViewModel(
    private val familyMemberService: FamilyMemberService,
    private val messageService: FamilyMessageService
) : ViewModel() {

    private val internalState = MutableLiveData<State>().apply {
        value = State()
    }

    val state: LiveData<State> = internalState

    fun onEvent(event: Event) {
        val currentState = state.value!!
        val newState = reduce(currentState, event)
        internalState.postValue(newState)

        if (newState.postingInProgress) {
            submit(newState)
        }

    }

    private fun submit(state: State) {

        val message = Message(
            content = mapOf(
                "What event" to state.whatInput,
                "Event date" to state.whenInput
            ),
            category = "event",
            memberSenderRef = familyMemberService.currentMemberRef()
        )

        messageService.postMessage(message).addOnSuccessListener {
            onEvent(Event.Submitted)
        }.addOnFailureListener {
            //TODO
        }
    }

}