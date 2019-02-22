package com.pantaubersama.app.ui.penpol.tanyakandidat.create

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.data.model.user.Profile

interface CreateTanyaKandidatView : BaseView {
    fun showEmptyQuestionAlert()
    fun showSuccessCreateTanyaKandidatAlert()
    fun finishActivity(question: Pertanyaan?)
    fun showFailedCreateTanyaKandidatAlert()
    fun hideActions()
    fun bindProfileData(profile: Profile?)
    fun bindAvailableQuestions(questions: MutableList<Pertanyaan>)
    fun showFailedGetAvailableQuestions()
    fun showEmptyAvailableQuestionAlert()
    // method
}