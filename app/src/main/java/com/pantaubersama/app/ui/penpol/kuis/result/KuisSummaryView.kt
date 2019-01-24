package com.pantaubersama.app.ui.penpol.kuis.result

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.kuis.AnsweredQuestion

interface KuisSummaryView : BaseView {
    fun showAnswers(list: List<AnsweredQuestion>)
}
