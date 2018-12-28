package com.pantaubersama.app.ui.penpol.tanyakandidat.create

import com.pantaubersama.app.base.BaseView

interface CreateTanyaKandidatView : BaseView {
    fun showEmptyQuestionAlert()
    fun showSuccessCreateTanyaKandidatAlert()
    fun finishActivity()
    fun showFailedCreateTanyaKandidatAlert()
    // method
}