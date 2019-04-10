package com.pantaubersama.app.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pantaubersama.app.data.model.tps.image.ImageLocalModel

class ImageTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToImages(data: String): MutableList<ImageLocalModel> {
        return gson.fromJson(data, object : TypeToken<MutableList<ImageLocalModel>>() {}.type)
    }

    @TypeConverter
    fun imagesToString(images: MutableList<ImageLocalModel>): String {
        return gson.toJson(images)
    }
}