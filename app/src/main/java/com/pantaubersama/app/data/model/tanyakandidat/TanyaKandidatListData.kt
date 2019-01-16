package com.pantaubersama.app.data.model.tanyakandidat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.meta.Meta
import java.io.Serializable

data class TanyaKandidatListData(
    @SerializedName("questions")
    @Expose
    var questions: MutableList<Pertanyaan>? = null,
    @SerializedName("meta")
    @Expose
    var meta: Meta? = null,
    @SerializedName("status")
    @Expose
    var status: Boolean? = false,
    @SerializedName("question")
    @Expose
    var question: Pertanyaan? = null
) : Serializable

data class TanyaKandidatData(
    @SerializedName("question")
    var question: Pertanyaan? = null
)