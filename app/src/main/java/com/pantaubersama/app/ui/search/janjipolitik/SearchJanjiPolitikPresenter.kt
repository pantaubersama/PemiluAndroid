package com.pantaubersama.app.ui.search.janjipolitik

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.JanjiPolitikInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.model.user.Profile
import javax.inject.Inject

class SearchJanjiPolitikPresenter @Inject constructor(
    private val janpolInteractor: JanjiPolitikInteractor,
    private val profileInteractor: ProfileInteractor
) : BasePresenter<SearchJanjiPolitikView>() {
    var perPage = 20

    fun getMyProfile(): Profile {
        return profileInteractor.getProfile()
    }

    fun getList(keyword: String, page: Int) {
        if (page == 1) {
            view?.showLoading()
        }

        disposables.add(
            janpolInteractor.getJanPol(keyword, page, perPage)
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