package com.pantaubersama.app.ui.profile.linimasa

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import javax.inject.Inject

/**
 * @author edityomurti on 27/12/2018 15:29
 */
class ProfileJanjiPolitikPresenter @Inject constructor() : BasePresenter<ProfileJanjiPolitikView>() {
    fun getJanjiPolitikList() {
        view?.showLoading()

        val janpolList: MutableList<JanjiPolitik> = ArrayList()

        for (i in 1..20) {
            val janpol = JanjiPolitik()
            janpol.id = "123$i"
            janpol.body = "janpol $i"
            janpol.title = "My Janpol $i"
            janpolList.add(janpol)
        }

        view?.dismissLoading()
        view?.bindDataJanjiPolitik(janpolList)
    }
}