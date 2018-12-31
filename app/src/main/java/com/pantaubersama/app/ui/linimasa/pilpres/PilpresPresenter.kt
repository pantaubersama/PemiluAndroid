package com.pantaubersama.app.ui.linimasa.pilpres

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.PilpresInteractor
import com.pantaubersama.app.data.model.linimasa.FeedsItem
import io.reactivex.disposables.Disposable
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
        view?.showLoading()
        val selectedFilter = pilpresInteractor?.getPilpresFilter()

        disposables?.add(pilpresInteractor?.getFeeds(page, perPage)
            ?.doOnSuccess {
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