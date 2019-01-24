package com.pantaubersama.app.ui.penpol.kuis.detail

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.kuis.KuisItem

interface DetailKuisView : BaseView {
    fun showQuiz(item: KuisItem)
    fun onFailedGetQuiz()
}