package com.pantaubersama.app.ui.search.history

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.local.cache.DataCache
import javax.inject.Inject

class SearchHistoryPresenter @Inject constructor(private val dataCache: DataCache) : BasePresenter<SearchHistoryView>() {
    fun getSearchHistory() {
        view?.showLoading()
        dataCache.getSearchHistory().let {
            if (it.size != 0) {
                view?.showSearchHistory(it)
            } else {
                view?.onEmptySearchHistory()
            }
            view?.dismissLoading()
        }
    }

    fun clearKeyword(keyword: String) {
        dataCache.clearItemSearchHistory(keyword)
    }

    fun clearSearchHistory() {
        dataCache.clearAllSearchHistory()
        view?.showLoading()
        view?.dismissLoading()
        view?.onEmptySearchHistory()
    }
}