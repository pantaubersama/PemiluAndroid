package com.pantaubersama.app.ui.search.janjipolitik

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik

interface SearchJanjiPolitikView : BaseView {
    fun showJanpolList(janjiPolitikList: MutableList<JanjiPolitik>)
    fun showMoreJanpolList(janjiPolitikList: MutableList<JanjiPolitik>)
    fun showEmptyData()
    fun showFailedGetData()
    fun showFailedGetMoreData()
    fun onSuccessDeleteItem(position: Int)
    fun onFailedDeleteItem(throwable: Throwable)
}