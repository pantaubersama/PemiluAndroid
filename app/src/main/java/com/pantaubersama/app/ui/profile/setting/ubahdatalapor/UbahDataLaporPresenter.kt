package com.pantaubersama.app.ui.profile.setting.ubahdatalapor

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import javax.inject.Inject

class UbahDataLaporPresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor
) : BasePresenter<UbahDataLaporView>() {
    fun getDataLapor() {
        disposables?.add(
            profileInteractor
                .getDataLapor()
                .subscribe(
                    {
                        view?.setDataLapor(it)
                    },
                    {
                        view?.showError(it)
                        view?.showFailedGetDataLaporAlert()
                    }
                )
        )
    }

    fun updateDataLapor(
        idNumber: String?,
        pob: String?,
        dob: String?,
        gender: Int?,
        occupation: String?,
        nationality: String?,
        address: String?,
        phoneNumber: String?
    ) {
        view?.showLoading()
        view?.disableView()
        disposables?.add(
            profileInteractor
                .updateDataLapor(
                    idNumber,
                    pob,
                    dob,
                    gender,
                    occupation,
                    nationality,
                    address,
                    phoneNumber)
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.showSuccessUpdateDataLaporAlert()
                        view?.finishActivity()
                    },
                    {
                        view?.enableView()
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedUpdateDataLaporAlert()
                    }
                )
        )
    }
}