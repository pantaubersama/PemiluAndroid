package com.pantaubersama.app.ui.search.linimasa

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.PilpresInteractor
import javax.inject.Inject

class SearchLinimasaPresenter @Inject constructor(
    private val pilpresInteractor: PilpresInteractor
) : BasePresenter<SearchLinimasaView>() {
    var perPage = 20

    fun getList(keyword: String, page: Int) {
        if (page == 1) {
            view?.showLoading()
        }

        disposables.add(
            pilpresInteractor.getFeeds(keyword, pilpresInteractor.getSearchPilpresFilter(), page, perPage)
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