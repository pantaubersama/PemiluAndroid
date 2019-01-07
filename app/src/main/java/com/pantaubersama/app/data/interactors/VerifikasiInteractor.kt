package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import okhttp3.MultipartBody
import javax.inject.Inject

class VerifikasiInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers
    ) {
    fun submitKtpNumber(ktpNumber: String): Completable {
        return apiWrapper
            .getPantauOAuthApi()
            .submitKtpNumber(ktpNumber)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun submitSelfieKtp(ktpSelfie: MultipartBody.Part?): Completable {
        return apiWrapper
            .getPantauOAuthApi()
            .submitSelfieKtp(ktpSelfie)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

}