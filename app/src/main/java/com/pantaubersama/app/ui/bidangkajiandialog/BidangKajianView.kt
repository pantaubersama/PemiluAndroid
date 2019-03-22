package com.pantaubersama.app.ui.bidangkajiandialog

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tags.TagItem

interface BidangKajianView : BaseView {
    fun onSuccessGetTags(tagList: MutableList<TagItem>)
    fun onSuccessGetMoreTags(tagList: MutableList<TagItem>)
    fun onEmptyTags()
    fun onErrorGetTags(t: Throwable)
    fun onErrorGetMoreTags(t: Throwable)
}