package com.pantaubersama.app.ui.search

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.local.cache.DataCache
import javax.inject.Inject

class SearchPresenter @Inject constructor(private val dataCache: DataCache) : BasePresenter<BaseView>() {
    fun saveKeyword(keyword: String) {
        dataCache.saveSearchHistory(keyword)
    }
}