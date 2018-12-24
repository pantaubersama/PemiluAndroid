package com.pantaubersama.app.data.model.kuis

sealed class KuisListItem {
    data class Result(val percentage: Int, val candidate: String) : KuisListItem()
    data class Item(val week: Int, val count: Int, val state: KuisState) : KuisListItem()
}

enum class KuisState {
    COMPLETED, INCOMPLETE, NOT_TAKEN
}