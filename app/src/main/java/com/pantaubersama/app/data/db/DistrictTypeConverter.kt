package com.pantaubersama.app.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pantaubersama.app.data.model.tps.District
import com.pantaubersama.app.data.model.tps.Regency
import java.util.*

/**
 * Created by alimustofa on 27/02/18.
 */
class DistrictTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToDistrict(data: String): District {
        return gson.fromJson(data, District::class.java)
    }

    @TypeConverter
    fun districtToString(district: District): String {
        return gson.toJson(district)
    }
}