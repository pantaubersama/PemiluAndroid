package com.pantaubersama.app.ui.penpol.kuis.kuisstart

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.KuisInteractor
import com.pantaubersama.app.data.model.kuis.Question
import javax.inject.Inject

class KuisQuestionPresenter @Inject constructor(
    private val kuisInteractor: KuisInteractor
) : BasePresenter<KuisQuestionView>() {

    private var questions = mutableListOf<Question>()
    private var answered = 0
    private var total = 0

    fun getQuestions(kuisId: String) {
        view?.showLoading()
        val disposable = kuisInteractor.getKuisQuestions(kuisId)
            .doOnEvent { _, _ -> view?.dismissLoading() }
            .subscribe({
                questions = it.questions.toMutableList()
                answered = it.answered
                total = it.total
                goToNextQuestion()
            }, {
                view?.showError(it)
            })
        disposables?.add(disposable)
    }

    fun answerQuestion(kuisId: String, questionId: String, answerId: String) {
        view?.showLoading()
        val disposable = kuisInteractor.answerQuestion(kuisId, questionId, answerId)
            .doOnEvent { view?.dismissLoading() }
            .subscribe({
                questions.removeAt(0)
                answered++
                goToNextQuestion()
            }, {
                view?.showError(it)
            })
        disposables?.add(disposable)
    }

    private fun goToNextQuestion() {
        if (questions.isEmpty()) {
            view?.onKuisFinished()
        } else {
            val questionNo = answered + 1
            view?.showQuestion(questions.first(), questionNo, total)
        }
    }
}