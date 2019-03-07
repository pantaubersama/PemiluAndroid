package com.pantaubersama.app.ui.debat.detail

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.user.Profile
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject


/**
 * @author edityomurti on 06/03/2019 18:19
 */

class DetailDebatDialogPresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val wordStadiumInteractor: WordStadiumInteractor
) : BasePresenter<DetailDebatDialogView>() {
    fun getMyProfile(): Profile {
        return profileInteractor.getProfile()
    }

    fun getStatementSourcePreview(url: String) {
        view?.showLoading()
        disposables += wordStadiumInteractor.getConvertLink(url)
            .doOnEvent { _, _ -> view?.dismissLoading() }
            .subscribe(
                {
                    it?.html?.let { html -> view?.showStatementSource(html) }
                },
                {
                    view?.onErrorStatementSource(it)
                }
            )
    }
}