package com.pantaubersama.app.ui.debat.detail

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.urlpreview.UrlItem
import com.pantaubersama.app.data.model.wordstadium.OEmbedLink

/**
 * @author edityomurti on 01/03/2019 18:04
 */

interface DetailDebatView : BaseView {
    fun showChallenge(challenge: Challenge)
    fun onErrorGetChallenge(t: Throwable)

    fun showLoadingConfirmOpponentCandidate()
    fun dismissLoadingConfirmOpponentCandidate()
    fun onSuccessConfirmOpponentCandidate(audienceId: String)
    fun onErrorConfirmOpponentcandidate(t: Throwable)

    fun showLoadingAskAsOpponent()
    fun dismissLoadingAskAsOpponent()
    fun onSuccessAskAsOpponent()
    fun onErrorAskAsOpponent(t: Throwable)

    fun showLoadingApproveDirect()
    fun dismissLoadingApproveDirect()
    fun onSuccessApproveDirect()
    fun onErrorApproveDirect(t: Throwable)

    fun showLoadingRejectDirect()
    fun dismissLoadingRejectDirect()
    fun onSuccessRejectDirect()
    fun onErrorRejectDirect(t: Throwable)

    fun showLoadingUrlPreview()
    fun dismissLoadingUrlPreview()
    fun showTweetPreview(oEmbedLink: OEmbedLink)
    fun showUrlPreview(urlItem: UrlItem)
    fun onErrorUrlPreview(t: Throwable)

    fun onSuccessLikeChallenge()
    fun onErrorLikeChallenge(t: Throwable)
    fun onSuccessUnikeChallenge()
    fun onErrorUnlikeChallenge(t: Throwable)
}