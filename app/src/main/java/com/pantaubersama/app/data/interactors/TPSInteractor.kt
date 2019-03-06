package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.db.AppDB
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.createdat.CreatedAtInWord
import com.pantaubersama.app.data.model.tps.* // ktlint-disable
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class TPSInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers,
    private val appDB: AppDB,
    private val dataCache: DataCache
) {
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
    ): Completable {
        if (appDB.getTPSDAO().loadTPS().size != 0) {
            return Completable.fromCallable {
                appDB.getTPSDAO().saveTPS(
                    TPS(
                        (appDB.getTPSDAO().loadTPS().size + 1).toString(),
                        tpsNumber,
                        selectedProvince,
                        selectedRegency,
                        selectedDistrict,
                        selectedVillage,
                        lat,
                        long,
                        "draft",
                        "",
                        CreatedAtInWord("", "", "")
                    )
                )
            }
        } else {
            return Completable.fromCallable {
                appDB.getTPSDAO().saveTPS(
                    TPS(
                        "1",
                        tpsNumber,
                        selectedProvince,
                        selectedRegency,
                        selectedDistrict,
                        selectedVillage,
                        lat,
                        long,
                        "draft",
                        "",
                        CreatedAtInWord("", "", "")
                    )
                )
            }
        }
    }

    fun getTpses(page: Int, perPage: Int): Maybe<MutableList<TPS>> {
        val api =
            apiWrapper.getPantauApi()
                .getTPSes(
                    page,
                    perPage,
                    dataCache.loadUserProfile().id
                )
                .map {
                    it.tpsData.tpses
                }
                .filter { it.size != 0 }
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())

        val db = Maybe.fromCallable {
            appDB.getTPSDAO().loadTPS()
        }
            .subscribeOn(rxSchedulers.io())
            // sementara
            .observeOn(rxSchedulers.mainThread())

        val mergedData = Maybe.concatArray(db, api)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())

        // sementara
        return db
    }
}