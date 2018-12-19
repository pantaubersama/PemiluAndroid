package com.pantaubersama.app.ui.tanyakandidat.create

import com.pantaubersama.app.base.BasePresenter

class CreateTanyaKandidatPresenter : BasePresenter<CreateTanyaKandidatView>() {
    fun submitQuestion(question: String?) {
        if (question != "") {
            // procced question
        } else {
            view?.showEmptyQuestionAlert()
        }
    }
}