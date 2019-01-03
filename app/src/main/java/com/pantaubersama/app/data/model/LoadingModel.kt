package com.pantaubersama.app.data.model

import com.pantaubersama.app.utils.PantauConstants

class LoadingModel :  ItemModel {
    override fun getType(): Int = PantauConstants.ItemModel.TYPE_LOADING
}