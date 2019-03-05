package com.pantaubersama.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pantaubersama.app.data.model.tps.Province
import com.pantaubersama.app.utils.PantauConstants

@Database(entities = [Province::class], version = PantauConstants.DATABASE_VERSION)
abstract class AppDB : RoomDatabase() {
    companion object {
        fun getInstance(context: Context): AppDB {
            return Room
                .databaseBuilder(context, AppDB::class.java, PantauConstants.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
        }
    }

    abstract fun getProvinceDao(): PerhitunganDAO.ProvinceDao
}