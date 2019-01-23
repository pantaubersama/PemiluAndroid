package com.pantaubersama.app.ui.linimasa.pilpres

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.PilpresInteractor
import com.pantaubersama.app.utils.PantauConstants
import javax.inject.Inject

/**
 * @author edityomurti on 19/12/2018 14:45
 */
class PilpresPresenter @Inject constructor(
    private val pilpresInteractor: PilpresInteractor,
    private val bannerInfoInteractor: BannerInfoInteractor
) : BasePresenter<PilpresView>() {

    var perPage = 20

    fun getList() {
        view?.showLoading()
//        if (bannerInfoInteractor.isBannerPilpresShown()!!) {
        disposables.add(bannerInfoInteractor.getBannerInfo(PantauConstants.BANNER_PILPRES)
            .subscribe(
                {
                    view?.showBanner(it)
                    getFeeds(1)
                },
                {
                    view?.dismissLoading()
                    view?.showError(it)
                    view?.showFailedGetData()
                }
            )
        )
//        } else {
//            getFeeds(1, 20)
//        }
    }

    fun getFeeds(page: Int) {
        if (page == 1) {
            view?.showLoading()
        }

        disposables.add(pilpresInteractor.getFeeds("", pilpresInteractor.getPilpresFilter(), page, perPage)
            .subscribe(
                {
                    if (page == 1) {
                        view?.dismissLoading()
                        if (it.feeds?.feedList != null) {
                            if (it.feeds?.feedList?.size != 0) {
                                view?.showFeeds(it.feeds?.feedList!!)
                            } else {
                                view?.showEmptyData()
                            }
                        } else {
                            view?.showFailedGetData()
                        }
                    } else {
                        view?.showMoreFeeds(it.feeds?.feedList!!)
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
}