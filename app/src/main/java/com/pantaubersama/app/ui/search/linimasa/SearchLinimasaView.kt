package com.pantaubersama.app.ui.search.linimasa

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.linimasa.FeedsItem

interface SearchLinimasaView : BaseView {
    fun showFeeds(feedsList: MutableList<FeedsItem>)
    fun showMoreFeeds(feedsList: MutableList<FeedsItem>)
    fun showEmptyData()
    fun showFailedGetData()
    fun showFailedGetMoreData()
}