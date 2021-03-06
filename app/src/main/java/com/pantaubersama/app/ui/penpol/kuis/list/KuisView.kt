package com.pantaubersama.app.ui.penpol.kuis.list

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.data.model.user.Profile

interface KuisView : BaseView {
    fun showTopPageItems(itemModels: List<ItemModel>)
    fun showFailedGetData()
    fun showLoadingMore()
    fun dismissLoadingMore()
    fun showMoreKuis(list: List<KuisItem>)
    fun setNoMoreItems()
    fun bindProfile(profile: Profile)
}