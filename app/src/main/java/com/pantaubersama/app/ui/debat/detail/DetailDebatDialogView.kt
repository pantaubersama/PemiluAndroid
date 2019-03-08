package com.pantaubersama.app.ui.debat.detail

import com.pantaubersama.app.base.BaseView

/**
 * @author edityomurti on 06/03/2019 18:19
 */
interface DetailDebatDialogView : BaseView {
    fun showStatementSource(url: String)
    fun onErrorStatementSource(throwable: Throwable)
}