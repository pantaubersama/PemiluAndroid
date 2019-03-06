package com.pantaubersama.app.ui.merayakan.perhitungan.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.TPSInteractor
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.utils.PantauConstants
import javax.inject.Inject

class PerhitunganPresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val bannerInfoInteractor: BannerInfoInteractor,
    private val tpsInteractor: TPSInteractor
) : BasePresenter<PerhitunganView>() {
    fun getProfile() {
        view?.bindProfile(profileInteractor.getProfile())
    }

    fun getPerhitunganData(page: Int, perPage: Int) {
        view?.showLoading()
        disposables.add(
            tpsInteractor.getTpses(page, perPage)
                .subscribe(
                    {
                        view?.dismissLoading()
                        if (it.size != 0) {
                            view?.bindTPSes(it)
                        } else {
                            view?.showEmptyAlert()
                        }
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedGetDataAlert()
                    }
                )
        )
    }

    fun getBanner() {
        view?.showLoading()
        disposables.add(
            bannerInfoInteractor.getBannerInfo(PantauConstants.PERHITUNGAN)
                .subscribe(
                    {
                        view?.showBanner(it)
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedGetDataAlert()
                    }
                )
        )
    }

    fun deletePerhitungan(tps: TPS, position: Int) {
        view?.showDeleteItemLoading()
        disposables.add(
            tpsInteractor.deleteTps(tps)
                .subscribe(
                    {
                        view?.dismissDeleteItemLoading()
                        view?.onSuccessDeleteItem(position)
                    },
                    {
                        view?.dismissDeleteItemLoading()
                        view?.showError(it)
                        view?.showFailedDeleteItemAlert()
                    }
                )
        )
    }
}