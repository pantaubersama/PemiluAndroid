package com.pantaubersama.app.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pantaubersama.app.data.model.tps.Village
import java.util.*

/**
 * Created by alimustofa on 27/02/18.
 */
class VillageTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToVillage(data: String): Village {
        return gson.fromJson(data, Village::class.java)
    }

    @TypeConverter
    fun villageToString(village: Village): String {
        return gson.toJson(village)
    }
}