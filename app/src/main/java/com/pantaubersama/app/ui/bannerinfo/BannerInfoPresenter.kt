package com.pantaubersama.app.ui.bannerinfo

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author edityomurti on 27/12/2018 21:44
 */
class BannerInfoPresenter @Inject constructor(private val bannerInfoInteractor: BannerInfoInteractor?) : BasePresenter<BannerInfoView>() {

    fun getBannerInfo() {
        view?.showLoading()
        val disposable: Disposable?
        disposable = bannerInfoInteractor?.getBannerInfo()
            ?.doOnSuccess { bannerInfoResponse ->
                bannerInfoInteractor.getBannerInfo()
            }
            ?.doOnError { e ->
                view?.dismissLoading()
                view?.showError(e!!)
                view?.showBannerInfo()
            }
            ?.subscribe()
        disposables?.add(disposable!!)
    }
}