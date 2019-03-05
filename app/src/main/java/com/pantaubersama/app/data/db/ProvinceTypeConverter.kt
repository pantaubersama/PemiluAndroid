package com.pantaubersama.app.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pantaubersama.app.data.model.tps.Province
import java.util.*

/**
 * Created by alimustofa on 27/02/18.
 */
class ProvinceTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToProvince(data: String): Province {
        return gson.fromJson(data, Province::class.java)
    }

    @TypeConverter
    fun provinceToString(province: Province): String {
        return gson.toJson(province)
    }
}