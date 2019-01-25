package com.pantaubersama.app.data.local

import android.content.Context
import android.content.SharedPreferences

/**
 * @author edityomurti on 21/12/2018 17:33
 */
abstract class SharedPref(var context: Context) {

    private fun pref(): SharedPreferences {
        return context.getSharedPreferences(prefId(), Context.MODE_PRIVATE)
    }

    private fun edit(): SharedPreferences.Editor {
        return pref().edit()
    }

    protected abstract fun prefId(): String

    protected fun putString(key: String, value: String) {
        edit().putString(key, value).apply()
    }

    protected fun getString(key: String): String? {
        return pref().getString(key, null)
    }

    protected fun putInt(key: String, value: Int) {
        edit().putInt(key, value).apply()
    }

    protected fun getInt(key: String): Int {
        return pref().getInt(key, 0)
    }

    protected fun putLong(key: String, value: Long) {
        edit().putLong(key, value).apply()
    }

    protected fun getLong(key: String): Long {
        return pref().getLong(key, 0)
    }

    protected fun putBoolean(key: String, value: Boolean) {
        edit().putBoolean(key, value).apply()
    }

    protected fun getBoolean(key: String): Boolean {
        return pref().getBoolean(key, false)
    }

    protected fun putFloat(key: String, value: Float) {
        edit().putFloat(key, value).apply()
    }

    protected fun getFloat(key: String): Float {
        return pref().getFloat(key, 0f)
    }

    protected fun contains(key: String): Boolean {
        return pref().contains(key)
    }

    fun clear() {
        edit().clear().apply()
    }

    fun clear(key: String) {
        edit().remove(key).apply()
    }
}