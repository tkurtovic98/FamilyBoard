package com.hr.kurtovic.tomislav.familyboard

import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember

class CurrentBoardKeyHolder {

    var currentKey = "main"

    var currentUser: FamilyMember? = null

    companion object {
        private var instance: CurrentBoardKeyHolder? = CurrentBoardKeyHolder()

        init {
            instance = CurrentBoardKeyHolder()
        }

        fun getInstance(): CurrentBoardKeyHolder? {
            return instance
        }
    }
}
