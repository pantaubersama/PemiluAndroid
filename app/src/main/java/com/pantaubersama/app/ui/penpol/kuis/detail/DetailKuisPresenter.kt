package com.pantaubersama.app.ui.penpol.kuis.detail

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.KuisInteractor
import javax.inject.Inject

class DetailKuisPresenter @Inject constructor(private val kuisInteractor: KuisInteractor) : BasePresenter<DetailKuisView>() {
    fun getQuizById(quizId: String) {
        view?.showLoading()
        disposables.add(
            kuisInteractor.getKuisById(quizId)
                .doOnEvent { _, _ -> view?.dismissLoading() }
                .subscribe(
                    {
                        view?.showQuiz(it)
                    },
                    {
                        view?.onFailedGetQuiz()
                        view?.showError(it)
                    }
                )
        )
    }
}