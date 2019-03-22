package com.pantaubersama.app.ui.bidangkajiandialog

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.OpiniumServiceInteractor
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class BidangKajianPresenter @Inject constructor(private val opiniumServiceInteractor: OpiniumServiceInteractor)
    : BasePresenter<BidangKajianView>() {
    var perPage = 20

    fun getTagList(page: Int, keyword: String) {
        if (page == 1) {
            view?.showLoading()
        }

        disposables += opiniumServiceInteractor.getTags(page, perPage, keyword)
            .doOnEvent { _, _ -> if (page == 1) view?.dismissLoading() }
            .subscribe(
                {
                    if (page == 1) {
                        if (it.size != 0) {
                            view?.onSuccessGetTags(it)
                        } else view?.onEmptyTags()
                    } else view?.onSuccessGetMoreTags(it)
                },
                {
                    if (page == 1) {
                        view?.onErrorGetTags(it)
                    } else view?.onErrorGetMoreTags(it)
                }
            )
    }
}