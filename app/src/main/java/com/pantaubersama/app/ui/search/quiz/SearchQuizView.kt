package com.pantaubersama.app.ui.search.quiz

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.kuis.KuisItem

interface SearchQuizView : BaseView {
    fun showEmptyData()
    fun showFirstPage(list: List<KuisItem>)
    fun showMoreData(list: List<KuisItem>)
    fun showFailedGetData()
    fun showFailedGetMoreData()
    fun showLoadingMore()
    fun dismissLoadingMore()
    fun setNoMoreData()
}
