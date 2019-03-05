package com.pantaubersama.app.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pantaubersama.app.data.model.createdat.CreatedAtInWord
import com.pantaubersama.app.data.model.tps.Village
import java.util.*

/**
 * Created by alimustofa on 27/02/18.
 */
class TimeTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToTime(data: String): CreatedAtInWord {
        return gson.fromJson(data, CreatedAtInWord::class.java)
    }

    @TypeConverter
    fun timeToString(time: CreatedAtInWord): String {
        return gson.toJson(time)
    }
}