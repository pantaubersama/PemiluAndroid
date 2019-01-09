package com.pantaubersama.app.ui.linimasa.janjipolitik

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik

/**
 * @author edityomurti on 25/12/2018 22:12
 */
interface JanjiPolitikView : BaseView {
    fun showBanner(bannerInfo: BannerInfo)
    fun showJanpolList(janjiPolitikList: MutableList<JanjiPolitik>)
    fun showMoreJanpolList(janjiPolitikList: MutableList<JanjiPolitik>)
    fun showEmptyData()
    fun showFailedGetData()
    fun showFailedGetMoreData()
    fun onSuccessDeleteItem(position: Int)
    fun onFailedDeleteItem(throwable: Throwable)
}