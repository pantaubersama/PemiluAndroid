package com.pantaubersama.app.ui.linimasa.pilpres

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.PilpresInteractor
import javax.inject.Inject

/**
 * @author edityomurti on 19/12/2018 14:45
 */
class PilpresPresenter @Inject constructor(
    private val pilpresInteractor: PilpresInteractor?
) : BasePresenter<PilpresView>() {

    fun isBannerShown() {
        if (!pilpresInteractor?.isBannerShown()!!) {
            view?.showBanner()
        }
    }

    fun getFeeds(page: Int, perPage: Int) {
        if (page == 1) {
            view?.showLoading()
        }

        val selectedFilter = pilpresInteractor?.getPilpresFilter()

        disposables?.add(pilpresInteractor?.getFeeds(page, perPage)
            ?.doOnSuccess {
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
            }
            ?.doOnError {
                view?.dismissLoading()
                view?.showFailedGetData()
                view?.showError(it)
            }
            ?.subscribe()!!
        )
    }
}