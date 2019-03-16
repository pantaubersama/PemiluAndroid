package com.pantaubersama.app.data.model.tps.candidate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CandidateResponse(
    @SerializedName("data")
    @Expose
    var data: MutableList<CandidateData>
)