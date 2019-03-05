package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.db.AppDB
import com.pantaubersama.app.data.model.createdat.CreatedAtInWord
import com.pantaubersama.app.data.model.tps.*
import com.pantaubersama.app.data.model.user.EMPTY_PROFILE
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class TPSInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers,
    private val appDB: AppDB
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
        lat: Float,
        long: Float
    ): Completable {
        if (appDB.getTPSDAO().loadTPS().size != 0) {
            return Completable.fromCallable {
                appDB.getTPSDAO().saveTPS(
                    TPSData(
                        appDB.getTPSDAO().loadTPS().size.toString(),
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
                    TPSData(
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
}