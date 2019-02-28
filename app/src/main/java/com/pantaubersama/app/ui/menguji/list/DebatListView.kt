package com.pantaubersama.app.ui.menguji.list

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.debat.Challenge

interface DebatListView : BaseView {
    fun showChallenge(list: List<Challenge>)
}
