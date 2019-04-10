package com.pantaubersama.app.data.model.rekapitulasi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.createdat.CreatedAtInWord

data class LastUpdate(
    @SerializedName("created_at")
    @Expose
    var createdAt: String,
    @SerializedName("created_at_in_word")
    @Expose
    var createdAtInWord: CreatedAtInWord
)