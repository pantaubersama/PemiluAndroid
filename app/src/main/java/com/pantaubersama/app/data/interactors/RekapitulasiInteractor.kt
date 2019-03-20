package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.model.rekapitulasi.Percentage
import com.pantaubersama.app.data.model.rekapitulasi.TotalParticipantData
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
            .map {
                it.data.percentage
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
}