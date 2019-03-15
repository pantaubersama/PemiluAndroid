package com.pantaubersama.app.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pantaubersama.app.data.model.tps.Party

/**
 * Created by alimustofa on 27/02/18.
 */
class PartiesTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToParties(data: String): MutableList<Party> {
        return gson.fromJson(data, object : TypeToken<MutableList<Party>>() {}.type)
    }

    @TypeConverter
    fun partiesToString(parties: MutableList<Party>): String {
        return gson.toJson(parties)
    }
}