package com.pantaubersama.app.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pantaubersama.app.data.model.tps.realcount.Candidate

/**
 * Created by alimustofa on 27/02/18.
 */
class CandidatesTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToCandidates(data: String): MutableList<Candidate> {
        return gson.fromJson(data, object : TypeToken<MutableList<Candidate>>() {}.type)
    }

    @TypeConverter
    fun candidatesToString(parties: MutableList<Candidate>): String {
        return gson.toJson(parties)
    }
}