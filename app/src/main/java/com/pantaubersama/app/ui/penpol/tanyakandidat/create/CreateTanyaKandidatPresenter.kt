package com.pantaubersama.app.ui.penpol.tanyakandidat.create

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.TanyaKandidatInteractor
import javax.inject.Inject

class CreateTanyaKandidatPresenter @Inject constructor(private val tanyaKandidatInteractor: TanyaKandidatInteractor?) : BasePresenter<CreateTanyaKandidatView>() {
    fun submitQuestion(question: String?) {
        if (question != "") {
            view?.showLoading()
            view?.hideActions()
            disposables?.add(tanyaKandidatInteractor
                ?.createTanyaKandidat(question)
                ?.subscribe({
                    view?.dismissLoading()
                    view?.showSuccessCreateTanyaKandidatAlert()
                    view?.finishActivity(it.data?.question)
                },{
                    view?.dismissLoading()
                    view?.showError(it)
                    view?.showFailedCreateTanyaKandidatAlert()
                })!!
            )
        } else {
            view?.showEmptyQuestionAlert()
        }
    }
}