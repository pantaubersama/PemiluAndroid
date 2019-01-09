package com.pantaubersama.app.ui.linimasa.janjipolitik.detail

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.JanjiPolitikInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import javax.inject.Inject

class DetailJanjiPolitikPresenter @Inject constructor(
    private val janPolInteractor: JanjiPolitikInteractor,
    private val profileInteractor: ProfileInteractor
) : BasePresenter<DetailJanjiPolitikView>() {
    fun getUserId(): String {
        return profileInteractor.getProfile().id
    }

    fun deleteJanjiPolitik(id: String) {
        view?.showLoading()
        disposables?.add(janPolInteractor.deleteJanjiPolitik(id)
            .doOnComplete { view?.dismissLoading() }
            .subscribe(
                {
                    view?.onSuccessDeleteItem()
                },
                {
                    view?.showError(it)
                }
            )
        )
    }
}