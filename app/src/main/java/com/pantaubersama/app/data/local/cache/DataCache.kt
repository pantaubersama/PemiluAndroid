package com.pantaubersama.app.data.local.cache

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pantaubersama.app.data.local.SharedPref
import com.pantaubersama.app.data.model.user.EMPTY_PROFILE
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.utils.PantauConstants

/**
 * @author edityomurti on 21/12/2018 18:05
 */

class DataCache(context: Context) : SharedPref(context) {
    private var gson: Gson = GsonBuilder().setFieldNamingPolicy(com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    companion object {

        fun getInstance(context: Context): DataCache {
            return DataCache(context)
        }

        const val PREF_ID = "com.pantaubersama.cache.data"

        const val KEY_FILTER_PILPRES = "KEY_FILTER_PILPRES"

        const val IS_USER_LOGGED_IN = "is_user_logged_in"

        const val ACCESS_TOKEN_FIELD = "client_token"

        const val REFRESH_TOKEN_FIELD = "client_refresh_token"

        const val IS_BANNER_PILPRES_OPENED = "IS_BANNER_PILPRES_OPENED"
        const val IS_BANNER_JANPOL_OPENED = "IS_BANNER_JANPOL_OPENED"
        const val IS_BANNER_TANYA_OPENED = "IS_BANNER_TANYA_OPENED"
        const val IS_BANNER_KUIS_OPENED = "IS_BANNER_KUIS_OPENED"

        const val USER_PROFILE = "user_profile"

        const val TANYA_KANDIDAT_USER_FILTER = "tanya_kandidat_user_filter"
        const val TANYA_KANDIDAT_ORDER_FILTER = "tanya_kandidat_order_filter"
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

    fun setBannerPilpresOpened(isOpened: Boolean) {
        putBoolean(IS_BANNER_PILPRES_OPENED, isOpened)
    }

    fun isBannerPilpresOpened(): Boolean? {
        return getBoolean(IS_BANNER_PILPRES_OPENED)
    }

    fun setBannerJanpolOpened(isOpened: Boolean) {
        putBoolean(IS_BANNER_JANPOL_OPENED, isOpened)
    }

    fun isBannerJanpolOpened(): Boolean? {
        return getBoolean(IS_BANNER_JANPOL_OPENED)
    }

    fun setBannerTanyaKandidatOpened(isOpened: Boolean) {
        putBoolean(IS_BANNER_TANYA_OPENED, isOpened)
    }

    fun isBannerTanyaKandidatOpened(): Boolean? {
        return getBoolean(IS_BANNER_TANYA_OPENED)
    }

    fun setBannerKuisOpened(isOpened: Boolean) {
        putBoolean(IS_BANNER_KUIS_OPENED, isOpened)
    }

    fun isBannerKuisOpened(): Boolean? {
        return getBoolean(IS_BANNER_KUIS_OPENED)
    }

    fun saveUserProfile(profile: Profile) {
        putString(USER_PROFILE, gson.toJson(profile))
    }

    fun loadUserProfile(): Profile {
        return gson.fromJson(getString(USER_PROFILE), Profile::class.java) ?: EMPTY_PROFILE
    }

    fun saveTanyaKandidatUserFilter(userFilter: String) {
        putString(TANYA_KANDIDAT_USER_FILTER, userFilter)
    }

    fun saveTanyaKandidatOrderFilter(orderFilter: String) {
        putString(TANYA_KANDIDAT_ORDER_FILTER, orderFilter)
    }

    fun loadTanyaKandidatUserFilter(): String? {
        return if (getString(TANYA_KANDIDAT_USER_FILTER) != null) {
            getString(TANYA_KANDIDAT_USER_FILTER)
        } else {
            PantauConstants.TanyaKandidat.Filter.ByVerified.USER_VERIFIED_ALL
        }
    }

    fun loadTanyaKandidatOrderFilter(): String? {
        return if (getString(TANYA_KANDIDAT_ORDER_FILTER) != null) {
            getString(TANYA_KANDIDAT_ORDER_FILTER)
        } else {
            PantauConstants.TanyaKandidat.Filter.ByVotes.LATEST
        }
    }
}