package com.pantaubersama.app.ui.penpol.kuis.list

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.ItemModel

interface KuisView : BaseView {
    fun showTopPageItems(itemModels: List<ItemModel>)
    fun showFailedGetData()
}