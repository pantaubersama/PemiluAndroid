package com.pantaubersama.app.data.db

import androidx.room.* //ktlint-disable
import com.pantaubersama.app.data.model.tps.Province
import com.pantaubersama.app.data.model.tps.RealCount
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

        @Query("SELECT * FROM tps ORDER BY id DESC")
        fun loadTPS(): MutableList<TPS>

        @Delete
        fun deleteTPS(tps: TPS)

        @Update
        fun updateTps(tps: TPS)

        @Query("SELECT * FROM tps WHERE id = :tpsId")
        fun getTps(tpsId: String): TPS
    }

    @Dao
    interface RealCountDAO {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun saveRealCount(realCount: RealCount)

        @Query("SELECT * FROM real_count WHERE id = :tpsId")
        fun getRealCount(tpsId: String): RealCount?

        @Query("SELECT * FROM real_count ORDER BY id DESC")
        fun getRealCounts(): MutableList<RealCount>
    }
}