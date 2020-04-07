package com.hr.kurtovic.tomislav.familyboard.main_board.input.pets

import com.hr.kurtovic.tomislav.familyboard.api.FamilyMessageService
import com.hr.kurtovic.tomislav.familyboard.models.Message

interface PetsService {

    fun petMessages(familyName: String)
    fun postPetMessage(message: Message, familyName: String)
    val category: String

}

class PetsServiceImpl(private val familyMessageService: FamilyMessageService) : PetsService {

    override fun petMessages(familyName: String) {
        familyMessageService.messages(familyName)
                .whereEqualTo("category", category)
    }

    override fun postPetMessage(message: Message, familyName: String) {
        familyMessageService.postMessage(message.copy(category = category), familyName)
        //todo check for errors
    }

    override val category: String
        get() = "pets"

}