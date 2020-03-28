package com.hr.kurtovic.tomislav.familyboard.main_board.input.pets

import com.hr.kurtovic.tomislav.familyboard.api.FamilyMessageService
import com.hr.kurtovic.tomislav.familyboard.models.Message

interface PetsService {

    fun petMessages(familyName: String)
    fun postPetMessage(message: Message)

}

class PetsServiceImpl(val familyMessageService: FamilyMessageService) : PetsService {

    override fun petMessages(familyName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun postPetMessage(message: Message) {
        TODO()
    }

}