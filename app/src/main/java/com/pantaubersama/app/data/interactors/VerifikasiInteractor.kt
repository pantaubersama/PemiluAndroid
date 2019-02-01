package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.model.user.VerificationStep
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class VerifikasiInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers
) {

    fun getStatusVerifikasi(): Single<VerificationStep> {
        return apiWrapper.getPantauOAuthApi()
            .getVerificationStatus()
            .map {
                when (it.verificationData.user.nextStep) {
                    1 -> VerificationStep.SUBMIT_KTP_NO
                    2 -> VerificationStep.SUBMIT_SELFIE
                    3 -> VerificationStep.SUBMIT_KTP_PHOTO
                    4 -> VerificationStep.SUBMIT_SIGNATURE
                    else -> VerificationStep.FINISHED
                }
            }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

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

    fun submitKtpPhoto(ktpPhoto: MultipartBody.Part?): Completable {
        return apiWrapper
            .getPantauOAuthApi()
            .submitKtpPhoto(ktpPhoto)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun submitSignaturePhoto(signPhoto: MultipartBody.Part?): Completable {
        return apiWrapper
            .getPantauOAuthApi()
            .submitSignaturePhoto(signPhoto)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }
}