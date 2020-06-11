package com.hr.kurtovic.tomislav.familyboard.main_board.input.pets

import com.hr.kurtovic.tomislav.familyboard.api.FamilyMessageService
import com.hr.kurtovic.tomislav.familyboard.models.Message

interface PetsService {

    fun petMessages()
    fun postPetMessage(message: Message)
    val category: String

}

class PetsServiceImpl(private val familyMessageService: FamilyMessageService) : PetsService {

    override fun petMessages() {
        familyMessageService.messages()
                .whereEqualTo("category", category)
    }

    override fun postPetMessage(message: Message) {
        familyMessageService.postMessage(message.copy(category = category))
        //todo check for errors
    }

    override val category: String
        get() = "pets"

}