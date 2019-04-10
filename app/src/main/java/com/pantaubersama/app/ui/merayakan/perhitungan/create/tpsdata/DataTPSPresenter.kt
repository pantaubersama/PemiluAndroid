package com.pantaubersama.app.ui.merayakan.perhitungan.create.tpsdata

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.TPSInteractor
import com.pantaubersama.app.data.model.tps.* // ktlint-disable
import javax.inject.Inject

class DataTPSPresenter @Inject constructor(
    private val tpsInteractor: TPSInteractor
) : BasePresenter<DataTPSView>() {
    fun getProvincesData() {
        view?.showProvincesLoading()
        disposables.add(
            tpsInteractor.getProvinces()
                .subscribe(
                    {
                        view?.dismissProvincesLoading()
                        view?.bindProvincesToSpinner(it)
                    },
                    {
                        view?.dismissProvincesLoading()
                        view?.showFailedGetProvincesAlert()
                        view?.showError(it)
                    }
                )
        )
    }

    fun getRegenciesData(provinceId: Int) {
        view?.showRegenciesLoading()
        disposables.add(
            tpsInteractor.getRegencies(provinceId)
                .subscribe(
                    {
                        view?.dismissRegenciesLoading()
                        view?.bindRegenciesToSpinner(it)
                    },
                    {
                        view?.dismissRegenciesLoading()
                        view?.showError(it)
                        view?.showFailedGetRegenciesAlert()
                    }
                )
        )
    }

    fun getDistrictsData(regencyId: Int) {
        view?.showDistrictsLoading()
        disposables.add(
            tpsInteractor.getDistricts(regencyId)
                .subscribe(
                    {
                        view?.dismissDistrictsLoading()
                        view?.bindDistrictsToSpinner(it)
                    },
                    {
                        view?.dismissDistrictsLoading()
                        view?.showError(it)
                        view?.showFailedGetDistrictsAlert()
                    }
                )
        )
    }

    fun getVillagesData(districtId: Int) {
        view?.showVillagesLoading()
        disposables.add(
            tpsInteractor.getVillages(districtId)
                .subscribe(
                    {
                        view?.dismissVillagesLoading()
                        view?.bindVillagesToSpinner(it)
                    },
                    {
                        view?.dismissVillagesLoading()
                        view?.showError(it)
                        view?.showFailedGetVillagesAlert()
                    }
                )
        )
    }

    fun saveDataTPS(
        tpsNumber: Int,
        selectedProvince: Province,
        selectedRegency: Regency,
        selectedDistrict: District,
        selectedVillage: Village,
        lat: Double,
        long: Double
    ) {
        view?.showLoading()
        disposables.add(
            tpsInteractor.saveTPS(
                tpsNumber,
                selectedProvince,
                selectedRegency,
                selectedDistrict,
                selectedVillage,
                lat,
                long
            )
                .subscribe(
                    {
                        view?.dismissLoading()
                        view?.onSuccessSaveTPS(it)
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.failedSaveTpsAlert()
                    }
                )
        )
    }

    fun updateTps(
        tps: TPS
    ) {
        view?.showLoading()
        disposables.add(
            tpsInteractor.updateTps(
                tps
            )
                .subscribe(
                    {
                        view?.onSuccessUpdateTPS()
                        view?.dismissLoading()
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.failedUpdateTpsAlert()
                    }
                )
        )
    }
}