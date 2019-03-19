package com.pantaubersama.app.data.db

import androidx.room.* //ktlint-disable
import com.pantaubersama.app.data.model.tps.C1Form
import com.pantaubersama.app.data.model.tps.Province
import com.pantaubersama.app.data.model.tps.RealCount
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.data.model.tps.image.ImageDoc

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

        @Update
        fun updateRealCount(realCount: RealCount)

        @Delete
        fun deleteRealCount(realCount: RealCount)

        @Query("SELECT * FROM real_count WHERE hitung_real_count_id = :tpsId AND calculationType = :realCountType")
        fun getRealCount(tpsId: String, realCountType: String): RealCount?

        @Query("SELECT * FROM real_count")
        fun getRealCounts(): MutableList<RealCount>

        @Query("SELECT * FROM real_count WHERE hitung_real_count_id = :tpsId")
        fun getRealCounts(tpsId: String): MutableList<RealCount>
    }

    @Dao
    interface C1DAO {

        @Query("SELECT * FROM c1_form")
        fun getC1s(): MutableList<C1Form>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun saveC1(c1Form: C1Form)

        @Query("SELECT * FROM c1_form WHERE tpsId = :tpsId AND formC1Type = :c1Type")
        fun getC1(tpsId: String, c1Type: String): C1Form?

        @Update
        fun updateC1(c1: C1Form?)
    }

    @Dao
    interface ImagesDAO {
        @Query("SELECT * FROM image_doc WHERE tpsId = :tpsId")
        fun getImage(tpsId: String): ImageDoc?

        @Update
        fun updateImage(it: ImageDoc)

        @Query("SELECT * FROM image_doc")
        fun getImages(): MutableList<ImageDoc>

        @Insert
        fun saveImage(imageDoc: ImageDoc)
    }
}