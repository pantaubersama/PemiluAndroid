package com.pantaubersama.app.ui.penpol.kuis.result

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.kuis.TeamPercentage

interface KuisResultView : BaseView {
    fun showResult(team: TeamPercentage, userName: String)
}
