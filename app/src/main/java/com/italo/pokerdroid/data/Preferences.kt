package com.italo.pokerdroid.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import java.lang.reflect.Type

object Preferences {

    private const val SHARED_PREFERENCE_NAME = "POKERDROID_PREFERENCES"
    private lateinit var sharedPreference: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        sharedPreference =
            context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun <T> get(key: String, type: Type): T {
        val stringValue = sharedPreference.getString(key, null) ?: throw Exception()
        return gson.fromJson(stringValue, type) ?: throw Exception()
    }

    fun set(key: String, value: Any?) {
        value?.let {
            sharedPreference.edit().putString(key, gson.toJson(it)).apply()
        } ?: sharedPreference.edit().remove(key).apply()
    }

    fun exists(key: String): Boolean {
        val stringValue = sharedPreference.getString(key, null)
        return !stringValue.isNullOrBlank()
    }

    fun clear() {
        sharedPreference.edit().clear().apply()
    }
}