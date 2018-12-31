package com.pantaubersama.app.ui.penpol.tanyakandidat.create

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.TanyaKandidateInteractor
import javax.inject.Inject

class CreateTanyaKandidatPresenter @Inject constructor(private val tanyaKandidateInteractor: TanyaKandidateInteractor?) : BasePresenter<CreateTanyaKandidatView>() {
    fun submitQuestion(question: String?) {
        if (question != "") {
            view?.showLoading()
            view?.hideActions()
            disposables?.add(tanyaKandidateInteractor
                ?.createTanyaKandidat(question)
                ?.doOnSuccess {
                    view?.dismissLoading()
                    view?.showSuccessCreateTanyaKandidatAlert()
                    view?.finishActivity(it.data.question)
                }
                ?.doOnError {
                    view?.dismissLoading()
                    view?.showError(it)
                    view?.showFailedCreateTanyaKandidatAlert()
                }
                ?.subscribe()!!
            )
        } else {
            view?.showEmptyQuestionAlert()
        }
    }
}