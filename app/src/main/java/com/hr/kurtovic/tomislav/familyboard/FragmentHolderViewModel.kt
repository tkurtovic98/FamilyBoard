package com.hr.kurtovic.tomislav.familyboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import kotlinx.coroutines.launch

data class State(
    val loading: Boolean = true,
    val noSavedFamilies: Boolean = false,
    val savedFamilyName: String? = null
)

class FragmentHolderViewModel(
    private val context: Context,
    private val familyMemberService: FamilyMemberService
) : ViewModel() {


    private val internalState = MutableLiveData<State>().apply {
        value = State()
    }

    val state: LiveData<State> = internalState

    init {
        viewModelScope.launch {
            try {
                val families = familyMemberService.families()

                if (families.isEmpty()) {
                    internalState.postValue(State(noSavedFamilies = true, loading = false))
                    return@launch
                }

                val savedFamilyName = familyName()
                internalState.postValue(State(savedFamilyName = savedFamilyName, loading = false))

            } catch (e: FirebaseFirestoreException) {
                //TODO
            }
        }
    }

    private fun familyName(): String? =
            context.getSharedPreferences(
                context.getString(R.string.family_shared_prefs),
                Context.MODE_PRIVATE
            ).getString(R.string.family_name_key.toString(), "")


}