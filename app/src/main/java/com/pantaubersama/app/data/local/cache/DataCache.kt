package com.pantaubersama.app.data.local.cache

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.pantaubersama.app.data.local.SharedPref
import com.pantaubersama.app.data.model.cluster.Category
import com.pantaubersama.app.data.model.cluster.ClusterItem
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

        const val KEY_FILTER_FEED = "KEY_FILTER_FEED"
        const val KEY_FILTER_SEARCH_FEED = "KEY_FILTER_SEARCH_FEED"

        const val KEY_FILTER_JANPOL_USER = "KEY_FILTER_JANPOL_USER"
        const val KEY_FILTER_JANPOL_CLUSTER = "KEY_FILTER_JANPOL_CLUSTER"

        const val KEY_FILTER_SEARCH_CLUSTER_CATEGORY = "KEY_FILTER_SEARCH_CLUSTER_CATEGORY"

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

        const val KEY_SEARCH_HISTORY = "KEY_SEARCH_HISTORY"
        const val IS_ONBOARDING_COMPLETE = "is_onboarding_complete"
    }

    override fun prefId(): String {
        return PREF_ID
    }

    fun saveFilterPilpres(selectedFilterPilpres: String) {
        putString(KEY_FILTER_FEED, selectedFilterPilpres)
    }

    fun getFilterPilpres(): String {
        return getString(KEY_FILTER_FEED) ?: PantauConstants.Filter.Pilpres.FILTER_ALL
    }

    fun saveFilterSearchPilpres(selectedFilterSearchPilpres: String) {
        putString(KEY_FILTER_SEARCH_FEED, selectedFilterSearchPilpres)
    }

    fun getFilterSearchPilpres(): String {
        return getString(KEY_FILTER_SEARCH_FEED) ?: PantauConstants.Filter.Pilpres.FILTER_ALL
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

    fun loadTanyaKandidatOrderFilterDirection(): String? {
        return if (getString(PantauConstants.TanyaKandidat.Filter.FILTER_ORDER_DIRECTION) != null) {
            getString(PantauConstants.TanyaKandidat.Filter.FILTER_ORDER_DIRECTION)
        } else {
            "desc"
        }
    }

    fun getKuisFilter(): String {
        return getString(PantauConstants.Kuis.KUIS_FILTER) ?: PantauConstants.Kuis.Filter.KUIS_ALL
    }

    fun saveKuisFilter(kuisFilter: String) {
        putString(PantauConstants.Kuis.KUIS_FILTER, kuisFilter)
    }

    fun getJanpolUserFilter(): String {
        return getString(KEY_FILTER_JANPOL_USER) ?: PantauConstants.Filter.Janpol.USER_VERIFIED_ALL
    }

    fun saveJanpolUserFilter(janpolUserFilter: String) {
        putString(KEY_FILTER_JANPOL_USER, janpolUserFilter)
    }

    fun getJanpolClusterFilter(): ClusterItem? {
        return gson.fromJson(getString(KEY_FILTER_JANPOL_CLUSTER), ClusterItem::class.java)
    }

    fun saveJanpolClusterFilter(janpolClusterFilter: ClusterItem?) {
        putString(KEY_FILTER_JANPOL_CLUSTER, gson.toJson(janpolClusterFilter))
    }

    fun getFilterSearchClusterCategory(): Category? {
        return gson.fromJson(getString(KEY_FILTER_SEARCH_CLUSTER_CATEGORY), Category::class.java)
    }

    fun saveFilterSearchClusterCategory(categoryItem: Category) {
        putString(KEY_FILTER_SEARCH_CLUSTER_CATEGORY, gson.toJson(categoryItem))
    }

    fun getSearchHistory(): MutableList<String> {
        getString(KEY_SEARCH_HISTORY)?.let {
            return gson.fromJson(it, object : TypeToken<MutableList<String>>() {}.type)
        } ?: return ArrayList()
    }

    fun saveSearchHistory(keyword: String) {
        val MAX_KEYWORD_HISTORY_COUNT = 10
        val latestSearchHistory = getSearchHistory()
        if (latestSearchHistory.contains(keyword)) latestSearchHistory.remove(keyword)
        latestSearchHistory.add(0, keyword)
        if (latestSearchHistory.size > MAX_KEYWORD_HISTORY_COUNT) latestSearchHistory.removeAt(MAX_KEYWORD_HISTORY_COUNT)
        putString(KEY_SEARCH_HISTORY, gson.toJson(latestSearchHistory))
    }

    fun clearItemSearchHistory(keyword: String) {
        val latestSearchHistory = getSearchHistory()
        if (latestSearchHistory.contains(keyword)) latestSearchHistory.remove(keyword)
        putString(KEY_SEARCH_HISTORY, gson.toJson(latestSearchHistory))
    }

    fun clearAllSearchHistory() {
        putString(KEY_SEARCH_HISTORY, gson.toJson(ArrayList<String>()))
    }

    fun setOnboardingComplete() {
        putBoolean(IS_ONBOARDING_COMPLETE, true)
    }

    fun isOnBoardingComplete(): Boolean {
        return getBoolean(IS_ONBOARDING_COMPLETE)
    }
}