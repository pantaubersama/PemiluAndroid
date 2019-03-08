package com.pantaubersama.app.ui.debat.detail

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.debat.Challenge

/**
 * @author edityomurti on 01/03/2019 18:04
 */

interface DetailDebatView : BaseView {
    fun showChallenge(challenge: Challenge)
    fun onErrorGetChallenge(t: Throwable)

    fun showLoadingStatementSource()
    fun dismissLoadingStatementSource()
    fun showStatementSource(url: String)
    fun onErrorStatementSource(throwable: Throwable)

    fun showLoadingConfirmOpponentCandidate()
    fun dismissLoadingConfirmOpponentCandidate()
    fun onSuccessConfirmOpponentCandidate(audienceId: String)
    fun onErrorConfirmOpponentcandidate(t: Throwable)

    fun showLoadingAskAsOpponent()
    fun dismissLoadingAskAsOpponent()
    fun onSuccessAskAsOpponent()
    fun onErrorAskAsOpponent(t: Throwable)
}