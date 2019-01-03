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
    private val pilpresInteractor: PilpresInteractor?,
    private val bannerInfoInteractor: BannerInfoInteractor
) : BasePresenter<PilpresView>() {

    fun getList() {
        view?.showLoading()
        if (bannerInfoInteractor.isBannerPilpresShown()!!) {
            disposables?.add(bannerInfoInteractor.getBannerInfo(PantauConstants.BANNER_PILPRES)
                ?.subscribe(
                    {
                        view?.showBanner(it)
                        getFeeds(1, 20)
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedGetData()
                    }
                )!!
            )
        } else {
            getFeeds(1, 20)
        }
    }

    fun getFeeds(page: Int, perPage: Int) {
        if (page == 1) {
            view?.showLoading()
        }

        val selectedFilter = pilpresInteractor?.getPilpresFilter()

        disposables?.add(pilpresInteractor?.getFeeds(page, perPage)
            ?.subscribe(
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
                    it.printStackTrace()
                    view?.dismissLoading()
                    view?.showFailedGetData()
                    view?.showError(it)
                }
            )!!
        )
    }
}