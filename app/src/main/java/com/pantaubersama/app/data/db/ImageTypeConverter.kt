package com.pantaubersama.app.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pantaubersama.app.data.model.tps.image.Image

class ImageTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToImages(data: String): MutableList<Image> {
        return gson.fromJson(data, object : TypeToken<MutableList<Image>>() {}.type)
    }

    @TypeConverter
    fun imagesToString(images: MutableList<Image>): String {
        return gson.toJson(images)
    }
}