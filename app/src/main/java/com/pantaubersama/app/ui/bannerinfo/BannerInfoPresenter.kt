package com.pantaubersama.app.ui.bannerinfo

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author edityomurti on 27/12/2018 21:44
 */
class BannerInfoPresenter @Inject constructor(private val bannerInfoInteractor: BannerInfoInteractor?) : BasePresenter<BannerInfoView>() {

    fun getBannerInfo(pageName: String) {
        view?.showLoading()
        val disposable: Disposable?
        disposable = bannerInfoInteractor?.getBannerInfo()
            ?.doOnSuccess { bannerInfoResponse ->
                for (bannerInfo in bannerInfoResponse.data.bannerInfoList) {
                    if (bannerInfo.pageName?.toLowerCase()?.contains(pageName)!!) {
                        view?.showBannerInfo(bannerInfo)
                        view?.dismissLoading()
                        bannerInfoInteractor.setBannerOpened()
                        break
                    }
                }
            }
            ?.doOnError { e ->
                view?.dismissLoading()
                view?.showError(e!!)
            }
            ?.subscribe()
        disposables?.add(disposable!!)
    }
}