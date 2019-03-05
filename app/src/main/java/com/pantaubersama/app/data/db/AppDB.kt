package com.pantaubersama.app.data.db

import android.content.Context
import androidx.room.*
import com.pantaubersama.app.data.model.tps.Province
import com.pantaubersama.app.data.model.tps.TPSData
import com.pantaubersama.app.utils.PantauConstants

@Database(entities = [Province::class, TPSData::class], version = PantauConstants.DATABASE_VERSION)
@TypeConverters(
    ProvinceTypeConverter::class,
    RegencyTypeConverter::class,
    DistrictTypeConverter::class,
    VillageTypeConverter::class,
    TimeTypeConverter::class
)
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
    abstract fun getTPSDAO(): PerhitunganDAO.TPSDAO
}