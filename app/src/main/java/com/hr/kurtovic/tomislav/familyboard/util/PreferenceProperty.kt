package com.hr.kurtovic.tomislav.familyboard.util

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferenceProperty<T>(
    private val key: String,
    private val defaultValue: T,
    private val getter: SharedPreferences.(String, T) -> T,
    private val setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
) : ReadWriteProperty<Context, T> {

    override fun getValue(thisRef: Context, property: KProperty<*>): T =
            thisRef.getPreferences()
                    .getter(key, defaultValue)

    override fun setValue(thisRef: Context, property: KProperty<*>, value: T) =
            thisRef.getPreferences()
                    .edit()
                    .setter(key, value)
                    .apply()

    private fun Context.getPreferences(): SharedPreferences =
            getSharedPreferences("FAMILY_BOARD_SHARED_PREFS", Context.MODE_PRIVATE)
}


fun stringPreference(key: String, defaultValue: String? = ""): ReadWriteProperty<Context, String?> =
        PreferenceProperty(
            key = key,
            defaultValue = defaultValue,
            getter = SharedPreferences::getString,
            setter = SharedPreferences.Editor::putString
        )


enum class PreferenceType {
    STRING
}