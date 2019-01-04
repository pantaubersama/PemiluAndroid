package com.pantaubersama.app.data.model.kuis

import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.utils.PantauConstants

sealed class KuisListItem {
    data class Result(val percentage: Int, val candidate: String) : KuisListItem(), ItemModel {
        override fun getType(): Int = PantauConstants.ItemModel.TYPE_BANNER
    }
    data class Item(val id: Int, val week: Int, val count: Int, val state: KuisState) : KuisListItem(), ItemModel {
        override fun getType(): Int = PantauConstants.ItemModel.TYPE_BANNER
    }
}

enum class KuisState {
    COMPLETED, INCOMPLETE, NOT_TAKEN
}