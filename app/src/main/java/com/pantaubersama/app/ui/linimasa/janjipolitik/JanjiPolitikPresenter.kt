package com.pantaubersama.app.ui.linimasa.janjipolitik

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.JanjiPolitikInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.utils.PantauConstants
import javax.inject.Inject

/**
 * @author edityomurti on 25/12/2018 22:12
 */

class JanjiPolitikPresenter @Inject constructor(
    private val janPolInteractor: JanjiPolitikInteractor,
    private val bannerInfoInteractor: BannerInfoInteractor,
    private val profileInteractor: ProfileInteractor
) : BasePresenter<JanjiPolitikView>() {

    var perPage = 20

    fun getMyProfile(): Profile {
        return profileInteractor.getProfile()
    }

    fun isUserEligible(): Boolean {
        return profileInteractor.isEligible()
    }

    fun getList() {
        view?.showLoading()
        disposables.add(bannerInfoInteractor.getBannerInfo(PantauConstants.BANNER_JANPOL)
            .subscribe(
                {
                    view?.showBanner(it)
                    getJanjiPolitikList(1)
                },
                {
                    view?.dismissLoading()
                    view?.showError(it)
                    view?.showFailedGetData()
                }
            )
        )
    }

    fun getJanjiPolitikList(page: Int) {
        if (page == 1) {
            view?.showLoading()
        }

        disposables.add(janPolInteractor.getJanPol("", page, perPage)
            .subscribe(
                {
                    if (page == 1) {
                        view?.dismissLoading()
                        if (it?.janjiPolitikList?.size != 0) {
                            view?.showJanpolList(it?.janjiPolitikList!!)
                        } else {
                            view?.showEmptyData()
                        }
                    } else {
                        view?.showMoreJanpolList(it?.janjiPolitikList!!)
                    }
                },
                {
                    if (page == 1) {
                        view?.dismissLoading()
                        view?.showFailedGetData()
                    } else {
                        view?.showFailedGetMoreData()
                    }
                    it.printStackTrace()
                    view?.showError(it)
                }
            )
        )
    }

    fun deleteJanjiPolitik(id: String, position: Int) {
        disposables.add(janPolInteractor.deleteJanjiPolitik(id)
            .subscribe(
                {
                    view?.onSuccessDeleteItem(position)
                },
                {
                    view?.onFailedDeleteItem(it)
                    view?.showError(it)
                }
            )
        )
    }
}