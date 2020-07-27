package com.hr.kurtovic.tomislav.familyboard.main_board.message_display

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMessageService
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember
import com.hr.kurtovic.tomislav.familyboard.models.Message
import kotlinx.coroutines.launch

data class State(
    val message: Message? = null,
    val memberWhoAccepted: FamilyMember? = null
)

class MessageDisplayViewModel(
    messageId: String,
    private val familyMemberService: FamilyMemberService,
    private val messageService: FamilyMessageService
) : ViewModel() {

    private val internalLiveData = MutableLiveData<State>()

    val data = internalLiveData

    init {
        viewModelScope.launch {
            val message = messageService.getMessage(messageId)
            val author = familyMemberService.getMember(message?.memberWhoAcceptedId!!)
            internalLiveData.postValue(State(message = message, memberWhoAccepted = author))
        }
    }
}