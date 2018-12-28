package com.pantaubersama.app.data.model.tanyakandidat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TanyaKandidatData(
    @SerializedName("questions")
    @Expose
    var questions: MutableList<Pertanyaan>? = null,
    @SerializedName("meta")
    @Expose
    var meta: Meta? = null
) : Serializable