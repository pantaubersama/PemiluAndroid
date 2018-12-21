package com.pantaubersama.app.ui.home.linimasa.pilpres

import android.os.Handler
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.PilpresInteractor
import com.pantaubersama.app.data.model.tweet.PilpresTweet
import javax.inject.Inject

/**
 * @author edityomurti on 19/12/2018 14:45
 */
class PilpresPresenter @Inject constructor(private val pilpresInteractor: PilpresInteractor?) : BasePresenter<PilpresView>() {
    fun getPilpresTweet() {
        view?.showLoading()
        var tweetList: MutableList<PilpresTweet> = ArrayList()

        for (i in 1..25) {
            var tweet = PilpresTweet()
            tweet.tweetContent = "tweet $i"
            tweetList.add(tweet)
        }
        Handler().postDelayed({
            view?.dismissLoading()
            view?.showPilpresTweet(tweetList) }, 2000)
    }

    fun getPilpresFilter(): Int {
        return pilpresInteractor?.getPilpresFilter()!!
    }
}