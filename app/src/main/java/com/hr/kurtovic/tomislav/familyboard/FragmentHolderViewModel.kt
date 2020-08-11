package com.hr.kurtovic.tomislav.familyboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.util.SharedPreference
import kotlinx.coroutines.launch

data class State(
    val loading: Boolean = true,

    val hasSavedFamily: Boolean = false,

    val savedFamilyName: String = ""
)

class FragmentHolderViewModel(
    private val familyMemberService: FamilyMemberService,
    private val sharedPreference: SharedPreference
) : ViewModel() {

    private val internalState = MutableLiveData<State>().apply {
        value = State()
    }

    val state: LiveData<State> = internalState

    init {
        getAndPostFamilyInfo()
    }

    private fun getAndPostFamilyInfo() {
        viewModelScope.launch {

            try {
                familyMemberService.families().apply {
                    val savedFamilyName = sharedPreference.getSavedFamilyName()

                    internalState.postValue(
                        State(
                            hasSavedFamily = this.isNotEmpty(),
                            savedFamilyName = savedFamilyName!!,
                            loading = false
                        )
                    )
                }

            } catch (e: FirebaseFirestoreException) {
                //TODO
            }
        }
    }
}