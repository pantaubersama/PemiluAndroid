package com.pantaubersama.app.ui.debat.detail

import com.pantaubersama.app.base.BaseView

/**
 * @author edityomurti on 01/03/2019 18:04
 */

interface DetailDebatView : BaseView {
    fun showLoadingStatementSource()
    fun dismissLoadingStatementSource()
    fun onErrorStatementSource(throwable: Throwable)
    fun showStatementSource(url: String)

    fun showLoadingConfirmOpponentCandidate()
    fun dismissLoadingConfirmOpponentCandidate()
    fun onSuccessConfirmOpponentCandidate(audienceId: String)
    fun onErrorConfirmOpponentcandidate(t: Throwable)
}