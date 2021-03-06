package com.pantaubersama.app.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pantaubersama.app.data.model.tps.Logs

/**
 * Created by alimustofa on 27/02/18.
 */
class LogsTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringTologs(data: String): Logs {
        return gson.fromJson(data, Logs::class.java)
    }

    @TypeConverter
    fun logsToString(logs: Logs): String {
        return gson.toJson(logs)
    }
}