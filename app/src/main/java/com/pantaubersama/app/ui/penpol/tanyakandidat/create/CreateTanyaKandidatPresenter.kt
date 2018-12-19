package com.pantaubersama.app.ui.penpol.tanyakandidat.create

import com.pantaubersama.app.base.BasePresenter
import javax.inject.Inject

class CreateTanyaKandidatPresenter @Inject constructor() : BasePresenter<CreateTanyaKandidatView>() {
    fun submitQuestion(question: String?) {
        if (question != "") {
            // procced question
        } else {
            view?.showEmptyQuestionAlert()
        }
    }
}