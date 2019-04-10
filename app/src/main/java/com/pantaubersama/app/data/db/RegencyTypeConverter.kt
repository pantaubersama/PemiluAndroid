package com.pantaubersama.app.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pantaubersama.app.data.model.tps.Regency
/**
 * Created by alimustofa on 27/02/18.
 */
class RegencyTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToRegency(data: String): Regency {
        return gson.fromJson(data, Regency::class.java)
    }

    @TypeConverter
    fun regencyToString(regency: Regency): String {
        return gson.toJson(regency)
    }
}