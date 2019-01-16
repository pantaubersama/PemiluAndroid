package com.pantaubersama.app.ui.profile.linimasa

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.JanjiPolitikInteractor
import javax.inject.Inject

/**
 * @author edityomurti on 27/12/2018 15:29
 */
class ProfileJanjiPolitikPresenter @Inject constructor(private val janpolInteractor: JanjiPolitikInteractor) : BasePresenter<ProfileJanjiPolitikView>() {
    var perPage = 20

    fun getJanjiPolitikList(page: Int) {
        if (page == 1) {
            view?.showLoading()
        }

        disposables.add(janpolInteractor.getMyJanPol("", page, perPage)
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
        disposables.add(janpolInteractor.deleteJanjiPolitik(id)
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