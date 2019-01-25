package com.pantaubersama.app.ui.search.history

import com.pantaubersama.app.base.BaseView

interface SearchHistoryView : BaseView {
    fun showSearchHistory(keywordList: MutableList<String>)
    fun onEmptySearchHistory()
}