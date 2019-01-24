package com.pantaubersama.app.ui.profile.linimasa

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik

/**
 * @author edityomurti on 27/12/2018 15:29
 */
interface ProfileJanjiPolitikView : BaseView {
    fun showJanpolList(janpolList: MutableList<JanjiPolitik>)
    fun showMoreJanpolList(janpolList: MutableList<JanjiPolitik>)
    fun showEmptyData()
    fun showFailedGetData()
    fun showFailedGetMoreData()
    fun onSuccessDeleteItem(position: Int)
    fun onFailedDeleteItem(throwable: Throwable)
}