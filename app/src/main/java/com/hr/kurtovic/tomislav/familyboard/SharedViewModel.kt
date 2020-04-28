package com.hr.kurtovic.tomislav.familyboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel(val context: Context) : ViewModel() {

    private val internalFamilyName = MutableLiveData<String>().apply {
        value = retrieveFamilyName()
    }

    val sharedFamilyName: LiveData<String> = internalFamilyName

    fun changeFamilyName(familyName: String) {
        if (familyName.contentEquals(internalFamilyName.value.toString())) return
        internalFamilyName.postValue(familyName)
        setFamilySharedPref(familyName)
    }

    private fun retrieveFamilyName(): String? =
            context.getSharedPreferences("FAMILY_SHARED_PREFS", Context.MODE_PRIVATE)
                    .getString(R.string.family_name_key.toString(), "")

    private fun setFamilySharedPref(familyName: String) {
        context.getSharedPreferences("FAMILY_SHARED_PREFS", Context.MODE_PRIVATE)
                .edit()
                .putString(R.string.family_name_key.toString(), familyName)
                .apply()
    }

}
