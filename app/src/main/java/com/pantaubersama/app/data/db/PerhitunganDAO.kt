package com.pantaubersama.app.data.db

import androidx.room.*
import com.pantaubersama.app.data.model.tps.Province
import com.pantaubersama.app.data.model.tps.TPS

interface PerhitunganDAO {
    @Dao
    interface ProvinceDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun saveProvinces(provinces: MutableList<Province>)

        @Query("SELECT * FROM province ORDER BY id ASC")
        fun loadProvinces(): MutableList<Province>
    }

    @Dao
    interface TPSDAO {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun saveTPS(tpsData: TPS)

        @Query("SELECT * FROM tps ORDER BY id ASC")
        fun loadTPS(): MutableList<TPS>

        @Delete
        fun deleteTPS(tps: TPS)
    }
}