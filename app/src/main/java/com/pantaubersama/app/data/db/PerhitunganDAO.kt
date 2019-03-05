package com.pantaubersama.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pantaubersama.app.data.model.tps.Province

interface PerhitunganDAO {
    @Dao
    interface ProvinceDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun saveProvinces(provinces: MutableList<Province>)

        @Query("SELECT * FROM province ORDER BY id ASC")
        fun loadProvinces(): MutableList<Province>
    }
}