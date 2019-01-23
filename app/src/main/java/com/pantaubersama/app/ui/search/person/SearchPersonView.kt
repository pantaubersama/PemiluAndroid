package com.pantaubersama.app.ui.search.person

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Profile

interface SearchPersonView : BaseView {
    fun showFailedGetDataAlert()
    fun showFailedGetMoreDataAlert()
    fun bindData(users: MutableList<Profile>)
    fun setDataEnd()
    fun showEmptyData()
    fun bindMoreData(users: MutableList<Profile>)
    fun bindProfile(profile: Profile)
}