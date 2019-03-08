package com.pantaubersama.app.data.db

import android.content.Context
import androidx.room.* // ktlint-disable
import com.pantaubersama.app.data.model.tps.Province
import com.pantaubersama.app.data.model.tps.RealCount
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.utils.PantauConstants

@Database(entities = [Province::class, TPS::class, RealCount::class], version = PantauConstants.DATABASE_VERSION)
@TypeConverters(
    ProvinceTypeConverter::class,
    RegencyTypeConverter::class,
    DistrictTypeConverter::class,
    VillageTypeConverter::class,
    TimeTypeConverter::class,
    PartiesTypeConverter::class,
    CandidatesTypeConverter::class,
    UserTypeConverter::class,
    LogsTypeConverter::class
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
    abstract fun getRealCountDao(): PerhitunganDAO.RealCountDAO
}