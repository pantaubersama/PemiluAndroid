package com.pantaubersama.app.data.local.cache

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pantaubersama.app.data.local.SharedPref
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
}