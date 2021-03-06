package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.model.rekapitulasi.* // ktlint-disable
import com.pantaubersama.app.data.model.tps.c1.C1Form
import com.pantaubersama.app.data.model.tps.image.Image
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class RekapitulasiInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers
) {
    fun getRekapitulasiNasional(): Single<Percentage> {
        return apiWrapper.getPantauApi().getRekapitulasiNasional()
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map { response ->
                response.data.percentage?.let {
                    it
                } ?: EMPTY_PERCENTAGE
            }
    }

    fun getTotalParticipant(): Single<TotalParticipantData> {
        return apiWrapper.getPantauApi().getTotalParticipant()
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map {
                it.data
            }
    }

    fun getRekapitulasiList(parent: String, regionCode: Long? = null): Single<MutableList<Rekapitulasi>> {
        var api: Single<RekapitulasiResponse> = apiWrapper.getPantauApi().getRekapitulasiList(0)
        when (parent) {
            "provinsi" -> api = apiWrapper.getPantauApi().getRekapitulasiList(1, regionCode)
            "kabupaten" -> api = apiWrapper.getPantauApi().getRekapitulasiList(2, regionCode)
            "kecamatan" -> api = apiWrapper.getPantauApi().getRekapitulasiList(3, regionCode)
            else -> apiWrapper.getPantauApi().getRekapitulasiList(0)
        }
        return api
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map {
                it.data.percentages
            }
    }

    fun getRekapitulasiDetail(tpsId: String, villageCode: Long, tpsNumber: Int): Single<Percentage> {
        return apiWrapper.getPantauApi().getRekapitulasiDetail(tpsId, villageCode, tpsNumber)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map {
                it.data.percentage
            }
    }

    fun getImages(tpsId: String, imagesType: String): Single<MutableList<Image>> {
        return apiWrapper.getPantauApi().getImages(tpsId, imagesType)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map { it.data.image }
    }

    fun getC1Summary(tpsId: String, c1Type: String): Single<C1Form> {
        return apiWrapper.getPantauApi().getC1(tpsId, c1Type)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map { it.data.c1Form }
    }
}