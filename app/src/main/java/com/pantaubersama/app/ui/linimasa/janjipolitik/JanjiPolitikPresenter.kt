package com.pantaubersama.app.ui.linimasa.janjipolitik

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.JanjiPolitikInteractor
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.utils.PantauConstants
import javax.inject.Inject

/**
 * @author edityomurti on 25/12/2018 22:12
 */

class JanjiPolitikPresenter @Inject constructor(
    private val janjiPolitikInteractor: JanjiPolitikInteractor,
    private val bannerInfoInteractor: BannerInfoInteractor
) : BasePresenter<JanjiPolitikView>() {

    fun getList() {
        view?.showLoading()
        disposables?.add(bannerInfoInteractor.getBannerInfo(PantauConstants.BANNER_JANPOL)
            ?.subscribe(
                {
                    view?.showBanner(it)
                    getJanjiPolitikList()
                },
                {
                    view?.dismissLoading()
                    view?.showError(it)
                }
            )!!
        )
    }

    fun getJanjiPolitikList() {
        view?.showLoading()

        val janPolList: MutableList<JanjiPolitik> = ArrayList()

        for (i in 1..20) {
            val janpol = JanjiPolitik()
            janpol.id = "777$i"
            janpol.title = "Jan Pol $i"
            janpol.content = "Dukung partai nomer $i !!"
            janPolList.add(janpol)
        }

        view?.dismissLoading()
        view?.showJanjiPolitikList(janPolList)
    }
}