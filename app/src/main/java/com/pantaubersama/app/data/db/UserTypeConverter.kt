package com.pantaubersama.app.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pantaubersama.app.data.model.user.Profile

/**
 * Created by alimustofa on 27/02/18.
 */
class UserTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToUser(data: String): Profile {
        return gson.fromJson(data, Profile::class.java)
    }

    @TypeConverter
    fun userToString(user: Profile): String {
        return gson.toJson(user)
    }
}