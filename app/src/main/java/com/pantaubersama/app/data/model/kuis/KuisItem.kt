package com.pantaubersama.app.data.model.kuis

import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.utils.PantauConstants
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.createdat.CreatedAt
import com.pantaubersama.app.data.model.image.Image
import com.pantaubersama.app.data.model.meta.Meta

data class KuisResponse(
    @SerializedName("data") val data: KuisData
)

data class KuisData(
    @SerializedName("quizzes") val kuisList: List<KuisItem>,
    @SerializedName("meta") val meta: Meta
)

data class KuisItem(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: Image,
    @SerializedName("quiz_questions_count") val kuisQuestionsCount: Int,
    @SerializedName("created_at") val createdAt: CreatedAt,
    @SerializedName("participation_status") val participationStatus: String
) : ItemModel {

    val state: KuisState
        get() = KuisState.fromString(participationStatus)

    override fun getType(): Int = PantauConstants.ItemModel.TYPE_KUIS_ITEM
}

