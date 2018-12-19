package com.pantaubersama.app.ui.home.linimasa.pilpres

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tweet.PilpresTweet

/**
 * @author edityomurti on 19/12/2018 14:46
 */
interface PilpresView : BaseView {
    fun showPilpresTweet(tweetList: List<PilpresTweet>)
}