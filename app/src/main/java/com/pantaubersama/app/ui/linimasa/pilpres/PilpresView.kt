package com.pantaubersama.app.ui.linimasa.pilpres

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.linimasa.FeedsItem

/**
 * @author edityomurti on 19/12/2018 14:46
 */
interface PilpresView : BaseView {
    fun showFeeds(feedsList: MutableList<FeedsItem>)
    fun showMoreFeeds(feedsList: MutableList<FeedsItem>)
    fun showEmptyData()
    fun showFailedGetData()
}