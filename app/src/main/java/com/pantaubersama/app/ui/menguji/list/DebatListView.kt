package com.pantaubersama.app.ui.menguji.list

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.debat.DebatItem

interface DebatListView : BaseView {
    fun showDebatItems(list: List<DebatItem>)
}
