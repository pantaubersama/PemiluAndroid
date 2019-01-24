package com.pantaubersama.app.ui.linimasa.janjipolitik.detail

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.JanjiPolitikInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.model.user.Profile
import javax.inject.Inject

class DetailJanjiPolitikPresenter @Inject constructor(
    private val janPolInteractor: JanjiPolitikInteractor,
    private val profileInteractor: ProfileInteractor
) : BasePresenter<DetailJanjiPolitikView>() {
    fun getMyProfile(): Profile {
        return profileInteractor.getProfile()
    }

    fun getJanpol(id: String) {
        view?.showLoading()
        disposables.add(janPolInteractor.getJanPolById(id)
            .doAfterTerminate { view?.dismissLoading() }
            .subscribe(
                {
                    view?.onBindData(it)
                },
                {
                    view?.showError(it)
                    view?.onFailedGetData(it)
                }
            )
        )
    }

    fun deleteJanjiPolitik(id: String) {
        disposables.add(janPolInteractor.deleteJanjiPolitik(id)
            .subscribe(
                {
                    view?.onSuccessDeleteItem()
                },
                {
                    view?.showError(it)
                    view?.onFailedDeleteItem(it)
                }
            )
        )
    }
}