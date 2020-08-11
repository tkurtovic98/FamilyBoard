package com.hr.kurtovic.tomislav.familyboard.util

import android.content.Context
import com.hr.kurtovic.tomislav.familyboard.R

interface SharedPreference {
    fun saveNewFamilyName(newFamilyName: String)
    fun getSavedFamilyName(): String?
}

class SharedPreferenceImpl(val context: Context) : SharedPreference {

    override fun saveNewFamilyName(newFamilyName: String) {
        context.getSharedPreferences(
            context.getString(R.string.family_shared_prefs),
            Context.MODE_PRIVATE
        )
                .edit()
                .putString(R.string.family_name_key.toString(), newFamilyName)
                .apply()

    }

    override fun getSavedFamilyName(): String? =
            context.getSharedPreferences(
                context.getString(R.string.family_shared_prefs),
                Context.MODE_PRIVATE
            )
                    .getString(R.string.family_name_key.toString(), "")


}