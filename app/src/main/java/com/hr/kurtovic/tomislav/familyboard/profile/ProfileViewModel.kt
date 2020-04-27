package com.hr.kurtovic.tomislav.familyboard.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.auth.AuthService
import io.reactivex.disposables.CompositeDisposable

class ProfileViewModel(
    private val familyMemberService: FamilyMemberService,
    private val authService: AuthService
) : ViewModel() {

    private val internalFamilyChange = MutableLiveData<List<String>>().apply { value = emptyList() }

    val familyChange: LiveData<List<String>> = internalFamilyChange

    val compositeDisposable = CompositeDisposable()

    private val familyChangeListener = familyMemberService.families()
            .addSnapshotListener { querySnapshot, _ ->
                val families = arrayListOf<String>()
                for (documentSnapshot in querySnapshot!!.documents) {
                    val familyName = documentSnapshot.get("familyName").toString()
                    families.add(familyName)
                }

                internalFamilyChange.postValue(families)
            }

    fun logout() {
        familyChangeListener.remove()
        authService.logout()
    }

    override fun onCleared() {
        super.onCleared()
        familyChangeListener.remove()
    }

}
