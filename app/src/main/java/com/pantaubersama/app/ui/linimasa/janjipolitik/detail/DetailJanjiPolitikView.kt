package com.pantaubersama.app.ui.linimasa.janjipolitik.detail

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.data.model.user.Profile

interface DetailJanjiPolitikView : BaseView {
    fun onBindData(janjiPolitik: JanjiPolitik)
    fun onSuccessDeleteItem()
    fun onFailedDeleteItem(throwable: Throwable)
    fun onFailedGetData(throwable: Throwable)
    fun onDataNotFound()
    fun bindProfile(profile: Profile)
}