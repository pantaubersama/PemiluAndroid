package com.pantaubersama.app.ui.menguji.list

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.debat.Challenge

interface DebatListView : BaseView {
    fun showChallenges(list: List<Challenge>)
    fun showMoreChallenges(list: List<Challenge>)
    fun setNoMoreItems()
}
