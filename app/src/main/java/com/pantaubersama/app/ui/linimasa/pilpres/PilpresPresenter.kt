package com.pantaubersama.app.ui.linimasa.pilpres

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
        val selectedFilter = pilpresInteractor?.getPilpresFilter()

        val tweetList: MutableList<PilpresTweet> = ArrayList()

        for (i in 1..25) {
            val tweet = PilpresTweet()
            tweet.id = "666$i"
            tweet.tweetContent = "tweet $i, filter = $selectedFilter"
            tweet.tweetUrl = ""
            tweetList.add(tweet)
        }

        view?.dismissLoading()
        view?.showPilpresTweet(tweetList)
    }
}