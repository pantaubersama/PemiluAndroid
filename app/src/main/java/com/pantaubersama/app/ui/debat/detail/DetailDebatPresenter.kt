package com.pantaubersama.app.ui.debat.detail

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.WordStadiumInteractor
import com.pantaubersama.app.data.model.user.Profile
import javax.inject.Inject

/**
 * @author edityomurti on 01/03/2019 18:05
 */

class DetailDebatPresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val wordStadiumInteractor: WordStadiumInteractor
) : BasePresenter<DetailDebatView>() {
    fun getMyProfile(): Profile {
        return profileInteractor.getProfile()
    }

    fun getStatementSourcePreview(url: String) {
        view?.showLoadingStatementSource()
        disposables.add(wordStadiumInteractor.getConvertLink(url)
            .doOnEvent { _, _ -> view?.dismissLoadingStatementSource() }
            .subscribe(
                {
                    it?.html?.let { html -> view?.showStatementSource(html) }
                },
                {
                    view?.onErrorStatementSource(it)
                }
            )
        )
    }
}