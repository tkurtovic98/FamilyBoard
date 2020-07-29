package com.hr.kurtovic.tomislav.familyboard.main_board.input.store

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMessageService
import com.hr.kurtovic.tomislav.familyboard.models.Message

data class State(
    val loading: Boolean = true,

    val storeItems: List<String> = emptyList(),

    val itemFrequency: Map<String, Int> = emptyMap(),

    val postingInProgress: Boolean = false,

    val recyclerViewConfigured: Boolean = false
)

sealed class Event {
    object Loaded : Event()

    object RecyclerViewConfigured : Event()

    object Submit : Event()

    object SubmitSuccessful : Event()

    data class ItemsLoaded(val storeItems: List<String>) : Event()

    data class StoreItemCountChange(val item: String, val count: Int) : Event()
}

fun reduce(state: State, event: Event): State =
        when (event) {
            is Event.StoreItemCountChange -> state.copy(
                itemFrequency = state.itemFrequency.mapValues {
                    if (it.key == event.item) {
                        event.count
                    } else {
                        it.value
                    }
                }
            )

            is Event.ItemsLoaded -> state.copy(
                storeItems = event.storeItems,
                itemFrequency = event.storeItems.map { it to 0 }.toMap()
            )
            is Event.SubmitSuccessful -> state.copy(
                postingInProgress = false,
                recyclerViewConfigured = false
            )
            is Event.Submit -> state.copy(postingInProgress = true)
            is Event.Loaded -> state.copy(loading = false)
            is Event.RecyclerViewConfigured -> state.copy(recyclerViewConfigured = true)
        }


class StoreViewModel(
    resources: Resources,
    private val familyMessageService: FamilyMessageService,
    private val familyMemberService: FamilyMemberService
) :
    ViewModel() {

    private val internalState = MutableLiveData<State>().apply { value = State() }

    val state = internalState

    init {
        val storeItems = resources.getStringArray(R.array.store_items).toList()
        onEvent(Event.ItemsLoaded(storeItems))
    }

    fun onEvent(event: Event) {
        val currentState = state.value!!
        val newState = reduce(currentState, event)
        internalState.postValue(newState)

        if (newState.postingInProgress) {
            postMessage(newState)
        }
    }

    private fun postMessage(state: State) {

        val message = Message(
            content = state.itemFrequency.mapValues { it.value.toString() },
            memberSenderRef = familyMemberService.currentMemberRef(),
            category = "store"
        )

        familyMessageService.postMessage(message).addOnSuccessListener {
            onEvent(Event.SubmitSuccessful)
        }.addOnFailureListener {
            //TODO
        }
    }
}