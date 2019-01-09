package com.pantaubersama.app.ui.linimasa.janjipolitik.detail

import com.pantaubersama.app.base.BaseView

interface DetailJanjiPolitikView : BaseView {
    fun onSuccessDeleteItem()
    fun onFailedDeleteItem(throwable: Throwable)
}