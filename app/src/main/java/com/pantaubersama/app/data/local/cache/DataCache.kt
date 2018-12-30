package com.pantaubersama.app.data.local.cache

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pantaubersama.app.data.local.SharedPref
import com.pantaubersama.app.data.model.user.EMPTY_USER
import com.pantaubersama.app.data.model.user.User

/**
 * @author edityomurti on 21/12/2018 18:05
 */

class DataCache(context: Context) : SharedPref(context) {
    private var gson: Gson = GsonBuilder().setFieldNamingPolicy(com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    companion object {
        private var gson: Gson? = null

        fun getInstance(context: Context): DataCache {
            return DataCache(context)
        }

        const val PREF_ID = "com.pantaubersama.cache.data"

        const val KEY_FILTER_PILPRES = "KEY_FILTER_PILPRES"

        const val IS_USER_LOGGED_IN = "is_user_logged_in"

        const val ACCESS_TOKEN_FIELD = "client_token"

        const val REFRESH_TOKEN_FIELD = "client_refresh_token"

        const val USER_PROFILE = "user_profile"
    }

    override fun prefId(): String {
        return PREF_ID
    }

    fun setFilterPilpres(selectedFilterPilpres: Int) {
        putInt(KEY_FILTER_PILPRES, selectedFilterPilpres)
    }

    fun getFilterPilpres(): Int {
        return getInt(KEY_FILTER_PILPRES)
    }

    fun saveLoginState(isLogin: Boolean) {
        putBoolean(IS_USER_LOGGED_IN, isLogin)
    }

    fun loadLoginState(): Boolean {
        return getBoolean(IS_USER_LOGGED_IN)
    }

    fun saveToken(accessToken: String) {
        putString(ACCESS_TOKEN_FIELD, accessToken)
    }

    fun saveRefreshToken(refreshToken: String) {
        putString(REFRESH_TOKEN_FIELD, refreshToken)
    }

    fun loadToken(): String? {
        return getString(ACCESS_TOKEN_FIELD)
    }

    fun loadRefreshToken(): String? {
        return getString(REFRESH_TOKEN_FIELD)
    }

    fun saveUserProfile(userProfile: User) {
        putString(USER_PROFILE, gson.toJson(userProfile))
    }

    fun loadUserProfile(): User {
        return gson.fromJson(getString(USER_PROFILE), User::class.java) ?: EMPTY_USER
    }
}