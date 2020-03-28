package com.hr.kurtovic.tomislav.familyboard.main_board.input.pets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.models.Message
import io.reactivex.disposables.CompositeDisposable

data class Input(
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
}

fun reduce(input: Input, event: Event): Input =
        when (event) {
            Event.Submit -> input.copy(postingInProgress = true)
            is Event.WhatInputChange -> input.copy(whatInput = event.whatInput)
            is Event.WhoInputChange -> input.copy(whoInput = event.whoInput)
            is Event.UntilWhenInputChange -> input.copy(untilWhenInput = event.untilWhenInput)
        }


class PetsViewModel(
    private val petsService: PetsService,
    private val familyMemberService: FamilyMemberService
) :
    ViewModel() {

    private val internalInput = MutableLiveData<Input>().apply { value = Input() }

    private val compositeDisposable = CompositeDisposable()

    val input: LiveData<Input> = internalInput

    fun onEvent(event: Event) {
        val currentInput = internalInput.value!!
        val newInput = reduce(currentInput, event)
        internalInput.postValue(newInput)

        if (newInput.postingInProgress) {
            postMessage(newInput)
        }

    }

    private fun postMessage(input: Input) {
        //todo check if who exists in room
        //todo check if when is valid time
        val message = Message(
            category = "pets",
            content = mapOf(
                "who" to input.whoInput,
                "what" to input.whatInput,
                "until" to input.untilWhenInput
            ),
            memberSenderRef = familyMemberService.currentUserRef()
        )
        petsService.postPetMessage(message)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}