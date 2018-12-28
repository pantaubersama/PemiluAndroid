package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import okhttp3.ResponseBody
import javax.inject.Inject

class TanyaKandidateInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers
) {
    fun createTanyaKandidat(body: String?): Single<ResponseBody> {
        return apiWrapper.getPantauApi().createTanyaKandidat(body)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }
}