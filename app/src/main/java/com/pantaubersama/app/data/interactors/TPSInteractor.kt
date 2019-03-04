package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.tps.District
import com.pantaubersama.app.data.model.tps.Province
import com.pantaubersama.app.data.model.tps.Regency
import com.pantaubersama.app.data.model.tps.Village
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class TPSInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val dataCache: DataCache,
    private val rxSchedulers: RxSchedulers
) {
    fun getProvinces(): Single<MutableList<Province>> {
        return if (dataCache.getProvinces() != null) {
            Single.fromCallable {
                dataCache.getProvinces()
            }
        } else {
            apiWrapper.getPantauApi().getProvinces()
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
                .map { it.data.provinces }
                .doOnSuccess {
                    dataCache.saveProvinces(it)
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
}