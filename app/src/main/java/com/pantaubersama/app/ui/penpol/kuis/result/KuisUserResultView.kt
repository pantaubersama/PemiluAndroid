package com.pantaubersama.app.ui.penpol.kuis.result

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.kuis.KuisUserResult

interface KuisUserResultView : BaseView {
    fun showKuisUserResult(kuisUserResult: KuisUserResult, userName: String)
}