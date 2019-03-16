package com.pantaubersama.app.data.interactors

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pantaubersama.app.data.db.AppDB
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.createdat.CreatedAtInWord
import com.pantaubersama.app.data.model.tps.* // ktlint-disable
import com.pantaubersama.app.data.model.user.EMPTY_PROFILE
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class TPSInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers,
    private val appDB: AppDB,
    private val dataCache: DataCache
) {
    private var gson: Gson = GsonBuilder().setFieldNamingPolicy(com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    fun getProvinces(): Single<MutableList<Province>> {
        return if (appDB.getProvinceDao().loadProvinces().size != 0) {
            Single.fromCallable {
                appDB.getProvinceDao().loadProvinces()
            }
        } else {
            apiWrapper.getPantauApi().getProvinces()
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
                .map { it.data.provinces }
                .doOnSuccess {
                    appDB.getProvinceDao().saveProvinces(it)
                }
        }
    }

    fun getRegencies(provinceId: Int): Single<MutableList<Regency>> {
        return apiWrapper.getPantauApi().getRegencies(provinceId)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map {
                it.data.regencies
            }
    }

    fun getDistricts(regencyId: Int): Single<MutableList<District>> {
        return apiWrapper.getPantauApi().getDistricts(regencyId)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map {
                it.data.districts
            }
    }

    fun getVillages(districtId: Int): Single<MutableList<Village>> {
        return apiWrapper.getPantauApi().getVillages(districtId)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map {
                it.data.villages
            }
    }

    fun saveTPS(
        tpsNumber: Int,
        selectedProvince: Province,
        selectedRegency: Regency,
        selectedDistrict: District,
        selectedVillage: Village,
        lat: Double,
        long: Double
    ): Single<TPS> {
        var newId: Int = 0

        appDB.getTPSDAO().loadTPS().forEachIndexed { i, item ->
            newId = i + 1
        }

        val tps = TPS(
            newId.toString(),
            tpsNumber,
            selectedProvince,
            selectedRegency,
            selectedDistrict,
            selectedVillage,
            lat,
            long,
            "draft",
            "",
            CreatedAtInWord("", "", ""),
            dataCache.loadUserProfile()
        )
        appDB.getTPSDAO().saveTPS(tps)
        return Single.just(tps)
    }

    fun getTpses(page: Int, perPage: Int): Single<MutableList<TPS>> {
        return apiWrapper.getPantauApi()
                .getTPSes(
                    page,
                    perPage,
                    dataCache.loadUserProfile().id
                )
                .map {
                    it.tpsData.tpses
                }
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
    }

    fun deleteTps(tps: TPS): Completable {
        return when (tps.status) {
            "published" -> apiWrapper.getPantauApi().deleteTPS(tps.id)
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
            "draft" -> Completable.fromCallable {
                appDB.getTPSDAO().deleteTPS(tps)
            }
            else -> Completable.fromCallable {
                appDB.getTPSDAO().deleteTPS(tps)
            }
        }
    }

    fun updateTps(
        tpsId: String,
        tpsNumber: Int,
        status: String
    ): Completable {
        when (status) {
            "published" -> return apiWrapper.getPantauApi().updateTPS(
                tpsId,
                tpsNumber
            )
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
            "draft" -> {
                val tps = appDB.getTPSDAO().getTps(tpsId)
                tps.tps = tpsNumber
                return Completable.fromCallable {
                    appDB.getTPSDAO().updateTps(tps)
                }
            }
            else -> {
                val tps = appDB.getTPSDAO().getTps(tpsId)
                tps.tps = tpsNumber
                return Completable.fromCallable {
                    appDB.getTPSDAO().updateTps(tps)
                }
            }
        }
    }

    fun getLocalTpses(): MutableList<TPS> {
        return appDB.getTPSDAO().loadTPS()
    }

    fun getTps(tpsId: String): TPS {
        return appDB.getTPSDAO().getTps(tpsId)
    }

    fun createSandboxTps(): Completable {
        var newId: Int = 0

        appDB.getTPSDAO().loadTPS().forEachIndexed { i, item ->
            newId = i + 1
        }
        return Completable.fromCallable {
            val province = "{\n" +
                "        \"id\": 34,\n" +
                "        \"code\": 34,\n" +
                "        \"name\": \"DAERAH ISTIMEWA YOGYAKARTA\",\n" +
                "        \"level\": 1,\n" +
                "        \"domain_name\": \"yogyakartaprov\",\n" +
                "        \"id_wilayah\": 41863\n" +
                "      }"
            val regency = "{\n" +
                "        \"id\": 3404,\n" +
                "        \"province_id\": 34,\n" +
                "        \"code\": 3404,\n" +
                "        \"name\": \"SLEMAN\",\n" +
                "        \"level\": 2,\n" +
                "        \"domain_name\": \"slemankab\",\n" +
                "        \"id_wilayah\": 42221,\n" +
                "        \"id_parent\": 41863\n" +
                "      }"
            val district = "{\n" +
                "        \"id\": 340407,\n" +
                "        \"code\": 340407,\n" +
                "        \"regency_code\": 3404,\n" +
                "        \"name\": \"DEPOK\",\n" +
                "        \"id_parent\": 42221,\n" +
                "        \"id_wilayah\": 42259,\n" +
                "        \"level\": 3\n" +
                "      }"
            val village = "{\n" +
                "        \"id\": 3404072003,\n" +
                "        \"code\": 3404072003,\n" +
                "        \"district_code\": 340407,\n" +
                "        \"name\": \"Condongcatur\"\n" +
                "      }"
            val tps = TPS(
                newId.toString(),
                1,
                gson.fromJson(province, Province::class.java),
                gson.fromJson(regency, Regency::class.java),
                gson.fromJson(district, District::class.java),
                gson.fromJson(village, Village::class.java),
                -7.752120,
                110.405296,
                "sandbox",
                "",
                CreatedAtInWord("", "", ""),
                EMPTY_PROFILE
            )
            appDB.getTPSDAO().saveTPS(tps)
        }
            .doOnComplete { dataCache.setCreatedSandbox() }
    }

    fun isSandboxTpsCreated(): Boolean {
        return dataCache.isSandboxCreated()
    }
}