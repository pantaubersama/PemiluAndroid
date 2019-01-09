package com.pantaubersama.app.ui.penpol.kuis.kuisstart

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.kuis.Question

interface KuisQuestionView : BaseView {
    fun showQuestion(question: Question, questionNo: Int, total: Int)
    fun onKuisFinished()
}
