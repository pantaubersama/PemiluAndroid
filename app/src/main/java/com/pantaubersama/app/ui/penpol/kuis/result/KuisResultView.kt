package com.pantaubersama.app.ui.penpol.kuis.result

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.kuis.KuisResult

interface KuisResultView : BaseView {
    fun showResult(kuisResult: KuisResult, userName: String)
}
