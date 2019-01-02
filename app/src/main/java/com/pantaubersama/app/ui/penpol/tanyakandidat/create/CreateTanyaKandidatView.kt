package com.pantaubersama.app.ui.penpol.tanyakandidat.create

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan

interface CreateTanyaKandidatView : BaseView {
    fun showEmptyQuestionAlert()
    fun showSuccessCreateTanyaKandidatAlert()
    fun finishActivity(question: Pertanyaan?)
    fun showFailedCreateTanyaKandidatAlert()
    fun hideActions()
    // method
}