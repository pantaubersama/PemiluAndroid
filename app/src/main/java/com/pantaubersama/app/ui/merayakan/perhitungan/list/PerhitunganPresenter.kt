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
                        val tpses: MutableList<TPS> = ArrayList()
                        if (tpsInteractor.getLocalTpses().size != 0) {
                            it.addAll(tpsInteractor.getLocalTpses())
                            it.forEach { tps ->
                                if (tps.status == "sandbox") {
                                    tpses.add(0, tps)
                                } else {
                                    tpses.add(tps)
                                }
                            }
                        }
                        if (it.size != 0) {
                            view?.bindTPSes(tpses)
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

    fun createSandboxTps() {
        if (!tpsInteractor.isSandboxTpsCreated()) {
            disposables.add(
                tpsInteractor.createSandboxTps()
                    .subscribe(
                        {
                            view?.onSuccessCreateSandboxTps()
                        },
                        {
                            view?.onFailureCreateSandboxTps()
                            view?.showError(it)
                        }
                    )
            )
        } else {
            view?.onSuccessCreateSandboxTps()
        }
    }
}